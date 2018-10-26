package com.example.ov_mm.notes.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Consumer;
import android.util.Log;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.model.NotesUpdate;
import com.example.ov_mm.notes.service.dao.CommonDataDao;
import com.example.ov_mm.notes.service.dao.NotesDao;
import com.example.ov_mm.notes.service.dao.NotesUpdateDao;
import com.example.ov_mm.notes.service.network.RemoteNotesService;
import com.example.ov_mm.notes.service.network.SyncException;
import com.example.ov_mm.notes.util.Function;
import com.example.ov_mm.notes.util.ListUtils;
import com.example.ov_mm.notes.vm.SyncInfo;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NotesSyncTask implements Runnable {

    private static final String TAG = "NotesSyncTask";

    @Nullable private volatile Consumer<SyncInfo.SyncResult> mResultConsumer;
    @NonNull private final RemoteNotesService mRemoteNotesService;
    @NonNull private final CommonDataDao mCommonDataDao;
    @NonNull private final NotesDao mNotesDao;
    @NonNull private final NotesUpdateDao mNotesUpdateDao;
    @NonNull private final Lock mSyncLock;
    @NonNull private final Lock mConsumeLock = new ReentrantLock();

    public NotesSyncTask(@NonNull Consumer<SyncInfo.SyncResult> resultConsumer,
                         @NonNull RemoteNotesService remoteNotesService,
                         @NonNull CommonDataDao commonDataDao,
                         @NonNull NotesDao notesDao,
                         @NonNull NotesUpdateDao notesUpdateDao,
                         @NonNull Lock syncLock) {
        mResultConsumer = resultConsumer;
        mRemoteNotesService = remoteNotesService;
        mCommonDataDao = commonDataDao;
        mNotesDao = notesDao;
        mNotesUpdateDao = notesUpdateDao;
        mSyncLock = syncLock;
    }

    @Override
    public void run() {
        try {
            synchronize();
            consumeResult(SyncInfo.SyncResult.SUCCESS);
        } catch (IOException | SyncException | RuntimeException ex) {
            Log.e(TAG, ex.getMessage(), ex);
            consumeResult(SyncInfo.SyncResult.FAILED);
        }
    }

    public void synchronize() throws IOException, SyncException {
        mSyncLock.lock();
        try {
            RemoteNotesService.SyncObject syncObject = mRemoteNotesService.synchronize(createSyncObject());

            if (syncObject.getVersion() != null && syncObject.getVersion() > 0) {
                Long version = syncObject.getVersion();
                NotesUpdate notesUpdate = new NotesUpdate();
                notesUpdate.setVersion(version);
                notesUpdate.setDate(new Date());
                mNotesUpdateDao.save(notesUpdate);
                if (syncObject.getNotes() != null) {
                    mNotesDao.saveNotes(ListUtils.map(Arrays.asList(syncObject.getNotes()),
                            new Function<RemoteNotesService.JNote, Note>() {
                                @Override
                                public Note apply(RemoteNotesService.JNote jNote) {
                                    Note note = new Note();
                                    note.setGuid(jNote.getGuid());
                                    if (jNote.getDate() != null) {
                                        note.setDate(new Date(jNote.getDate()));
                                    }
                                    note.setTitle(jNote.getTitle());
                                    note.setContent(jNote.getContent());
                                    note.setSynced(true);
                                    if (jNote.getDeleted() != null) {
                                        note.setDeleted(jNote.getDeleted());
                                    }
                                    return note;
                                }
                            }));
                }
            }
        } finally {
            mSyncLock.unlock();
        }
    }

    @NonNull
    private RemoteNotesService.SyncObject createSyncObject() {
        return new RemoteNotesService.SyncObject(mNotesUpdateDao.getLastVersion(), mCommonDataDao.getUser(),
                ListUtils.map(mNotesDao.getUnsyncedNotes(), new Function<Note, RemoteNotesService.JNote>() {
                    @Override
                    public RemoteNotesService.JNote apply(Note note) {
                        return new RemoteNotesService.JNote(note.getGuid(), note.getTitle(), note.getContent(), note.getDate().getTime(), note.isDeleted());
                    }
                }));
    }

    private void consumeResult(@NonNull SyncInfo.SyncResult result) {
        mConsumeLock.lock();
        try {
            Consumer<SyncInfo.SyncResult> resultConsumer = mResultConsumer;
            if (resultConsumer != null) {
                resultConsumer.accept(result);
            }
        } finally {
            mConsumeLock.unlock();
        }
    }

    public void cancel() {
        mConsumeLock.lock();
        try {
            mResultConsumer = null;
        } finally {
            mConsumeLock.unlock();
        }
    }
}

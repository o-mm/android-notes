package com.example.ov_mm.notes.repository;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.db.TransactionManager;
import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.model.NotesUpdate;
import com.example.ov_mm.notes.service.dao.CommonDataDao;
import com.example.ov_mm.notes.service.dao.NotesDao;
import com.example.ov_mm.notes.service.dao.NotesUpdateDao;
import com.example.ov_mm.notes.service.network.RemoteNotesService;
import com.example.ov_mm.notes.service.network.SyncException;
import com.example.ov_mm.notes.util.Function;
import com.example.ov_mm.notes.util.ListUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class NotesSyncTaskImpl implements NotesSyncTask {

    @NonNull private final RemoteNotesService mRemoteNotesService;
    @NonNull private final CommonDataDao mCommonDataDao;
    @NonNull private final NotesDao mNotesDao;
    @NonNull private final NotesUpdateDao mNotesUpdateDao;
    @NonNull private final TransactionManager mTransactionManager;

    public NotesSyncTaskImpl(@NonNull RemoteNotesService remoteNotesService,
                             @NonNull CommonDataDao commonDataDao,
                             @NonNull NotesDao notesDao,
                             @NonNull NotesUpdateDao notesUpdateDao,
                             @NonNull TransactionManager transactionManager) {
        mRemoteNotesService = remoteNotesService;
        mCommonDataDao = commonDataDao;
        mNotesDao = notesDao;
        mNotesUpdateDao = notesUpdateDao;
        mTransactionManager = transactionManager;
    }

    @Override
    public void run() throws Exception {
        synchronize();
    }

    private void synchronize() throws IOException, SyncException {
        final RemoteNotesService.SyncObject syncObject = mRemoteNotesService.synchronize(createSyncObject());

        if (syncObject.getVersion() != null && syncObject.getVersion() > 0) {
            Long version = syncObject.getVersion();
            final NotesUpdate notesUpdate = new NotesUpdate();
            notesUpdate.setVersion(version);
            notesUpdate.setDate(new Date());
            mTransactionManager.doInTransaction(new TransactionManager.DbExecution<Void>() {
                @Nullable
                @Override
                public Void execute(@NonNull SQLiteDatabase database) {
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
                    return null;
                }
            });
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
}

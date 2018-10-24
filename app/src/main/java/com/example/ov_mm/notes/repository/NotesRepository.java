package com.example.ov_mm.notes.repository;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Consumer;
import android.util.Log;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.service.dao.NotesDao;
import com.example.ov_mm.notes.service.dao.NotesUpdateDao;
import com.example.ov_mm.notes.service.network.RemoteNotesService;
import com.example.ov_mm.notes.service.network.SyncException;
import com.example.ov_mm.notes.vm.SyncInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class NotesRepository {

    private static final String TAG = "NotesRepository";
    @NonNull
    private final NotesDao mNotesDao;
    @NonNull
    private final NotesUpdateDao mNotesUpdateDao;
    @NonNull
    private final RemoteNotesService mRemoteNotesService;

    public NotesRepository(@NonNull NotesDao notesDao, @NonNull NotesUpdateDao notesUpdateDao, @NonNull RemoteNotesService remoteNotesService) {
        this.mNotesDao = notesDao;
        mNotesUpdateDao = notesUpdateDao;
        mRemoteNotesService = remoteNotesService;
    }

    public AsyncTask loadNotes(@NonNull Consumer<List<NoteWrapper>> resultConsumer, @Nullable String query, @Nullable SortProperty sortBy, boolean desc) {
        return new DataSelectionTask(mNotesDao, resultConsumer, query, sortBy, desc).execute();
    }

    public void saveNote(@NonNull NoteWrapper note) {
        if (note.isChanged()) {
            note.getNote().setDate(new Date());
            note.getNote().setSynced(false);
            mNotesDao.saveNote(note.getNote());
            note.setChanged(false);
        }
    }

    public void deleteNote(@NonNull NoteWrapper note) {
        note.getNote().setDate(new Date());
        note.getNote().setSynced(false);
        note.getNote().setDeleted(true);
        mNotesDao.saveNote(note.getNote());
    }

    @NonNull
    public NoteWrapper createNote() {
        return new NoteWrapper(new Note());
    }

    @Nullable
    public NoteWrapper getNote(@NonNull Long id) {
        Note note = mNotesDao.getNote(id);
        if (note != null)
            return new NoteWrapper(note);
        else {
            return null;
        }
    }

    @Nullable
    public Date getLastSyncDate() {
        return mNotesUpdateDao.getLastUpdateDate();
    }

    public NotesSyncFuture syncNotes(Consumer<SyncInfo.SyncResult> asyncConsumer) {
        NotesSyncTask notesSyncTask = new NotesSyncTask(asyncConsumer, mRemoteNotesService);
        return new NotesSyncFuture(Executors.newSingleThreadExecutor().submit(notesSyncTask), notesSyncTask);
    }

    public static class DataSelectionTask extends AsyncTask<Object, Void, List<NoteWrapper>> {

        @NonNull private final NotesDao mNotesDao;
        @Nullable private Consumer<List<NoteWrapper>> mResultConsumer;
        @Nullable private final String mQuery;
        @Nullable private final SortProperty mSortBy;
        private final boolean mDesc;

        public DataSelectionTask(@NonNull NotesDao dao, @NonNull Consumer<List<NoteWrapper>> resultConsumer,  @Nullable String query, @Nullable SortProperty sortBy,
                                 boolean desc) {
            mNotesDao = dao;
            mResultConsumer = resultConsumer;
            mQuery = query;
            mSortBy = sortBy;
            mDesc = desc;
        }

        @Override
        protected List<NoteWrapper> doInBackground(Object... objects) {
            List<NoteWrapper> result = new ArrayList<>();
            List<Note> notes = mNotesDao.getNotes(mQuery, mSortBy == null ? null : mSortBy.getDbColumn(), mDesc);
            for (Note note : notes) {
                result.add(new NoteWrapper(note));
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<NoteWrapper> noteWrappers) {
            super.onPostExecute(noteWrappers);
            if (mResultConsumer != null) {
                mResultConsumer.accept(noteWrappers);
            }
        }

        @Override
        protected void onCancelled() {
            mResultConsumer = null;
        }
    }

    public static class NotesSyncFuture {
        @NonNull private final Future mFuture;
        @NonNull private final NotesSyncTask mTask;

        public NotesSyncFuture(@NonNull Future future, @NonNull NotesSyncTask task) {
            mFuture = future;
            mTask = task;
        }

        public void cancel() {
            mTask.cancel();
            mFuture.cancel(true);
        }
    }

    public static class NotesSyncTask implements Runnable {

        @Nullable private volatile Consumer<SyncInfo.SyncResult> mResultConsumer;
        @NonNull private final RemoteNotesService mRemoteNotesService;
        @NonNull private final ReentrantLock lock = new ReentrantLock();

        public NotesSyncTask(@NonNull Consumer<SyncInfo.SyncResult> resultConsumer, @NonNull RemoteNotesService remoteNotesService) {
            mResultConsumer = resultConsumer;
            mRemoteNotesService = remoteNotesService;
        }

        @Override
        public void run() {
            try {
                mRemoteNotesService.synchronize();
                consumeResult(SyncInfo.SyncResult.SUCCESS);
            } catch (IOException | SyncException | RuntimeException ex) {
                Log.e(TAG, ex.getMessage(), ex);
                consumeResult(SyncInfo.SyncResult.FAILED);
            }
        }

        private void consumeResult(SyncInfo.SyncResult result) {
            lock.lock();
            try {
                Consumer<SyncInfo.SyncResult> resultConsumer = mResultConsumer;
                if (resultConsumer != null) {
                    resultConsumer.accept(result);
                }
            } finally {
                lock.unlock();
            }
        }

        public void cancel() {
            lock.lock();
            try {
                mResultConsumer = null;
            } finally {
                lock.unlock();
            }
        }
    }
}

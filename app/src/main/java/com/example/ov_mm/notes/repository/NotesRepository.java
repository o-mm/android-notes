package com.example.ov_mm.notes.repository;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Consumer;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.service.dao.NotesDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesRepository {

    @NonNull
    private final NotesDao mNotesDao;

    public NotesRepository(@NonNull NotesDao notesDao) {
        this.mNotesDao = notesDao;
    }

    public AsyncTask loadNotes(@NonNull Consumer<List<NoteWrapper>> resultConsumer, @Nullable String query, @Nullable SortProperty sortBy, boolean desc) {
        return new DataSelectionTask(mNotesDao, resultConsumer, query, sortBy, desc).execute();
    }

    public void saveNote(@NonNull NoteWrapper note) {
        if (note.isChanged()) {
            note.getNote().setDate(new Date());
            mNotesDao.saveNote(note.getNote());
            note.setChanged(false);
        }
    }

    public void deleteNote(@NonNull NoteWrapper note) {
        mNotesDao.removeNote(note.getNote());
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
}

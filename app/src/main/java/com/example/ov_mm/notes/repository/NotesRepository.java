package com.example.ov_mm.notes.repository;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Consumer;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.service.Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesRepository {

    @NonNull
    private final Dao mDao;

    public NotesRepository(@NonNull Dao dao) {
        this.mDao = dao;
    }

    public void loadNotes(@NonNull Consumer<List<NoteWrapper>> resultConsumer, @Nullable String query, @Nullable SortProperty sortBy, boolean desc) {
        new DataSelectionTask(mDao, resultConsumer, query, sortBy, desc).execute();
    }

    public void saveNote(@NonNull NoteWrapper note) {
        if (note.isChanged()) {
            note.getNote().setDate(new Date());
            mDao.saveNote(note.getNote());
        }
    }

    public void deleteNote(@NonNull NoteWrapper note) {
        mDao.removeNote(note.getNote());
    }

    @NonNull
    public NoteWrapper createNote() {
        return new NoteWrapper(new Note());
    }

    @Nullable
    public NoteWrapper getNote(@NonNull Long id) {
        Note note = mDao.getNote(id);
        if (note != null)
            return new NoteWrapper(note);
        else {
            return null;
        }
    }

    public static class DataSelectionTask extends AsyncTask<Object, Void, List<NoteWrapper>> {

        @NonNull private final Dao mDao;
        @NonNull private final Consumer<List<NoteWrapper>> mResultConsumer;
        @Nullable private final String mQuery;
        @Nullable private final SortProperty mSortBy;
        private final boolean mDesc;

        public DataSelectionTask(@NonNull Dao dao, @NonNull Consumer<List<NoteWrapper>> resultConsumer,  @Nullable String query, @Nullable SortProperty sortBy,
                                 boolean desc) {
            mDao = dao;
            mResultConsumer = resultConsumer;
            mQuery = query;
            mSortBy = sortBy;
            mDesc = desc;
        }

        @Override
        protected List<NoteWrapper> doInBackground(Object... objects) {
            List<NoteWrapper> result = new ArrayList<>();
            List<Note> notes = mDao.getNotes(mQuery, mSortBy == null ? null : mSortBy.getDbColumn(), mDesc);
            for (Note note : notes) {
                result.add(new NoteWrapper(note));
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<NoteWrapper> noteWrappers) {
            super.onPostExecute(noteWrappers);
            mResultConsumer.accept(noteWrappers);
        }
    }
}

package com.example.ov_mm.notes.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.service.dao.NotesDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class NotesRepository {

    @NonNull
    private final NotesDao mNotesDao;

    @Inject
    public NotesRepository(@NonNull NotesDao notesDao) {
        this.mNotesDao = notesDao;
    }

    public void load(@NonNull MutableLiveData<List<NoteWrapper>> notes, @Nullable String query, @Nullable SortProperty sortBy, boolean desc) {
        new DataSelectionTask(mNotesDao, notes, query, sortBy, desc).execute();
    }

    public void saveNote(@NonNull NoteWrapper note) {
        if (note.isChanged()) {
            note.getNote().setDate(new Date());
            mNotesDao.saveNote(note.getNote());
        }
    }

    public void deleteNote(@NonNull NoteWrapper note) {
        mNotesDao.removeNote(note.getNote());
    }

    @NonNull
    public NoteWrapper createNote() {
        return new NoteWrapper(new Note());
    }

    public void reorderNotes(MutableLiveData<List<NoteWrapper>> notes, String term, SortProperty sortProperty, boolean desc) {
        notes.setValue(reorderNotes(notes.getValue() == null ? Collections.<NoteWrapper>emptyList() : notes.getValue(), sortProperty, desc));
    }

    @NonNull
    private List<NoteWrapper> reorderNotes(@NonNull List<NoteWrapper> notes, @Nullable final SortProperty sortBy, final boolean desc) {
        Collections.sort(notes, new Comparator<NoteWrapper>() {
            @Override
            public int compare(NoteWrapper o1, NoteWrapper o2) {
                if (SortProperty.DATE.equals(sortBy)) {
                    return o1.getDate().compareTo(o2.getDate()) * (desc ? -1 : 1);
                } else if (SortProperty.TITLE.equals(sortBy)) {
                    return o1.getTitle().compareTo(o2.getTitle()) * (desc ? -1 : 1);
                } else {
                    return 0;
                }
            }
        });
        return notes;
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
        @NonNull private final MutableLiveData<List<NoteWrapper>> mNotes;
        @Nullable private final String mQuery;
        @Nullable private final SortProperty mSortBy;
        private final boolean mDesc;

        public DataSelectionTask(@NonNull NotesDao notesDao, @NonNull MutableLiveData<List<NoteWrapper>> notes, @Nullable String query, @Nullable SortProperty sortBy,
                                 boolean desc) {
            mNotesDao = notesDao;
            mNotes = notes;
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
            mNotes.setValue(noteWrappers);
        }
    }
}

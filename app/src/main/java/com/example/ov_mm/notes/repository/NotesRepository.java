package com.example.ov_mm.notes.repository;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.service.Dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class NotesRepository {

    @NonNull
    private final Dao dao = new Dao();

    public void load(@NonNull MutableLiveData<List<NoteWrapper>> notes, @Nullable String query, @Nullable SortProperty sortBy, boolean desc) {
        notes.setValue(getNotes(query, sortBy, desc));
    }

    public void saveNote(@NonNull NoteWrapper note) {
        if (note.isChanged()) {
            note.getNote().setDate(new Date());
            dao.saveNote(note.getNote());
        }
    }

    public void deleteNote(@NonNull NoteWrapper note) {
        dao.removeNote(note.getNote());
    }

    @NonNull
    public NoteWrapper createNote() {
        return new NoteWrapper(new Note());
    }

    public void reorderNotes(MutableLiveData<List<NoteWrapper>> notes, String term, SortProperty sortProperty, boolean desc) {
        notes.setValue(reorderNotes(notes.getValue() == null ? Collections.<NoteWrapper>emptyList() : notes.getValue(), sortProperty, desc));
    }

    @NonNull
    private List<NoteWrapper> getNotes(@Nullable String query, @Nullable final SortProperty sortBy, final boolean desc) {
        List<NoteWrapper> result = new ArrayList<>();
        Collection<Note> notes = query == null ? dao.getAllNotes() : dao.getNotes(query);
        for (Note note : notes) {
            result.add(new NoteWrapper(note));
        }
        return reorderNotes(result, sortBy, desc);
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
        Note note = dao.getNote(id);
        if (note != null)
            return new NoteWrapper(note);
        else {
            return null;
        }
    }
}

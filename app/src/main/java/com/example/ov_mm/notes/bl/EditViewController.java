package com.example.ov_mm.notes.bl;

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

public class EditViewController {

    @NonNull
    private final Dao dao = new Dao();

    @NonNull
    public List<ParcelableNote> getNotes(@Nullable String query, @Nullable final SortProperty sortBy, final boolean desc) {
        List<ParcelableNote> result = new ArrayList<>();
        Collection<Note> notes = query == null ? dao.getAllNotes() : dao.getNotes(query);
        for (Note note : notes) {
            result.add(new ParcelableNote(note));
        }

        Collections.sort(result, new Comparator<ParcelableNote>() {
            @Override
            public int compare(ParcelableNote o1, ParcelableNote o2) {
                if (SortProperty.DATE.equals(sortBy)) {
                    return o1.getDate().compareTo(o2.getDate()) * (desc ? -1 : 1);
                } else if (SortProperty.TITLE.equals(sortBy)) {
                    return o1.getTitle().compareTo(o2.getTitle()) * (desc ? -1 : 1);
                } else {
                    return 0;
                }
            }
        });
        return result;
    }

    public void saveNote(@NonNull ParcelableNote note) {
        if (note.isChanged()) {
            note.getNote().setDate(new Date());
            dao.saveNote(note.getNote());
        }
    }

    public void deleteNote(@NonNull ParcelableNote note) {
        dao.removeNote(note.getNote());
    }

    @NonNull
    public ParcelableNote createNote() {
        return new ParcelableNote(new Note());
    }
}

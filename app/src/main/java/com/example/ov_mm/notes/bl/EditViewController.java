package com.example.ov_mm.notes.bl;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.service.Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditViewController {

    @NonNull
    private final Dao dao = new Dao();

    @NonNull
    public List<ParcelableNote> getNotes() {
        List<ParcelableNote> result = new ArrayList<>();
        for (Note note : dao.getNotes()) {
            result.add(new ParcelableNote(note));
        }
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

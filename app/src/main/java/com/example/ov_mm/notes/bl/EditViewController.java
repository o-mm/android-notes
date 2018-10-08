package com.example.ov_mm.notes.bl;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.service.Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditViewController {

    private final Dao dao = new Dao();

    public List<ParcelableNote> getNotes() {
        List<ParcelableNote> result = new ArrayList<>();
        for (Note note : dao.getNotes()) {
            result.add(new ParcelableNote(note));
        }
        return result;
    }

    /**
     * @return true if saved, false if no changes detected
     */
    public boolean saveNote(ParcelableNote note) {
        if (note.isChanged()) {
            note.getNote().setDate(new Date());
            dao.saveNote(note.getNote());
            return true;
        }
        return false;
    }

    public void deleteNote(ParcelableNote note) {
        dao.removeNote(note.getNote());
    }

    public ParcelableNote createNote() {
        return new ParcelableNote(new Note());
    }
}

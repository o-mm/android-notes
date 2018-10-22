package com.example.ov_mm.notes.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Consumer;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.service.Dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class NotesRepository {

    @NonNull
    private final Dao mDao;

    @Inject
    public NotesRepository(@NonNull Dao dao) {
        this.mDao = dao;
    }

    public void loadNotes(@NonNull Consumer<List<NoteWrapper>> resultConsumer,  @Nullable String query) {
        List<NoteWrapper> result = new ArrayList<>();
        Collection<Note> notes = query == null ? mDao.getAllNotes() : mDao.getNotes(query);
        for (Note note : notes) {
            result.add(new NoteWrapper(note));
        }
        resultConsumer.accept(result);
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
}

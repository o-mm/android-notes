package com.example.ov_mm.notes.activity;

import android.arch.lifecycle.ViewModel;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.service.Dao;

import java.util.List;

public class ViewNotesVM extends ViewModel {

    private final Dao dao = new Dao();
    private int scrollPosition;

    public List<Note> getNotes() {
        return dao.getNotes();
    }

    public void updateScrollPosition(int newPosition) {
        scrollPosition = newPosition;
    }

    public int getScrollPosition() {
        return scrollPosition;
    }
}

package com.example.ov_mm.notes.repository;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.model.Note;

import java.util.Date;
import java.util.Objects;

public class NoteWrapper {

    @NonNull
    private final Note mNote;
    private boolean mChanged;

    NoteWrapper(@NonNull Note note) {
        this.mNote = note;
    }

    public String getTitle() {
        return mNote.getTitle();
    }

    public void setTitle(String title) {
        if (!Objects.equals(mNote.getTitle(), title)) {
            mNote.setTitle(title);
            mChanged = true;
        }
    }

    public String getContent() {
        return mNote.getContent();
    }

    public void setContent(String content) {
        if (!Objects.equals(mNote.getContent(), content)) {
            mNote.setContent(content);
            mChanged = true;
        }
    }

    public boolean isChanged() {
        return mChanged;
    }

    public Date getDate() {
        return mNote.getDate();
    }

    @NonNull
    Note getNote() {
        return mNote;
    }

    public Long getId() {
        return mNote.getId();
    }
}

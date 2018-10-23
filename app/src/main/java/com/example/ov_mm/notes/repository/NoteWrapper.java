package com.example.ov_mm.notes.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    @Nullable
    public String getTitle() {
        return mNote.getTitle();
    }

    public void setTitle(@Nullable String title) {
        if (!Objects.equals(mNote.getTitle(), title)) {
            mNote.setTitle(title);
            mChanged = true;
        }
    }

    @Nullable
    public String getContent() {
        return mNote.getContent();
    }

    public void setContent(@Nullable String content) {
        if (!Objects.equals(mNote.getContent(), content)) {
            mNote.setContent(content);
            mChanged = true;
        }
    }

    @Nullable
    public Date getDate() {
        return mNote.getDate();
    }

    @NonNull
    Note getNote() {
        return mNote;
    }

    @Nullable
    public Long getId() {
        return mNote.getId();
    }

    boolean isChanged() {
        return mChanged;
    }

    void setChanged(boolean changed) {
        mChanged = changed;
    }

}

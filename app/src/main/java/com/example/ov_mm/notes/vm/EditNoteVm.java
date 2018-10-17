package com.example.ov_mm.notes.vm;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.di.BaseViewModel;
import com.example.ov_mm.notes.repository.NoteWrapper;

public class EditNoteVm extends BaseViewModel {

    private NoteWrapper mNote;

    public EditNoteVm(@NonNull Application application) {
        super(application);
    }

    public void init(@Nullable Long id) {
        if (id != null)
            mNote = mRepository.getNote(id);
        if (mNote == null)
            mNote = mRepository.createNote();
    }

    @NonNull
    public NoteWrapper getNote() {
        if (mNote == null) {
            mNote = mRepository.createNote();
        }
        return mNote;
    }

    public void update(String title, String content) {
        getNote().setTitle(title);
        getNote().setContent(content);
        mRepository.saveNote(getNote());
    }
}

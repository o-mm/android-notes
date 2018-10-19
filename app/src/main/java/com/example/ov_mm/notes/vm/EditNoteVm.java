package com.example.ov_mm.notes.vm;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.repository.NoteWrapper;
import com.example.ov_mm.notes.repository.NotesRepository;

public class EditNoteVm extends ViewModel {

    private NoteWrapper mNote;
    private NotesRepository mRepository = new NotesRepository();

    public void init(@Nullable Long id) {
        if (mNote == null) {
            if (id != null)
                mNote = mRepository.getNote(id);
            if (mNote == null)
                mNote = mRepository.createNote();
        }
    }

    public NoteWrapper getNote() {
        return mNote;
    }

    public void update(String title, String content) {
        mNote.setTitle(title);
        mNote.setContent(content);
        mRepository.saveNote(mNote);
    }
}

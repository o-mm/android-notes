package com.example.ov_mm.notes.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.repository.NoteWrapper;
import com.example.ov_mm.notes.repository.NotesRepository;

public class EditNoteVm extends ViewModel {

    @NonNull private final MutableLiveData<NoteWrapper> mNote = new MutableLiveData<>();
    @NonNull private final NotesRepository mRepository = new NotesRepository();

    public void init(@Nullable Long id) {
        if (mNote.getValue() == null) {
            NoteWrapper note = null;
            if (id != null) {
                note = mRepository.getNote(id);
            }
            if (note == null) {
                note = mRepository.createNote();
            }
            mNote.setValue(note);
        }
    }

    @NonNull
    public LiveData<NoteWrapper> getNote() {
        return mNote;
    }

    public void update(@Nullable String title, @Nullable String content) {
        NoteWrapper note = mNote.getValue();
        if (note != null) {
            note.setTitle(title);
            note.setContent(content);
            mRepository.saveNote(note);
        }
    }
}

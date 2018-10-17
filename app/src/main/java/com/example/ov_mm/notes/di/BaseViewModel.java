package com.example.ov_mm.notes.di;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.ov_mm.notes.NotesApp;
import com.example.ov_mm.notes.repository.NotesRepository;

import javax.inject.Inject;

public abstract class BaseViewModel extends AndroidViewModel {

    @Inject
    protected NotesRepository mRepository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        ((NotesApp) application).getNotesAppComponent().inject(this);
    }
}

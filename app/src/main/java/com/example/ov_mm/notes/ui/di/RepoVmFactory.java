package com.example.ov_mm.notes.ui.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.ov_mm.notes.repository.NotesRepository;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;

@ActivityScope
public class RepoVmFactory implements ViewModelProvider.Factory {

    private final NotesRepository mRepository;

    @Inject
    public RepoVmFactory(NotesRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(NotesRepository.class).newInstance(mRepository);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

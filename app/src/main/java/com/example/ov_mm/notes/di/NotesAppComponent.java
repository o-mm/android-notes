package com.example.ov_mm.notes.di;

import com.example.ov_mm.notes.repository.NotesRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = RepositoryModule.class)
public interface NotesAppComponent {

    void inject(BaseViewModel baseViewModel);

    NotesRepository repository();
}

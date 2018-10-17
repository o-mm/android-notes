package com.example.ov_mm.notes.di;

import com.example.ov_mm.notes.repository.NotesRepository;
import com.example.ov_mm.notes.service.Dao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = DaoModule.class)
class RepositoryModule {

    @Provides
    @Singleton
    NotesRepository provideNotesRepository(Dao dao) {
        return new NotesRepository(dao);
    }
}

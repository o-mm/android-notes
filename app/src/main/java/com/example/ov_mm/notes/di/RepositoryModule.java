package com.example.ov_mm.notes.di;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.repository.NotesRepository;
import com.example.ov_mm.notes.service.dao.NotesDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = DaoModule.class)
class RepositoryModule {

    @Provides
    @Singleton
    @NonNull
    NotesRepository provideNotesRepository(NotesDao notesDao) {
        return new NotesRepository(notesDao);
    }
}

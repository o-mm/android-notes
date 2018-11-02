package com.example.ov_mm.notes.di;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.TransactionManager;
import com.example.ov_mm.notes.repository.NotesRepository;
import com.example.ov_mm.notes.repository.NotesSyncTaskProvider;
import com.example.ov_mm.notes.repository.NotesSyncTaskProviderImpl;
import com.example.ov_mm.notes.service.dao.CommonDataDao;
import com.example.ov_mm.notes.service.dao.NotesDao;
import com.example.ov_mm.notes.service.dao.NotesUpdateDao;
import com.example.ov_mm.notes.service.network.RemoteNotesService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {RemoteServiceModule.class, DaoModule.class})
class RepositoryModule {

    @Provides
    @Singleton
    @NonNull
    NotesRepository provideNotesRepository(@NonNull NotesDao notesDao,
                                           @NonNull NotesUpdateDao notesUpdateDao,
                                           @NonNull NotesSyncTaskProvider notesSyncTaskProvider) {
        return new NotesRepository(notesDao, notesUpdateDao, notesSyncTaskProvider);
    }

    @Provides
    @Singleton
    @NonNull
    NotesSyncTaskProvider provideNotesSyncTask(@NonNull NotesDao notesDao,
                                               @NonNull NotesUpdateDao notesUpdateDao,
                                               @NonNull CommonDataDao commonDataDao,
                                               @NonNull RemoteNotesService remoteNotesService,
                                               @NonNull TransactionManager transactionManager) {
        return new NotesSyncTaskProviderImpl(notesDao, notesUpdateDao, commonDataDao, remoteNotesService, transactionManager);
    }
}

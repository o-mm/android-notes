package com.example.ov_mm.notes.di;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.service.dao.CommonDataDao;
import com.example.ov_mm.notes.service.dao.NotesDao;
import com.example.ov_mm.notes.service.dao.NotesUpdateDao;
import com.example.ov_mm.notes.service.network.RemoteNotesService;

import javax.inject.Singleton;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module(includes = DaoModule.class)
public class RemoteServiceModule {

    @Provides
    @Singleton
    RemoteNotesService provideRemoteNotesService(@NonNull Lazy<OkHttpClient> httpClient,
                                                 @NonNull CommonDataDao commonDataDao,
                                                 @NonNull NotesDao notesDao,
                                                 @NonNull NotesUpdateDao notesUpdateDao) {
        return new RemoteNotesService(httpClient, commonDataDao, notesDao, notesUpdateDao);
    }

    @Provides
    @Singleton
    OkHttpClient provideHttpClient() {
        return new OkHttpClient();
    }
}

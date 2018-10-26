package com.example.ov_mm.notes.di;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.service.network.RemoteNotesService;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class RemoteServiceModule {

    @Provides
    @Singleton
    @NonNull
    RemoteNotesService provideRemoteNotesService(@NonNull Lazy<OkHttpClient> httpClient, @NonNull Gson gson) {
        return new RemoteNotesService(httpClient, gson);
    }

    @Provides
    @Singleton
    @NonNull
    OkHttpClient provideHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    @NonNull
    Gson provideGson() {
        return new Gson();
    }
}

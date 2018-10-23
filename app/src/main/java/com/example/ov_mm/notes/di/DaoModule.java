package com.example.ov_mm.notes.di;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.service.Dao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @Provides
    @Singleton
    @NonNull
    Dao provideDao() {
        return new Dao();
    }
}

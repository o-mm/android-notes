package com.example.ov_mm.notes.di;

import com.example.ov_mm.notes.service.Dao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @Provides
    @Singleton
    Dao provideDao() {
        return new Dao();
    }
}

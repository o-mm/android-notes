package com.example.ov_mm.notes.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.NotesDatabaseHelper;
import com.example.ov_mm.notes.service.Dao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @NonNull private final Context mContext;

    public DaoModule(@NonNull Context context) {
        mContext = context;
    }

    @Singleton
    @Provides
    @NonNull
    Dao provideDao(@NonNull SQLiteDatabase db) {
        return new Dao(db);
    }

    @Singleton
    @Provides
    @NonNull
    NotesDatabaseHelper provideDatabaseHelper() {
        return new NotesDatabaseHelper(mContext);
    }

    @Singleton
    @Provides
    @NonNull
    SQLiteDatabase provideDatabase(@NonNull NotesDatabaseHelper helper) {
        return helper.getWritableDatabase();
    }
}

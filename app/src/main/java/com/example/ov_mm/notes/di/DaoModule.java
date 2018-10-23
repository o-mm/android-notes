package com.example.ov_mm.notes.di;

import android.support.annotation.NonNull;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.ov_mm.notes.db.NotesDatabaseHelper;
import com.example.ov_mm.notes.service.Dao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    private final Context mContext;

    public DaoModule(Context context) {
        mContext = context;
    }

    @Singleton
    @Provides
    Dao provideDao(SQLiteDatabase db) {
        return new Dao(db);
    }

    @Singleton
    @Provides
    NotesDatabaseHelper provideDatabaseHelper() {
        return new NotesDatabaseHelper(mContext);
    }

    @Singleton
    @Provides
    SQLiteDatabase provideDatabase(NotesDatabaseHelper helper) {
        return helper.getWritableDatabase();
    }
}

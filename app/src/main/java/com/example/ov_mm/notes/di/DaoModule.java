package com.example.ov_mm.notes.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.ov_mm.notes.db.NotesDatabaseHelper;
import com.example.ov_mm.notes.service.dao.NotesDao;

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
    NotesDao provideDao(SQLiteDatabase db) {
        return new NotesDao(db);
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

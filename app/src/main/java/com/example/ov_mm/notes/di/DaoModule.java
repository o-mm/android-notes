package com.example.ov_mm.notes.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.NotesDatabaseHelper;
import com.example.ov_mm.notes.db.TransactionManager;
import com.example.ov_mm.notes.service.dao.CommonDataDao;
import com.example.ov_mm.notes.service.dao.NotesDao;
import com.example.ov_mm.notes.service.dao.NotesUpdateDao;

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
    NotesDao provideNotesDao(@NonNull SQLiteDatabase db) {
        return new NotesDao(db);
    }

    @Singleton
    @Provides
    @NonNull
    CommonDataDao provideCommonDataDao(@NonNull SQLiteDatabase db) {
        return new CommonDataDao(db);
    }

    @Singleton
    @Provides
    @NonNull
    NotesUpdateDao provideNotesUpdateDao(@NonNull SQLiteDatabase db) {
        return new NotesUpdateDao(db);
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

    @Singleton
    @Provides
    @NonNull
    TransactionManager provideTransactionManager(@NonNull SQLiteDatabase db) {
        return new TransactionManager(db);
    }
}

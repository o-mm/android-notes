package com.example.ov_mm.notes.service.dao;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class CommonDao {

    @NonNull
    private final SQLiteDatabase mDatabase;

    @Inject
    public CommonDao(@NonNull SQLiteDatabase db) {
        if (db.isReadOnly())
            throw new IllegalStateException("Database must be writable");
        mDatabase = db;
    }

    public String getUser() {
        return null; //TODO
    }

    public Long getLastSyncTime() {
        return null;
    }
}

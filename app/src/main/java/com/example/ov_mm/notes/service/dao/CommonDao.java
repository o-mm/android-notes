package com.example.ov_mm.notes.service.dao;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.Date;

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

    /**
     * @param version version of notes data from server
     * @param startDate date
     * @return true if syncTime is greater than value in DB, false otherwise
     */
    public boolean updateVersion(Long version, Date startDate) {

    }
}

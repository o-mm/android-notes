package com.example.ov_mm.notes.service.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.tables.CommonDataColumns;
import com.example.ov_mm.notes.model.CommonData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommonDataDao {

    @NonNull
    private final SQLiteDatabase mDatabase;

    public CommonDataDao(@NonNull SQLiteDatabase db) {
        if (db.isReadOnly()) {
            throw new IllegalStateException("Database must be writable");
        }
        mDatabase = db;
    }

    @NonNull
    public String getUser() {
        try (Cursor cursor = mDatabase.query(CommonDataColumns.TABLE_NAME,
                new String[]{CommonDataColumns.VALUE.getColumnName()},
                CommonDataColumns.KEY.getColumnName() + " = ?",
                new String[]{CommonData.USER_KEY},
                null,
                null,
                null)) {

            List<String> values = new ArrayList<>();
            while (cursor.moveToNext()) {
                values.add(cursor.getString(0));
            }
            if (values.size() == 0) {
                throw new IllegalStateException("User is not specified");
            } else if (values.size() > 1) {
                throw new IllegalStateException("There are more than one user in DB");
            } else {
                return Objects.requireNonNull(values.get(0), "User in the DB is null");
            }
        }
    }
}

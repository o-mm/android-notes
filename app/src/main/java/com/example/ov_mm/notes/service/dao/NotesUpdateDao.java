package com.example.ov_mm.notes.service.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.db.tables.NotesUpdateColumns;
import com.example.ov_mm.notes.model.NotesUpdate;

import java.util.Date;

public class NotesUpdateDao {

    @NonNull private final SQLiteDatabase mDatabase;

    public NotesUpdateDao(@NonNull SQLiteDatabase db) {
        mDatabase = db;
    }

    @Nullable
    public Long getLastVersion() {
        try (Cursor cursor = mDatabase.query(
                NotesUpdateColumns.TABLE_NAME,
                new String[]{NotesUpdateColumns.VERSION.getColumnName()},
                null,
                null,
                null,
                null,
                NotesUpdateColumns.VERSION.getColumnName() + " DESC",
                "1")) {

            Long version = null;
            while (cursor.moveToNext()) {
                version = cursor.getLong(0);
            }
            return version;
        }
    }

    public void save(@NonNull NotesUpdate notesUpdate) {
        mDatabase.insert(NotesUpdateColumns.TABLE_NAME, null, getContent(notesUpdate));
    }

    @NonNull
    private ContentValues getContent(@NonNull NotesUpdate notesUpdate) {
        ContentValues values = new ContentValues();
        values.put(NotesUpdateColumns.DATE.getColumnName(), notesUpdate.getDate().getTime());
        values.put(NotesUpdateColumns.VERSION.getColumnName(), notesUpdate.getVersion());
        return values;
    }

    public Date getLastUpdateDate() {
        try (Cursor cursor = mDatabase.query(
                NotesUpdateColumns.TABLE_NAME,
                new String[]{NotesUpdateColumns.DATE.getColumnName()},
                null,
                null,
                null,
                null,
                NotesUpdateColumns.DATE.getColumnName() + " DESC",
                "1")) {

            Date date = null;
            while (cursor.moveToNext()) {
                date = new Date(cursor.getLong(0));
            }
            return date;
        }
    }
}

package com.example.ov_mm.notes.db.tables;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.NotesDatabaseContract;

public enum NotesUpdateColumns implements NotesDatabaseContract.TableDefinition {
    ID("id", "INTEGER PRIMARY KEY"),
    DATE("date", "DATETIME NOT NULL"),
    VERSION("version", "INTEGER NOT NULL");

    public static final String TABLE_NAME = "notes_updates";
    private final String mColumnName;
    private final String mDefinition;

    NotesUpdateColumns(String columnName, String definition) {
        mColumnName = columnName;
        mDefinition = definition;
    }

    @NonNull
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @NonNull
    @Override
    public String getColumnName() {
        return mColumnName;
    }

    @NonNull
    @Override
    public String getDefinition() {
        return mDefinition;
    }
}

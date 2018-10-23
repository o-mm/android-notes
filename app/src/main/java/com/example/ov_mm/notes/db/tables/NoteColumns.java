package com.example.ov_mm.notes.db.tables;

import com.example.ov_mm.notes.db.NotesDatabaseContract;

import android.support.annotation.NonNull;

public enum NoteColumns implements NotesDatabaseContract.TableDefinition {
    ID("id", "INTEGER PRIMARY KEY"),
    GUID("guid", "TEXT"),
    TITLE("title", "TEXT"),
    CONTENT("content", "TEXT"),
    DATE("date", "DATETIME");

    public static final String TABLE_NAME = "notes";
    private final String mColumn;
    private final String mDefinition;

    NoteColumns(String column, String definition) {
        this.mColumn = column;
        this.mDefinition = definition;
    }

    @NonNull
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @NonNull
    @Override
    public String getColumnName() {
        return mColumn;
    }

    @NonNull
    @Override
    public String getDefinition() {
        return mDefinition;
    }
}

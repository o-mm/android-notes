package com.example.ov_mm.notes.db.tables;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.NotesDatabaseContract;

public enum CommonDataColumns implements NotesDatabaseContract.TableDefinition {
    ID("id", "INTEGER PRIMARY KEY"),
    KEY("key", "TEXT"),
    VALUE("value", "TEXT"),;

    public static final String TABLE_NAME = "common_data";
    private final String mColumn;
    private final String mDefinition;

    CommonDataColumns(String column, String definition) {
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

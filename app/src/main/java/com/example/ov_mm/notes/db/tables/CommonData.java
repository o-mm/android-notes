package com.example.ov_mm.notes.db.tables;

import com.example.ov_mm.notes.db.NotesDatabaseContract;

public enum CommonData implements NotesDatabaseContract.TableDefinition {
    ID("id", "INTEGER PRIMARY KEY"),
    KEY("key", "TEXT"),
    GUID("guid", "TEXT"),
    TIME("sync_time", "DATETIME"),
    VERSION("version", "INTEGER");

    public static final String TABLE_NAME = "common_data";
    private final String mColumn;
    private final String mDefinition;

    CommonData(String column, String definition) {
        this.mColumn = column;
        this.mDefinition = definition;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String getColumnName() {
        return mColumn;
    }

    public String getDefinition() {
        return mDefinition;
    }
}

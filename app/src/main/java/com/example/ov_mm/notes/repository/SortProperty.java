package com.example.ov_mm.notes.repository;

import com.example.ov_mm.notes.db.NoteColumns;

public enum SortProperty {
    DATE(NoteColumns.DATE),
    TITLE(NoteColumns.TITLE);

    private final NoteColumns mDbColumn;

    SortProperty(NoteColumns dbColumn) {
        mDbColumn = dbColumn;
    }

    NoteColumns getDbColumn() {
        return mDbColumn;
    }
}

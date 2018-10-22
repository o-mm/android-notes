package com.example.ov_mm.notes.db.migration;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.NotesDatabaseHelper;
import com.example.ov_mm.notes.db.tables.CommonData;
import com.example.ov_mm.notes.db.tables.NoteColumns;

public class CommonDataAdded implements Migration {

    @NonNull
    @Override
    public String getMigration() {
        StringBuilder builder = new StringBuilder();
        NotesDatabaseHelper.buildCreateTableQuery(CommonData.class, builder);
        builder.append("ALTER TABLE ")
                .append(NoteColumns.TABLE_NAME)
                .append(" ADD COLUMN IF NOT EXISTS ")
                .append(NoteColumns.GUID.getColumnName())
                .append(";\n");
        return builder.toString();
    }
}

package com.example.ov_mm.notes.db.migration;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.tables.CommonDataColumns;
import com.example.ov_mm.notes.db.tables.NoteColumns;
import com.example.ov_mm.notes.model.CommonData;

import java.util.Calendar;
import java.util.UUID;

public class InitialMigration implements Migration {

    @Override
    public void executeMigration(@NonNull SQLiteDatabase db) {
        db.execSQL(String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES " + generateValues(),
                NoteColumns.TABLE_NAME,
                NoteColumns.GUID.getColumnName(),
                NoteColumns.TITLE.getColumnName(),
                NoteColumns.CONTENT.getColumnName(),
                NoteColumns.DATE.getColumnName(),
                NoteColumns.SYNCED.getColumnName(),
                NoteColumns.DELETED.getColumnName()));
        db.execSQL(String.format("INSERT INTO %s (%s, %s) VALUES ('%s', '%s')",
                CommonDataColumns.TABLE_NAME,
                CommonDataColumns.KEY.getColumnName(),
                CommonDataColumns.VALUE.getColumnName(),
                CommonData.USER_KEY,
                CommonData.USER_ID));
    }

    @NonNull
    private String generateValues() {
        StringBuilder values = new StringBuilder();
        for (int i = 0; i < 45; i++) {
            if (i > 0) {
                values.append(",\n");
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(2018, 0, i, Math.abs(24 - i), 0);
            values.append("('").append(UUID.randomUUID()).append("',");
            values.append("'Note number ").append(i + 1).append("', '");
            values.append(generateContent(i + 1)).append("', ");
            values.append(calendar.getTimeInMillis()).append(", ");
            values.append("0").append(", ");
            values.append("0").append(")");
        }
        return values.toString();
    }

    @NonNull
    private String generateContent(int number) {
        StringBuilder todoList = new StringBuilder();
        for (int i = 0; i < number; i++) {
            if (i > 0) {
                todoList.append(" \n");
            }
            todoList.append(i + 1).append(". TODO task number ").append(i + 1).append(";");
        }
        return todoList.toString();
    }
}

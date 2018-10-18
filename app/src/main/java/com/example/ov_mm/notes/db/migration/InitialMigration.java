package com.example.ov_mm.notes.db.migration;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.NoteColumns;

import java.util.Calendar;

public class InitialMigration implements Migration {

    @NonNull
    @Override
    public String getMigration() {
        return String.format("INSERT INTO %s (%s, %s, %s) VALUES " + generateValues() + ";",
                NoteColumns.TABLE_NAME, NoteColumns.TITLE.getColumnName(), NoteColumns.CONTENT.getColumnName(), NoteColumns.DATE.getColumnName());
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
            values.append("('Note number ").append(i + 1).append("', '");
            values.append(generateContent(i + 1)).append("', ");
            values.append(calendar.getTimeInMillis()).append(")");
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

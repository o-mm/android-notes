package com.example.ov_mm.notes.db.migration;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.tables.CommonDataColumns;
import com.example.ov_mm.notes.model.CommonData;

public class InitialMigration implements Migration {

    @Override
    public void executeMigration(@NonNull SQLiteDatabase db) {
        db.execSQL(String.format("INSERT INTO %s (%s, %s) VALUES ('%s', '%s')",
                CommonDataColumns.TABLE_NAME,
                CommonDataColumns.KEY.getColumnName(),
                CommonDataColumns.VALUE.getColumnName(),
                CommonData.USER_KEY,
                CommonData.USER_ID));
    }
}

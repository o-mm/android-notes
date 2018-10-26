package com.example.ov_mm.notes.db.migration;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.NotesDatabaseContract;
import com.example.ov_mm.notes.db.NotesDatabaseHelper;
import com.example.ov_mm.notes.db.tables.NoteColumns;

public class CommonDataAdded implements Migration {

    @Override
    public void executeMigration(@NonNull SQLiteDatabase db) {
        db.execSQL("DROP TABLE " + NoteColumns.TABLE_NAME);
        for (Class<? extends NotesDatabaseContract.TableDefinition> cls : NotesDatabaseContract.TABLES) {
            db.execSQL(NotesDatabaseHelper.buildCreateTableQuery(cls));
        }
        new InitialMigration().executeMigration(db);
    }
}

package com.example.ov_mm.notes.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ov_mm.notes.db.migration.InitialMigration;
import com.example.ov_mm.notes.db.migration.Migration;

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "NotesDatabaseHelper";
    private static final String DATABASE_NAME = "notes.db";

    public NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, NotesDatabaseContract.MIGRATIONS.size() + 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder createQuery = new StringBuilder();
        for (Class<? extends NotesDatabaseContract.TableDefinition> cls : NotesDatabaseContract.TABLES) {
            if (!cls.isEnum())
                throw new UnsupportedOperationException("All classes describing tables must be enums");
            NotesDatabaseContract.TableDefinition[] columns = cls.getEnumConstants();
            createQuery
                    .append("CREATE TABLE ")
                    .append(columns[0].getTableName())
                    .append(" (\n");

            for (int i = 0; i < columns.length; i++) {
                if (i > 0) {
                    createQuery.append(", \n");
                }
                createQuery
                        .append(columns[i].getColumnName())
                        .append(" ")
                        .append(columns[i].getDefinition());
            }
            createQuery.append(");\n");
        }
        db.execSQL(createQuery.toString());
        db.execSQL(new InitialMigration().getMigration());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            StringBuilder fullMigration = new StringBuilder();
            for (Class<? extends Migration> migration : NotesDatabaseContract.MIGRATIONS.subList(oldVersion - 1, newVersion - 1)) {
                fullMigration.append(migration.newInstance().getMigration()).append("\n");
            }
            db.execSQL(fullMigration.toString());
        } catch (IllegalAccessException | InstantiationException | SQLException e) {
            Log.e(TAG, "Unable to apply migrations from %d version to %d version", e);
        }
    }
}

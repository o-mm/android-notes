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

    public static String buildCreateTableQuery(Class<? extends NotesDatabaseContract.TableDefinition> cls) {
        StringBuilder builder = new StringBuilder();
        if (!cls.isEnum())
            throw new UnsupportedOperationException("All classes describing tables must be enums");
        NotesDatabaseContract.TableDefinition[] columns = cls.getEnumConstants();
        builder
                .append("CREATE TABLE ")
                .append(columns[0].getTableName())
                .append(" (\n");

        for (int i = 0; i < columns.length; i++) {
            if (i > 0) {
                builder.append(", \n");
            }
            builder
                    .append(columns[i].getColumnName())
                    .append(" ")
                    .append(columns[i].getDefinition());
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            for (Class<? extends NotesDatabaseContract.TableDefinition> cls : NotesDatabaseContract.TABLES) {
                db.execSQL(buildCreateTableQuery(cls));
            }
            new InitialMigration().executeMigration(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();
        try {
            for (Class<? extends Migration> migration : NotesDatabaseContract.MIGRATIONS.subList(oldVersion - 1, newVersion - 1)) {
                migration.newInstance().executeMigration(db);
            }
            db.setTransactionSuccessful();
        } catch (IllegalAccessException | InstantiationException | SQLException e) {
            Log.e(TAG, "Unable to apply migrations from %d version to %d version", e);
        } finally {
            db.endTransaction();
        }
    }
}

package com.example.ov_mm.notes.db;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TransactionManager {

    @NonNull private final SQLiteDatabase mDatabase;

    public TransactionManager(@NonNull SQLiteDatabase db) {
        if (db.isReadOnly()) {
            throw new IllegalStateException("Database must be writable");
        }
        mDatabase = db;
    }

    @Nullable
    public <T> T doInTransaction(@NonNull DbExecution<T> dbExecution) {
        mDatabase.beginTransaction();
        try {
            T result = dbExecution.execute(mDatabase);
            mDatabase.setTransactionSuccessful();
            return result;
        } finally {
            mDatabase.endTransaction();
        }
    }

    public interface DbExecution<T> {
        @Nullable
        T execute(@NonNull SQLiteDatabase database);
    }
}

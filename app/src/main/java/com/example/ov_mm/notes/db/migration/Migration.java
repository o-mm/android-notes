package com.example.ov_mm.notes.db.migration;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

public interface Migration {

    void executeMigration(@NonNull SQLiteDatabase db);
}

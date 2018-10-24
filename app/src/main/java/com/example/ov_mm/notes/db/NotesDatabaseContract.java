package com.example.ov_mm.notes.db;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.migration.CommonDataAdded;
import com.example.ov_mm.notes.db.migration.Migration;
import com.example.ov_mm.notes.db.tables.CommonDataColumns;
import com.example.ov_mm.notes.db.tables.NoteColumns;
import com.example.ov_mm.notes.db.tables.NotesUpdateColumns;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class NotesDatabaseContract {

    @NonNull public static final List<Class<? extends TableDefinition>> TABLES = Collections.unmodifiableList(Arrays.<Class<? extends TableDefinition>>asList(
            NoteColumns.class,
            NotesUpdateColumns.class,
            CommonDataColumns.class
    ));

    @NonNull static final List<Class<? extends Migration>> MIGRATIONS = Collections.unmodifiableList(Arrays.<Class<? extends Migration>>asList(
            CommonDataAdded.class
    ));

    private NotesDatabaseContract() {
        throw new UnsupportedOperationException("Class " + getClass().getName() + " can not be instantiated");
    }

    public interface TableDefinition {

        @NonNull
        String getTableName();
        @NonNull
        String getColumnName();
        @NonNull
        String getDefinition();
    }

}

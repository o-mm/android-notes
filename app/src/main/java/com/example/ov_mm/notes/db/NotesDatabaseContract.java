package com.example.ov_mm.notes.db;

import com.example.ov_mm.notes.db.migration.CommonDataAdded;
import com.example.ov_mm.notes.db.migration.Migration;
import com.example.ov_mm.notes.db.tables.CommonData;
import com.example.ov_mm.notes.db.tables.NoteColumns;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class NotesDatabaseContract {

    static final List<Class<? extends TableDefinition>> TABLES = Collections.unmodifiableList(Arrays.<Class<? extends TableDefinition>>asList(
            NoteColumns.class,
            CommonData.class
    ));

    static final List<Class<? extends Migration>> MIGRATIONS = Collections.unmodifiableList(Arrays.<Class<? extends Migration>>asList(
            CommonDataAdded.class
    ));

    private NotesDatabaseContract() {
        throw new UnsupportedOperationException("Class " + getClass().getName() + " can not be instantiated");
    }

    public interface TableDefinition {

        String getTableName();
        String getColumnName();
        String getDefinition();
    }

}

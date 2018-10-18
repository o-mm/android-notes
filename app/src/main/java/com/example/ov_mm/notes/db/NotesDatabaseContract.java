package com.example.ov_mm.notes.db;

import com.example.ov_mm.notes.db.migration.Migration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class NotesDatabaseContract {

    static final List<Class<? extends TableDefinition>> TABLES = Collections.unmodifiableList(Arrays.<Class<? extends TableDefinition>>asList(
            NoteColumns.class
    ));

    static final List<Class<? extends Migration>> MIGRATIONS = Collections.unmodifiableList(Collections.<Class<? extends Migration>>emptyList());

    private NotesDatabaseContract() {
        throw new UnsupportedOperationException("Class " + getClass().getName() + " can not be instantiated");
    }

    public interface TableDefinition {

        String getTableName();
        String getColumnName();
        String getDefinition();
    }

}

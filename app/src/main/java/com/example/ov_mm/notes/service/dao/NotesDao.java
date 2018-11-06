package com.example.ov_mm.notes.service.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.db.tables.NoteColumns;
import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.util.Function;
import com.example.ov_mm.notes.util.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class NotesDao {

    @NonNull private final SQLiteDatabase mDatabase;
    @NonNull private final List<NoteColumns> mSearchColumns = Arrays.asList(NoteColumns.TITLE, NoteColumns.CONTENT);

    public NotesDao(@NonNull SQLiteDatabase db) {
        if (db.isReadOnly()) {
            throw new IllegalStateException("Database must be writable");
        }
        mDatabase = db;
    }

    @NonNull
    public Note saveNote(@NonNull Note note) {
        if (note.getGuid() == null) {
            note.setGuid(UUID.randomUUID().toString());
        }
        if (note.getId() == null || update(note) == 0) {
            note.setId(insert(note));
        }
        return note;
    }

    public void removeNote(@NonNull Note note) {
        if (note.getId() == null) {
            return; //does not exist in DB
        }
        mDatabase.delete(NoteColumns.TABLE_NAME, NoteColumns.ID.getColumnName() + " = ?", new String[]{note.getId().toString()});
    }

    @NonNull
    public List<Note> getNotes(@Nullable String term, @Nullable NoteColumns column, boolean desc) {
        String orderBy = column == null ? null : column.getColumnName() + (desc ? " DESC" : " ASC");
        if (term == null) {
            return executeQuery(NoteColumns.DELETED.getColumnName() + " = ?", new String[]{String.valueOf(0)}, orderBy);
        }
        StringBuilder where = new StringBuilder();
        where.append(NoteColumns.DELETED.getColumnName()).append(" = ? AND (");
        for (NoteColumns col : mSearchColumns) {
            if (where.length() != 0) {
                where.append(" OR ");
            }
            where.append("lower(").append(col.getColumnName()).append(") LIKE ?");
        }
        where.append(")");
        String[] params = new String[mSearchColumns.size() + 1];
        params[0] = String.valueOf(0);
        Arrays.fill(params, 1, params.length, "%" + term.toLowerCase() + "%");
        return executeQuery(where.toString(), params, orderBy);
    }

    @Nullable
    public Note getNote(@NonNull Long id) {
        List<Note> notes = executeQuery(NoteColumns.ID.getColumnName() + " = ? AND " + NoteColumns.DELETED.getColumnName() + " = ?",
                new String[]{id.toString(), String.valueOf(0)}, null);
        if (notes.isEmpty()) {
            return null;
        } else if (notes.size() > 1) {
            throw new IllegalStateException("There are more than one note with the same ID in DB");
        }
        return notes.get(0);
    }

    private boolean exists(@NonNull String guid) {
        try (Cursor cursor = mDatabase.query(NoteColumns.TABLE_NAME,
                new String[]{NoteColumns.ID.getColumnName()}, NoteColumns.GUID.getColumnName() + " = ?", new String[]{guid}, null, null, null)) {

            if (cursor.getCount() > 1) {
                throw new IllegalStateException("There are more than one note with the same ID in DB");
            }
            return cursor.getCount() == 1;
        }
    }

    @NonNull
    public List<Note> getUnsyncedNotes() {
        return executeQuery(NoteColumns.SYNCED.getColumnName() + " = ?", new String[]{String.valueOf(0)}, null);
    }

    public void saveNotes(Collection<Note> notes) {
        for (Note note : notes) {
            if (!exists(note.getGuid())) {
                saveNote(note);
            } else {
                updateFromRemote(note);
            }
        }
    }

    @NonNull
    private List<Note> readFromCursor(Cursor cursor) {
        List<Note> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Note note = new Note();
            note.setId(cursor.getLong(cursor.getColumnIndexOrThrow(NoteColumns.ID.getColumnName())));
            note.setGuid(cursor.getString(cursor.getColumnIndexOrThrow(NoteColumns.GUID.getColumnName())));
            note.setDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(NoteColumns.DATE.getColumnName()))));
            note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(NoteColumns.TITLE.getColumnName())));
            note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(NoteColumns.CONTENT.getColumnName())));
            note.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow(NoteColumns.SYNCED.getColumnName())) == 1);
            note.setSynced(cursor.getInt(cursor.getColumnIndexOrThrow(NoteColumns.DELETED.getColumnName())) == 1);
            result.add(note);
        }
        return result;
    }

    @NonNull
    private ContentValues getValues(@NonNull Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteColumns.TITLE.getColumnName(), note.getTitle());
        values.put(NoteColumns.GUID.getColumnName(), note.getGuid());
        values.put(NoteColumns.CONTENT.getColumnName(), note.getContent());
        values.put(NoteColumns.DATE.getColumnName(), note.getDate().getTime());
        values.put(NoteColumns.SYNCED.getColumnName(), note.isSynced() ? 1 : 0);
        values.put(NoteColumns.DELETED.getColumnName(), note.isDeleted() ? 1 : 0);
        return values;
    }

    private int update(@NonNull Note note) {
        return mDatabase.update(NoteColumns.TABLE_NAME, getValues(note), NoteColumns.ID.getColumnName() + " = ?",
                new String[]{note.getId().toString()});
    }

    private void updateFromRemote(@NonNull Note note) {
        mDatabase.update(NoteColumns.TABLE_NAME, getValues(note),
                NoteColumns.GUID.getColumnName() + " = ?",
                new String[]{note.getGuid()});
    }

    private long insert(@NonNull Note note) {
        return mDatabase.insert(NoteColumns.TABLE_NAME, null, getValues(note));
    }

    @NonNull
    private List<Note> executeQuery(@Nullable String where, @Nullable String[] whereParams, @Nullable String orderBy) {
        try (Cursor cursor = mDatabase.query(
                NoteColumns.TABLE_NAME,
                ListUtils.map(NoteColumns.values(), String.class, new Function<NoteColumns, String>() {
                    @Override
                    public String apply(NoteColumns noteColumn) {
                        return noteColumn.getColumnName();
                    }
                }),
                where,
                whereParams,
                null,
                null,
                orderBy)) {
            return readFromCursor(cursor);
        }
    }

    public void fillDatabase() {
        try { //pretend a long operation
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        mDatabase.execSQL(String.format(
            "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES " + generateValues(),
            NoteColumns.TABLE_NAME,
            NoteColumns.GUID.getColumnName(),
            NoteColumns.TITLE.getColumnName(),
            NoteColumns.CONTENT.getColumnName(),
            NoteColumns.DATE.getColumnName(),
            NoteColumns.SYNCED.getColumnName(),
            NoteColumns.DELETED.getColumnName()));
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
            values.append("('").append(UUID.randomUUID()).append("',");
            values.append("'Note number ").append(i + 1).append("', '");
            values.append(generateContent(i + 1)).append("', ");
            values.append(calendar.getTimeInMillis()).append(", ");
            values.append("0").append(", ");
            values.append("0").append(")");
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

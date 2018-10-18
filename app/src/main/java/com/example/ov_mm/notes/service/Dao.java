package com.example.ov_mm.notes.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.db.NoteColumns;
import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.util.Function;
import com.example.ov_mm.notes.util.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class Dao {

    @NonNull private final SQLiteDatabase mDatabase;
    @NonNull private final List<NoteColumns> mSearchColumns = Arrays.asList(NoteColumns.TITLE, NoteColumns.CONTENT);

    @Inject
    public Dao(@NonNull SQLiteDatabase db) {
        if (db.isReadOnly())
            throw new IllegalStateException("Database must be writable");
        mDatabase = db;
    }

    public Note saveNote(@NonNull Note note) {
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
            return executeQuery(null, null, orderBy);
        }
        StringBuilder where = new StringBuilder();
        for (NoteColumns col : mSearchColumns) {
            if (where.length() != 0) {
                where.append(" OR ");
            }
            where.append("lower(").append(col.getColumnName()).append(") LIKE ?");
        }
        String[] params = new String[mSearchColumns.size()];
        Arrays.fill(params, "%" + term.toLowerCase() + "%");
        return executeQuery(where.toString(), params, orderBy);
    }

    @Nullable
    public Note getNote(@NonNull Long id) {
        List<Note> notes = executeQuery(NoteColumns.ID + " = ?", new String[]{id.toString()}, null);
        if (notes.isEmpty()) {
            return null;
        } else if (notes.size() > 1) {
            throw new IllegalStateException("There are more than one note with the same ID in DB");
        }
        return notes.get(0);
    }

    private List<Note> readFromCursor(Cursor cursor) {
        List<Note> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Note note = new Note();
            note.setId(cursor.getLong(cursor.getColumnIndexOrThrow(NoteColumns.ID.getColumnName())));
            note.setDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(NoteColumns.DATE.getColumnName()))));
            note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(NoteColumns.TITLE.getColumnName())));
            note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(NoteColumns.CONTENT.getColumnName())));
            result.add(note);
        }
        return result;
    }

    @NonNull
    private ContentValues getValues(@NonNull Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteColumns.TITLE.getColumnName(), note.getTitle());
        values.put(NoteColumns.CONTENT.getColumnName(), note.getContent());
        values.put(NoteColumns.DATE.getColumnName(), note.getDate().getTime());
        return values;
    }

    private int update(@NonNull Note note) {
        return mDatabase.update(NoteColumns.TABLE_NAME, getValues(note), NoteColumns.ID.getColumnName() + " = ?",
                new String[]{note.getId().toString()});
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
}

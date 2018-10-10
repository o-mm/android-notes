package com.example.ov_mm.notes.service;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.model.Note;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//TODO database
public class Dao {

    private static final Map<Long, Note> NOTES = new HashMap<>();

    private static long sNoteId = 1;

    static {
        while (sNoteId <= 30) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2018, 0, (int) sNoteId, (int) Math.abs(24 - sNoteId), 0);
            NOTES.put(sNoteId, Note.create(sNoteId, calendar.getTime(), "Note number " + sNoteId, generateContent(sNoteId)));
            sNoteId++;
        }
    }

    private static String generateContent(long id) {
        StringBuilder builder = new StringBuilder();
        for (long i = 0; i < id / 2; i++) {
            builder.append("Имена классов, полей, переменных и параметров должны выбираться из слов " +
                    "английского языка (использование транслитерации не допускается) " +
                    "либо устоявшихся аббревиатуры (при этом аббревиатуры следует использовать " +
                    "как слова). Не следует использовать малоизвестные аббревиатуры.\n");
        }
        return builder.toString();
    }

    @NonNull
    public Collection<Note> getAllNotes() {
        return Collections.unmodifiableCollection(NOTES.values());
    }

    public Note saveNote(@NonNull Note note) {
        if (note.getId() == null) {
            note.setId(sNoteId++);
        }
        NOTES.put(note.getId(), note);
        return note;
    }

    public void removeNote(@NonNull Note note) {
        NOTES.remove(note.getId());
    }

    @NonNull
    public Collection<Note> getNotes(@NonNull String query) {
        String lowerCaseQuery = query.toLowerCase();
        List<Note> result = new ArrayList<>(NOTES.values());
        Iterator<Note> iterator = result.iterator();
        while (iterator.hasNext()) {
            Note next = iterator.next();
            if ((next.getTitle() == null || !next.getTitle().toLowerCase().contains(lowerCaseQuery)) && (
                    next.getContent() == null || !next.getContent().toLowerCase().contains(lowerCaseQuery))) {
                iterator.remove();
            }
        }
        return Collections.unmodifiableCollection(result);
    }
}

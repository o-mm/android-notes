package com.example.ov_mm.notes.service;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.model.Note;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

    public List<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>(NOTES.values());
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return Collections.unmodifiableList(notes);
    }

    public Note saveNote(Note note) {
        if (note.getId() == null) {
            note.setId(sNoteId++);
        }
        NOTES.put(note.getId(), note);
        return note;
    }

    public void removeNote(@NonNull Note note) {
        NOTES.remove(note.getId());
    }
}

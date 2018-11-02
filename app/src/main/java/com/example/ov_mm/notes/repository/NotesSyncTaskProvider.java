package com.example.ov_mm.notes.repository;

import android.support.annotation.NonNull;

public interface NotesSyncTaskProvider {

    @NonNull
    NotesSyncTask provideNotesSyncTask();
}

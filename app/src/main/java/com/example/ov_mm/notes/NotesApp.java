package com.example.ov_mm.notes;

import android.app.Application;

import com.example.ov_mm.notes.di.DaggerNotesAppComponent;
import com.example.ov_mm.notes.di.NotesAppComponent;

public class NotesApp extends Application {

    private NotesAppComponent mNotesAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotesAppComponent = DaggerNotesAppComponent.builder().build();
    }

    public NotesAppComponent getNotesAppComponent() {
        return mNotesAppComponent;
    }
}
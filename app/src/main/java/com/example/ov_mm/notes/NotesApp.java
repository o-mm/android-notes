package com.example.ov_mm.notes;

import android.app.Application;

import com.example.ov_mm.notes.di.DaggerNotesAppComponent;
import com.example.ov_mm.notes.di.DaoModule;
import com.example.ov_mm.notes.di.NotesAppComponent;

public class NotesApp extends Application {

    private NotesAppComponent mNotesAppComponent;

    @Override
    public void onCreate() {
        mNotesAppComponent = DaggerNotesAppComponent.builder().daoModule(new DaoModule(this)).build();
        super.onCreate();
    }

    public NotesAppComponent getNotesAppComponent() {
        return mNotesAppComponent;
    }
}

package com.example.ov_mm.notes.ui.di;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ov_mm.notes.NotesApp;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivitySubComponent mActivitySubComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySubComponent = ((NotesApp) getApplication()).getNotesAppComponent().activityComponent(new ActivityModule(this));
    }

    protected ActivitySubComponent getActivitySubComponent() {
        return mActivitySubComponent;
    }
}

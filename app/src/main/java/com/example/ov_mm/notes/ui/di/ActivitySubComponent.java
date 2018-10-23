package com.example.ov_mm.notes.ui.di;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.ui.ViewNotesActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivitySubComponent {

    void inject(@NonNull ViewNotesActivity activity);
}

package com.example.ov_mm.notes.ui.di;

import com.example.ov_mm.notes.ui.ViewNotesActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivitySubComponent {

    void inject(ViewNotesActivity activity);
}

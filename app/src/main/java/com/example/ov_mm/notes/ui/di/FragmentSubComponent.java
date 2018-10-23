package com.example.ov_mm.notes.ui.di;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.ui.EditNoteFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = FragmentModule.class)
public interface FragmentSubComponent {

    void inject(@NonNull EditNoteFragment fragment);
}

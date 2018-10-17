package com.example.ov_mm.notes.ui.di;

import com.example.ov_mm.notes.ui.ViewNotesActivity;
import com.example.ov_mm.notes.vm.EditNoteVm;
import com.example.ov_mm.notes.vm.ViewNotesVm;

import dagger.Component;

@ActivityScope
@Component(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(ViewNotesActivity activity);

    EditNoteVm editNoteVm();

    ViewNotesVm viewNotesVm();
}

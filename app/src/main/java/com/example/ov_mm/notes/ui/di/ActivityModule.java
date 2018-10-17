package com.example.ov_mm.notes.ui.di;

import android.arch.lifecycle.ViewModelProviders;

import com.example.ov_mm.notes.vm.EditNoteVm;
import com.example.ov_mm.notes.vm.ViewNotesVm;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private BaseActivity mActivity;

    public ActivityModule(BaseActivity activity) {
        mActivity = activity;
    }

    @ActivityScope
    @Provides
    EditNoteVm provideEditNote() {
        return ViewModelProviders.of(mActivity).get(EditNoteVm.class);
    }

    @ActivityScope
    @Provides
    ViewNotesVm provideViewNotesVm() {
        return ViewModelProviders.of(mActivity).get(ViewNotesVm.class);
    }
}

package com.example.ov_mm.notes.ui.di;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;

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
    ViewNotesVm provideViewNotesVm(ViewModelProvider.Factory factory) {
        return ViewModelProviders.of(mActivity, factory).get(ViewNotesVm.class);
    }

    @ActivityScope
    @Provides
    ViewModelProvider.Factory provideFactory(RepoVmFactory factory) {
        return factory;
    }
}

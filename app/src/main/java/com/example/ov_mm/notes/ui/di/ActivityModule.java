package com.example.ov_mm.notes.ui.di;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.example.ov_mm.notes.repository.NotesRepository;
import com.example.ov_mm.notes.vm.ViewNotesVm;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    @NonNull private BaseActivity mActivity;

    public ActivityModule(@NonNull BaseActivity activity) {
        mActivity = activity;
    }

    @ActivityScope
    @Provides
    @NonNull
    ViewNotesVm provideViewNotesVm(@NonNull ViewModelProvider.Factory factory) {
        return ViewModelProviders.of(mActivity, factory).get(ViewNotesVm.class);
    }

    @ActivityScope
    @Provides
    @NonNull
    ViewModelProvider.Factory provideFactory(final NotesRepository repository) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                if (ViewNotesVm.class.equals(modelClass)) {
                    return (T) new ViewNotesVm(repository);
                } else if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
                    return ViewModelProvider.AndroidViewModelFactory.getInstance(mActivity.getApplication()).create(modelClass);
                } else {
                    return new ViewModelProvider.NewInstanceFactory().create(modelClass);
                }
            }
        };
    }
}

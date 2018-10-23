package com.example.ov_mm.notes.ui.di;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.example.ov_mm.notes.repository.NotesRepository;
import com.example.ov_mm.notes.vm.EditNoteVm;

import dagger.Module;
import dagger.Provides;

@Module()
public class FragmentModule {

    @NonNull private final Fragment mFragment;

    public FragmentModule(@NonNull Fragment fragment) {
        mFragment = fragment;
    }

    @FragmentScope
    @Provides
    @NonNull
    EditNoteVm provideEditNoteVm(@NonNull ViewModelProvider.Factory factory) {
        return ViewModelProviders.of(mFragment, factory).get(EditNoteVm.class);
    }

    @FragmentScope
    @Provides
    @NonNull
    ViewModelProvider.Factory provideFactory(final NotesRepository repository) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                if (EditNoteVm.class.equals(modelClass)) {
                    return (T) new EditNoteVm(repository);
                } else if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
                    return ViewModelProvider.AndroidViewModelFactory.getInstance(mFragment.requireActivity().getApplication()).create(modelClass);
                } else {
                    return new ViewModelProvider.NewInstanceFactory().create(modelClass);
                }
            }
        };
    }
}

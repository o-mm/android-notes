package com.example.ov_mm.notes.di;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.event.EventManager;
import com.example.ov_mm.notes.repository.NotesRepository;
import com.example.ov_mm.notes.ui.di.ActivityModule;
import com.example.ov_mm.notes.ui.di.ActivitySubComponent;
import com.example.ov_mm.notes.ui.di.FragmentModule;
import com.example.ov_mm.notes.ui.di.FragmentSubComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = RepositoryModule.class)
public interface NotesAppComponent {

    @NonNull
    NotesRepository repository();

    @NonNull
    ActivitySubComponent activityComponent(ActivityModule activityModule);

    @NonNull
    FragmentSubComponent fragmentSubComponent(FragmentModule fragmentModule);

    @NonNull
    EventManager eventManager();
}

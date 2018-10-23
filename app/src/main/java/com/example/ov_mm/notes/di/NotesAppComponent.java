package com.example.ov_mm.notes.di;

import com.example.ov_mm.notes.repository.NotesRepository;
import com.example.ov_mm.notes.ui.di.ActivityModule;
import com.example.ov_mm.notes.ui.di.ActivitySubComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = RepositoryModule.class)
public interface NotesAppComponent {

    NotesRepository repository();

    ActivitySubComponent activityComponent(ActivityModule activityModule);
}

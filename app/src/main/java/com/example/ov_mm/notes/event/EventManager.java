package com.example.ov_mm.notes.event;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventManager {

    @NonNull private final MutableLiveData<Boolean> generatingRunning = new MutableLiveData<>();

    @Inject
    public EventManager() {}

    @NonNull
    public LiveData<Boolean> getGeneratingRunning() {
        return generatingRunning;
    }

    public void startGeneration() {
        generatingRunning.setValue(true);
    }

    public void stopGenerating() {
        generatingRunning.postValue(false);
    }
}

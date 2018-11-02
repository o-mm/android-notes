package com.example.ov_mm.notes.vm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.ov_mm.notes.event.EventManager;
import com.example.ov_mm.notes.ui.service.GenerateNotesService;

public class ActivityViewModel extends AndroidViewModel {

    @NonNull private final EventManager mEventManager;

    public ActivityViewModel(@NonNull Application application, @NonNull EventManager eventManager) {
        super(application);
        mEventManager = eventManager;
    }

    public void startService() {
        mEventManager.startGeneration();
        getApplication().startService(new Intent(getApplication(), GenerateNotesService.class));
    }

    public LiveData<Boolean> getGeneratingRunning() {
        return mEventManager.getGeneratingRunning();
    }
}

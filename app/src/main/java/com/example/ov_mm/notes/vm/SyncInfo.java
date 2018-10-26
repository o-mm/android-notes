package com.example.ov_mm.notes.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SyncInfo {

    @NonNull private final MutableLiveData<Boolean> mRunning = new MutableLiveData<>();
    @NonNull private final MutableLiveData<SyncResult> mSyncResult = new MutableLiveData<>();

    @NonNull
    public LiveData<Boolean> getRunning() {
        return mRunning;
    }

    @NonNull
    public LiveData<SyncResult> getSyncResult() {
        return mSyncResult;
    }

    void setRunning(@Nullable Boolean running) {
        mRunning.setValue(running);
    }

    void setSyncResult(@Nullable SyncResult syncResult) {
        mSyncResult.setValue(syncResult);
    }

    public void postSyncResult(SyncResult syncResult) {
        mSyncResult.postValue(syncResult);
    }

    public void postRunning(boolean running) {
        mRunning.postValue(running);
    }

    public enum SyncResult {
        SUCCESS,
        FAILED
    }
}

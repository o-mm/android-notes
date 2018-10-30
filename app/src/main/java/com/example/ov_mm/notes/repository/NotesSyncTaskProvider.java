package com.example.ov_mm.notes.repository;

import android.support.annotation.NonNull;
import android.support.v4.util.Consumer;

import com.example.ov_mm.notes.vm.SyncInfo;

public interface NotesSyncTaskProvider {

    @NonNull
    NotesSyncTask provideNotesSyncTask(@NonNull Consumer<SyncInfo.SyncResult> asyncConsumer);
}

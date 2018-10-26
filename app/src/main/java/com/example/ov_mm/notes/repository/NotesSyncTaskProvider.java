package com.example.ov_mm.notes.repository;

import android.support.annotation.NonNull;
import android.support.v4.util.Consumer;

import com.example.ov_mm.notes.service.dao.CommonDataDao;
import com.example.ov_mm.notes.service.dao.NotesDao;
import com.example.ov_mm.notes.service.dao.NotesUpdateDao;
import com.example.ov_mm.notes.service.network.RemoteNotesService;
import com.example.ov_mm.notes.vm.SyncInfo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NotesSyncTaskProvider {

    @NonNull private final NotesDao mNotesDao;
    @NonNull private final NotesUpdateDao mNotesUpdateDao;
    @NonNull private final CommonDataDao mCommonDataDao;
    @NonNull private final RemoteNotesService mRemoteNotesService;
    @NonNull private final Lock notesSynchronizationLock = new ReentrantLock();

    public NotesSyncTaskProvider(@NonNull NotesDao notesDao,
                                 @NonNull NotesUpdateDao notesUpdateDao,
                                 @NonNull CommonDataDao commonDataDao,
                                 @NonNull RemoteNotesService remoteNotesService) {

        mNotesDao = notesDao;
        mNotesUpdateDao = notesUpdateDao;
        mCommonDataDao = commonDataDao;
        mRemoteNotesService = remoteNotesService;
    }

    public NotesSyncTask provideNotesSyncTask(Consumer<SyncInfo.SyncResult> asyncConsumer) {
        return new NotesSyncTask(
                asyncConsumer,
                mRemoteNotesService,
                mCommonDataDao,
                mNotesDao,
                mNotesUpdateDao,
                notesSynchronizationLock);
    }
}

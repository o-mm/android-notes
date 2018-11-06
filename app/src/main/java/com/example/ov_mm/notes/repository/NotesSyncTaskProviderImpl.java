package com.example.ov_mm.notes.repository;

import android.support.annotation.NonNull;
import android.support.v4.util.Consumer;

import com.example.ov_mm.notes.db.TransactionManager;
import com.example.ov_mm.notes.service.dao.CommonDataDao;
import com.example.ov_mm.notes.service.dao.NotesDao;
import com.example.ov_mm.notes.service.dao.NotesUpdateDao;
import com.example.ov_mm.notes.service.network.RemoteNotesService;
import com.example.ov_mm.notes.vm.SyncInfo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NotesSyncTaskProviderImpl implements NotesSyncTaskProvider {

    @NonNull private final NotesDao mNotesDao;
    @NonNull private final NotesUpdateDao mNotesUpdateDao;
    @NonNull private final CommonDataDao mCommonDataDao;
    @NonNull private final RemoteNotesService mRemoteNotesService;
    @NonNull private final TransactionManager mTransactionManager;
    @NonNull private final Lock notesSynchronizationLock = new ReentrantLock();

    public NotesSyncTaskProviderImpl(@NonNull NotesDao notesDao,
                                     @NonNull NotesUpdateDao notesUpdateDao,
                                     @NonNull CommonDataDao commonDataDao,
                                     @NonNull RemoteNotesService remoteNotesService,
                                     @NonNull TransactionManager transactionManager) {

        mNotesDao = notesDao;
        mNotesUpdateDao = notesUpdateDao;
        mCommonDataDao = commonDataDao;
        mRemoteNotesService = remoteNotesService;
        mTransactionManager = transactionManager;
    }

    @NonNull
    @Override
    public NotesSyncTask provideNotesSyncTask(@NonNull Consumer<SyncInfo.SyncResult> asyncConsumer) {
        return new NotesSyncTaskImpl(
            asyncConsumer,
            mRemoteNotesService,
            mCommonDataDao,
            mNotesDao,
            mNotesUpdateDao,
            mTransactionManager,
            notesSynchronizationLock);
    }
}

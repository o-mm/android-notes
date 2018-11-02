package com.example.ov_mm.notes.repository;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.db.TransactionManager;
import com.example.ov_mm.notes.service.dao.CommonDataDao;
import com.example.ov_mm.notes.service.dao.NotesDao;
import com.example.ov_mm.notes.service.dao.NotesUpdateDao;
import com.example.ov_mm.notes.service.network.RemoteNotesService;

public class NotesSyncTaskProviderImpl implements NotesSyncTaskProvider {

    @NonNull private final NotesDao mNotesDao;
    @NonNull private final NotesUpdateDao mNotesUpdateDao;
    @NonNull private final CommonDataDao mCommonDataDao;
    @NonNull private final RemoteNotesService mRemoteNotesService;
    @NonNull private final TransactionManager mTransactionManager;

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
    public NotesSyncTask provideNotesSyncTask() {
        return new NotesSyncTaskImpl(
            mRemoteNotesService,
            mCommonDataDao,
            mNotesDao,
            mNotesUpdateDao,
            mTransactionManager);
    }
}

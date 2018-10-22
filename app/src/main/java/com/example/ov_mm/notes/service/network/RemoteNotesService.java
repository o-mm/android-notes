package com.example.ov_mm.notes.service.network;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.service.dao.CommonDao;
import com.example.ov_mm.notes.service.dao.NotesDao;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Lazy;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RemoteNotesService {

    private static final String NOTES_SERVER_URL = "TODO";
    private final Lazy<OkHttpClient> mClient;
    private final CommonDao mCommonDao;
    private final NotesDao mNotesDao;

    @Inject
    public RemoteNotesService(@NonNull Lazy<OkHttpClient> httpClient, CommonDao commonDao, NotesDao notesDao) {
        mClient = httpClient;
        mCommonDao = commonDao;
        mNotesDao = notesDao;
    }

    public void synchronize() {

        RequestBody requestBody = RequestBody.create();
        Request request = new Request.Builder()
                .url(NOTES_SERVER_URL + "/" + mCommonDao.getUser() + "/" + mCommonDao.getLastSyncTime())
                .build();

        try {
            Response response = mClient.get().newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

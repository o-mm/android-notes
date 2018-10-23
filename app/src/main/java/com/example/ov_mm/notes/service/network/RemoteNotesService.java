package com.example.ov_mm.notes.service.network;

import android.support.annotation.NonNull;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.service.dao.CommonDao;
import com.example.ov_mm.notes.service.dao.NotesDao;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;

import dagger.Lazy;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RemoteNotesService {

    private static final String NOTES_SERVER_URL = "TODO";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private final Lazy<OkHttpClient> mClient;
    private final CommonDao mCommonDao;
    private final NotesDao mNotesDao;
    private final Lock syncLock = new ReentrantLock();

    @Inject
    public RemoteNotesService(@NonNull Lazy<OkHttpClient> httpClient, CommonDao commonDao, NotesDao notesDao) {
        mClient = httpClient;
        mCommonDao = commonDao;
        mNotesDao = notesDao;
    }

    public void synchronize(Date startDate) throws IOException {
        mCommonDao.get
        mNotesDao.getNotes()
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, toJson(createSyncObject()));
        Request request = new Request.Builder()
                .url(NOTES_SERVER_URL + "/" + mCommonDao.getUser())
                .post(requestBody)
                .build();
        Response response = mClient.get().newCall(request).execute();
        if (response.body() != null) {
            SyncObject syncObject = fromJson(response.body().string());
            if (syncObject != null) {
                Long version = syncObject.getVersion();
                syncLock.lock();
                try {
                    if (mCommonDao.updateVersion(version, startDate)) {
                        mNotesDao.saveNotes(syncObject.getNotes());
                    }
                } finally {
                    syncLock.unlock();
                }

            }
        }
    }

    private List<Note> createSyncObject(Date startDate) {
        return null; //TODO
    }

    private String toJson(List<Note> notes) {
        //TODO
    }

    private SyncObject fromJson(String json) {
        //TODO
    }

    public static class SyncObject {

        @NonNull private Long mVersion;
        @NonNull private Collection<Note> mNotes;

        private SyncObject(@NonNull Long version, @NonNull Collection<Note> notes) {
            mVersion = version;
            mNotes = notes;
        }

        @NonNull
        public Long getVersion() {
            return mVersion;
        }

        public void setVersion(@NonNull Long version) {
            mVersion = version;
        }

        @NonNull
        public Collection<Note> getNotes() {
            return mNotes;
        }

        public void setNotes(@NonNull Collection<Note> notes) {
            mNotes = notes;
        }
    }
}

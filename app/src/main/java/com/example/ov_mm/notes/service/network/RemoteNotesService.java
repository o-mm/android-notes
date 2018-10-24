package com.example.ov_mm.notes.service.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.model.NotesUpdate;
import com.example.ov_mm.notes.service.dao.CommonDataDao;
import com.example.ov_mm.notes.service.dao.NotesDao;
import com.example.ov_mm.notes.service.dao.NotesUpdateDao;
import com.example.ov_mm.notes.util.Function;
import com.example.ov_mm.notes.util.ListUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dagger.Lazy;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RemoteNotesService {

    private static final String NOTES_SERVER_URL = "http://10.0.2.2:8080/notes/sync";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private final Lazy<OkHttpClient> mClient;
    private final CommonDataDao mCommonDataDao;
    private final NotesDao mNotesDao;
    private final Lock syncLock = new ReentrantLock();
    private final NotesUpdateDao mNotesUpdateDao;

    public RemoteNotesService(@NonNull Lazy<OkHttpClient> httpClient,
                              @NonNull CommonDataDao commonDataDao,
                              @NonNull NotesDao notesDao,
                              @NonNull NotesUpdateDao notesUpdateDao) {
        mClient = httpClient;
        mCommonDataDao = commonDataDao;
        mNotesDao = notesDao;
        mNotesUpdateDao = notesUpdateDao;
    }

    public void synchronize() throws IOException, SyncException {
        syncLock.lock();
        try {
            RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, toJson(createSyncObject()));
            Request request = new Request.Builder()
                    .url(NOTES_SERVER_URL)
                    .post(requestBody)
                    .build();
            try (Response response = mClient.get().newCall(request).execute()) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        SyncObject syncObject = fromJson(response.body().string());
                        if (syncObject != null && syncObject.getVersion() != null) {
                            Long version = syncObject.getVersion();
                            NotesUpdate notesUpdate = new NotesUpdate();
                            notesUpdate.setVersion(version);
                            notesUpdate.setDate(new Date());
                            mNotesUpdateDao.save(notesUpdate);
                            if (syncObject.getNotes() != null) {
                                mNotesDao.saveNotes(ListUtils.map(Arrays.asList(syncObject.getNotes()),
                                        new Function<JNote, Note>() {
                                            @Override
                                            public Note apply(JNote jNote) {
                                                Note note = new Note();
                                                note.setGuid(jNote.getGuid());
                                                if (jNote.getDate() != null) {
                                                    note.setDate(new Date(jNote.getDate()));
                                                }
                                                note.setTitle(jNote.getTitle());
                                                note.setContent(jNote.getContent());
                                                note.setSynced(true);
                                                if (jNote.getDeleted() != null) {
                                                    note.setDeleted(jNote.getDeleted());
                                                }
                                                return note;
                                            }
                                        }));
                            }
                        }
                    }
                } else {
                    throw new SyncException(response.message());
                }
            }
        } finally {
            syncLock.unlock();
        }
    }

    private SyncObject createSyncObject() {
        return new SyncObject(mNotesUpdateDao.getLastVersion(), mCommonDataDao.getUser(),
                ListUtils.map(mNotesDao.getUnsyncedNotes(), new Function<Note, JNote>() {
                    @Override
                    public JNote apply(Note note) {
                        return new JNote(note.getGuid(), note.getTitle(), note.getContent(), note.getDate().getTime(), note.isDeleted());
                    }
                }));
    }

    @NonNull
    private String toJson(@NonNull SyncObject syncObject) {
        return new Gson().toJson(syncObject);
    }

    @Nullable
    private SyncObject fromJson(@Nullable String json) {
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, SyncObject.class);
    }

    public static class SyncObject {

        @Nullable private Long version;
        @Nullable private String user;
        @Nullable private JNote[] notes;

        public SyncObject() {}

        private SyncObject(@Nullable Long version, @Nullable String user, @NonNull Collection<JNote> notes) {
            this.version = version;
            this.user = user;
            this.notes = notes.toArray(new JNote[0]);
        }

        @Nullable
        public Long getVersion() {
            return version;
        }

        public void setVersion(@NonNull Long version) {
            this.version = version;
        }

        @Nullable
        public JNote[] getNotes() {
            return notes;
        }

        public void setNotes(@NonNull JNote[] notes) {
            this.notes = notes;
        }

        @Nullable
        public String getUser() {
            return user;
        }

        public void setUser(@Nullable String user) {
            this.user = user;
        }
    }

    public static class JNote {

        @Nullable private String guid;
        @Nullable private String title;
        @Nullable private String content;
        @Nullable private Long date;
        @Nullable private Boolean deleted;

        public JNote(@Nullable String guid, @Nullable String title, @Nullable String content, @Nullable Long date, boolean deleted) {
            this.guid = guid;
            this.title = title;
            this.content = content;
            this.date = date;
            this.deleted = deleted;
        }

        @Nullable
        public String getGuid() {
            return guid;
        }

        public void setGuid(@Nullable String guid) {
            this.guid = guid;
        }

        @Nullable
        public String getTitle() {
            return title;
        }

        public void setTitle(@Nullable String title) {
            this.title = title;
        }

        @Nullable
        public String getContent() {
            return content;
        }

        public void setContent(@Nullable String content) {
            this.content = content;
        }

        @Nullable
        public Long getDate() {
            return date;
        }

        public void setDate(@Nullable Long date) {
            this.date = date;
        }

        @Nullable
        public Boolean getDeleted() {
            return deleted;
        }

        public void setDeleted(@Nullable Boolean deleted) {
            this.deleted = deleted;
        }
    }
}

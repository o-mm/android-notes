package com.example.ov_mm.notes.service.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import dagger.Lazy;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RemoteNotesService {

    private static final String NOTES_SERVER_URL = "http://10.0.2.2:8080/notes/sync";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    @NonNull private final Lazy<OkHttpClient> mClient;
    @NonNull private final Gson mGson;

    public RemoteNotesService(@NonNull Lazy<OkHttpClient> httpClient, @NonNull Gson gson) {
        mClient = httpClient;
        mGson = gson;
    }

    @NonNull
    public SyncObject synchronize(SyncObject syncObject) throws IOException, SyncException {
        RequestBody requestBody = RequestBody.create(JSON_MEDIA_TYPE, toJson(syncObject));
        Request request = new Request.Builder()
                .url(NOTES_SERVER_URL)
                .post(requestBody)
                .build();
        try (Response response = mClient.get().newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return Objects.requireNonNull(fromJson(response.body().string()), "Null sync object was received from server");
            } else {
                throw new SyncException(response.message());
            }
        }
    }

    @NonNull
    private String toJson(@NonNull SyncObject syncObject) {
        return mGson.toJson(syncObject);
    }

    @Nullable
    private SyncObject fromJson(@Nullable String json) {
        if (json == null) {
            return null;
        }
        return mGson.fromJson(json, SyncObject.class);
    }

    public static class SyncObject {

        @Nullable private Long version;
        @Nullable private String user;
        @Nullable private JNote[] notes;

        public SyncObject() {}

        public SyncObject(@Nullable Long version, @Nullable String user, @NonNull Collection<JNote> notes) {
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

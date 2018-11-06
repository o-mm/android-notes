package com.example.ov_mm.notes.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.model.Note;
import com.example.ov_mm.notes.service.dao.NotesDao;
import com.example.ov_mm.notes.service.dao.NotesUpdateDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NotesRepository {

    @NonNull private final NotesDao mNotesDao;
    @NonNull private final NotesUpdateDao mNotesUpdateDao;
    @NonNull private final NotesSyncTaskProvider mNotesSyncTaskProvider;

    public NotesRepository(@NonNull NotesDao notesDao,
                           @NonNull NotesUpdateDao notesUpdateDao,
                           @NonNull NotesSyncTaskProvider notesSyncTaskProvider) {
        mNotesDao = notesDao;
        mNotesUpdateDao = notesUpdateDao;
        mNotesSyncTaskProvider = notesSyncTaskProvider;
    }

    @NonNull
    public Single<List<NoteWrapper>> loadNotes(@Nullable final String query, @Nullable final SortProperty sortBy, final boolean desc) {
        return Single
            .fromCallable(() -> mNotesDao.getNotes(query, sortBy == null ? null : sortBy.getDbColumn(), desc))
            .map(notes -> {
                List<NoteWrapper> result = new ArrayList<>();
                for (Note note : notes) {
                    result.add(new NoteWrapper(note));
                }
                return result;
            })
            .subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Integer> getDbObservable() {
        return mNotesDao.getDbObservable();
    }

    public void saveNote(@NonNull NoteWrapper note) {
        if (note.isChanged()) {
            note.getNote().setDate(new Date());
            note.getNote().setSynced(false);
            mNotesDao.saveNote(note.getNote());
            note.setChanged(false);
        }
    }

    public void deleteNote(@NonNull NoteWrapper note) {
        note.getNote().setDate(new Date());
        note.getNote().setSynced(false);
        note.getNote().setDeleted(true);
        mNotesDao.saveNote(note.getNote());
    }

    @NonNull
    public NoteWrapper createNote() {
        return new NoteWrapper(new Note());
    }

    @Nullable
    public NoteWrapper getNote(@NonNull Long id) {
        Note note = mNotesDao.getNote(id);
        if (note != null)
            return new NoteWrapper(note);
        else {
            return null;
        }
    }

    @Nullable
    public Date getLastSyncDate() {
        return mNotesUpdateDao.getLastUpdateDate();
    }

    @NonNull
    public Completable syncNotes() {
        NotesSyncTask notesSyncTask = mNotesSyncTaskProvider.provideNotesSyncTask();
        return Completable.fromAction(notesSyncTask).subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread());
    }

    public void fillDatabase() {
        mNotesDao.fillDatabase();
    }
}

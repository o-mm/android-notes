package com.example.ov_mm.notes.vm;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Consumer;

import com.example.ov_mm.notes.repository.NoteWrapper;
import com.example.ov_mm.notes.repository.NotesRepository;
import com.example.ov_mm.notes.repository.SortProperty;
import com.example.ov_mm.notes.ui.SearchSortFragment;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ViewNotesVm extends ViewModel implements SearchSortFragment.OnSearchSortListener {

    @NonNull private final NotesRepository mRepository;
    private boolean initialized;
    @NonNull private final MutableLiveData<List<NoteWrapper>> mNotes = new MutableLiveData<>();
    @NonNull private final MutableLiveData<String> mLastSyncText = new MutableLiveData<>();
    @NonNull private final SyncInfo mSyncInfo = new SyncInfo();
    @Nullable private String mTerm;
    @Nullable private SortProperty mSortProperty;
    @Nullable private NotesRepository.CancellableTask mLoadTask;
    @Nullable private volatile NotesRepository.CancellableTask mNotesSyncFuture;
    private volatile boolean mSyncTaskRunning;
    @NonNull private final Lock syncLock = new ReentrantLock();
    private boolean mDesc;

    @NonNull private final Comparator defaultComparator = new Comparator<Comparable>() {
        @Override
        public int compare(Comparable o1, Comparable o2) {
            if (o2 == null) {
                return 1;
            } else if (o1 == null) {
                return -1;
            } else {
                return o1.compareTo(o2);
            }
        }
    };

    public ViewNotesVm(@NonNull NotesRepository repository) {
        mRepository = repository;
        setLastSyncText(getLastSyncFormatted());
    }

    public void init(@Nullable String term, @Nullable SortProperty sortProperty, boolean desc) {
        if (!initialized) {
            initialized = true;
            mTerm = term;
            mSortProperty = sortProperty;
            mDesc = desc;
            loadNotes();
        }
    }

    @NonNull
    public LiveData<List<NoteWrapper>> getNotes() {
        return mNotes;
    }

    public void refreshNotes() {
        if (initialized) {
            loadNotes();
        }
    }

    @Override
    public void onSearch(@Nullable String term) {
        if (initialized) {
            mTerm = term;
            loadNotes();
        }
    }

    @Override
    public void onSortPropertyChanged(@Nullable SortProperty sortProperty) {
        if (initialized) {
            mSortProperty = sortProperty;
            setNotes(reorderNotes(getNotes().getValue()));
        }
    }

    @Override
    public void onOrderChanged(boolean desc) {
        if (initialized) {
            mDesc = desc;
            setNotes(reorderNotes(getNotes().getValue()));
        }
    }

    @NonNull
    public NoteWrapper createNote() {
        return mRepository.createNote();
    }

    @NonNull
    public LiveData<String> getLastSyncText() {
        return mLastSyncText;
    }

    @NonNull
    public SyncInfo getSyncInfo() {
        return mSyncInfo;
    }

    public void startSyncTask() {
        getSyncInfo().setRunning(true);
        getSyncInfo().setSyncResult(null);
        NotesRepository.CancellableTask notesSyncFuture = mNotesSyncFuture;
        if (notesSyncFuture != null) {
            notesSyncFuture.cancel();
        }
        mSyncTaskRunning = true;
        notesSyncFuture = mRepository.syncNotes(new Consumer<SyncInfo.SyncResult>() {
            @Override
            public void accept(SyncInfo.SyncResult syncResult) {
                getSyncInfo().postSyncResult(syncResult);
                postLastSyncText(getLastSyncFormatted());
                getSyncInfo().postRunning(false);
                syncLock.lock();
                try {
                    mNotesSyncFuture = null;
                    mSyncTaskRunning = false;
                } finally {
                    syncLock.unlock();
                }
            }
        });
        if (mSyncTaskRunning) {
            syncLock.lock();
            try {
                if (mSyncTaskRunning) {
                    mNotesSyncFuture = notesSyncFuture;
                }
            } finally {
                syncLock.unlock();
            }
        }
    }

    public void cancelSyncTask() {
        NotesRepository.CancellableTask notesSyncFuture = mNotesSyncFuture;
        if (notesSyncFuture != null) {
            notesSyncFuture.cancel();
        }
        mNotesSyncFuture = null;
        getSyncInfo().setRunning(false);
    }

    void setNotes(@Nullable List<NoteWrapper> notes) {
        mNotes.setValue(notes);
    }

    void postLastSyncText(@Nullable String lastSyncDate) {
        mLastSyncText.postValue(lastSyncDate);
    }

    void setLastSyncText(@Nullable String lastSyncDate) {
        mLastSyncText.setValue(lastSyncDate);
    }

    @SuppressLint("SimpleDateFormat")
    @Nullable
    private String getLastSyncFormatted() {
        Date lastSyncDate = mRepository.getLastSyncDate();
        if (lastSyncDate != null) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(lastSyncDate);
        }
        return null;
    }

    private void loadNotes() {
        if (mLoadTask != null) {
            mLoadTask.cancel();
        }
        mLoadTask = mRepository.loadNotes(new Consumer<List<NoteWrapper>>() {
            @Override
            public void accept(List<NoteWrapper> notes) {
                setNotes(reorderNotes(notes));
                mLoadTask = null;
            }
        }, mTerm, mSortProperty, mDesc);
    }

    @NonNull
    private List<NoteWrapper> reorderNotes(@Nullable List<NoteWrapper> notes) {
        if (notes == null) {
            return Collections.emptyList();
        }
        Collections.sort(notes, new Comparator<NoteWrapper>() {
            @Override
            public int compare(NoteWrapper o1, NoteWrapper o2) {
                if (SortProperty.DATE.equals(mSortProperty)) {
                    return Objects.compare(o1.getDate(), o2.getDate(), defaultComparator)
                            * (mDesc ? -1 : 1);
                } else if (SortProperty.TITLE.equals(mSortProperty)) {
                    return Objects.compare(o1.getTitle(), o2.getTitle(), defaultComparator)
                            * (mDesc ? -1 : 1);
                } else {
                    return 0;
                }
            }
        });
        return notes;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        cancelSyncTask();
        if (mLoadTask != null) {
            mLoadTask.cancel();
        }
        mLoadTask = null;
    }
}

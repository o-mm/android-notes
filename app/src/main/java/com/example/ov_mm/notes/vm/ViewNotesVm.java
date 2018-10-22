package com.example.ov_mm.notes.vm;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Consumer;

import com.example.ov_mm.notes.di.BaseViewModel;
import com.example.ov_mm.notes.repository.NoteWrapper;
import com.example.ov_mm.notes.repository.SortProperty;
import com.example.ov_mm.notes.ui.SearchSortFragment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ViewNotesVm extends BaseViewModel implements SearchSortFragment.OnSearchSortListener {

    private boolean initialized;
    @NonNull private final MutableLiveData<List<NoteWrapper>> mNotes = new MutableLiveData<>();
    @Nullable private String mTerm;
    @Nullable private SortProperty mSortProperty;
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

    public ViewNotesVm(@NonNull Application application) {
        super(application);
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
            mNotes.setValue(reorderNotes(mNotes.getValue()));
        }
    }

    @Override
    public void onOrderChanged(boolean desc) {
        if (initialized) {
            mDesc = desc;
            mNotes.setValue(reorderNotes(mNotes.getValue()));
        }
    }

    private void loadNotes() {
        mRepository.loadNotes(new Consumer<List<NoteWrapper>>() {
            @Override
            public void accept(List<NoteWrapper> notes) {
                mNotes.setValue(reorderNotes(notes));
            }
        }, mTerm, mSortProperty, mDesc);
    }

    @NonNull
    public NoteWrapper createNote() {
        return mRepository.createNote();
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
}

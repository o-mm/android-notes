package com.example.ov_mm.notes.vm;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.di.BaseViewModel;
import com.example.ov_mm.notes.repository.NoteWrapper;
import com.example.ov_mm.notes.repository.SortProperty;
import com.example.ov_mm.notes.ui.SearchSortFragment;

import java.util.List;

public class ViewNotesVm extends BaseViewModel implements SearchSortFragment.OnSearchSortListener {

    private MutableLiveData<List<NoteWrapper>> mNotes;
    private String mTerm;
    private SortProperty mSortProperty;
    private boolean mDesc;

    public ViewNotesVm(@NonNull Application application) {
        super(application);
    }

    public void init(String term, SortProperty sortProperty, boolean desc) {
        mTerm = term;
        mSortProperty = sortProperty;
        mDesc = desc;
        loadNotes();
    }

    @NonNull
    public LiveData<List<NoteWrapper>> getNotes() {
        if (mNotes == null) {
            mNotes = new MutableLiveData<>();
        }
        return mNotes;
    }

    public void refreshNotes() {
        loadNotes();
    }

    @Override
    public void onSearch(@Nullable String term) {
        mTerm = term;
        loadNotes();
    }

    @Override
    public void onSortPropertyChanged(@Nullable SortProperty sortProperty) {
        mSortProperty = sortProperty;
        mRepository.reorderNotes(mNotes, mTerm, mSortProperty, mDesc);
    }

    @Override
    public void onOrderChanged(boolean desc) {
        mDesc = desc;
        mRepository.reorderNotes(mNotes, mTerm, mSortProperty, mDesc);
    }

    private void loadNotes() {
        mRepository.load(mNotes, mTerm, mSortProperty, mDesc);
    }

    public NoteWrapper createNote() {
        return mRepository.createNote();
    }
}

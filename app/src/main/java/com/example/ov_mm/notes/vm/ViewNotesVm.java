package com.example.ov_mm.notes.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ov_mm.notes.repository.NoteWrapper;
import com.example.ov_mm.notes.repository.NotesRepository;
import com.example.ov_mm.notes.repository.SortProperty;
import com.example.ov_mm.notes.ui.SearchSortFragment;

import java.util.List;

public class ViewNotesVm extends ViewModel implements SearchSortFragment.OnSearchSortListener {

    private MutableLiveData<List<NoteWrapper>> mNotes;
    private String mTerm;
    private SortProperty mSortProperty;
    private boolean mDesc;
    private NotesRepository mNotesRepository = new NotesRepository();

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
        mNotesRepository.reorderNotes(mNotes, mTerm, mSortProperty, mDesc);
    }

    @Override
    public void onOrderChanged(boolean desc) {
        mDesc = desc;
        mNotesRepository.reorderNotes(mNotes, mTerm, mSortProperty, mDesc);
    }

    private void loadNotes() {
        mNotesRepository.load(mNotes, mTerm, mSortProperty, mDesc);
    }

    public NoteWrapper createNote() {
        return mNotesRepository.createNote();
    }
}

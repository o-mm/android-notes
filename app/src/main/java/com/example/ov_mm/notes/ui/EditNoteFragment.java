package com.example.ov_mm.notes.ui;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.repository.NoteWrapper;
import com.example.ov_mm.notes.ui.di.BaseFragment;
import com.example.ov_mm.notes.vm.EditNoteVm;

import javax.inject.Inject;

public class EditNoteFragment extends BaseFragment {

    private static final String EXTRA_ITEM_ID_FOR_EDIT = "com.example.ov_mm.notes.ui.ITEM_ID_FOR_EDIT";
    @Inject EditNoteVm mEditNoteVm;
    private EditText mTitleInput;
    private EditText mContentInput;
    @Nullable private Long mNoteId;

    @NonNull
    public static EditNoteFragment newInstance(@Nullable Long noteId) {
        EditNoteFragment instance = new EditNoteFragment();
        Bundle bundle = new Bundle();
        if (noteId != null) {
            bundle.putLong(EXTRA_ITEM_ID_FOR_EDIT, noteId);
        }
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentSubComponent().inject(this);
        if (savedInstanceState != null) {
            mNoteId = savedInstanceState.getLong(EXTRA_ITEM_ID_FOR_EDIT);
        } else if (getArguments() != null) {
            mNoteId = getArguments().getLong(EXTRA_ITEM_ID_FOR_EDIT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        mTitleInput = view.findViewById(R.id.title_edit_text);
        mContentInput = view.findViewById(R.id.content_edit_text);

        mEditNoteVm.getNote().observe(this, new Observer<NoteWrapper>() {
            @Override
            public void onChanged(@Nullable NoteWrapper noteWrapper) {
                mTitleInput.setText(noteWrapper == null ? null : noteWrapper.getTitle());
                mContentInput.setText(noteWrapper == null ? null : noteWrapper.getContent());
            }
        });
        mEditNoteVm.init(mNoteId);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        mEditNoteVm
                .update(TextUtils.isEmpty(mTitleInput.getText()) ? null : mTitleInput.getText().toString(),
                        TextUtils.isEmpty(mContentInput.getText()) ? null : mContentInput.getText().toString());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNoteId != null) {
            outState.putLong(EXTRA_ITEM_ID_FOR_EDIT, mNoteId);
        }
    }
}

package com.example.ov_mm.notes.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.bl.EditViewController;
import com.example.ov_mm.notes.bl.ParcelableNote;

public class EditNoteFragment extends Fragment {

    private static final String EXTRA_ITEM_FOR_EDIT = "com.example.ov_mm.notes.activity.ITEM_FOR_EDIT";
    private static final String TAG = "EditNoteFragment";
    @NonNull
    private final EditViewController mController = new EditViewController();
    private ParcelableNote mNote;
    private EditText mTitleInput;
    private EditText mContentInput;

    public static EditNoteFragment newInstance(@NonNull ParcelableNote note) {
        EditNoteFragment instance = new EditNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ITEM_FOR_EDIT, note);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mNote = savedInstanceState.getParcelable(EXTRA_ITEM_FOR_EDIT);
        } else if (getArguments() != null) {
            mNote = getArguments().getParcelable(EXTRA_ITEM_FOR_EDIT);
        }
        if (mNote == null) { //should not happen
            mNote = mController.createNote();
            Log.e(TAG, "Unable to restore note from the state");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        mTitleInput = view.findViewById(R.id.title_edit_text);
        mContentInput = view.findViewById(R.id.content_edit_text);
        mTitleInput.setText(mNote.getTitle());
        mContentInput.setText(mNote.getContent());
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        String title = mTitleInput.getText().toString();
        String content = mContentInput.getText().toString();
        mNote.setTitle(title.isEmpty() ? null : title);
        mNote.setContent(content.isEmpty() ? null : content);
        mController.saveNote(mNote);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_ITEM_FOR_EDIT, mNote);
    }
}

package com.example.ov_mm.notes.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.bl.EditViewController;
import com.example.ov_mm.notes.bl.ParcelableNote;

public class EditNoteFragment extends Fragment {

    @NonNull
    private final EditViewController mController = new EditViewController();
    private NoteSupplierContext mNoteSupplier;
    private ParcelableNote mNote;
    private EditText mTitleInput;
    private EditText mContentInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mNote = savedInstanceState.getParcelable(ViewNotesFragment.EXTRA_ITEM_FOR_EDIT);
        } else {
            mNote = mNoteSupplier.getNote();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NoteSupplierContext) {
            mNoteSupplier = (NoteSupplierContext) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NoteSupplierContext");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNoteSupplier = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        String title = mTitleInput.getText().toString();
        String content = mContentInput.getText().toString();
        mNote.setTitle(title.isEmpty() ? null : title);
        mNote.setContent(content.isEmpty() ? null : content);
        if (mController.saveNote(mNote)) {
            mNoteSupplier.setResult(Activity.RESULT_OK);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ViewNotesFragment.EXTRA_ITEM_FOR_EDIT, mNote);
    }

    public interface NoteSupplierContext {

        @NonNull
        ParcelableNote getNote();

        void setResult(int code);
    }
}

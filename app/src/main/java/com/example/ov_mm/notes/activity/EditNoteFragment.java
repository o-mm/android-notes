package com.example.ov_mm.notes.activity;

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
import com.example.ov_mm.notes.activity.bl.EditViewController;
import com.example.ov_mm.notes.activity.bl.ParcelableNote;

public class EditNoteFragment extends Fragment {

    private final EditViewController controller = new EditViewController();
    private NoteSupplierContext noteSupplier;
    private ParcelableNote note;
    private EditText titleInput;
    private EditText contentInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            note = savedInstanceState.getParcelable(ViewNotesActivity.EXTRA_ITEM_FOR_EDIT);
        else
            note = noteSupplier.getNote();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        titleInput = view.findViewById(R.id.title_edit_text);
        contentInput = view.findViewById(R.id.content_edit_text);
        titleInput.setText(note.getTitle());
        contentInput.setText(note.getContent());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NoteSupplierContext)
            noteSupplier = (NoteSupplierContext) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        noteSupplier = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        note.setTitle(titleInput.getText().toString());
        note.setContent(contentInput.getText().toString());
        controller.saveNote(note);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ViewNotesActivity.EXTRA_ITEM_FOR_EDIT, note);
    }

    public interface NoteSupplierContext {

        @NonNull
        ParcelableNote getNote();
    }
}

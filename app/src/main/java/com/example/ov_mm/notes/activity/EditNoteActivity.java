package com.example.ov_mm.notes.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.bl.EditViewController;
import com.example.ov_mm.notes.bl.ParcelableNote;

public class EditNoteActivity extends AppCompatActivity implements EditNoteFragment.NoteSupplierContext {

    private final EditViewController mController = new EditViewController();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
    }

    @NonNull
    @Override
    public ParcelableNote getNote() {
        ParcelableNote note = getIntent().getParcelableExtra(ViewNotesFragment.EXTRA_ITEM_FOR_EDIT);
        if (note == null) { //should not happen
            note = mController.createNote();
        }
        return note;
    }
}

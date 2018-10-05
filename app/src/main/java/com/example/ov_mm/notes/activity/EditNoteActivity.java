package com.example.ov_mm.notes.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.model.Note;

public class EditNoteActivity extends AppCompatActivity implements EditNoteFragment.NoteSupplierContext {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
    }

    @NonNull
    @Override
    public Note getNote() {
        Note note = getIntent().getParcelableExtra(ViewNotesActivity.EXTRA_ITEM_FOR_EDIT);
        if (note == null)
            note = new Note();
        return note;
    }
}

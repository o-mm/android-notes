package com.example.ov_mm.notes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.bl.EditViewController;
import com.example.ov_mm.notes.bl.ParcelableNote;

import java.util.Objects;

public class EditNoteActivity extends AppCompatActivity implements EditNoteFragment.NoteSupplierContext {

    public static final String EXTRA_ITEM_FOR_EDIT = "com.example.ov_mm.notes.activity.ITEM_FOR_EDIT";

    @NonNull
    private final EditViewController mController = new EditViewController();

    public static void startActivity(@NonNull Context context, @NonNull ParcelableNote note) {
        Intent intent = new Intent(context, EditNoteActivity.class);
        intent.putExtra(EditNoteActivity.EXTRA_ITEM_FOR_EDIT, Objects.requireNonNull(note, "Item should not be null"));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
    }

    @NonNull
    @Override
    public ParcelableNote getNote() {
        ParcelableNote note = getIntent().getParcelableExtra(EXTRA_ITEM_FOR_EDIT);
        if (note == null) { //should not happen
            note = mController.createNote();
        }
        return note;
    }
}

package com.example.ov_mm.notes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.bl.EditViewController;
import com.example.ov_mm.notes.bl.ParcelableNote;

import java.util.List;
import java.util.Objects;


public class ViewNotesFragment extends Fragment {

    public static final String EXTRA_ITEM_FOR_EDIT = "com.example.ov_mm.notes.activity.ITEM_FOR_EDIT";
    @NonNull
    private final EditViewController mController = new EditViewController();
    private NoteRecyclerViewAdapter mNoteAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_notes, container, false);

        view.findViewById(R.id.add_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEditActivity(mController.createNote());
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.note_recycle_view);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mNoteAdapter = new NoteRecyclerViewAdapter(
                new NoteRecyclerViewAdapter.NotesSupplier() {
                    @Override
                    public List<ParcelableNote> getNotes() {
                        return mController.getNotes();
                    }
                },
                new NoteRecyclerViewAdapter.OnListFragmentInteractionListener() {
                    @Override
                    public void onListFragmentInteraction(ParcelableNote note) {
                        toEditActivity(note);
                    }
                });
        recyclerView.setAdapter(mNoteAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mNoteAdapter.resetData();
        mNoteAdapter.notifyDataSetChanged();
    }

    private void toEditActivity(@NonNull ParcelableNote item) {
        Intent intent = new Intent(this.getContext(), EditNoteActivity.class);
        intent.putExtra(EXTRA_ITEM_FOR_EDIT, Objects.requireNonNull(item, "Item should not be null"));
        startActivity(intent);
    }
}

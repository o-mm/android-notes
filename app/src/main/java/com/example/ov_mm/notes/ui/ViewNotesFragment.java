package com.example.ov_mm.notes.ui;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.repository.NoteWrapper;
import com.example.ov_mm.notes.vm.ViewNotesVm;

import java.util.List;


public class ViewNotesFragment extends Fragment implements OnBackPressedHandler {

    private NoteRecyclerViewAdapter mNoteAdapter;
    private OnListItemInteractionListener mInteractionListener;
    private View mBottomFragmentContainer;

    @NonNull
    public static ViewNotesFragment newInstance() {
        ViewNotesFragment fragment = new ViewNotesFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListItemInteractionListener) {
            mInteractionListener = (OnListItemInteractionListener) context;
        } else {
            throw new UnsupportedOperationException("Fragment context must implement OnListItemInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInteractionListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_notes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.note_recycle_view);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mNoteAdapter = new NoteRecyclerViewAdapter(mInteractionListener);
        recyclerView.setAdapter(mNoteAdapter);
        mInteractionListener.getViewVm().getNotes().observe(this, new Observer<List<NoteWrapper>>() {
            @Override
            public void onChanged(@Nullable List<NoteWrapper> notes) {
                mNoteAdapter.updateData(notes);
                mNoteAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.add_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInteractionListener.onListItemInteraction(mInteractionListener.getViewVm().createNote());
            }
        });
        mBottomFragmentContainer = view.findViewById(R.id.bottom_sheet_fragment);
        if (getChildFragmentManager().findFragmentById(R.id.bottom_sheet_fragment) == null) {
            getChildFragmentManager().beginTransaction().add(R.id.bottom_sheet_fragment, SearchSortFragment.newInstance()).commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mInteractionListener.getViewVm().refreshNotes();
    }

    @Override
    public boolean handleBackPressed() {
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(mBottomFragmentContainer);
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            return false;
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        }
    }

    public interface OnListItemInteractionListener {

        void onListItemInteraction(@NonNull NoteWrapper note);

        ViewNotesVm getViewVm();
    }
}

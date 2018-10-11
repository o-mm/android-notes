package com.example.ov_mm.notes.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.bl.EditViewController;
import com.example.ov_mm.notes.bl.ParcelableNote;
import com.example.ov_mm.notes.bl.SortProperty;

import java.util.List;


public class ViewNotesFragment extends Fragment implements SearchSortFragment.OnSearchSortListener {

    @NonNull
    private final EditViewController mController = new EditViewController();
    private NoteRecyclerViewAdapter mNoteAdapter;
    private OnListItemInteractionListener mInteractionListener;

    public static ViewNotesFragment newInstance() {
        return new ViewNotesFragment();
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
        mNoteAdapter = new NoteRecyclerViewAdapter(
                new NoteRecyclerViewAdapter.NoteListSupplier() {
                    @NonNull
                    @Override
                    public List<ParcelableNote> getNotes() {
                        return getNotesData();
                    }
                },
                mInteractionListener);
        recyclerView.setAdapter(mNoteAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.add_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInteractionListener.onListItemInteraction(mController.createNote());
            }
        });
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction().add(R.id.bottom_sheet_fragment, SearchSortFragment.newInstance()).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resetListData();
    }

    @Override
    public void onSearch() {
        resetListData();
    }

    @Override
    public void onSortPropertyChanged() {
        resetListData();
    }

    @Override
    public void onOrderChanged() {
        resetListData();
    }

    @NonNull
    private List<ParcelableNote> getNotesData() {
        SearchSortFragment searchSort = (SearchSortFragment) getChildFragmentManager().findFragmentById(R.id.bottom_sheet_fragment);
        if (searchSort == null) {
            return mController.getNotes(null, SortProperty.DATE, true);
        } else {
            return mController.getNotes(searchSort.getSearchString(), searchSort.getSortProperty(), searchSort.isDescOrder());
        }
    }

    private void resetListData() {
        mNoteAdapter.resetData();
        mNoteAdapter.notifyDataSetChanged();
    }

    public interface OnListItemInteractionListener {

        void onListItemInteraction(@NonNull ParcelableNote note);
    }
}

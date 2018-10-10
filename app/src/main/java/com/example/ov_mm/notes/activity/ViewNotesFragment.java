package com.example.ov_mm.notes.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.bl.EditViewController;
import com.example.ov_mm.notes.bl.ParcelableNote;

import java.util.List;


public class ViewNotesFragment extends Fragment {

    @NonNull
    private final EditViewController mController = new EditViewController();
    private NoteRecyclerViewAdapter mNoteAdapter;
    private OnListItemInteractionListener mInteractionListener;

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

        view.findViewById(R.id.add_note_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInteractionListener.onListItemInteraction(mController.createNote());
            }
        });

        final Spinner sortBySpinner = view.findViewById(R.id.sort_by_spinner);
        final ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.sort_by_items, android.R.layout.simple_spinner_item);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(sortByAdapter);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resetListData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resetListData();
            }
        });

        final SearchView searchInput = view.findViewById(R.id.search_input);
        searchInput.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                resetListData();
                return false;
            }
        });
        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                resetListData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        final ToggleButton sortOrderButton = view.findViewById(R.id.sort_order_button);

        sortOrderButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resetListData();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.note_recycle_view);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mNoteAdapter = new NoteRecyclerViewAdapter(
                new NoteRecyclerViewAdapter.NoteListSupplier() {
                    @NonNull
                    @Override
                    public List<ParcelableNote> getNotes() {
                        CharSequence query = searchInput.getQuery();
                        int selectedItemPosition = sortBySpinner.getSelectedItemPosition();
                        CharSequence sortByProp = selectedItemPosition >= 0 ? sortByAdapter.getItem(selectedItemPosition) : null;
                        return mController.getNotes(query != null && query.length() > 0 ? query.toString() : null,
                                sortByProp == null ? null : sortByProp.toString(), sortOrderButton.isChecked());
                    }
                },
                mInteractionListener);
        recyclerView.setAdapter(mNoteAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void resetListData() {
        mNoteAdapter.resetData();
        mNoteAdapter.notifyDataSetChanged();
    }

    public interface OnListItemInteractionListener {

        void onListItemInteraction(@NonNull ParcelableNote note);
    }
}

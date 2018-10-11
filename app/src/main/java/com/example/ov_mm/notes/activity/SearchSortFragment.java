package com.example.ov_mm.notes.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.example.ov_mm.notes.bl.SortProperty;

public class SearchSortFragment extends Fragment {

    private SearchSortListenerProvider mSearchSortProvider;
    private SearchView mSearchView;
    private Spinner mSortBySpinner;
    private ToggleButton mSortOrderButton;

    public static SearchSortFragment newInstance() {
        return new SearchSortFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchSortListenerProvider) {
            mSearchSortProvider = (SearchSortListenerProvider) context;
        } else {
            throw new UnsupportedOperationException("Fragment context must implement SearchSortListenerProvider");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSearchSortProvider = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_sort, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSortBySpinner = view.findViewById(R.id.sort_by_spinner);
        final ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.sort_by_items, android.R.layout.simple_spinner_item);
        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortBySpinner.setAdapter(sortByAdapter);
        mSortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSearchSortProvider.getSearchSortListener().onSortPropertyChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSearchSortProvider.getSearchSortListener().onSortPropertyChanged();
            }
        });

        mSearchView = view.findViewById(R.id.search_input);
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setIconified(false);
            }
        });
        mSearchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mSearchSortProvider.getSearchSortListener().onSearch();
                }
            }
        });

        mSortOrderButton = view.findViewById(R.id.sort_order_button);
        mSortOrderButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSearchSortProvider.getSearchSortListener().onOrderChanged();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSearchString() == null)
                    mSearchView.setIconified(true);
                else
                    mSearchView.clearFocus();
            }
        });
    }

    @Nullable
    public String getSearchString() {
        return TextUtils.isEmpty(mSearchView.getQuery()) ? null : mSearchView.getQuery().toString();
    }

    @Nullable
    public SortProperty getSortProperty() {
        if (mSortBySpinner.getSelectedItemPosition() == -1) {
            return null;
        } else {
            return SortProperty.values()[mSortBySpinner.getSelectedItemPosition()];
        }
    }

    public boolean isDescOrder() {
        return mSortOrderButton.isChecked();
    }

    public interface SearchSortListenerProvider {
        @NonNull
        OnSearchSortListener getSearchSortListener();
    }

    public interface OnSearchSortListener {

        void onSearch();

        void onSortPropertyChanged();

        void onOrderChanged();
    }
}

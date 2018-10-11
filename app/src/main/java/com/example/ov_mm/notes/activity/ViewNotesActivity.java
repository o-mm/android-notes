package com.example.ov_mm.notes.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.bl.ParcelableNote;

import java.util.Objects;

public class ViewNotesActivity extends AppCompatActivity implements ViewNotesFragment.OnListItemInteractionListener,
        SearchSortFragment.SearchSortListenerProvider {

    private static final String TAG = "ViewNotesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                requireActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, ViewNotesFragment.newInstance()).commit();
        }
    }

    @Override
    public void onListItemInteraction(@NonNull ParcelableNote note) {
        EditNoteFragment editNoteFragment = EditNoteFragment.newInstance(note);
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, editNoteFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        requireActionBar().setDisplayHomeAsUpEnabled(false);
        super.onBackPressed();
        return super.onSupportNavigateUp();
    }

    @NonNull
    private ActionBar requireActionBar() {
        return Objects.requireNonNull(getSupportActionBar(), "Action bar must be set");
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof OnBackPressedHandler && ((OnBackPressedHandler) fragment).handleBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @NonNull
    @Override
    public SearchSortFragment.OnSearchSortListener getSearchSortListener() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof SearchSortFragment.OnSearchSortListener) {
            return (SearchSortFragment.OnSearchSortListener) fragment;
        } else {
            Log.e(TAG, "No fragment implementing OnSearchSortListener is present");
            return new SearchSortFragment.OnSearchSortListener() {
                @Override
                public void onSearch() {
                }

                @Override
                public void onSortPropertyChanged() {
                }

                @Override
                public void onOrderChanged() {
                }
            };
        }
    }
}

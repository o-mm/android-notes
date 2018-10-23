package com.example.ov_mm.notes.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.repository.NoteWrapper;
import com.example.ov_mm.notes.ui.di.BaseActivity;
import com.example.ov_mm.notes.vm.ViewNotesVm;

import java.util.Objects;

import javax.inject.Inject;

public class ViewNotesActivity extends BaseActivity implements ViewNotesFragment.OnListItemInteractionListener,
        SearchSortFragment.SearchSortListenerProvider {

    @Inject ViewNotesVm mViewNotesVm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivitySubComponent().inject(this);
        setContentView(R.layout.activity_view_notes);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                switchUpButton();
            }
        });
        switchUpButton();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, ViewNotesFragment.newInstance()).commit();
        }
    }

    @Override
    public void onListItemInteraction(@NonNull NoteWrapper note) {
        EditNoteFragment editNoteFragment = EditNoteFragment.newInstance(note.getId());
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
        return getViewVm();
    }

    @NonNull
    @Override
    public ViewNotesVm getViewVm() {
        return mViewNotesVm;
    }

    @NonNull
    private ActionBar requireActionBar() {
        return Objects.requireNonNull(getSupportActionBar(), "Action bar must be set");
    }

    private void switchUpButton() {
        requireActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
    }
}

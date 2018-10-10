package com.example.ov_mm.notes.activity;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.bl.ParcelableNote;

import java.util.Objects;

public class ViewNotesActivity extends AppCompatActivity implements ViewNotesFragment.OnListItemInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (savedInstanceState == null) {
            ViewNotesFragment viewNotesFragment = new ViewNotesFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, viewNotesFragment).commit();
        }
    }

    @Override
    public void onListItemInteraction(@NonNull ParcelableNote note) {
        EditNoteFragment editNoteFragment = EditNoteFragment.newInstance(note);
        editNoteFragment.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
            public void onResume() {
                requireActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, editNoteFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (getSupportActionBar() != null) {
            boolean showBack = !(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ViewNotesFragment);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        requireActionBar().setDisplayHomeAsUpEnabled(false);
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @NonNull
    private ActionBar requireActionBar() {
        return Objects.requireNonNull(getSupportActionBar(), "Action bar must be set");
    }
}

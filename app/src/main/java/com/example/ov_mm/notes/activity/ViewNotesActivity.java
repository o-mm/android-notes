package com.example.ov_mm.notes.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.bl.ParcelableNote;

public class ViewNotesActivity extends AppCompatActivity implements ViewNotesFragment.OnListItemInteractionListener {

    private FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mFragmentContainer = findViewById(R.id.fragment_container);
        if (savedInstanceState == null) {
            ViewNotesFragment viewNotesFragment = new ViewNotesFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, viewNotesFragment).commit();
        }
    }

    @Override
    public void onListItemInteraction(@NonNull ParcelableNote note) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, EditNoteFragment.createInstance(note))
                .commit();
    }
}

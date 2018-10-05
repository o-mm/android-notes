package com.example.ov_mm.notes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.model.Note;

import java.util.Objects;

public class ViewNotesActivity extends AppCompatActivity implements ViewNotesFragment.OnListFragmentInteractionListener {

    public static final String EXTRA_ITEM_FOR_EDIT = "com.example.ov_mm.notes.activity.ITEM_FOR_EDIT";
    private Toolbar toolbar;
    private FloatingActionButton addNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addNoteButton = findViewById(R.id.add_note_button);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEditActivity(new Note());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Note item) {
        toEditActivity(item);
    }

    private void toEditActivity(@NonNull Note item) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(EXTRA_ITEM_FOR_EDIT, Objects.requireNonNull(item, "Item should not be null"));
        startActivity(intent);
    }
}

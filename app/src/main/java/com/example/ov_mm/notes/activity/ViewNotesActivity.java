package com.example.ov_mm.notes.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.ov_mm.notes.R;

public class ViewNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }
}

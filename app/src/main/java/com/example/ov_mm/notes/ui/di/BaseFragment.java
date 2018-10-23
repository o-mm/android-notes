package com.example.ov_mm.notes.ui.di;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.ov_mm.notes.NotesApp;

public abstract class BaseFragment extends Fragment {

    private FragmentSubComponent mFragmentSubComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentSubComponent = ((NotesApp) requireContext().getApplicationContext())
                .getNotesAppComponent()
                .fragmentSubComponent(new FragmentModule(this));
    }

    protected FragmentSubComponent getFragmentSubComponent() {
        return mFragmentSubComponent;
    }
}

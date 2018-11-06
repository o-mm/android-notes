package com.example.ov_mm.notes.ui;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.repository.NoteWrapper;
import com.example.ov_mm.notes.ui.di.BaseActivity;
import com.example.ov_mm.notes.vm.ActivityViewModel;
import com.example.ov_mm.notes.vm.SyncInfo;
import com.example.ov_mm.notes.vm.ViewNotesVm;

import java.util.Objects;

import javax.inject.Inject;

public class ViewNotesActivity extends BaseActivity implements ViewNotesFragment.OnListItemInteractionListener,
        SearchSortFragment.SearchSortListenerProvider {

    @Inject ViewNotesVm mViewNotesVm;
    @Inject ActivityViewModel mActivityViewModel;
    private View mToolsContainer;
    private TextView mLastSyncText;
    private ProgressBar mSyncProgressBar;
    private ImageButton mSyncButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivitySubComponent().inject(this);
        setContentView(R.layout.activity_view_notes);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mToolsContainer = findViewById(R.id.toolbar_layout);
        mLastSyncText = findViewById(R.id.last_sync_date);
        mSyncProgressBar = findViewById(R.id.sync_progress_bar);
        mSyncButton = findViewById(R.id.sync_button);
        mSyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSyncTask();
            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                updateToolbar();
            }
        });
        updateToolbar();
        mViewNotesVm.getLastSyncText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null)
                    mLastSyncText.setText(String.format(getString(R.string.last_sync), s));
            }
        });
        mLastSyncText.setText(mViewNotesVm.getLastSyncText().getValue());
        mViewNotesVm.getSyncInfo().getRunning().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                updateSyncBlock(aBoolean);
            }
        });
        mViewNotesVm.getSyncInfo().getSyncResult().observe(this, new Observer<SyncInfo.SyncResult>() {
            @Override
            public void onChanged(@Nullable SyncInfo.SyncResult syncResult) {
                handleSyncError(syncResult);
            }
        });
        updateSyncBlock(mViewNotesVm.getSyncInfo().getRunning().getValue());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, ViewNotesFragment.newInstance()).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuItem menuItem = menu.add(R.string.generate_notes);
        switchGenMenuItem(menuItem, mActivityViewModel.getGeneratingRunning().getValue());
        mActivityViewModel.getGeneratingRunning().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                switchGenMenuItem(menuItem, aBoolean);
                if (!Boolean.TRUE.equals(aBoolean)) {
                    mViewNotesVm.refreshNotes();
                }
            }
        });
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                startNotesGenService();
                Toast.makeText(ViewNotesActivity.this, R.string.generating_notes, Toast.LENGTH_LONG).show();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

    private void switchGenMenuItem(MenuItem menuItem, Boolean genRunning) {
        if (Boolean.TRUE.equals(genRunning)) {
            menuItem.setTitle(R.string.generating_notes);
            menuItem.setEnabled(false);
        } else {
            menuItem.setTitle(R.string.generate_notes);
            menuItem.setEnabled(true);
        }
    }

    @NonNull
    private ActionBar requireActionBar() {
        return Objects.requireNonNull(getSupportActionBar(), "Action bar must be set");
    }

    private void updateToolbar() {
        boolean stackNotEmpty = getSupportFragmentManager().getBackStackEntryCount() > 0;
        requireActionBar().setDisplayHomeAsUpEnabled(stackNotEmpty);
        mToolsContainer.setVisibility(stackNotEmpty ? View.GONE : View.VISIBLE);
    }

    private void updateSyncBlock(Boolean syncTaskRunning) {
        mSyncButton.setVisibility(Boolean.TRUE.equals(syncTaskRunning) ? View.GONE : View.VISIBLE);
        mSyncProgressBar.setVisibility(!Boolean.TRUE.equals(syncTaskRunning) ? View.GONE : View.VISIBLE);
    }

    private void startSyncTask() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mViewNotesVm.startSyncTask();
        } else {
            showMessage(R.string.no_network_message);
        }
    }

    private void handleSyncError(@Nullable SyncInfo.SyncResult syncResult) {
        if (syncResult == null) {
            return;
        }
        switch (syncResult) {
            case SUCCESS:
                showMessage(R.string.sync_success);
                break;
            case FAILED:
                showMessage(R.string.sync_failed);
                break;
        }
    }

    private void showMessage(int messageResource) {
        Toast.makeText(this, getText(messageResource), Toast.LENGTH_LONG).show();
    }

    private void startNotesGenService() {
        mActivityViewModel.startService();
    }
}

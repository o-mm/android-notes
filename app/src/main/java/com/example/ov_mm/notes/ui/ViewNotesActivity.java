package com.example.ov_mm.notes.ui;

import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Consumer;
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
import com.example.ov_mm.notes.ui.service.GenerateNotesService;
import com.example.ov_mm.notes.vm.SyncInfo;
import com.example.ov_mm.notes.vm.ViewNotesVm;

import java.util.Objects;

import javax.inject.Inject;

public class ViewNotesActivity extends BaseActivity implements ViewNotesFragment.OnListItemInteractionListener,
        SearchSortFragment.SearchSortListenerProvider {

    @Inject ViewNotesVm mViewNotesVm;
    private View mToolsContainer;
    private TextView mLastSyncText;
    private ProgressBar mSyncProgressBar;
    private ImageButton mSyncButton;
    @NonNull private final CommonBroadcastReceiver mBroadcastReceiver = new CommonBroadcastReceiver();

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
        this.registerReceiver(mBroadcastReceiver, new IntentFilter(CommonBroadcastReceiver.NOTES_GENERATION_FINISHED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mBroadcastReceiver);
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
        menu.add(R.string.generate_notes).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                startNotesGenService();
                final CharSequence title = item.getTitle();
                item.setTitle(R.string.generating_notes);
                item.setEnabled(false);
                mBroadcastReceiver.addListener(CommonBroadcastReceiver.NOTES_GENERATION_FINISHED,
                    new Consumer<Intent>() {
                        @Override
                        public void accept(Intent intent) {
                            item.setTitle(title);
                            item.setEnabled(true);
                            mViewNotesVm.refreshNotes();
                            mBroadcastReceiver.removeListener(CommonBroadcastReceiver.NOTES_GENERATION_FINISHED, this);
                        }
                    });
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
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0,
            new Intent(CommonBroadcastReceiver.NOTES_GENERATION_FINISHED), 0);
        Intent intent = new Intent(this, GenerateNotesService.class);
        intent.putExtra(GenerateNotesService.FINISH_GENERATING_BROADCAST, broadcast);
        startService(intent);
    }
}

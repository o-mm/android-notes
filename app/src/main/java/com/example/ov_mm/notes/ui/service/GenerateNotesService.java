package com.example.ov_mm.notes.ui.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.ov_mm.notes.NotesApp;
import com.example.ov_mm.notes.R;
import com.example.ov_mm.notes.event.EventManager;
import com.example.ov_mm.notes.repository.NotesRepository;
import com.example.ov_mm.notes.ui.ViewNotesActivity;

public class GenerateNotesService extends IntentService {

    public static final String FINISH_GENERATING_BROADCAST = "com.example.ov_mm.notes.ui.service.FINISH_GENERATING_BROADCAST";
    private static final String TAG = "GenerateNotesService";
    private static final int NOTIFICATION_FOREGROUND_ID = 1;
    private static final int NOTIFICATION_FINISHED_ID = 2;
    private NotesRepository mRepository;
    private EventManager mEventManager;

    public GenerateNotesService() {
        this(TAG);
    }

    public GenerateNotesService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRepository = ((NotesApp) getApplication()).getNotesAppComponent().repository();
        mEventManager = ((NotesApp) getApplication()).getNotesAppComponent().eventManager();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEventManager.stopGenerating();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        startForeground();
        boolean failed = false;
        try {
            mRepository.fillDatabase();
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage(), e);
            failed = true;
        } finally {
            stopForeground(true);
            NotificationManagerCompat.from(GenerateNotesService.this)
                .notify(NOTIFICATION_FINISHED_ID,
                    createNotification(
                        getString(R.string.generate_notes_title),
                        getString(failed ? R.string.generate_notes_failed : R.string.generate_notes_finished),
                        getString(R.string.generate_notes_title),
                        true));
            mEventManager.stopGenerating();
        }
    }

    @NonNull
    private Notification createNotification(@Nullable String title, @Nullable String text, @Nullable String ticker, boolean autoCancel) {
        Intent notificationIntent = new Intent(this, ViewNotesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        return new NotificationCompat.Builder(this, Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            ? NotificationChannel.DEFAULT_CHANNEL_ID : "")
            .setSmallIcon(R.drawable.ic_android)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setTicker(ticker).setAutoCancel(autoCancel).build();
    }

    private void startForeground() {
        startForeground(NOTIFICATION_FOREGROUND_ID, createNotification(
            getString(R.string.generate_notes_title),
            getString(R.string.generate_notes_running),
            getString(R.string.generate_notes_title),
            false));
    }
}

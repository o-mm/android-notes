package com.example.ov_mm.notes.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.util.Consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommonBroadcastReceiver extends BroadcastReceiver {

    public static final String NOTES_GENERATION_FINISHED = "com.example.ov_mm.notes.ui.NOTES_GENERATION_FINISHED";

    @NonNull private final Map<String, Set<Consumer<Intent>>> listeners = new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Set<Consumer<Intent>> consumers = listeners.get(action);
        if (consumers != null) {
            for (Consumer<Intent> consumer : new ArrayList<>(consumers)) { //consumer can remove himself from listeners
                consumer.accept(intent);
            }
        }
    }

    public void addListener(@NonNull String action, @NonNull Consumer<Intent> consumer) {
        if (listeners.get(action) == null) {
            listeners.put(action, new HashSet<Consumer<Intent>>());
        }
        listeners.get(action).add(consumer);
    }

    public void removeListener(@NonNull String action, @NonNull Consumer<Intent> consumer) {
        if (listeners.get(action) != null) {
            listeners.get(action).remove(consumer);
        }
    }
}

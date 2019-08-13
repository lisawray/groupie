package com.xwray.groupie;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import java.util.Collection;

/**
 * A wrapper around {@link DiffUtil} that calculates diff in a background thread
 */
class AsyncDiffUtil {
    interface Callback extends ListUpdateCallback {
        /**
         * Called on the main thread before DiffUtil dispatches the result
         */
        @MainThread
        void onDispatchAsyncResult(@NonNull Collection<? extends Group> newGroups);
    }

    private final Callback asyncDiffUtilCallback;
    private int maxScheduledGeneration;
    private Collection<? extends Group> groups;

    AsyncDiffUtil(@NonNull Callback callback) {
        this.asyncDiffUtilCallback = callback;
    }

    @NonNull
    Callback getAsyncDiffUtilCallback() {
        return asyncDiffUtilCallback;
    }

    @NonNull
    Collection<? extends Group> getGroups() {
        return groups;
    }

    int getMaxScheduledGeneration() {
        return maxScheduledGeneration;
    }

    void calculateDiff(@NonNull Collection<? extends Group> newGroups,
                       @NonNull DiffUtil.Callback diffUtilCallback,
                       @Nullable OnAsyncUpdateListener onAsyncUpdateListener,
                       boolean detectMoves) {
        groups = newGroups;
        // incrementing generation means any currently-running diffs are discarded when they finish
        final int runGeneration = ++maxScheduledGeneration;
        new DiffTask(this, diffUtilCallback, runGeneration, detectMoves, onAsyncUpdateListener).execute();
    }
}

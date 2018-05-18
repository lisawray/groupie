package com.xwray.groupie;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import java.util.Collection;

class AsyncDiffUtil {
    interface Callback extends ListUpdateCallback {
        /**
         * Called before DiffUtil dispatches the result
         */
        void onDispatchResult(Collection<? extends Group> newGroups);
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

    void calculateDiff(Collection<? extends Group> newGroups, DiffUtil.Callback diffUtilCallback) {
        groups = newGroups;
        // incrementing generation means any currently-running diffs are discarded when they finish
        final int runGeneration = ++maxScheduledGeneration;
        new DiffTask(this, diffUtilCallback, runGeneration).execute();
    }
}

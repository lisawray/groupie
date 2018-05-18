package com.xwray.groupie;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import java.lang.ref.WeakReference;

class DiffTask extends AsyncTask<Void, Void, DiffUtil.DiffResult> {
    private final DiffUtil.Callback diffCallback;
    private final WeakReference<AsyncDiffUtil> asyncListDiffer;
    private final int runGeneration;

    DiffTask(@NonNull AsyncDiffUtil asyncDiffUtil,
             @NonNull DiffUtil.Callback callback,
             int runGeneration) {
        this.diffCallback = callback;
        this.asyncListDiffer = new WeakReference<>(asyncDiffUtil);
        this.runGeneration = runGeneration;
    }

    @Override
    protected DiffUtil.DiffResult doInBackground(Void... voids) {
        return DiffUtil.calculateDiff(diffCallback);
    }

    @Override
    protected void onPostExecute(DiffUtil.DiffResult diffResult) {
        AsyncDiffUtil async = asyncListDiffer.get();
        if (async != null && runGeneration == async.getMaxScheduledGeneration()) {
            async.getAsyncDiffUtilCallback().onDispatchResult(async.getGroups());
            diffResult.dispatchUpdatesTo(async.getAsyncDiffUtilCallback());
        }
    }
}

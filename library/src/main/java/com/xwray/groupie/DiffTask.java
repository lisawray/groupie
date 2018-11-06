package com.xwray.groupie;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * An async task implementation that runs {@link DiffUtil#calculateDiff(DiffUtil.Callback)}
 * in a background thread. This task will call {@link AsyncDiffUtil.Callback#onDispatchResult(Collection)}
 * passing the new list just before dispatching the diff result to the provided
 * {@link DiffUtil.Callback} so that the new list.
 * <p>This task is executed via {@link AsyncDiffUtil#calculateDiff(Collection, DiffUtil.Callback)}.
 */
class DiffTask extends AsyncTask<Void, Void, DiffUtil.DiffResult> {
    private final DiffUtil.Callback diffCallback;
    private final WeakReference<AsyncDiffUtil> asyncListDiffer;
    private final int runGeneration;
    private Exception backgroundException = null;

    DiffTask(@NonNull AsyncDiffUtil asyncDiffUtil,
             @NonNull DiffUtil.Callback callback,
             int runGeneration) {
        this.diffCallback = callback;
        this.asyncListDiffer = new WeakReference<>(asyncDiffUtil);
        this.runGeneration = runGeneration;
    }

    @Override
    @Nullable
    protected DiffUtil.DiffResult doInBackground(Void... voids) {
        try {
            return DiffUtil.calculateDiff(diffCallback);
        } catch (Exception e) {
            backgroundException = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(@Nullable DiffUtil.DiffResult diffResult) {
        if (backgroundException != null) {
            throw new RuntimeException(backgroundException);
        }
        AsyncDiffUtil async = asyncListDiffer.get();
        if (shouldDispatchResult(diffResult, async)) {
            async.getAsyncDiffUtilCallback().onDispatchResult(async.getGroups());
            diffResult.dispatchUpdatesTo(async.getAsyncDiffUtilCallback());
        }
    }

    private boolean shouldDispatchResult(@Nullable DiffUtil.DiffResult diffResult, AsyncDiffUtil async) {
        return diffResult != null && async != null && runGeneration == async.getMaxScheduledGeneration();
    }
}

package com.xwray.groupie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A wrapper around {@link DiffUtil} that calculates diff in a background thread
 */
class AsyncDiffUtil implements ListUpdateCallback, OnAsyncUpdateListener {

    interface Callback extends ListUpdateCallback {
        void onUpdateComplete(List<Group> groups);
    }

    private final Callback asyncDiffUtilCallback;
    private int maxScheduledGeneration;
    private List<Group> newGroupList;
    private List<Group> resultGroups;

    AsyncDiffUtil(@NonNull Callback callback) {
        this.asyncDiffUtilCallback = callback;
    }

    @Override
    public void onInserted(int position, int count) {
        List<Group> groupList = newGroupList.subList(position, position + count);
        resultGroups.addAll(position, groupList);

        asyncDiffUtilCallback.onInserted(position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
        List<Group> subList = resultGroups.subList(position, position + count);
        resultGroups.removeAll(subList);

        asyncDiffUtilCallback.onRemoved(position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        resultGroups.remove(fromPosition);
        Group group = newGroupList.get(toPosition);
        resultGroups.add(toPosition, group);

        asyncDiffUtilCallback.onMoved(fromPosition, toPosition);
    }

    @Override
    public void onChanged(int position, int count, @Nullable Object payload) {
        List<Group> subList = resultGroups.subList(position, position + count);
        resultGroups.removeAll(subList);
        List<Group> newSubList = newGroupList.subList(position, position + count);
        resultGroups.addAll(position, newSubList);

        asyncDiffUtilCallback.onChanged(position, count, payload);
    }

    @Override
    public void onUpdateComplete() {
        List<Group> groups = this.resultGroups;
        newGroupList = null;
        resultGroups = null;
        asyncDiffUtilCallback.onUpdateComplete(groups);
    }

    @NonNull
    ListUpdateCallback getAsyncDiffUtilCallback() {
        return asyncDiffUtilCallback;
    }

    int getMaxScheduledGeneration() {
        return maxScheduledGeneration;
    }

    void calculateDiff(@NonNull Collection<Group> oldGroups,
                       @NonNull Collection<Group> newGroups,
                       @NonNull DiffUtil.Callback diffUtilCallback,
                       @Nullable final OnAsyncUpdateListener onAsyncUpdateListener,
                       boolean detectMoves) {
        newGroupList = new ArrayList<>(newGroups);
        resultGroups = new ArrayList<>(oldGroups);
        // incrementing generation means any currently-running diffs are discarded when they finish
        final int runGeneration = ++maxScheduledGeneration;
        new DiffTask(this, diffUtilCallback, runGeneration, detectMoves, mergeListener(onAsyncUpdateListener)).execute();
    }

    private OnAsyncUpdateListener mergeListener(@Nullable final OnAsyncUpdateListener onAsyncUpdateListener) {
        final WeakReference<OnAsyncUpdateListener> listenerWeakReference = new WeakReference<OnAsyncUpdateListener>(this);

        return new OnAsyncUpdateListener() {

            @Override
            public void onUpdateComplete() {
                listenerWeakReference.get().onUpdateComplete();
                onAsyncUpdateListener.onUpdateComplete();
            }
        };
    }
}

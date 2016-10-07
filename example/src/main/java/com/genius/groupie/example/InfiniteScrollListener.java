package com.genius.groupie.example;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int current_page = 0;
    private LinearLayoutManager linearLayoutManager;
    private Handler handler;

    public InfiniteScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
        this.handler = new Handler();
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal || totalItemCount == 0) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                // End has been reached
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    current_page++;
                    onLoadMore(current_page);
                    loading = true;
                }
            }
        };

        // We shouldn't directly mutate the RecyclerView data in onScrolled
        // This is because it may be called during the measure & layout pass where you cannot change the data
        // Post-pone any adapter changes until the next frame
        handler.post(runnable);
    }

    public abstract void onLoadMore(int current_page);
}

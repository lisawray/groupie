package com.xwray.groupie.example.core;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int currentPage = 0;
    private LinearLayoutManager linearLayoutManager;
    private Runnable loadMore = new Runnable() {
        @Override
        public void run() {
            onLoadMore(currentPage);
        }
    };

    public InfiniteScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
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
            currentPage++;
            recyclerView.post(loadMore);
            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}

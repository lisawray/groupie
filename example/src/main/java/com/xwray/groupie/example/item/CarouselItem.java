package com.xwray.groupie.example.item;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xwray.groupie.Item;
import com.xwray.groupie.example.R;
import com.xwray.groupie.example.viewholder.CarouselViewHolder;

/**
 * A horizontally scrolling RecyclerView, for use in a vertically scrolling RecyclerView.
 */
public class CarouselItem extends Item<CarouselViewHolder> {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.ItemDecoration carouselDecoration;
    private LinearLayoutManager layoutManager;

    public CarouselItem(RecyclerView.ItemDecoration itemDecoration) {
        this.carouselDecoration = itemDecoration;
    }

    @Override public int getLayout() {
        return R.layout.item_carousel;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public CarouselViewHolder createViewHolder(View itemView) {
        return new CarouselViewHolder(itemView);
    }

    @Override
    public void bind(CarouselViewHolder viewHolder, int position) {
        recyclerView = viewHolder.recyclerView;
        layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // We don't know if the layout we're passed has been bound before so
        // we need to ensure we don't register the item decoration multiple times,
        // by trying to remove it first. (Nothing happens if it's not registered.)
        recyclerView.removeItemDecoration(carouselDecoration);
        recyclerView.addItemDecoration(carouselDecoration);
    }
}

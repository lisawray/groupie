package com.xwray.groupie.example.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xwray.groupie.ViewHolder;
import com.xwray.groupie.example.R;

public class CarouselViewHolder extends ViewHolder {

    public final RecyclerView recyclerView;

    public CarouselViewHolder(View rootView) {
        super(rootView);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
    }
}

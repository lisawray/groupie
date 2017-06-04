package com.xwray.groupie;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public abstract class TouchCallback extends ItemTouchHelper.SimpleCallback {

    public TouchCallback() {
        super(0, 0);
    }

    @Override public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return ((ViewHolder) viewHolder).getSwipeDirs();
    }

    @Override public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return ((ViewHolder) viewHolder).getDragDirs();
    }
}

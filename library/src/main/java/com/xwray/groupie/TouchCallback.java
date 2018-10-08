package com.xwray.groupie;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

public abstract class TouchCallback extends ItemTouchHelper.SimpleCallback {

    public TouchCallback() {
        super(0, 0);
    }

    @Override public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return ((ViewHolder) viewHolder).getSwipeDirs();
    }

    @Override public int getDragDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return ((ViewHolder) viewHolder).getDragDirs();
    }
}

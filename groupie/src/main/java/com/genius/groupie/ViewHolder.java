package com.genius.groupie;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

public class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final T binding;
    private final Map<String, Object> extras = new HashMap<>();
    private int swipeDirs = 0;
    private int dragDirs = 0;

    public ViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public int getSwipeDirs() {
        return swipeDirs;
    }

    public void setSwipeDirs(int swipeDirs) {
        this.swipeDirs = swipeDirs;
    }

    public int getDragDirs() {
        return dragDirs;
    }

    public void setDragDirs(int dragDirs) {
        this.dragDirs = dragDirs;
    }
}

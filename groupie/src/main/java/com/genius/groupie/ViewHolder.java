package com.genius.groupie;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import java.util.Map;

public class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final T binding;
    private Item item;

    public ViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Item item) {
        this.item = item;
    }

    public void unbind() {
        this.item = null;
    }

    public Map<String, Object> getExtras() {
        return item.getExtras();
    }

    public int getSwipeDirs() {
        return item.getSwipeDirs();
    }

    public int getDragDirs() {
        return item.getDragDirs();
    }

    public Item getItem() {
        return item;
    }
}

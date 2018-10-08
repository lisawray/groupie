package com.xwray.groupie.example.databinding.item;

import androidx.annotation.ColorRes;

public class UpdatableItem extends SmallCardItem {

    private final int index;

    public UpdatableItem(@ColorRes int colorRes, int index) {
        super(colorRes, String.valueOf(index));
        this.index = index;
    }

    @Override
    public long getId() {
        return index;
    }
}

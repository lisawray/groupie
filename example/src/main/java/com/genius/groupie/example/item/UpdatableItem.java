package com.genius.groupie.example.item;

import android.support.annotation.ColorRes;

import com.genius.groupie.UpdatingGroup;

public class UpdatableItem extends SmallCardItem implements UpdatingGroup.Comparable<UpdatableItem> {

    private final int index;

    public UpdatableItem(@ColorRes int colorRes, int index) {
        super(colorRes, String.valueOf(index));
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override public boolean areContentsTheSame(UpdatableItem other) {
        return true;
    }

    @Override public boolean areItemsTheSame(UpdatableItem other) {
        return other.getIndex() == index;
    }
}

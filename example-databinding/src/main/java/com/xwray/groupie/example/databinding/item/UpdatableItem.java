package com.xwray.groupie.example.databinding.item;

public class UpdatableItem extends SmallCardItem {

    private final int index;

    public UpdatableItem(int index) {
        super(String.valueOf(index));
        this.index = index;
    }

    @Override
    public long getId() {
        return index;
    }
}

package com.genius.groupie.example.item;

import android.support.annotation.ColorRes;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SwipeToDeleteItem extends CardItem {
    public SwipeToDeleteItem(@ColorRes int colorRes) {
        super(colorRes);
    }

    @Override public int getSwipeDirs() {
        return ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    }
}

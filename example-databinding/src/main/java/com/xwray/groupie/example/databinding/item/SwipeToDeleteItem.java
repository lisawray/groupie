package com.xwray.groupie.example.databinding.item;

import android.support.annotation.ColorInt;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SwipeToDeleteItem extends CardItem {
    public SwipeToDeleteItem(@ColorInt int colorRes) {
        super(colorRes);
    }

    @Override public int getSwipeDirs() {
        return ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    }
}

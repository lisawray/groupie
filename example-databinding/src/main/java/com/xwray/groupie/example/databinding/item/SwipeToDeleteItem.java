package com.xwray.groupie.example.databinding.item;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.ItemTouchHelper;

public class SwipeToDeleteItem extends CardItem {
    public SwipeToDeleteItem(@ColorInt int colorRes) {
        super();
    }

    @Override public int getSwipeDirs() {
        return ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    }
}

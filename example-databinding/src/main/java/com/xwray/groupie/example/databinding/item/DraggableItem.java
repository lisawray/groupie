package com.xwray.groupie.example.databinding.item;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.ItemTouchHelper;

public class DraggableItem extends CardItem {
    public DraggableItem(@ColorInt int colorRes) {
        super();
    }

    @Override public int getDragDirs() {
        return ItemTouchHelper.DOWN | ItemTouchHelper.UP;
    }
}

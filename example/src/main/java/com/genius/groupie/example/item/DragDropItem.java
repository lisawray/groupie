package com.genius.groupie.example.item;

import android.support.annotation.ColorRes;
import android.support.v7.widget.helper.ItemTouchHelper;

public class DragDropItem extends SmallCardItem {
    public DragDropItem(@ColorRes int colorRes, int index) {
        super(colorRes);
        setText(String.valueOf(index));
    }

    @Override public int getDragDirs() {
        return ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    }
}

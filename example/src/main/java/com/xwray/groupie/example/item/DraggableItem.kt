package com.xwray.groupie.example.item

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.ItemTouchHelper

class DraggableItem(@ColorInt colorInt: Int) : CardItem(colorInt) {

    override fun getDragDirs(): Int {
        return ItemTouchHelper.UP or ItemTouchHelper.DOWN
    }
}

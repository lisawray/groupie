package com.xwray.groupie.example.item

import androidx.annotation.ColorRes
import androidx.recyclerview.widget.ItemTouchHelper

class SwipeToDeleteItem(@ColorRes colorRes: Int) : CardItem(colorRes) {

    override val swipeDirs: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
}

package com.xwray.groupie.example.viewbinding.item

import androidx.annotation.ColorInt
import androidx.recyclerview.widget.ItemTouchHelper

class SwipeToDeleteItem(@ColorInt colorInt: Int) : CardItem(colorInt) {
  override fun getSwipeDirs(): Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
}

package com.xwray.groupie.example.item

import android.support.annotation.ColorInt
import android.support.v7.widget.helper.ItemTouchHelper

class SwipeToDeleteItem(@ColorInt colorRes: Int) : CardItem(colorRes) {

    override fun getSwipeDirs(): Int {
        return ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    }
}

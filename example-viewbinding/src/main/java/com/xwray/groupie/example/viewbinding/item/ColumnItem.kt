package com.xwray.groupie.example.viewbinding.item

import androidx.annotation.ColorInt
import com.xwray.groupie.example.viewbinding.INSET
import com.xwray.groupie.example.viewbinding.INSET_TYPE_KEY

class ColumnItem(
    @ColorInt colorInt:
    Int, index: Int
) : CardItem(colorInt, index.toString()) {

    init {
        extras[INSET_TYPE_KEY] = INSET
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount / 2
}

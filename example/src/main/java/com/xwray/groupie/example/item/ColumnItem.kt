package com.xwray.groupie.example.item

import androidx.annotation.ColorRes
import com.xwray.groupie.example.INSET
import com.xwray.groupie.example.INSET_TYPE_KEY

class ColumnItem(@ColorRes colorRes: Int, index: Int) : CardItem(colorRes, index.toString()) {

    init {
        extras[INSET_TYPE_KEY] = INSET
    }

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2

}

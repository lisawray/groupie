package com.xwray.groupie.example.item

import android.support.annotation.ColorInt
import com.xwray.groupie.example.INSET
import com.xwray.groupie.example.INSET_TYPE_KEY

class ColumnItem(@ColorInt colorRes: Int, index: Int) : CardItem(colorRes, index.toString()) {

    init {
        extras.put(INSET_TYPE_KEY, INSET)
    }

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2

}

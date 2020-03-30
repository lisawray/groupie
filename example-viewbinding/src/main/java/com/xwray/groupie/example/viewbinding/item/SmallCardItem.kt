package com.xwray.groupie.example.viewbinding.item

import androidx.annotation.ColorInt

open class SmallCardItem : CardItem {
    constructor(@ColorInt colorInt: Int) : super(colorInt)
    constructor(@ColorInt colorInt: Int, text: CharSequence) : super(colorInt, text)

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount / 3
}

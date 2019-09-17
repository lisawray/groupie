package com.xwray.groupie.example.item

import androidx.annotation.ColorInt

open class SmallCardItem(@ColorInt private val colorInt: Int, text: CharSequence? = "") : CardItem(colorInt, text) {

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 3

}

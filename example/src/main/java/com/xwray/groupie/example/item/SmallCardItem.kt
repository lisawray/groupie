package com.xwray.groupie.example.item

import androidx.annotation.ColorRes

open class SmallCardItem(@ColorRes private val colorRes: Int, text: CharSequence? = "") : CardItem(colorRes, text) {

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 3

}

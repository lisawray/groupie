package com.xwray.groupie.example.item

import androidx.annotation.ColorInt

data class UpdatableItem(@ColorInt private val colorInt: Int, private val index: Int) : SmallCardItem(colorInt, index.toString()) {

    override fun getId() = index.toLong()

}

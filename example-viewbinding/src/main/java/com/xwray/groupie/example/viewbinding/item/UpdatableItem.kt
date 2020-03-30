package com.xwray.groupie.example.viewbinding.item

import androidx.annotation.ColorInt

class UpdatableItem(
    @ColorInt colorInt: Int,
    private val index: Int
) : SmallCardItem(colorInt, index.toString()) {

    override fun getId(): Long = index.toLong()
}

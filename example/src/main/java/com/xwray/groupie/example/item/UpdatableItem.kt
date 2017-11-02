package com.xwray.groupie.example.item

import android.support.annotation.ColorRes

class UpdatableItem(@ColorRes colorRes: Int, private val index: Int) : SmallCardItem(colorRes, index.toString()) {

    override fun getId() = index.toLong()

}

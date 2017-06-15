package com.xwray.groupie.example.item

import android.support.annotation.ColorInt
import android.view.View

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.example.R

/**
 * A card item with a fixed width so it can be used with a horizontal layout manager.
 */
class CarouselCardItem(@param:ColorInt private val colorRes: Int) : Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.item_square_card
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //viewHolder.getRoot().setBackgroundResource(colorRes);
    }
}

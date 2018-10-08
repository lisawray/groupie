package com.xwray.groupie.example.item

import androidx.annotation.ColorInt
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.example.R

/**
 * A card item with a fixed width so it can be used with a horizontal layout manager.
 */
class CarouselCardItem(@ColorInt private val colorRes: Int) : Item<ViewHolder>() {

    override fun getLayout() = R.layout.item_square_card

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //viewHolder.getRoot().setBackgroundResource(colorRes);
    }
}

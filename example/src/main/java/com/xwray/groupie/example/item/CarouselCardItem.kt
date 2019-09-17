package com.xwray.groupie.example.item

import androidx.annotation.ColorInt
import com.xwray.groupie.example.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

/**
 * A card item with a fixed width so it can be used with a horizontal layout manager.
 */
class CarouselCardItem(@ColorInt private val colorRes: Int) : Item() {

    override val layoutRes: Int = R.layout.item_square_card

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //viewHolder.getRoot().setBackgroundResource(colorRes);
    }
}

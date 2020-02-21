package com.xwray.groupie.example.item

import androidx.annotation.ColorInt
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.example.R

/**
 * A card item with a fixed width so it can be used with a horizontal layout manager.
 */
class CarouselCardItem(@ColorInt private val colorInt: Int) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.item_square_card

    override fun bind(viewHolder: GroupieViewHolder, position: Int, isSelected: Boolean) {
        viewHolder.root.setBackgroundColor(colorInt)
    }
}

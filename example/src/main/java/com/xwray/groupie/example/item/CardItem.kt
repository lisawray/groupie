package com.xwray.groupie.example.item

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.xwray.groupie.example.INSET
import com.xwray.groupie.example.INSET_TYPE_KEY
import com.xwray.groupie.example.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_card.*

open class CardItem (@ColorRes private val colorRes: Int, val text: CharSequence? = "") : Item() {

    init {
        extras[INSET_TYPE_KEY] = INSET
    }

    override val layoutRes: Int = R.layout.item_card

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        viewHolder.root.setBackgroundColor(ContextCompat.getColor(viewHolder.containerView.context, colorRes))
        viewHolder.text.text = text
    }
}

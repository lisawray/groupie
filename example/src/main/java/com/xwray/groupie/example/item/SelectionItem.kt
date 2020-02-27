package com.xwray.groupie.example.item

import android.graphics.Color
import androidx.annotation.ColorInt
import com.xwray.groupie.example.INSET
import com.xwray.groupie.example.INSET_TYPE_KEY
import com.xwray.groupie.example.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.item_card.*

open class SelectionItem (@ColorInt private val colorInt: Int, val text: CharSequence? = "") : Item() {

    init {
        extras[INSET_TYPE_KEY] = INSET
    }

    override fun getLayout() = R.layout.item_card

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (isSelectable) {
            viewHolder.root.setBackgroundColor(Color.GRAY)
        } else {
            viewHolder.root.setBackgroundColor(colorInt)
        }
        viewHolder.text.text = text
    }
}

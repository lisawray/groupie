package com.xwray.groupie.example.item

import android.support.annotation.ColorInt
import com.xwray.groupie.example.INSET
import com.xwray.groupie.example.INSET_TYPE_KEY
import com.xwray.groupie.example.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_card.*

open class CardItem @JvmOverloads constructor(@param:ColorInt private val colorRes: Int, var text: CharSequence? = "") : Item() {

    init {
        extras.put(INSET_TYPE_KEY, INSET)
    }

    override fun getLayout(): Int {
        return R.layout.item_card
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //viewBinding.getRoot().setBackgroundColor(colorRes);
        viewHolder.text.text = text
    }
}

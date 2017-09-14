package com.xwray.groupie.example.item

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.view.View
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.example.R
import kotlinx.android.synthetic.main.item_header.view.*


open class HeaderItem @JvmOverloads constructor(
        @param:StringRes private val titleStringResId: Int,
        @param:StringRes private val subtitleResId: Int = 0,
        @param:DrawableRes private val iconResId: Int = 0,
        private val onIconClickListener: View.OnClickListener? = null) : Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.item_header
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.title.setText(titleStringResId)

        viewHolder.itemView.subtitle.apply {
            visibility = if (subtitleResId > 0) View.VISIBLE else View.GONE
            if (subtitleResId > 0) {
                setText(subtitleResId)
            }
        }

        viewHolder.itemView.icon.apply {
            visibility = if (iconResId > 0) View.VISIBLE else View.GONE
            if (iconResId > 0) {
                setImageResource(iconResId)
                setOnClickListener(onIconClickListener)
            }
        }
    }
}

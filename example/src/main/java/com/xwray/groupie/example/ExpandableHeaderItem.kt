package com.xwray.groupie.example

import android.graphics.drawable.Animatable
import androidx.annotation.StringRes
import android.view.View
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.example.item.HeaderItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.item_header.*

class ExpandableHeaderItem(@StringRes titleStringResId: Int,
                           @StringRes subtitleResId: Int)
    : HeaderItem(titleStringResId, subtitleResId), ExpandableItem {

    var clickListener: ((ExpandableHeaderItem) -> Unit)? = null

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: GroupieViewHolder, position: Int, isSelected: Boolean) {
        super.bind(viewHolder, position, isSelected)

        // Initial icon state -- not animated.
        viewHolder.icon.apply {
            visibility = View.VISIBLE
            setImageResource(if (expandableGroup.isExpanded) R.drawable.collapse else R.drawable.expand)
            setOnClickListener {
                expandableGroup.onToggleExpanded()
                bindIcon(viewHolder)
            }
        }

        viewHolder.itemView.setOnClickListener {
            clickListener?.invoke(this)
        }
    }

    private fun bindIcon(viewHolder: GroupieViewHolder) {
        viewHolder.icon.apply {
            visibility = View.VISIBLE
            setImageResource(if (expandableGroup.isExpanded) R.drawable.collapse_animated else R.drawable.expand_animated)
            (drawable as Animatable).start()
        }
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }
}

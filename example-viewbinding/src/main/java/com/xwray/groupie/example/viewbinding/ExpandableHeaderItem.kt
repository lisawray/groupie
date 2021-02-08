package com.xwray.groupie.example.viewbinding

import android.graphics.drawable.Animatable
import android.view.View
import androidx.annotation.StringRes
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.Item
import com.xwray.groupie.example.viewbinding.databinding.ItemHeaderBinding
import com.xwray.groupie.example.viewbinding.item.HeaderItem

class ExpandableHeaderItem(
    val itemData: ExpandableHeaderItemData
) : HeaderItem(itemData.titleStringResId, itemData.subtitleResId), ExpandableItem {

    private var expandableGroup: ExpandableGroup? = null

    override fun bind(viewBinding: ItemHeaderBinding, position: Int) {
        super.bind(viewBinding, position)

        // Initial icon state -- not animated.
        viewBinding.icon.visibility = View.VISIBLE
        viewBinding.icon.setImageResource(if (expandableGroup!!.isExpanded) R.drawable.collapse else R.drawable.expand)
        viewBinding.icon.setOnClickListener {
            expandableGroup!!.onToggleExpanded()
            bindIcon(viewBinding)
        }
    }

    private fun bindIcon(viewBinding: ItemHeaderBinding) {
        viewBinding.icon.visibility = View.VISIBLE
        viewBinding.icon.setImageResource(if (expandableGroup!!.isExpanded) R.drawable.collapse_animated else R.drawable.expand_animated)
        val drawable = viewBinding.icon.drawable as Animatable
        drawable.start()
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    override fun isSameAs(other: Item<*>): Boolean {
        if (other !is ExpandableHeaderItem) return false
        return itemData.headerId == other.itemData.headerId
    }

    override fun hasSameContentAs(other: Item<*>): Boolean {
        if (other !is ExpandableHeaderItem) return false
        return itemData.titleStringResId == other.itemData.titleStringResId &&
            itemData.subtitleResId == other.itemData.subtitleResId
    }
}

data class ExpandableHeaderItemData(
    @StringRes val titleStringResId: Int,
    @StringRes val subtitleResId: Int,
    val headerId: Int? = null
)

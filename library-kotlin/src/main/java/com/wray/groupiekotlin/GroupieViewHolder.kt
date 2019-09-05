package com.wray.groupiekotlin

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class GroupieViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

    private var _item: AnyItem? = null

    val item: AnyItem
        get() = _item ?: throw IllegalStateException("Attempting to get Item from GroupieViewHolder but it's not yet set")

    val extras: Map<String, Any>
        get() = item.extras

    val swipeDirs: Int
        get() = item.swipeDirs

    val dragDirs: Int
        get() = item.dragDirs

    val root: View
        get() = itemView

    fun bind(item: AnyItem,
             onItemClickListener: OnItemClickListener? = null,
             onItemLongClickListener: OnItemLongClickListener? = null) {
        this._item = item

        // Only set the top-level click listeners if a) they exist, and b) the _item has
        // clicks enabled.  This ensures we don't interfere with user-set click listeners.

        // It would be nice to keep our listeners always attached and set them only once on creating
        // the viewholder, but different items of the same layout type may not have the same click
        // listeners or even agree on whether they are clickable.
        if (onItemClickListener != null && item.isClickable) {
            itemView.setOnClickListener(onClickListener)
            this.onItemClickListener = onItemClickListener
        }

        if (onItemLongClickListener != null && item.isLongClickable) {
            itemView.setOnLongClickListener(onLongClickListener)
            this.onItemLongClickListener = onItemLongClickListener
        }
    }

    fun unbind() {
        // Only set the top-level click listener to null if we had previously set it ourselves.

        // This avoids undoing any click listeners the user may set which might be persistent for
        // the life of the viewholder. (It's up to the user to make sure that's correct behavior.)
        if (onItemClickListener != null && item.isClickable) {
            itemView.setOnClickListener(null)
        }
        if (onItemLongClickListener != null && item.isLongClickable) {
            itemView.setOnLongClickListener(null)
        }
        this._item = null
        this.onItemClickListener = null
        this.onItemLongClickListener = null
    }

    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null

    private val onClickListener = View.OnClickListener { v ->
        // Discard click if the viewholder has been removed, but was still in the process of
        // animating its removal while clicked (unlikely, but technically possible)
        if (adapterPosition != RecyclerView.NO_POSITION) {
            onItemClickListener?.onItemClick(item, v)
        }
    }

    private val onLongClickListener = View.OnLongClickListener { v ->
        // Discard long click if the viewholder has been removed, but was still in the process of
        // animating its removal while long clicked (unlikely, but technically possible)
        if (adapterPosition != RecyclerView.NO_POSITION) {
            onItemLongClickListener?.onItemLongClick(item, v) ?: false
        } else false
    }

}

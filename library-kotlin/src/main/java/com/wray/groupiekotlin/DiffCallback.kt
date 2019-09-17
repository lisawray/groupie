package com.wray.groupiekotlin

import androidx.recyclerview.widget.DiffUtil

internal class DiffCallback(
    private val oldGroups: List<Group>,
    private val newGroups: List<Group>
) : DiffUtil.Callback() {

    private val oldItemCount = oldGroups.itemCount
    private val newItemCount = newGroups.itemCount

    override fun getOldListSize(): Int = oldItemCount

    override fun getNewListSize(): Int = newItemCount

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldGroups.getItem(oldItemPosition)
        val newItem = newGroups.getItem(newItemPosition)
        return newItem.isSameAs(oldItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldGroups.getItem(oldItemPosition)
        val newItem = newGroups.getItem(newItemPosition)
        return newItem == oldItem
    }
}
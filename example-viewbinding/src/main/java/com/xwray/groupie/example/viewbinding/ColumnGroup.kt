package com.xwray.groupie.example.viewbinding

import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item
import com.xwray.groupie.viewbinding.BindableItem
import java.util.*

/**
 * A simple, non-editable, non-nested group of Items which displays a list as vertical columns.
 */
class ColumnGroup(items: List<BindableItem<*>>) : Group {

    private val items: ArrayList<BindableItem<*>> = ArrayList()

    init {
        for (i in items.indices) {
            // Rearrange items so that the adapter appears to arrange them in vertical columns
            val index = if (i % 2 == 0) {
                i / 2
            } else {
                // If columns are uneven, we'll put an extra one at the end of the first column,
                // meaning the second column's indices will all be increased by 1
                (i - 1) / 2 + (items.size / 2f).toInt() + if (items.size % 2 == 1) 1 else 0
            }
            val trackItem = items[index]
            this.items.add(trackItem)
        }
    }

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        // no real need to do anything here
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        // no real need to do anything here
    }

    override fun getItem(position: Int): BindableItem<*> = items[position]

    override fun getPosition(item: Item<*>): Int = items.indexOf(item)

    override fun getItemCount(): Int = items.size
}

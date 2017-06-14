package com.xwray.groupie.example

import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item
import java.util.*

/**
 * A simple, non-editable, non-nested group of Items which displays a list as vertical columns.
 */
class ColumnGroup(items: List<Item<*>>) : Group {

    private val items = ArrayList<Item<*>>()

    init {
        for (i in items.indices) {
            // Rearrange items so that the adapter appears to arrange them in vertical columns
            var index: Int
            if (i % 2 == 0) {
                index = i / 2
            } else {
                index = (i - 1) / 2 + (items.size / 2f).toInt()
                // If columns are uneven, we'll put an extra one at the end of the first column,
                // meaning the second column's indices will all be increased by 1
                if (items.size % 2 == 1) index++
            }
            val trackItem = items[index]
            this.items.add(trackItem)
        }
    }

    override fun setGroupDataObserver(groupDataObserver: GroupDataObserver) {
        // no real need to do anything here
    }

    override fun getItem(position: Int): Item<*> {
        return items[position]
    }

    override fun getPosition(item: Item<*>): Int {
        return items.indexOf(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

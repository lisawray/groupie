package com.xwray.groupie.example

import com.wray.groupiekotlin.AnyItem
import com.wray.groupiekotlin.Group
import com.wray.groupiekotlin.GroupDataObserver
import java.util.*

/**
 * A simple, non-editable, non-nested group of Items which displays a list as vertical columns.
 */
class ColumnGroup(items: List<AnyItem>) : Group {

    private val items = ArrayList<AnyItem>()

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

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        // no need to do anything here
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        // no need to do anything here
    }

    override fun getItem(position: Int): AnyItem {
        return items[position]
    }

    override fun getPosition(item: AnyItem): Int {
        return items.indexOf(item)
    }

    override val itemCount: Int
        get() = items.size
}

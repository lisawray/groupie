package com.xwray.groupie.example.viewbinding

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.xwray.groupie.Group
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item
import com.xwray.groupie.example.viewbinding.item.CarouselItem

/**
 * A group that contains a single carousel item and is empty when the carousel is empty
 */
class CarouselGroup(
    itemDecoration: ItemDecoration,
    adapter: GroupieAdapter
) : Group {

    private var isEmpty = true
    private val adapter: RecyclerView.Adapter<*>
    private var groupDataObserver: GroupDataObserver? = null
    private val carouselItem: CarouselItem

    init {
        this.adapter = adapter
        carouselItem = CarouselItem(itemDecoration, adapter)
        isEmpty = adapter.itemCount == 0
        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                val empty = adapter.itemCount == 0
                if (empty && !isEmpty) {
                    isEmpty = empty
                    groupDataObserver?.onItemRemoved(carouselItem, 0)
                }
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                val empty = adapter.itemCount == 0
                if (isEmpty && !empty) {
                    isEmpty = empty
                    groupDataObserver?.onItemInserted(carouselItem, 0)
                }
            }
        })
    }

    override fun getItemCount(): Int = if (isEmpty) 0 else 1

    override fun getItem(position: Int): Item<*> =
        if (position == 0 && !isEmpty) carouselItem else throw IndexOutOfBoundsException()

    override fun getPosition(item: Item<*>): Int = if (item === carouselItem && !isEmpty) 0 else -1

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        this.groupDataObserver = groupDataObserver
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        this.groupDataObserver = null
    }
}

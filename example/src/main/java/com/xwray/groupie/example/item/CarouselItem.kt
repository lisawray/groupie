package com.xwray.groupie.example.item

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.example.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_carousel.*

/**
 * A horizontally scrolling RecyclerView, for use in a vertically scrolling RecyclerView.
 */
class CarouselItem(private val carouselDecoration: RecyclerView.ItemDecoration,
                   private val carouselAdapter: GroupAdapter<com.xwray.groupie.ViewHolder>) : Item() {

    override fun getLayout(): Int {
        return R.layout.item_carousel
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = carouselAdapter

            // We don't know if the layout we're passed has been bound before so
            // we need to ensure we don't register the item decoration multiple times,
            // by trying to remove it first. (Nothing happens if it's not registered.)
            removeItemDecoration(carouselDecoration)
            addItemDecoration(carouselDecoration)
        }
    }
}

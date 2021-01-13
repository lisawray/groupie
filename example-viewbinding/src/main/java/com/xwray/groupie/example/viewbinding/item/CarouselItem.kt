package com.xwray.groupie.example.viewbinding.item

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.example.viewbinding.R
import com.xwray.groupie.example.viewbinding.databinding.ItemCarouselBinding
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder

/**
 * A horizontally scrolling RecyclerView, for use in a vertically scrolling RecyclerView.
 */
class CarouselItem(
    private val carouselDecoration: ItemDecoration,
    private val adapter: GroupieAdapter
) : BindableItem<ItemCarouselBinding>(), OnItemClickListener {

    init {
        adapter.setOnItemClickListener(this)
    }

    override fun initializeViewBinding(view: View): ItemCarouselBinding =
        ItemCarouselBinding.bind(view)

    override fun createViewHolder(itemView: View): GroupieViewHolder<ItemCarouselBinding> =
        super.createViewHolder(itemView).also {
            it.binding.recyclerView.apply {
                addItemDecoration(carouselDecoration)
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            }
        }

    override fun bind(viewBinding: ItemCarouselBinding, position: Int) {
        viewBinding.recyclerView.adapter = adapter
    }

    override fun getLayout(): Int = R.layout.item_carousel

    override fun onItemClick(item: Item<*>, view: View) {
        adapter.remove(item)
    }
}

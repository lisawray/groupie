package com.wray.groupiekotlin

import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.atomic.AtomicLong

/**
 * A typealias for `Item<*>` to make it nicer looking.
 */
typealias AnyItem = Item<*>

abstract class Item<VH : GroupieViewHolder>(
    private val _id: Long = ID_COUNTER.decrementAndGet()
): Group, SpanSizeProvider {

    companion object {
        private val ID_COUNTER = AtomicLong(0)
    }

    /**
     * A set of key/value pairs stored on the ViewHolder that can be useful for distinguishing
     * items of the same view type.
     */
    val extras = mutableMapOf<String, Any>()
    // TODO: Add documentation
    open val swipeDirs: Int = 0
    // TODO: Add documentation
    open val dragDirs: Int = 0
    // TODO: Add documentation
    open val isClickable: Boolean = true
    // TODO: Add documentation
    open val isLongClickable: Boolean = true

    @get:LayoutRes
    abstract val layoutRes: Int

    abstract fun bind(viewHolder: VH, position: Int)


    /**
     * Whether the view should be recycled. Return false to prevent the view from being recycled.
     * (Note that it may still be re-bound.)
     *
     * @return Whether the view should be recycled.
     * @see RecyclerView.Adapter.onFailedToRecycleView
     */
    open val isRecyclable: Boolean = true

    private var parentDataObserver: GroupDataObserver? = null

    @Suppress("UNCHECKED_CAST")
    fun createViewHolder(itemView: View): VH = GroupieViewHolder(itemView) as VH

    /**
     * Perform any actions required to set up the view for display.
     *
     * @param viewHolder          The viewHolder to bind
     * @param position            The adapter position
     * @param payloads            Any payloads (this list may be empty)
     * @param onItemClickListener An optional adapter-level click listener
     * @param onItemLongClickListener An optional adapter-level long click listener
     */
    @CallSuper
    open fun bind(viewHolder: VH,
             position: Int,
             payloads: List<Any> = emptyList(),
             onItemClickListener: OnItemClickListener? = null,
             onItemLongClickListener: OnItemLongClickListener? = null) {
        viewHolder.bind(this, onItemClickListener, onItemLongClickListener)
        bind(viewHolder, position, payloads)
    }

    open fun bind(viewHolder: VH, position: Int, payloads: List<Any>) {
        bind(viewHolder, position)
    }

    @CallSuper
    open fun unbind(viewHolder: VH) {
        viewHolder.unbind()
    }

    /**
     * Override this method if the same layout needs to have different viewTypes.
     * @return the viewType, defaults to the layoutId
     * @see RecyclerView.Adapter.getItemViewType(Int)
     */
    val viewType: Int
        get() = layoutRes

    fun notifyChanged() {
        parentDataObserver?.onItemChanged(this, 0)
    }

    fun notifyChanged(payload: Any) {
        parentDataObserver?.onItemChanged(this, 0, payload)
    }

    /**
     * If you don't specify an id, this id is an auto-generated unique negative integer for each Item (the less
     * likely to conflict with your model IDs.)
     *
     *
     * You may prefer to override it with the ID of a model object, for example the primary key of
     * an object from a database that it represents.
     *
     * @return A unique id
     */
    open val id: Long
        get() = _id

    open fun isSameAs(other: AnyItem): Boolean =
        if (viewType != other.viewType) {
            false
        } else id == other.id

    open fun getChangePayload(newItem: Item<VH>): Any? = null

    //region: Group Interface

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        parentDataObserver = groupDataObserver
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        parentDataObserver = null
    }

    override fun getItem(position: Int): Item<VH> {
        if (position == 0) {
            return this
        } else {
            throw IndexOutOfBoundsException("Wanted item at position $position but this Item is a Group of size 1")
        }
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount

    override fun getPosition(item: AnyItem): Int =
        if (this == item) 0 else -1

    override val itemCount: Int = 1

    //endregion: Group interface
}
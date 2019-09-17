package com.wray.groupiekotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class GroupAdapter<VH : GroupieViewHolder> : RecyclerView.Adapter<VH>(), GroupDataObserver {

    private val groups = mutableListOf<Group>()
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null
    private var lastItemForViewTypeLookup: AnyItem? = null

    var spanCount = 1

    val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return try {
                getItem(position).getSpanSize(spanCount, position)
            } catch (e: IndexOutOfBoundsException) {
                // Bug in support lib?  TODO investigate further
                spanCount
            }
        }
    }

    private val diffUtilCallbacks = object : AsyncDiffUtil.Callback {
        override fun onDispatchAsyncResult(newGroups: Collection<Group>) {
            setNewGroups(newGroups)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            if (payload != null) {
                notifyItemRangeChanged(position, count, payload)
            } else {
                notifyItemRangeChanged(position, count)
            }
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }
    }

    private val asyncDiffUtil = AsyncDiffUtil(diffUtilCallbacks)

    private fun setNewGroups(newGroups: Collection<Group>) {
        groups.forEach { it.unregisterGroupDataObserver(this) }

        groups.clear()
        groups.addAll(newGroups)

        newGroups.forEach { it.registerGroupDataObserver(this) }
    }

    //region: GroupAdapter public API

    fun getItem(position: Int): AnyItem = groups.getItem(position)

    /**
     * Updates the adapter with a new list that will be diffed on a background thread
     * and displayed once diff results are calculated.
     *
     * NOTE: This update method is NOT compatible with partial updates (change notifications
     * driven by individual groups and items).  If you update using this method, all partial
     * updates will no longer work and you must use this method to update exclusively.
     *
     * @param newGroups List of {@link Group}
     * @param detectMoves Boolean is passed to [DiffUtil.calculateDiff(DiffUtil.Callback, boolean)]. Set to true
     *                    if you want DiffUtil to detect moved items.
     * @param updateListener Optional callback for when the async update is complete
     */
    fun updateAsync(newGroups: List<Group>, detectMoves: Boolean = true, updateListener: OnAsyncUpdateListener? = null) {
        val oldGroups = groups.toList()
        val diffCallback = DiffCallback(oldGroups, newGroups)

        asyncDiffUtil.calculateDiff(newGroups, diffCallback, updateListener, detectMoves)
    }

    /**
     * Updates the adapter with a new list that will be diffed on the **main** thread
     * and displayed once diff results are calculated. Not recommended for huge lists
     *
     * @param newGroups List of {@link Group}
     * @param detectMoves Boolean is passed to [DiffUtil.calculateDiff(DiffUtil.Callback, boolean)]. Set to true
     *                    if you want DiffUtil to detect moved items.
     */
    fun update(newGroups: List<Group>, detectMoves: Boolean = true) {
        val oldGroups = groups.toList()
        val diffCallback = DiffCallback(oldGroups, newGroups)

        val diffResult = DiffUtil.calculateDiff(diffCallback, detectMoves)
        setNewGroups(newGroups)

        diffResult.dispatchUpdatesTo(diffUtilCallbacks)
    }

    fun getAdapterPosition(item: AnyItem): Int {
        var count = 0
        for (group in groups) {
            val index = group.getPosition(item)
            if (index >= 0) {
                return index + count
            }
            count += group.itemCount
        }
        return -1
    }

    fun getAdapterPosition(group: Group): Int {
        val index = groups.indexOf(group)
        if (index == -1) return -1
        var position = 0
        for (i in 0 until index) {
            position += groups[i].itemCount
        }
        return position
    }

    val groupCount: Int
        get() = groups.size

    fun getItemCount(groupIndex: Int): Int {
        if (groupIndex > groups.size) {
            throw IndexOutOfBoundsException("Requested group index $groupIndex but there are only ${groups.size} groups")
        }
        return groups[groupIndex].itemCount
    }

    fun clear() {
        groups.forEach { group -> group.unregisterGroupDataObserver(this) }
        groups.clear()
        notifyDataSetChanged()
    }

    fun add(group: Group) {
        val itemCountBeforeGroup = itemCount
        group.registerGroupDataObserver(this)
        groups.add(group)
        notifyItemRangeInserted(itemCountBeforeGroup, group.itemCount)
    }

    fun add(index: Int, group: Group) {
        group.registerGroupDataObserver(this)
        groups.add(index, group)
        val itemCountBeforeGroup = getItemCountBeforeGroup(index)
        notifyItemRangeInserted(itemCountBeforeGroup, group.itemCount)
    }

    fun addAll(groups: List<Group>) {
        val itemCountBeforeGroup = itemCount
        var additionalSize = 0
        for (group in groups) {
            additionalSize += group.itemCount
            group.registerGroupDataObserver(this)
        }
        this.groups.addAll(groups)
        notifyItemRangeInserted(itemCountBeforeGroup, additionalSize)
    }

    fun remove(group: Group) {
        remove(groups.indexOf(group), group)
    }

    fun removeAtPosition(position: Int) {
        remove(position, getGroupAtPosition(position))
    }

    fun removeAll(groups: List<Group>) {
        groups.forEach { group -> remove(group) }
    }

    fun getGroupAtPosition(position: Int): Group {
        var previous = 0
        var size: Int
        for (group in groups) {
            size = group.itemCount
            if (position - previous < size) {
                return group
            }
            previous += group.itemCount
        }

        throw IndexOutOfBoundsException("Requested position $position in GroupAdapter but there are only $previous items")
    }

    fun getGroupForItem(item: AnyItem): Group =
        groups.first { group -> group.getPosition(item) >= 0 }

    private fun remove(position: Int, group: Group) {
        val itemCountBeforeGroup = getItemCountBeforeGroup(position)
        group.unregisterGroupDataObserver(this)
        groups.removeAt(position)
        notifyItemRangeRemoved(itemCountBeforeGroup, group.itemCount)
    }

    /**
     * Optionally register an [OnItemClickListener] that listens to click at the root of
     * each Item where [Item.isClickable] returns true
     *
     * @param onItemClickListener The click listener to set
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    /**
     * Optionally register an [OnItemLongClickListener] that listens to click at the root of
     * each Item where [Item.isLongClickable] returns true
     *
     * @param onItemLongClickListener The click listener to set
     */
    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener?) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    //endregion: GroupAdapter public API

    //region: RecyclerView.Adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val item = getItemForViewType(viewType)
        val itemView = inflater.inflate(item.layoutRes, parent, false)
        @Suppress("UNCHECKED_CAST")
        return item.createViewHolder(itemView) as VH
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        // Never called (all binds go through the version with payload)
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        @Suppress("UNCHECKED_CAST")
        val item = getItem(position) as Item<VH>
        item.bind(holder, position, payloads, onItemClickListener, onItemLongClickListener)
    }

    override fun getItemCount(): Int = groups.itemCount

    override fun getItemId(position: Int): Long =
        groups.getItem(position).id

    override fun getItemViewType(position: Int): Int {
        lastItemForViewTypeLookup = getItem(position)
        requireNotNull(lastItemForViewTypeLookup) { "Invalid position $position" }
        return lastItemForViewTypeLookup!!.viewType
    }

    override fun onViewRecycled(holder: VH) {
        @Suppress("UNCHECKED_CAST")
        val item = holder.item as Item<VH>
        item.unbind(holder)
    }

    override fun onFailedToRecycleView(holder: VH): Boolean =
        holder.item.isRecyclable

    //endregion: RecyclerView.Adapter

    /**
     * This idea was copied from Epoxy. :wave: Bright idea guys!
     *
     * Find the model that has the given view type so we can create a viewholder for that model.
     *
     * To make this efficient, we rely on the RecyclerView implementation detail that [GroupAdapter.getItemViewType]
     * is called immediately before [GroupAdapter.onCreateViewHolder]. We cache the last model
     * that had its view type looked up, and unless that implementation changes we expect to have a
     * very fast lookup for the correct model.
     *
     * To be safe, we fallback to searching through all models for a view type match. This is slow and
     * shouldn't be needed, but is a guard against RecyclerView behavior changing.
     */
    private fun getItemForViewType(viewType: Int): AnyItem {
        if (lastItemForViewTypeLookup != null && lastItemForViewTypeLookup?.viewType == viewType) {
            // We expect this to be a hit 100% of the time
            return lastItemForViewTypeLookup!!
        }

        // To be extra safe in case RecyclerView implementation details change...
        for (i in 0 until itemCount) {
            val item = getItem(i)
            if (item.viewType == viewType) {
                return item
            }
        }

        throw IllegalStateException("Could not find model for view type: $viewType")
    }

    private fun getItemCountBeforeGroup(groupIndex: Int): Int {
        var count = 0
        for (group in groups.subList(0, groupIndex)) {
            count += group.itemCount
        }
        return count
    }

    //region: GroupDataObserver

    override fun onChanged(group: Group) {
        notifyItemRangeChanged(getAdapterPosition(group), group.itemCount)
    }

    override fun onItemInserted(group: Group, position: Int) {
        notifyItemInserted(getAdapterPosition(group) + position)
    }

    override fun onItemChanged(group: Group, position: Int) {
        notifyItemChanged(getAdapterPosition(group) + position)
    }

    override fun onItemChanged(group: Group, position: Int, payload: Any) {
        notifyItemChanged(getAdapterPosition(group) + position, payload)
    }

    override fun onItemRemoved(group: Group, position: Int) {
        notifyItemRemoved(getAdapterPosition(group) + position)
    }

    override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int) {
        notifyItemRangeChanged(getAdapterPosition(group) + positionStart, itemCount)
    }

    override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int, payload: Any) {
        notifyItemRangeChanged(getAdapterPosition(group) + positionStart, itemCount, payload)
    }

    override fun onItemRangeInserted(group: Group, positionStart: Int, itemCount: Int) {
        notifyItemRangeInserted(getAdapterPosition(group) + positionStart, itemCount)
    }

    override fun onItemRangeRemoved(group: Group, positionStart: Int, itemCount: Int) {
        notifyItemRangeRemoved(getAdapterPosition(group) + positionStart, itemCount)
    }

    override fun onItemMoved(group: Group, fromPosition: Int, toPosition: Int) {
        val groupAdapterPosition = getAdapterPosition(group)
        notifyItemMoved(groupAdapterPosition + fromPosition, groupAdapterPosition + toPosition)
    }

    //endregion: GroupDataObserver
}
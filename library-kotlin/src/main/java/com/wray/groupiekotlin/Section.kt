package com.wray.groupiekotlin

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback

@Suppress("LeakingThis")
open class Section(
    private var header: Group? = null,
    private var footer: Group? = null,
    private var placeholder: Group? = null,
    children: List<Group> = emptyList()
) : NestedGroup() {

    private val children = mutableListOf<Group>()
    private var isPlaceholderVisible = false
    private var isHeaderAndFooterVisible = true

    /**
     * Whether this entire section should be hidden when empty. If false, it will
     * still show any footers, headers or placeholders set if empty.
     */
    var hideWhenEmpty = false
        set(value) {
            if (field == value) return
            field = value
            refreshEmptyState()
        }

    /**
     * Whether this section's contents are visually empty.
     */
    protected val isEmpty: Boolean
        get() = children.isEmpty() || children.itemCount == 0

    private val headerCount: Int
        get() = if (header == null || !isHeaderAndFooterVisible) 0 else 1
    private val headerItemCount: Int
        get() = if (headerCount == 0) 0 else header?.itemCount ?: 0
    private val isHeaderShown: Boolean
        get() = headerCount > 0

    private val footerCount: Int
        get() = if (footer == null || !isHeaderAndFooterVisible) 0 else 1
    private val footerItemCount: Int
        get() = if (footerCount == 0) 0 else footer?.itemCount ?: 0
    private val isFooterShown: Boolean
        get() = footerCount > 0

    private val placeholderCount: Int
        get() = if (isPlaceholderVisible) 1 else 0
    private val placeholderItemCount: Int
        get() = if (!isPlaceholderVisible || placeholder == null) 0 else placeholder?.itemCount ?: 0
    private val isPlaceholderShown: Boolean
        get() = placeholderCount > 0

    private val bodyItemCount: Int
        get() = if (isPlaceholderVisible) placeholderItemCount else children.itemCount

    private val itemCountWithoutFooter: Int
        get() = bodyItemCount + headerItemCount

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(headerItemCount + position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(headerItemCount + position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            val headerItemCount = headerItemCount
            notifyItemMoved(headerItemCount + fromPosition, headerItemCount + toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            if (payload != null) {
                notifyItemRangeChanged(headerItemCount + position, count, payload)
            } else {
                notifyItemRangeChanged(headerItemCount + position, count)
            }
        }
    };

    init {
        header?.registerGroupDataObserver(this)
        footer?.registerGroupDataObserver(this)
        placeholder?.registerGroupDataObserver(this)

        addAll(children)
    }

    override fun add(group: Group) {
        super.add(group)
        val position = itemCountWithoutFooter
        children.add(group)
        notifyItemRangeInserted(position, group.itemCount)
        refreshEmptyState()
    }

    override fun add(position: Int, group: Group) {
        super.add(position, group)
        children.add(position, group)
        val notifyPosition = headerItemCount + children.subList(0, position).itemCount
        notifyItemRangeInserted(notifyPosition, group.itemCount)
        refreshEmptyState()
    }

    override fun <T : Group> addAll(groups: Collection<T>) {
        if (groups.isEmpty()) return
        super.addAll(groups)

        val position = itemCountWithoutFooter
        children.addAll(groups)
        notifyItemRangeInserted(position, groups.itemCount)
        refreshEmptyState()
    }

    override fun <T : Group> addAll(position: Int, groups: Collection<T>) {
        if (groups.isEmpty()) return
        super.addAll(position, groups)
        children.addAll(position, groups)

        val notifyPosition = headerItemCount + children.subList(0, position).itemCount
        notifyItemRangeInserted(notifyPosition, groups.itemCount)
        refreshEmptyState()
    }

    override fun remove(group: Group) {
        super.remove(group)
        val position = getItemCountBeforeGroup(group)
        children.remove(group)
        notifyItemRangeRemoved(position, group.itemCount)
        refreshEmptyState()
    }

    override fun <T : Group> removeAll(groups: Collection<T>) {
        if (groups.isEmpty()) return
        super.removeAll(groups)

        groups.forEach { group ->
            val position = getItemCountBeforeGroup(group)
            children.remove(group)
            notifyItemRangeRemoved(position, group.itemCount)
        }

        refreshEmptyState()
    }

    /**
     * Replace all existing body content and dispatch fine-grained change notifications to the
     * parent using DiffUtil.
     *
     * Item comparisons are made using:
     * - `Item.isSameAs(Item otherItem)` (are items the same?)
     * - `Item.equals()` (are contents the same?)
     *
     * If you don't customize getId() or isSameAs() and equals(), the default implementations will return false,
     * meaning your Group will consider every update a complete change of everything.
     *
     * @param newBodyGroups The new content of the section
     * @param detectMoves is passed to [DiffUtil.calculateDiff(DiffUtil.Callback, boolean)]. Set to false if you
     *                    don't want DiffUtil to detect moved items.
     */
    fun update(newBodyGroups: List<Group>, detectMoves: Boolean = true) {
        val oldBodyGroups = children.toList()
        val oldBodyItemCount = oldBodyGroups.itemCount
        val newBodyItemCount = newBodyGroups.itemCount

        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldBodyItemCount

            override fun getNewListSize(): Int = newBodyItemCount

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldBodyGroups.getItem(oldItemPosition)
                val newItem = newBodyGroups.getItem(newItemPosition)
                return newItem.isSameAs(oldItem)
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldBodyGroups.getItem(oldItemPosition)
                val newItem = newBodyGroups.getItem(newItemPosition)
                return newItem == oldItem
            }
        }, detectMoves)

        super.removeAll(children)
        children.clear()
        children.addAll(newBodyGroups)
        super.addAll(newBodyGroups)

        diffResult.dispatchUpdatesTo(listUpdateCallback)
        if (newBodyItemCount == 0 || oldBodyItemCount == 0) {
            refreshEmptyState()
        }
    }

    /**
     * Overloaded version of update method in which you can pass your own [DiffUtil.DiffResult]
     */
    fun update(newBodyGroups: List<Group>, diffResult: DiffUtil.DiffResult) {
        super.removeAll(children)
        children.clear()
        children.addAll(newBodyGroups)
        super.addAll(newBodyGroups)

        diffResult.dispatchUpdatesTo(listUpdateCallback)
        refreshEmptyState()
    }

    /**
     * Remove all existing body content. Does not remove headers, footers or placeholders
     */
    @CallSuper
    open fun clear() {
        if (children.isEmpty()) {
            return
        }

        removeAll(children.toList())
    }

    fun setPlaceholder(placeholder: Group) {
        removePlaceholder()
        this.placeholder = placeholder
        refreshEmptyState()
    }

    fun removePlaceholder() {
        hidePlaceholder()
        placeholder = null
    }

    fun setHeader(header: Group) {
        val previousHeaderItemCount = headerItemCount
        this.header?.unregisterGroupDataObserver(this)
        this.header = header
        header.registerGroupDataObserver(this)
        notifyHeaderItemsChanged(previousHeaderItemCount)
    }

    fun removeHeader() {
        header?.let { header ->
            header.unregisterGroupDataObserver(this)
            val previousHeaderItemCount = headerItemCount
            this.header = null
            notifyHeaderItemsChanged(previousHeaderItemCount)
        }
    }

    fun setFooter(footer: Group) {
        val previousFooterItemCount = footerItemCount
        this.footer?.unregisterGroupDataObserver(this)
        this.footer = footer
        footer.registerGroupDataObserver(this)
        notifyFooterItemsChanged(previousFooterItemCount)
    }

    fun removeFooter() {
        footer?.let { footer ->
            footer.unregisterGroupDataObserver(this)
            val previousFooterItemCount = footerItemCount
            this.footer = null
            notifyFooterItemsChanged(previousFooterItemCount)
        }
    }

    private fun notifyHeaderItemsChanged(previousHeaderItemCount: Int) {
        val newHeaderItemCount = headerItemCount
        if (previousHeaderItemCount > 0) {
            notifyItemRangeRemoved(0, previousHeaderItemCount)
        }
        if (newHeaderItemCount > 0) {
            notifyItemRangeInserted(0, newHeaderItemCount)
        }
    }

    private fun notifyFooterItemsChanged(previousFooterItemCount: Int) {
        val newFooterItemCount = footerItemCount
        if (previousFooterItemCount > 0) {
            notifyItemRangeRemoved(itemCountWithoutFooter, previousFooterItemCount)
        }
        if (newFooterItemCount > 0) {
            notifyItemRangeInserted(itemCountWithoutFooter, newFooterItemCount)
        }
    }

    private fun showPlaceholder() {
        if (isPlaceholderVisible || placeholder == null)
            return

        isPlaceholderVisible = true
        notifyItemRangeInserted(headerItemCount, placeholder!!.itemCount)
    }

    private fun hidePlaceholder() {
        if (!isPlaceholderVisible || placeholder == null)
            return

        isPlaceholderVisible = true
        notifyItemRangeRemoved(headerItemCount, placeholder!!.itemCount)
    }

    private fun hideDecorations() {
        if (!isHeaderAndFooterVisible && !isPlaceholderVisible)
            return

        val count = headerItemCount + placeholderItemCount + footerItemCount
        isHeaderAndFooterVisible = false
        isPlaceholderVisible = false
        notifyItemRangeRemoved(0, count)
    }

    protected fun refreshEmptyState() {
        val isEmpty = isEmpty
        if (isEmpty) {
            if (hideWhenEmpty) {
                hideDecorations()
            } else {
                showPlaceholder()
                showHeadersAndFooters()
            }
        } else {
            hidePlaceholder()
            showHeadersAndFooters()
        }
    }

    private fun showHeadersAndFooters() {
        if (isHeaderAndFooterVisible)
            return

        isHeaderAndFooterVisible = true
        notifyItemRangeInserted(0, headerItemCount)
        notifyItemRangeInserted(itemCountWithoutFooter, footerItemCount)
    }

    //region: NestedGroup methods

    override fun getGroup(position: Int): Group {
        var pos = position

        if (isHeaderShown && pos == 0) {
            return header!!
        }
        pos -= headerCount

        if (isPlaceholderShown && pos == 0) {
            return placeholder!!
        }
        pos -= placeholderCount

        return if (pos == children.size) {
            if (isFooterShown) {
                footer!!
            } else {
                throw IndexOutOfBoundsException("Wanted group at position $pos but there are only $groupCount groups")
            }
        } else {
            children[pos]
        }
    }

    override val groupCount: Int
        get() = headerCount + footerCount + placeholderCount + children.size

    override fun getPosition(group: Group): Int {
        var count = 0

        if (isHeaderShown) {
            if (group == header) {
                return count
            }
        }
        count += headerCount

        if (isPlaceholderShown) {
            if (group == placeholder) {
                return count
            }
        }
        count += placeholderCount

        val index = children.indexOf(group)
        if (index >= 0) {
            return count + index
        }
        count += children.size

        if (isFooterShown && footer == group) {
            return count
        }

        return -1
    }

    //endregion: NestedGroup methods

    override fun onItemInserted(group: Group, position: Int) {
        super.onItemInserted(group, position)
        refreshEmptyState()
    }

    override fun onItemRemoved(group: Group, position: Int) {
        super.onItemRemoved(group, position)
        refreshEmptyState()
    }

    override fun onItemRangeInserted(group: Group, positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(group, positionStart, itemCount)
        refreshEmptyState()
    }

    override fun onItemRangeRemoved(group: Group, positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(group, positionStart, itemCount)
        refreshEmptyState()
    }
}

private fun <T : Group> Collection<T>.getItem(position: Int): AnyItem {
    var previousPosition = 0
    for (group in this) {
        val size = group.itemCount
        if (size + previousPosition > position) {
            return group.getItem(position - previousPosition)
        }
        previousPosition += size
    }

    throw IndexOutOfBoundsException("Wanted item at $position but there are only $previousPosition items")
}
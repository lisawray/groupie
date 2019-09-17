package com.wray.groupiekotlin

import androidx.annotation.CallSuper

abstract class NestedGroup: Group, GroupDataObserver {

    private val observable = GroupDataObservable()

    abstract fun getGroup(position: Int): Group

    abstract val groupCount: Int

    abstract fun getPosition(group: Group): Int

    protected fun getItemCountBeforeGroup(group: Group): Int {
        val groupIndex = getPosition(group)
        return getItemCountBeforeGroupIndex(groupIndex)
    }

    protected fun getItemCountBeforeGroupIndex(groupIndex: Int): Int {
        var size = 0
        for (i in 0 until groupIndex) {
            val currentGroup = getGroup(i)
            size += currentGroup.itemCount
        }
        return size
    }

    @CallSuper
    open fun add(group: Group) {
        group.registerGroupDataObserver(this)
    }

    @CallSuper
    open fun <T : Group> addAll(groups: Collection<T>) {
        groups.forEach { group -> group.registerGroupDataObserver(this) }
    }

    @CallSuper
    open fun add(position: Int, group: Group) {
        add(group)
    }

    @CallSuper
    open fun <T : Group> addAll(position: Int, groups: Collection<T>) {
        addAll(groups)
    }

    @CallSuper
    open fun remove(group: Group) {
        group.unregisterGroupDataObserver(this)
    }

    @CallSuper
    open fun <T : Group> removeAll(groups: Collection<T>) {
        groups.forEach { group -> group.unregisterGroupDataObserver(this) }
    }

    /**
     * A group should use this to notify that there is a change in itself.
     *
     * @param positionStart
     * @param itemCount
     */
    @CallSuper
    open fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        observable.onItemRangeInserted(this, positionStart, itemCount)
    }

    @CallSuper
    open fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        observable.onItemRangeRemoved(this, positionStart, itemCount)
    }

    @CallSuper
    open fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
        observable.onItemMoved(this, fromPosition, toPosition)
    }

    @CallSuper
    open fun notifyChanged() {
        observable.onChanged(this)
    }

    @CallSuper
    open fun notifyItemInserted(position: Int) {
        observable.onItemInserted(this, position)
    }

    @CallSuper
    open fun notifyItemChanged(position: Int) {
        observable.onItemChanged(this, position)
    }

    @CallSuper
    open fun notifyItemChanged(position: Int, payload: Any) {
        observable.onItemChanged(this, position, payload)
    }

    @CallSuper
    open fun notifyItemRemoved(position: Int) {
        observable.onItemRemoved(this, position)
    }

    @CallSuper
    open fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
        observable.onItemRangeChanged(this, positionStart, itemCount)
    }

    @CallSuper
    open fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any) {
        observable.onItemRangeChanged(this, positionStart, itemCount, payload)
    }

    //region: Group interface

    final override val itemCount: Int
        get() {
            var size = 0
            for (i in 0 until groupCount) {
                val group = getGroup(i)
                size += group.itemCount
            }
            return size
        }

    final override fun getItem(position: Int): AnyItem {
        var previousPosition = 0

        for (i in 0 until groupCount) {
            val group = getGroup(i)
            val size = group.itemCount
            if (size + previousPosition > position) {
                return group.getItem(position - previousPosition)
            }
            previousPosition += size
        }

        throw IndexOutOfBoundsException(
            "Wanted item at " + position + " but there are only "
                + itemCount + " items"
        )
    }

    final override fun getPosition(item: AnyItem): Int {
        var previousPosition = 0

        for (i in 0 until groupCount) {
            val group = getGroup(i)
            val position = group.getPosition(item)
            if (position >= 0) {
                return position + previousPosition
            }
            previousPosition += group.itemCount
        }

        return -1
    }

    final override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        observable.registerObserver(groupDataObserver)
    }

    final override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        observable.unregisterObserver(groupDataObserver)
    }

    //endregion: Group interface

    //region: GroupDataObserver interface


    /**
     * Every item in the group still exists but the data in each has changed (e.g. should rebind).
     *
     * @param group
     */
    @CallSuper
    override fun onChanged(group: Group) {
        observable.onItemRangeChanged(this, getItemCountBeforeGroup(group), group.itemCount)
    }

    @CallSuper
    override fun onItemInserted(group: Group, position: Int) {
        observable.onItemInserted(this, getItemCountBeforeGroup(group) + position)

    }

    @CallSuper
    override fun onItemChanged(group: Group, position: Int) {
        observable.onItemChanged(this, getItemCountBeforeGroup(group) + position)
    }

    @CallSuper
    override fun onItemChanged(group: Group, position: Int, payload: Any) {
        observable.onItemChanged(this, getItemCountBeforeGroup(group) + position, payload)
    }

    @CallSuper
    override fun onItemRemoved(group: Group, position: Int) {
        observable.onItemRemoved(this, getItemCountBeforeGroup(group) + position)
    }

    @CallSuper
    override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int) {
        observable.onItemRangeChanged(this, getItemCountBeforeGroup(group) + positionStart, itemCount)
    }

    @CallSuper
    override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int, payload: Any) {
        observable.onItemRangeChanged(this, getItemCountBeforeGroup(group) + positionStart, itemCount, payload)
    }

    @CallSuper
    override fun onItemRangeInserted(group: Group, positionStart: Int, itemCount: Int) {
        observable.onItemRangeInserted(this, getItemCountBeforeGroup(group) + positionStart, itemCount)
    }

    @CallSuper
    override fun onItemRangeRemoved(group: Group, positionStart: Int, itemCount: Int) {
        observable.onItemRangeRemoved(this, getItemCountBeforeGroup(group) + positionStart, itemCount)
    }

    @CallSuper
    override fun onItemMoved(group: Group, fromPosition: Int, toPosition: Int) {
        val groupPosition = getItemCountBeforeGroup(group)
        observable.onItemMoved(this, groupPosition + fromPosition, groupPosition + toPosition)
    }

    //endregion: GroupDataObserver interface

}

/**
 * Iterate using a listIterator to support items that might remove themselves while we iterate
 */
private class GroupDataObservable {

    private val observers = mutableListOf<GroupDataObserver>()

    internal fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int) {
        observers.listIterator().forEach { observer ->
            observer.onItemRangeChanged(group, positionStart, itemCount)
        }
    }

    internal fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int, payload: Any) {
        observers.listIterator().forEach { observer ->
            observer.onItemRangeChanged(group, positionStart, itemCount, payload)
        }
    }

    internal fun onItemInserted(group: Group, position: Int) {
        observers.listIterator().forEach { observer ->
            observer.onItemInserted(group, position)
        }
    }

    internal fun onItemChanged(group: Group, position: Int) {
        observers.listIterator().forEach { observer ->
            observer.onItemChanged(group, position)
        }
    }

    internal fun onItemChanged(group: Group, position: Int, payload: Any) {
        observers.listIterator().forEach { observer ->
            observer.onItemChanged(group, position, payload)
        }
    }

    internal fun onItemRemoved(group: Group, position: Int) {
        observers.listIterator().forEach { observer ->
            observer.onItemRemoved(group, position)
        }
    }

    internal fun onItemRangeInserted(group: Group, positionStart: Int, itemCount: Int) {
        observers.listIterator().forEach { observer ->
            observer.onItemRangeInserted(group, positionStart, itemCount)
        }
    }

    internal fun onItemRangeRemoved(group: Group, positionStart: Int, itemCount: Int) {
        observers.listIterator().forEach { observer ->
            observer.onItemRangeRemoved(group, positionStart, itemCount)
        }
    }

    internal fun onItemMoved(group: Group, fromPosition: Int, toPosition: Int) {
        observers.listIterator().forEach { observer ->
            observer.onItemMoved(group, fromPosition, toPosition)
        }
    }

    internal fun onChanged(group: Group) {
        observers.listIterator().forEach { observer ->
            observer.onChanged(group)
        }
    }

    internal fun registerObserver(observer: GroupDataObserver) {
        synchronized(observers) {
            check(!observers.contains(observer)) { "Observer $observer is already registered." }
            observers.add(observer)
        }
    }

    internal fun unregisterObserver(observer: GroupDataObserver) {
        synchronized(observers) {
            observers.remove(observer)
        }
    }
}
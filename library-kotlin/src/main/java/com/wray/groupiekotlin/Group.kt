package com.wray.groupiekotlin

interface Group {

    val itemCount: Int

    fun getItem(position: Int): AnyItem

    /**
     * Gets the position of an _item inside this Group
     * @param item Item to return position of
     * @return The position of the item or -1 if not present
     */
    fun getPosition(item: AnyItem): Int

    fun registerGroupDataObserver(groupDataObserver: GroupDataObserver)

    fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver)
}

internal fun Collection<Group>.getItem(position: Int): AnyItem {
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

internal val Collection<Group>.itemCount: Int
    get() = this.sumBy { it.itemCount }
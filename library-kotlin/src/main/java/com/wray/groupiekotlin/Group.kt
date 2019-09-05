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
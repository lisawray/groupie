package com.xwray.groupie.core;

/**
 * A group of items, to be used in an adapter.
 */
public interface Group<T extends BaseItem> {

    int getItemCount();

    T getItem(int position);

    /**
     * Gets the position of a
     * @param item
     * @return
     */
    int getPosition(T item);

    void setGroupDataObserver(GroupDataObserver groupDataObserver);

}
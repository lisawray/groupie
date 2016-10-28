package com.genius.groupie;

/**
 * A group of items, to be used in an adapter.
 */
public interface Group {

    int getItemCount();

    Item getItem(int position);

    /**
     * Gets the position of a
     * @param item
     * @return
     */
    int getPosition(Item item);

    void setGroupDataObserver(GroupDataObserver groupDataObserver);

}
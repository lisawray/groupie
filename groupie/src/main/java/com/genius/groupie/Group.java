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

    interface GroupDataObserver {
        void onChanged(Group group);

        void onItemInserted(Group group, int position);

        void onItemChanged(Group group, int position);

        void onItemRemoved(Group group, int position);

        void onItemRangeChanged(Group group, int positionStart, int itemCount);

        void onItemRangeInserted(Group group, int positionStart, int itemCount);

        void onItemRangeRemoved(Group group, int positionStart, int itemCount);

        void onItemMoved(Group group, int fromPosition, int toPosition);
        }
}
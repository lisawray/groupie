package com.xwray.groupie.core;

public interface GroupDataObserver {
    void onChanged(Group group);

    void onItemInserted(Group group, int position);

    void onItemChanged(Group group, int position);

    void onItemChanged(Group group, int position, Object payload);

    void onItemRemoved(Group group, int position);

    void onItemRangeChanged(Group group, int positionStart, int itemCount);

    void onItemRangeInserted(Group group, int positionStart, int itemCount);

    void onItemRangeRemoved(Group group, int positionStart, int itemCount);

    void onItemMoved(Group group, int fromPosition, int toPosition);
}

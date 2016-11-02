package com.genius.groupie;

import android.support.annotation.CallSuper;

import java.util.List;

/**
 * A base implementation of the Group interface, which supports nesting of Groups to arbitrary depth.
 * You can make a NestedGroup which contains only Items, one which contains Groups, or a mixture.
 *
 * It provides support for notifying the adapter about changes which happen in its child groups.
 */
public abstract class NestedGroup implements Group, GroupDataObserver {

    private GroupDataObserver parentDataObserver;

    public int getItemCount() {
        int size = 0;
        for (int i = 0; i < getGroupCount(); i++) {
            Group group = getGroup(i);
            size += group.getItemCount();
        }
        return size;
    }

    protected final int getItemCount(List<Group> groups) {
        int size = 0;
        for (Group group : groups) {
            size += group.getItemCount();
        }
        return size;
    }

    public abstract Group getGroup(int position);

    public abstract int getGroupCount();

    public Item getItem(int position) {
        int previousPosition = 0;

        for (int i = 0; i < getGroupCount(); i++) {
            Group group = getGroup(i);
            int size = group.getItemCount();
            if (size + previousPosition > position) {
                return group.getItem(position - previousPosition);
            }
            previousPosition += size;
        }

        return null;
    }

    /**
     * Gets the position of an
     *
     * @param item
     * @return
     */
    public final int getPosition(Item item) {
        int previousPosition = 0;

        for (int i = 0; i < getGroupCount(); i++) {
            Group group = getGroup(i);
            int position = group.getPosition(item);
            if (position >= 0) {
                return position + previousPosition;
            }
            previousPosition += group.getItemCount();
        }

        return -1;
    }

    public abstract int getPosition(Group group);

    @Override public final void setGroupDataObserver(GroupDataObserver groupDataObserver) {
        this.parentDataObserver = groupDataObserver;
    }

    @CallSuper
    public void add(Group group) {
        group.setGroupDataObserver(this);
    }

    @CallSuper
    public void addAll(List<Group> groups) {
        for (Group group : groups) {
            group.setGroupDataObserver(this);
        }
    }

    @CallSuper
    public void add(int position, Group group) {
        group.setGroupDataObserver(this);
    }

    @CallSuper
    public void addAll(int position, List<Group> groups) {
        for (Group group : groups) {
            group.setGroupDataObserver(this);
        }
    }

    @CallSuper
    public void remove(Group group) {
        group.setGroupDataObserver(null);
    }

    /**
     * Every item in the group still exists but the data in each has changed (e.g. should rebind).
     *
     * @param group
     */
    @Override public final void onChanged(Group group) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemRangeChanged(this, getPosition(group), group.getItemCount());
        }
    }

    @Override public final void onItemInserted(Group group, int position) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemInserted(this, getPosition(group) + position);
        }
    }

    @Override public final void onItemChanged(Group group, int position) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemChanged(this, getPosition(group) + position);
        }
    }

    @Override
    public void onItemChanged(Group group, int position, Object payload) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemChanged(this, getPosition(group) + position, payload);
        }
    }

    @Override public final void onItemRemoved(Group group, int position) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemRemoved(this, getPosition(group) + position);
        }
    }

    @Override public final void onItemRangeChanged(Group group, int positionStart, int itemCount) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemRangeChanged(this, getPosition(group) + positionStart, itemCount);
        }
    }

    @Override public final void onItemRangeInserted(Group group, int positionStart, int itemCount) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemRangeInserted(this, getPosition(group) + positionStart, itemCount);
        }
    }

    @Override public final void onItemRangeRemoved(Group group, int positionStart, int itemCount) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemRangeRemoved(this, getPosition(group) + positionStart, itemCount);
        }
    }

    @Override public final void onItemMoved(Group group, int fromPosition, int toPosition) {
        if (parentDataObserver != null) {
            int groupPosition = getPosition(group);
            parentDataObserver.onItemMoved(this, groupPosition + fromPosition, groupPosition + toPosition);
        }
    }


    /**
     * A group should use this to notify that there is a change in itself.
     * @param positionStart
     * @param itemCount
     */
    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemRangeInserted(this, positionStart, itemCount);
        }
    }

    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemRangeRemoved(this, positionStart, itemCount);
        }
    }

    public void notifyItemMoved(int fromPosition, int toPosition) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemMoved(this, fromPosition, toPosition);
        }
    }

    public void notifyChanged() {
        if (parentDataObserver != null) {
            parentDataObserver.onChanged(this);
        }
    }

    public void notifyItemInserted(int position) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemInserted(this, position);
        }
    }

    public void notifyItemChanged(int position) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemChanged(this, position);
        }
    }

    public void notifyItemChanged(int position, Object payload) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemChanged(this, position, payload);
        }
    }

    public void notifyItemRemoved(int position) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemRemoved(this, position);
        }
    }

    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemRangeChanged(this, positionStart, itemCount);
        }
    }
}

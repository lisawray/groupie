package com.genius.groupie;

import java.util.ArrayList;
import java.util.List;

/**
 * An ExpandableContentItem is one "base" content item with a list of children (any of which
 * may themselves be a group.)
 **/

public class ExpandableGroup extends NestedGroup {

    private boolean isExpanded = false;
    private final Group parent;
    private final List<Group> children = new ArrayList<>();

    public ExpandableGroup(Group expandableItem) {
        this.parent = expandableItem;
        ((ExpandableItem) expandableItem).setExpandableGroup(this);
    }

    @Override public void add(Group group) {
        super.add(group);
        if (isExpanded) {
            int itemCount = getItemCount();
            children.add(group);
            notifyItemRangeInserted(itemCount, group.getItemCount());
        } else {
            children.add(group);
        }
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public Group getGroup(int position) {
        if (position == 0) {
            return parent;
        } else {
            return children.get(position - 1);
        }
    }

    @Override public int getPosition(Group group) {
        if (group == parent) {
            return 0;
        } else {
            return 1 + children.indexOf(group);
        }
    }

    public int getGroupCount() {
        return 1 + (isExpanded ? children.size() : 0);
    }

    public void onToggleExpanded() {
        int oldSize = getItemCount();
        isExpanded = !isExpanded;
        int newSize = getItemCount();
        if (oldSize > newSize) {
            notifyItemRangeRemoved(newSize, oldSize - newSize);
        } else {
            notifyItemRangeInserted(oldSize, newSize - oldSize);
        }
    }
    
    private boolean dispatchChildChanges(Group group) {
        return isExpanded || group == parent;
    }

    @Override
    public void onChanged(Group group) {
        if (dispatchChildChanges(group)) {
            super.onChanged(group);
        }
    }

    @Override
    public void onItemInserted(Group group, int position) {
        if (dispatchChildChanges(group)) {
            super.onItemInserted(group, position);
        }
    }

    @Override
    public void onItemChanged(Group group, int position) {
        if (dispatchChildChanges(group)) {
            super.onItemChanged(group, position);
        }
    }

    @Override
    public void onItemChanged(Group group, int position, Object payload) {
        if (dispatchChildChanges(group)) {
            super.onItemChanged(group, position, payload);
        }
    }

    @Override
    public void onItemRemoved(Group group, int position) {
        if (dispatchChildChanges(group)) {
            super.onItemRemoved(group, position);
        }
    }

    @Override
    public void onItemRangeChanged(Group group, int positionStart, int itemCount) {
        if (dispatchChildChanges(group)) {
            super.onItemRangeChanged(group, positionStart, itemCount);
        }
    }

    @Override
    public void onItemRangeInserted(Group group, int positionStart, int itemCount) {
        if (dispatchChildChanges(group)) {
            super.onItemRangeInserted(group, positionStart, itemCount);
        }
    }

    @Override
    public void onItemRangeRemoved(Group group, int positionStart, int itemCount) {
        if (dispatchChildChanges(group)) {
            super.onItemRangeRemoved(group, positionStart, itemCount);
        }
    }

    @Override
    public void onItemMoved(Group group, int fromPosition, int toPosition) {
        if (dispatchChildChanges(group)) {
            super.onItemMoved(group, fromPosition, toPosition);
        }
    }
}

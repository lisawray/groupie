package com.xwray.groupie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * An ExpandableGroup is one "base" content item with a list of children (any of which
 * may themselves be a group.)
 **/

public class ExpandableGroup extends NestedGroup {

    private boolean isExpanded = false;
    private final Group parent;
    private final List<Group> children = new ArrayList<>();

    public <T extends Group & ExpandableItem> ExpandableGroup(T expandableItem) {
        this.parent = expandableItem;
        expandableItem.setExpandableGroup(this);
    }

    public <T extends Group & ExpandableItem> ExpandableGroup(T expandableItem, boolean isExpanded) {
        this.parent = expandableItem;
        expandableItem.setExpandableGroup(this);
        this.isExpanded = isExpanded;
    }

    @Override
    public void add(int position, @NonNull Group group) {
        super.add(position, group);
        children.add(position, group);
        if (isExpanded) {
            final int notifyPosition = 1 + GroupUtils.getItemCount(children.subList(0, position));
            notifyItemRangeInserted(notifyPosition, group.getItemCount());
        }
    }

    @Override
    public void add(@NonNull Group group) {
        super.add(group);
        if (isExpanded) {
            int itemCount = getItemCount();
            children.add(group);
            notifyItemRangeInserted(itemCount, group.getItemCount());
        } else {
            children.add(group);
        }
    }

    @Override
    public void addAll(@NonNull Collection<? extends Group> groups) {
        if (groups.isEmpty()) {
            return;
        }
        super.addAll(groups);
        if (isExpanded) {
            int itemCount = getItemCount();
            this.children.addAll(groups);
            notifyItemRangeInserted(itemCount, GroupUtils.getItemCount(groups));
        } else {
            this.children.addAll(groups);
        }
    }

    @Override
    public void addAll(int position, @NonNull Collection<? extends Group> groups) {
        if (groups.isEmpty()) {
            return;
        }
        super.addAll(position, groups);
        this.children.addAll(position, groups);

        if (isExpanded) {
            final int notifyPosition = 1 + GroupUtils.getItemCount(children.subList(0, position));
            notifyItemRangeInserted(notifyPosition, GroupUtils.getItemCount(groups));
        }
    }


    @Override
    public void remove(@NonNull Group group) {
        if (!this.children.contains(group)) return;
        super.remove(group);
        if (isExpanded) {
            int position = getItemCountBeforeGroup(group);
            children.remove(group);
            notifyItemRangeRemoved(position, group.getItemCount());
        } else {
            children.remove(group);
        }
    }

    @Override
    public void replaceAll(@NonNull Collection<? extends Group> groups) {
        if (isExpanded) {
            super.replaceAll(groups);
            children.clear();
            children.addAll(groups);
            notifyDataSetInvalidated();
        } else {
            children.clear();
            children.addAll(groups);
        }
    }

    @Override
    public void removeAll(@NonNull Collection<? extends Group> groups) {
        if (groups.isEmpty() || !this.children.containsAll(groups)) return;
        super.removeAll(groups);
        if (isExpanded) {
            this.children.removeAll(groups);
            for (Group group : groups) {
                int position = getItemCountBeforeGroup(group);
                children.remove(group);
                notifyItemRangeRemoved(position, group.getItemCount());
            }
        } else {
            this.children.removeAll(groups);
        }
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    @NonNull
    public Group getGroup(int position) {
        if (position == 0) {
            return parent;
        } else {
            return children.get(position - 1);
        }
    }

    @Override
    public int getPosition(@NonNull Group group) {
        if (group == parent) {
            return 0;
        }
        int index = children.indexOf(group);
        if (index >= 0) {
            return index + 1;
        }
        return -1;
    }

    public int getGroupCount() {
        return 1 + (isExpanded ? children.size() : 0);
    }

    public int getChildCount() {
        return children.size();
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

    public void setExpanded(boolean isExpanded) {
        if (this.isExpanded != isExpanded) {
            onToggleExpanded();
        }
    }

    private boolean dispatchChildChanges(Group group) {
        return isExpanded || group == parent;
    }

    @Override
    public void onChanged(@NonNull Group group) {
        if (dispatchChildChanges(group)) {
            super.onChanged(group);
        }
    }

    @Override
    public void onItemInserted(@NonNull Group group, int position) {
        if (dispatchChildChanges(group)) {
            super.onItemInserted(group, position);
        }
    }

    @Override
    public void onItemChanged(@NonNull Group group, int position) {
        if (dispatchChildChanges(group)) {
            super.onItemChanged(group, position);
        }
    }

    @Override
    public void onItemChanged(@NonNull Group group, int position, Object payload) {
        if (dispatchChildChanges(group)) {
            super.onItemChanged(group, position, payload);
        }
    }

    @Override
    public void onItemRemoved(@NonNull Group group, int position) {
        if (dispatchChildChanges(group)) {
            super.onItemRemoved(group, position);
        }
    }

    @Override
    public void onItemRangeChanged(@NonNull Group group, int positionStart, int itemCount) {
        if (dispatchChildChanges(group)) {
            super.onItemRangeChanged(group, positionStart, itemCount);
        }
    }

    @Override
    public void onItemRangeChanged(@NonNull Group group, int positionStart, int itemCount, Object payload) {
        if (dispatchChildChanges(group)) {
            super.onItemRangeChanged(group, positionStart, itemCount, payload);
        }
    }

    @Override
    public void onItemRangeInserted(@NonNull Group group, int positionStart, int itemCount) {
        if (dispatchChildChanges(group)) {
            super.onItemRangeInserted(group, positionStart, itemCount);
        }
    }

    @Override
    public void onItemRangeRemoved(@NonNull Group group, int positionStart, int itemCount) {
        if (dispatchChildChanges(group)) {
            super.onItemRangeRemoved(group, positionStart, itemCount);
        }
    }

    @Override
    public void onItemMoved(@NonNull Group group, int fromPosition, int toPosition) {
        if (dispatchChildChanges(group)) {
            super.onItemMoved(group, fromPosition, toPosition);
        }
    }

    @Override
    public void onDataSetInvalidated() {
        if (isExpanded) {
            super.onDataSetInvalidated();
        }
    }
}

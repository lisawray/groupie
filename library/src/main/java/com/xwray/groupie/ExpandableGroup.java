package com.xwray.groupie;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An ExpandableGroup is one "base" content item with a list of children (any of which
 * may themselves be a group.)
 **/

public class ExpandableGroup extends Section {

    private boolean isExpanded = false;
    private final List<Group> children = new ArrayList<>();

    public ExpandableGroup(@NonNull Group expandableItem) {
        super(expandableItem);
        ((ExpandableItem) expandableItem).setExpandableGroup(this);
    }

    public ExpandableGroup(@NonNull Group expandableItem, boolean isExpanded) {
        super(expandableItem);
        this.isExpanded = isExpanded;
        ((ExpandableItem) expandableItem).setExpandableGroup(this);
    }

    public ExpandableGroup(@NonNull Group expandableItem, @NonNull Collection<? extends Group> children, boolean isExpanded) {
        super(expandableItem, children);
        this.isExpanded = isExpanded;
        this.children.addAll(children);
        ((ExpandableItem) expandableItem).setExpandableGroup(this);
    }

    @Override
    public void setHeader(@NonNull Group expandableItem) {
        super.setHeader(expandableItem);
        ((ExpandableItem) expandableItem).setExpandableGroup(this);
    }

    @Override
    public void add(int position, @NonNull Group group) {
        children.add(position, group);

        if (isExpanded) {
            super.add(position, group);
        }
    }

    @Override
    public void add(@NonNull Group group) {
        children.add(group);

        if (isExpanded) {
            super.add(group);
        }
    }

    @Override
    public void addAll(@NonNull Collection<? extends Group> groups) {
        if (groups.isEmpty()) {
            return;
        }

        children.addAll(groups);

        if (isExpanded) {
            super.addAll(groups);
        }
    }

    @Override
    public void addAll(int position, @NonNull Collection<? extends Group> groups) {
        if (groups.isEmpty()) {
            return;
        }

        children.addAll(position, groups);

        if (isExpanded) {
            super.addAll(position, groups);
        }
    }

    @Override
    public void clear() {
        children.clear();

        if (isExpanded) {
            super.removeAll(children);
        }
    }

    @Override
    public void remove(@NonNull Group group) {
        if (!this.children.contains(group)) return;

        children.remove(group);

        if (isExpanded) {
            super.remove(group);
        }
    }

    @Override
    public void removeAll(@NonNull Collection<? extends Group> groups) {
        if (groups.isEmpty() || !this.children.containsAll(groups)) return;

        children.removeAll(groups);

        if (isExpanded) {
            super.removeAll(groups);
        }
    }

    @Override
    public void update(@NonNull Collection<? extends Group> newBodyGroups, DiffUtil.DiffResult diffResult) {
        children.clear();
        children.addAll(newBodyGroups);

        if (isExpanded) {
            super.update(newBodyGroups, diffResult);
        }
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void onToggleExpanded() {
        if (isExpanded) {
            super.removeAll(children);
            isExpanded = false;
        } else {
            super.addAll(children);
            isExpanded = true;
        }
    }

    public void setExpanded(boolean isExpanded) {
        if (this.isExpanded != isExpanded) {
            onToggleExpanded();
        }
    }
}

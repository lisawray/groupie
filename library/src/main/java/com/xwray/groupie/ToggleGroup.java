package com.xwray.groupie;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ToggleGroup extends NestedGroup {

    private int visibleIndex = 0;

    private final List<Group> children = new ArrayList<>();

    public ToggleGroup(final Group firstGroup) {
        super.add(firstGroup);
        children.add(firstGroup);
        notifyItemRangeInserted(0, firstGroup.getItemCount());
    }

    @Override
    public void add(@NonNull final Group group) {
        super.add(group);
        children.add(group);
    }

    @Override
    @Nullable
    public Group getGroup(final int position) {
        if (position != 0) {
            return null;
        }
        return children.get(visibleIndex);
    }

    @Override
    public int getPosition(@NonNull final Group group) {
        if (children.contains(group)) {
            return 0;
        }
        return -1;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    public int getVisibleIndex() {
        return visibleIndex;
    }

    public void setVisible(final int visibleIndex) {
        if (this.visibleIndex == visibleIndex) {
            return;
        }

        int oldSize = getItemCount();
        this.visibleIndex = visibleIndex;
        int newSize = getItemCount();

        if (oldSize == newSize) {
            notifyItemRangeChanged(0, newSize);
        } else {
            notifyItemRangeRemoved(0, oldSize);
            notifyItemRangeInserted(0, newSize);
        }
    }

    private boolean dispatchChildChanges(final Group group) {
        return visibleIndex == children.indexOf(group);
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

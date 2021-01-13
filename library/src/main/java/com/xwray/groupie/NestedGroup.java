package com.xwray.groupie;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A base implementation of the Group interface, which supports nesting of Groups to arbitrary depth.
 * You can make a NestedGroup which contains only Items, one which contains Groups, or a mixture.
 * <p>
 * It provides support for notifying the adapter about changes which happen in its child groups.
 */
public abstract class NestedGroup implements Group, GroupDataObserver {

    private final GroupDataObservable observable = new GroupDataObservable();

    public int getItemCount() {
        int size = 0;
        for (int i = 0; i < getGroupCount(); i++) {
            Group group = getGroup(i);
            size += group.getItemCount();
        }
        return size;
    }

    protected int getItemCountBeforeGroup(@NonNull final Group group) {
        final int groupIndex = getPosition(group);
        return getItemCountBeforeGroup(groupIndex);
    }

    protected int getItemCountBeforeGroup(final int groupIndex) {
        int size = 0;
        for (int i = 0; i < groupIndex; i++) {
            final Group currentGroup = getGroup(i);
            size += currentGroup.getItemCount();
        }
        return size;
    }

    @NonNull
    public abstract Group getGroup(int position);

    public abstract int getGroupCount();

    @NonNull
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

        throw new IndexOutOfBoundsException("Wanted item at " + position + " but there are only "
                + getItemCount() + " items");
    }

    public final int getPosition(@NonNull Item item) {
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

    public abstract int getPosition(@NonNull Group group);

    @Override
    public final void registerGroupDataObserver(@NonNull GroupDataObserver groupDataObserver) {
        observable.registerObserver(groupDataObserver);
    }

    @Override
    public void unregisterGroupDataObserver(@NonNull GroupDataObserver groupDataObserver) {
        observable.unregisterObserver(groupDataObserver);
    }

    @CallSuper
    public void add(@NonNull Group group) {
        group.registerGroupDataObserver(this);
    }

    @CallSuper
    public void addAll(@NonNull Collection<? extends Group> groups) {
        for (Group group : groups) {
            group.registerGroupDataObserver(this);
        }
    }

    @CallSuper
    public void add(int position, @NonNull Group group) {
        group.registerGroupDataObserver(this);
    }

    @CallSuper
    public void addAll(int position, @NonNull Collection<? extends Group> groups) {
        for (Group group : groups) {
            group.registerGroupDataObserver(this);
        }
    }

    @CallSuper
    public void remove(@NonNull Group group) {
        group.unregisterGroupDataObserver(this);
    }

    @CallSuper
    public void removeAll(@NonNull Collection<? extends Group> groups) {
        for (Group group : groups) {
            group.unregisterGroupDataObserver(this);
        }
    }

    @CallSuper
    public void replaceAll(@NonNull Collection<? extends Group> groups) {
        final int groupCount = getGroupCount();

        for (int i = groupCount - 1; i >= 0; i--) {
            getGroup(i).unregisterGroupDataObserver(this);
        }

        for (Group group: groups) {
            group.registerGroupDataObserver(this);
        }
    }

    /**
     * Every item in the group still exists but the data in each has changed (e.g. should rebind).
     *
     * @param group
     */
    @CallSuper
    @Override
    public void onChanged(@NonNull Group group) {
        observable.onItemRangeChanged(this, getItemCountBeforeGroup(group), group.getItemCount());
    }

    @CallSuper
    @Override
    public void onItemInserted(@NonNull Group group, int position) {
        observable.onItemInserted(this, getItemCountBeforeGroup(group) + position);
    }

    @CallSuper
    @Override
    public void onItemChanged(@NonNull Group group, int position) {
        observable.onItemChanged(this, getItemCountBeforeGroup(group) + position);
    }

    @CallSuper
    @Override
    public void onItemChanged(@NonNull Group group, int position, Object payload) {
        observable.onItemChanged(this, getItemCountBeforeGroup(group) + position, payload);
    }

    @CallSuper
    @Override
    public void onItemRemoved(@NonNull Group group, int position) {
        observable.onItemRemoved(this, getItemCountBeforeGroup(group) + position);
    }

    @CallSuper
    @Override
    public void onItemRangeChanged(@NonNull Group group, int positionStart, int itemCount) {
        observable.onItemRangeChanged(this, getItemCountBeforeGroup(group) + positionStart, itemCount);
    }

    @CallSuper
    @Override
    public void onItemRangeChanged(@NonNull Group group, int positionStart, int itemCount, Object payload) {
        observable.onItemRangeChanged(this, getItemCountBeforeGroup(group) + positionStart, itemCount, payload);
    }

    @CallSuper
    @Override
    public void onItemRangeInserted(@NonNull Group group, int positionStart, int itemCount) {
        observable.onItemRangeInserted(this, getItemCountBeforeGroup(group) + positionStart, itemCount);
    }

    @CallSuper
    @Override
    public void onItemRangeRemoved(@NonNull Group group, int positionStart, int itemCount) {
        observable.onItemRangeRemoved(this, getItemCountBeforeGroup(group) + positionStart, itemCount);
    }

    @CallSuper
    @Override
    public void onItemMoved(@NonNull Group group, int fromPosition, int toPosition) {
        int groupPosition = getItemCountBeforeGroup(group);
        observable.onItemMoved(this, groupPosition + fromPosition, groupPosition + toPosition);
    }

    @CallSuper
    @Override
    public void onDataSetInvalidated() {
        observable.onDataSetInvalidated();
    }

    /**
     * A group should use this to notify that there is a change in itself.
     *
     * @param positionStart
     * @param itemCount
     */
    @CallSuper
    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        observable.onItemRangeInserted(this, positionStart, itemCount);
    }

    @CallSuper
    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        observable.onItemRangeRemoved(this, positionStart, itemCount);
    }

    @CallSuper
    public void notifyItemMoved(int fromPosition, int toPosition) {
        observable.onItemMoved(this, fromPosition, toPosition);
    }

    @CallSuper
    public void notifyChanged() {
        observable.onChanged(this);
    }

    @CallSuper
    public void notifyItemInserted(int position) {
        observable.onItemInserted(this, position);
    }

    @CallSuper
    public void notifyItemChanged(int position) {
        observable.onItemChanged(this, position);
    }

    @CallSuper
    public void notifyItemChanged(int position, @Nullable Object payload) {
        observable.onItemChanged(this, position, payload);
    }

    @CallSuper
    public void notifyItemRemoved(int position) {
        observable.onItemRemoved(this, position);
    }

    @CallSuper
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        observable.onItemRangeChanged(this, positionStart, itemCount);
    }

    @CallSuper
    public void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
        observable.onItemRangeChanged(this, positionStart, itemCount, payload);
    }

    @CallSuper
    public void notifyDataSetInvalidated() {
        observable.onDataSetInvalidated();
    }

    /**
     * Iterate in reverse order in case any observer decides to remove themself from the list
     * in their callback
     */
    private static class GroupDataObservable {
        final List<GroupDataObserver> observers = new ArrayList<>();

        void onItemRangeChanged(Group group, int positionStart, int itemCount) {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onItemRangeChanged(group, positionStart, itemCount);
            }
        }

        void onItemRangeChanged(Group group, int positionStart, int itemCount, Object payload) {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onItemRangeChanged(group, positionStart, itemCount, payload);
            }
        }

        void onItemInserted(Group group, int position) {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onItemInserted(group, position);
            }
        }

        void onItemChanged(Group group, int position) {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onItemChanged(group, position);
            }
        }

        void onItemChanged(Group group, int position, Object payload) {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onItemChanged(group, position, payload);
            }
        }

        void onItemRemoved(Group group, int position) {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onItemRemoved(group, position);
            }
        }

        void onItemRangeInserted(Group group, int positionStart, int itemCount) {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onItemRangeInserted(group, positionStart, itemCount);
            }
        }

        void onItemRangeRemoved(Group group, int positionStart, int itemCount) {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onItemRangeRemoved(group, positionStart, itemCount);
            }
        }

        void onItemMoved(Group group, int fromPosition, int toPosition) {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onItemMoved(group, fromPosition, toPosition);
            }
        }

        void onChanged(Group group) {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onChanged(group);
            }
        }

        void registerObserver(GroupDataObserver observer) {
            synchronized(observers) {
                if (observers.contains(observer)) {
                    throw new IllegalStateException("Observer " + observer + " is already registered.");
                }
                observers.add(observer);
            }
        }

        void unregisterObserver(GroupDataObserver observer) {
            synchronized(observers) {
                int index = observers.indexOf(observer);
                observers.remove(index);
            }
        }

        void onDataSetInvalidated() {
            for (int i = observers.size() - 1; i >= 0; i--) {
                observers.get(i).onDataSetInvalidated();
            }
        }
    }
}

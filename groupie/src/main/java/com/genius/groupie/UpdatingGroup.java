package com.genius.groupie;

import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A group which accepts a list of items and diffs them against its previous contents,
 * generating the correct remove, add, move and change notifications to its parent observer,
 * to create an animated item-level update.
 */
public class UpdatingGroup<T extends Item & UpdatingGroup.Comparable<T>> extends NestedGroup {

    private ListUpdateCallback listUpdateCallback = new ListUpdateCallback() {
        @Override public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override public void onChanged(int position, int count, Object payload) {
            notifyItemRangeChanged(position, count);
        }
    };

    private List<T> items = new ArrayList<>();

    public void update(List<T> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new UpdatingCallback(items, newItems));
        items.clear();
        items.addAll(newItems);
        diffResult.dispatchUpdatesTo(listUpdateCallback);
    }

    @Override public Group getGroup(int position) {
        return items.get(position);
    }

    @Override public int getGroupCount() {
        return items.size();
    }

    @Override public int getPosition(Group group) {
        return items.indexOf((T) group);
    }

    public interface Comparable<T> {

        /**
         * Whether the items will be visually the same (e.g. whether onChanged()
         * should be called)
         *
         * @param other
         * @return
         */
        boolean areContentsTheSame(T other);

        /**
         * Whether the items represent the same content, even if its appearance has
         * changed.  Usually this will compare the id of the model object.
         *
         * @param other
         * @return
         */
        boolean areItemsTheSame(T other);
    }

    private class UpdatingCallback extends DiffUtil.Callback {

        private List<T> oldList;
        private List<T> newList;

        UpdatingCallback(List<T> oldList, List<T> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override public int getOldListSize() {
            return oldList.size();
        }

        @Override public int getNewListSize() {
            return newList.size();
        }

        @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            T oldItem = oldList.get(oldItemPosition);
            T newItem = newList.get(newItemPosition);
            return oldItem.areItemsTheSame(newItem);
        }

        @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            T oldItem = oldList.get(oldItemPosition);
            T newItem = newList.get(newItemPosition);
            return oldItem.areContentsTheSame(newItem);
        }
    }
}


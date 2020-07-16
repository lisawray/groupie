package com.xwray.groupie;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Deprecated.  Please use #{@link Section#update} instead.
 *
 * A group which accepts a list of items and diffs them against its previous contents,
 * generating the correct remove, add, move and change notifications to its parent observer,
 * to create an animated item-level update.
 * <p>
 * Item comparisons are made using:
 * - Item.getId() (are items the same?)
 * - Item.hasSameContentAs() (are contents the same?)
 * If you don't customize getId() or hasSameContentAs(), the default implementations will return false,
 * meaning your Group will consider every update a complete change of everything.
 */
@Deprecated
public class UpdatingGroup extends NestedGroup {

    private ListUpdateCallback listUpdateCallback = new ListUpdateCallback() {
        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count, Object payload) {
            notifyItemRangeChanged(position, count);
        }
    };

    private List<Item> items = new ArrayList<>();

    public void update(@NonNull List<? extends Item> newItems) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new UpdatingCallback(newItems));
        super.removeAll(items);
        items.clear();
        super.addAll(newItems);
        items.addAll(newItems);
        diffResult.dispatchUpdatesTo(listUpdateCallback);
    }

    @Override
    @NonNull
    public Group getGroup(int position) {
        return items.get(position);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getPosition(@NonNull Group group) {
        if (group instanceof Item) {
            return items.indexOf(group);
        } else {
            return -1;
        }
    }

    private class UpdatingCallback extends DiffUtil.Callback {

        private List<? extends Item> newList;

        UpdatingCallback(List<? extends Item> newList) {
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return items.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Item oldItem = items.get(oldItemPosition);
            Item newItem = newList.get(newItemPosition);
            if (oldItem.getViewType() != newItem.getViewType()) {
                return false;
            }
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Item oldItem = items.get(oldItemPosition);
            Item newItem = newList.get(newItemPosition);
            return oldItem.hasSameContentAs(newItem);
        }
    }
}


package com.xwray.groupie.core;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An adapter that holds a list of Groups.
 */
public abstract class BaseGroupAdapter<I extends BaseItem, VH extends BaseViewHolder<I>> extends RecyclerView.Adapter<VH> implements GroupDataObserver {

    private final List<Group<I>> groups = new ArrayList<>();
    private BaseOnItemClickListener<I> onItemClickListener;
    private BaseOnItemLongClickListener<I> onItemLongClickListener;
    private int spanCount = 1;
    
    private final GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override public int getSpanSize(int position) {
            try {
                return getItem(position).getSpanSize(spanCount, position);
            } catch (IndexOutOfBoundsException e) {
                // Bug in support lib?  TODO investigate further
                return spanCount;
            }
        }
    };

    public GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return spanSizeLookup;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public int getSpanCount() {
        return spanCount;
    }

    /**
     * Optionally register an {@link BaseOnItemClickListener} that listens to click at the root of
     * each Item where {@link BaseItem#isClickable()} returns true
     * @param onItemClickListener The click listener to set
     */
    public void setOnItemClickListener(BaseOnItemClickListener<I> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Optionally register an {@link BaseOnItemLongClickListener} that listens to long click at the root of
     * each Item where {@link BaseItem#isLongClickable()} returns true
     * @param onItemLongClickListener The long click listener to set
     */
    public void setOnItemLongClickListener(BaseOnItemLongClickListener<I> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override public abstract VH onCreateViewHolder(ViewGroup parent, int layoutResId);

    @Override public void onBindViewHolder(VH holder, int position) {
        // Never called (all binds go through the version with payload)
    }

    @Override public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        I contentItem = getItem(position);
        contentItem.bind(holder, position, payloads, onItemClickListener, onItemLongClickListener);
    }

    @Override
    public void onViewRecycled(VH holder) {
        I contentItem = holder.getItem();
        contentItem.unbind(holder);
    }

    @Override
    public boolean onFailedToRecycleView(VH holder) {
        I contentItem = holder.getItem();
        return contentItem.isRecyclable();
    }

    @Override public int getItemViewType(int position) {
        I contentItem = getItem(position);
        if (contentItem == null) throw new RuntimeException("Invalid position " + position);
        return getItem(position).getLayout();
    }

    public I getItem(VH holder) {
        return holder.getItem();
    }

    public I getItem(int position) {
        int count = 0;
        for (Group<I> group : groups) {
            if (position < count + group.getItemCount()) {
                return group.getItem(position - count);
            } else {
                count += group.getItemCount();
            }
        }
        throw new IndexOutOfBoundsException("Requested position " + position + "in group adapter " +
                "but there are only " + count + " items");
    }

    public int getAdapterPosition(I contentItem) {
        int count = 0;
        for (Group<I> group : groups) {
            int index = group.getPosition(contentItem);
            if (index >= 0) return index + count;
            count += group.getItemCount();
        }
        return -1;
    }

    /**
     * The position in the flat list of individual items at which the group starts
     *
     * @param group
     * @return
     */
    public int getAdapterPosition(Group<I> group) {
        int index = groups.indexOf(group);
        int position = 0;
        for (int i = 0; i < index; i++) {
            position += groups.get(i).getItemCount();
        }
        return position;
    }

    @Override public int getItemCount() {
        int count = 0;
        for (Group<I> group : groups) {
            count += group.getItemCount();
        }
        return count;
    }

    public int getItemCount(int groupIndex) {
        if (groupIndex >= groups.size()) {
            throw new IndexOutOfBoundsException("Requested group index " + groupIndex + " but there are " + groups.size() + " groups");
        }
        return groups.get(groupIndex).getItemCount();
    }

    public void clear() {
        for (Group<I> group : groups) {
            group.setGroupDataObserver(null);
        }
        groups.clear();
        notifyDataSetChanged();
    }

    public void add(@NonNull Group<I> group) {
        if (group == null) throw new RuntimeException("Group cannot be null");
        int itemCountBeforeGroup = getItemCount();
        group.setGroupDataObserver(this);
        groups.add(group);
        notifyItemRangeInserted(itemCountBeforeGroup, group.getItemCount());
    }

    /**
     * Adds the contents of the list of groups, in order, to the end of the adapter contents.
     * All groups in the list must be non-null.
     * @param groups
     */
    public void addAll(@NonNull Collection<? extends Group<I>> groups) {
        if (groups.contains(null)) throw new RuntimeException("List of groups can't contain null!");
        int itemCountBeforeGroup = getItemCount();
        int additionalSize = 0;
        for (Group<I> group : groups) {
            additionalSize += group.getItemCount();
            group.setGroupDataObserver(this);
        }
        this.groups.addAll(groups);
        notifyItemRangeInserted(itemCountBeforeGroup, additionalSize);
    }

    public void remove(@NonNull Group<I> group) {
        if (group == null) throw new RuntimeException("Group cannot be null");
        int position = groups.indexOf(group);
        int itemCountBeforeGroup = getItemCountBeforeGroup(position);
        group.setGroupDataObserver(null);
        groups.remove(position);
        notifyItemRangeRemoved(itemCountBeforeGroup, group.getItemCount());
    }

    public void add(@NonNull int index, Group<I> group) {
        if (group == null) throw new RuntimeException("Group cannot be null");
        group.setGroupDataObserver(this);
        groups.add(index, group);
        int itemCountBeforeGroup = getItemCountBeforeGroup(index);
        notifyItemRangeInserted(itemCountBeforeGroup, group.getItemCount());
    }

    /**
     * Get section, given a raw position in the list.
     *
     * @param position
     * @return
     */
    private Group<I> getGroup(int position) {
        int previous = 0;
        int size;
        for (Group<I> group : groups) {
            size = group.getItemCount();
            if (position - previous < size) return group;
            previous += group.getItemCount();
        }
        return null;
    }

    private int getItemCountBeforeGroup(int groupIndex) {
        int count = 0;
        for (Group<I> group : groups.subList(0, groupIndex)) {
            count += group.getItemCount();
        }
        return count;
    }

    public Group<I> getGroup(I contentItem) {
        for (Group<I> group : groups) {
            if (group.getPosition(contentItem) >= 0) {
                return group;
            }
        }
        return null;
    }

    @Override public void onChanged(Group group) {
        notifyItemRangeChanged(getAdapterPosition(group), group.getItemCount());
    }

    @Override public void onItemInserted(Group group, int position) {
        notifyItemInserted(getAdapterPosition(group) + position);
    }

    @Override public void onItemChanged(Group group, int position) {
        notifyItemChanged(getAdapterPosition(group) + position);
    }

    @Override public void onItemChanged(Group group, int position, Object payload) {
        notifyItemChanged(getAdapterPosition(group) + position, payload);
    }

    @Override public void onItemRemoved(Group group, int position) {
        notifyItemRemoved(getAdapterPosition(group) + position);
    }

    @Override public void onItemRangeChanged(Group group, int positionStart, int itemCount) {
        notifyItemRangeChanged(getAdapterPosition(group) + positionStart, itemCount);
    }

    @Override public void onItemRangeInserted(Group group, int positionStart, int itemCount) {
        notifyItemRangeInserted(getAdapterPosition(group) + positionStart, itemCount);
    }

    @Override public void onItemRangeRemoved(Group group, int positionStart, int itemCount) {
        notifyItemRangeRemoved(getAdapterPosition(group) + positionStart, itemCount);
    }

    @Override public void onItemMoved(Group group, int fromPosition, int toPosition) {
        int groupAdapterPosition = getAdapterPosition(group);
        notifyItemMoved(groupAdapterPosition + fromPosition, groupAdapterPosition + toPosition);
    }
}

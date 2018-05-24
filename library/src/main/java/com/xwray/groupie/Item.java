package com.xwray.groupie;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Item<VH extends ViewHolder> implements Group, SpanSizeProvider {

    private static AtomicLong ID_COUNTER = new AtomicLong(0);
    protected GroupDataObserver parentDataObserver;
    private final long id;
    private Map<String, Object> extras = new HashMap<>();

    public Item() {
        this(ID_COUNTER.decrementAndGet());
    }

    protected Item(long id) {
        this.id = id;
    }

    @NonNull
    public VH createViewHolder(@NonNull View itemView) {
        return (VH) new ViewHolder(itemView);
    }

    /**
     * Perform any actions required to set up the view for display.
     *
     * @param holder              The viewholder to bind
     * @param position            The adapter position
     * @param payloads            Any payloads (this list may be empty)
     * @param onItemClickListener An optional adapter-level click listener
     * @param onItemLongClickListener An optional adapter-level long click listener
     */
    @CallSuper
    public void bind(@NonNull VH holder, int position, @NonNull List<Object> payloads,
                     @Nullable OnItemClickListener onItemClickListener,
                     @Nullable OnItemLongClickListener onItemLongClickListener) {
        holder.bind(this, onItemClickListener, onItemLongClickListener);
        bind(holder, position, payloads);
    }

    public abstract void bind(@NonNull VH viewHolder, int position);

    /**
     * If you don't specify how to handle payloads in your implementation, they'll be ignored and
     * the adapter will do a full rebind.
     *
     * @param holder The ViewHolder to bind
     * @param position The adapter position
     * @param payloads A list of payloads (may be empty)
     */
    public void bind(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        bind(holder, position);
    }

    /**
     * Do any cleanup required for the viewholder to be reused.
     *
     * @param holder The ViewHolder being recycled
     */
    @CallSuper
    public void unbind(@NonNull VH holder) {
        holder.unbind();
    }

    /**
     * Whether the view should be recycled. Return false to prevent the view from being recycled.
     * (Note that it may still be re-bound.)
     *
     * @return Whether the view should be recycled.
     * @see RecyclerView.Adapter#onFailedToRecycleView(RecyclerView.ViewHolder)
     */
    public boolean isRecyclable() {
        return true;
    }

    @Override
    public int getSpanSize(int spanCount, int position) {
        return spanCount;
    }

    public int getSwipeDirs() {
        return 0;
    }

    public int getDragDirs() {
        return 0;
    }

    @LayoutRes
    public abstract int getLayout();

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    @NonNull
    public Item getItem(int position) {
        if (position == 0) {
            return this;
        } else {
            throw new IndexOutOfBoundsException("Wanted item at position " + position + " but" +
                    " an Item is a Group of size 1");
        }
    }

    @Override
    public void registerGroupDataObserver(@NonNull GroupDataObserver groupDataObserver) {
        this.parentDataObserver = groupDataObserver;
    }

    @Override
    public void unregisterGroupDataObserver(@NonNull GroupDataObserver groupDataObserver) {
        parentDataObserver = null;
    }

    @Override
    public int getPosition(@NonNull Item item) {
        return this == item ? 0 : -1;
    }

    public boolean isClickable() {
        return true;
    }

    public boolean isLongClickable() {
        return true;
    }

    public void notifyChanged() {
        if (parentDataObserver != null) {
            parentDataObserver.onItemChanged(this, 0);
        }
    }

    public void notifyChanged(@Nullable Object payload) {
        if (parentDataObserver != null) {
            parentDataObserver.onItemChanged(this, 0, payload);
        }
    }

    /**
     * A set of key/value pairs stored on the ViewHolder that can be useful for distinguishing
     * items of the same view type.
     *
     * @return The map of extras
     */
    public Map<String, Object> getExtras() {
        return extras;
    }

    /**
     * If you don't specify an id, this id is an auto-generated unique negative integer for each Item (the less
     * likely to conflict with your model IDs.)
     * <p>
     * You may prefer to override it with the ID of a model object, for example the primary key of
     * an object from a database that it represents.
     *
     * @return A unique id
     */
    public long getId() {
        return id;
    }

    /**
     * Whether two item objects represent the same underlying data when compared using DiffUtil,
     * even if there has been a change in that data.
     * <p>
     * The default implementation compares both view type and id.
     */
    public boolean isSameAs(Item other) {
        if (getLayout() != other.getLayout()) {
            return false;
        }
        return getId() == other.getId();
    }

    @Nullable
    public Object getChangePayload(Item newItem) {
        return null;
    }
}

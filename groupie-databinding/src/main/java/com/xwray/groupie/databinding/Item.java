package com.xwray.groupie.databinding;

import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;

import com.xwray.groupie.core.BaseItem;
import com.xwray.groupie.core.BaseOnItemClickListener;
import com.xwray.groupie.core.BaseOnItemLongClickListener;

import java.util.List;

/**
 * The base unit of content for a GroupAdapter.
 * <p>
 * Because an Item is a Group of size one, you don't need to use Groups directly if you don't want;
 * simply mix and match Items and add directly to the adapter.
 * <p>
 * If you want to use Groups, because Item extends Group, you can mix and match adding Items and
 * other Groups directly to the adapter.
 *
 * @param <T> The ViewDataBinding subclass associated with this Item.
 */
public abstract class Item<T extends ViewDataBinding> extends BaseItem<ViewHolder<T>> {

    public Item() {
        super();
    }

    protected Item(long id) {
        super(id);
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
    @Override
    public void bind(ViewHolder<T> holder, int position, List<Object> payloads, BaseOnItemClickListener onItemClickListener, BaseOnItemLongClickListener onItemLongClickListener) {
        super.bind(holder, position, payloads, onItemClickListener, onItemLongClickListener);
        T binding = holder.binding;

        bind(binding, position, payloads);
        binding.executePendingBindings();
    }

    /**
     * Perform any actions required to set up the view for display.
     *
     * @param viewBinding The ViewDataBinding to bind
     * @param position The adapter position
     */
    public abstract void bind(T viewBinding, int position);

    /**
     * Perform any actions required to set up the view for display.
     *
     * If you don't specify how to handle payloads in your implementation, they'll be ignored and
     * the adapter will do a full rebind.
     *
     * @param viewBinding The ViewDataBinding to bind
     * @param position The adapter position
     * @param payloads A list of payloads (may be empty)
     */
    public void bind(T viewBinding, int position, List<Object> payloads) {
        bind(viewBinding, position);
    }

}

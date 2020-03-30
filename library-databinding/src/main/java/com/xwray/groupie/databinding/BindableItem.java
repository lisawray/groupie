package com.xwray.groupie.databinding;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import android.view.View;

import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.OnItemLongClickListener;

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
 * @deprecated Use groupie-viewbinding
 *
 * @param <T> The ViewDataBinding subclass associated with this Item.
 */
@Deprecated
public abstract class BindableItem<T extends ViewDataBinding> extends Item<GroupieViewHolder<T>> {

    public BindableItem() {
        super();
    }

    protected BindableItem(long id) {
        super(id);
    }

    @NonNull
    @Override
    public GroupieViewHolder<T> createViewHolder(@NonNull View itemView) {
        T viewDataBinding = DataBindingUtil.bind(itemView);
        return new GroupieViewHolder<>(viewDataBinding);
    }

    /**
     * Perform any actions required to set up the view for display.
     *
     * @param viewHolder          The viewHolder to bind
     * @param position            The adapter position
     * @param payloads            Any payloads (this list may be empty)
     * @param onItemClickListener An optional adapter-level click listener
     * @param onItemLongClickListener An optional adapter-level long click listener
     */
    @CallSuper
    @Override
    public void bind(@NonNull GroupieViewHolder<T> viewHolder, int position, @NonNull List<Object> payloads, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        super.bind(viewHolder, position, payloads, onItemClickListener, onItemLongClickListener);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public void bind(@NonNull GroupieViewHolder<T> viewHolder, int position) {
        throw new RuntimeException("Doesn't get called");
    }

    @Override
    public void bind(@NonNull GroupieViewHolder<T> viewHolder, int position, @NonNull List<Object> payloads) {
        bind(viewHolder.binding, position, payloads);
    }

    /**
     * Perform any actions required to set up the view for display.
     *
     * @param viewBinding The ViewDataBinding to bind
     * @param position The adapter position
     */
    public abstract void bind(@NonNull T viewBinding, int position);

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
    public void bind(@NonNull T viewBinding, int position, @NonNull List<Object> payloads) {
        bind(viewBinding, position);
    }
}

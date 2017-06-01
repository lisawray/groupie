package com.xwray.groupie.databinding;

import android.databinding.ViewDataBinding;

import com.xwray.groupie.core.BaseViewHolder;

public class ViewHolder<T extends ViewDataBinding> extends BaseViewHolder<Item> {
    public final T binding;

    public ViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

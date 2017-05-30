package com.xwray.groupie;

import android.databinding.ViewDataBinding;

public class ViewHolder<T extends ViewDataBinding> extends BaseViewHolder<Item> {
    public final T binding;

    public ViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

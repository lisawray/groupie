package com.xwray.groupie.databinding;

import android.databinding.ViewDataBinding;

public class ViewHolder<T extends ViewDataBinding> extends com.xwray.groupie.ViewHolder {
    public final T binding;

    public ViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

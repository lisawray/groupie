package com.xwray.groupie.databinding;

import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;

public class ViewHolder<T extends ViewDataBinding> extends com.xwray.groupie.ViewHolder {
    public final T binding;

    public ViewHolder(@NonNull T binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.setLifecycleOwner(this);
    }
}

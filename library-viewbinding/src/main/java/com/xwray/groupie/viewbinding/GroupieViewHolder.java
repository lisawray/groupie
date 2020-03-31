package com.xwray.groupie.viewbinding;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

public class GroupieViewHolder<T extends ViewBinding> extends com.xwray.groupie.GroupieViewHolder {
    public final T binding;

    public GroupieViewHolder(@NonNull T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

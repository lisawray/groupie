package com.xwray.groupie.databinding;

import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;

/**
 * @deprecated Use groupie-viewbinding
 */
@Deprecated
public class GroupieViewHolder<T extends ViewDataBinding> extends com.xwray.groupie.GroupieViewHolder {
    public final T binding;

    public GroupieViewHolder(@NonNull T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}

package com.xwray.groupie;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collection;

/**
 * An adapter that holds a list of Groups.
 */
public class GroupAdapter extends BaseGroupAdapter<Item, ViewHolder<?>> {

    @Override public ViewHolder<? extends ViewDataBinding> onCreateViewHolder(ViewGroup parent, int layoutResId) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutResId, parent, false);
        return new ViewHolder<>(binding);
    }

    @Override
    public void addAll(@NonNull Collection<? extends Group<Item>> baseGroups) {
        super.addAll(baseGroups);
    }
}

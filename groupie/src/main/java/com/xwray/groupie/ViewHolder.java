package com.xwray.groupie;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Map;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final T binding;
    private Item item;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Discard click if the viewholder has been removed, but was still in the process of
            // animating its removal while clicked (unlikely, but technically possible)
            if (onItemClickListener != null && getAdapterPosition() != NO_POSITION) {
                onItemClickListener.onItemClick(getItem(), v);
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            // Discard long click if the viewholder has been removed, but was still in the process of
            // animating its removal while clicked (unlikely, but technically possible)
            if (onItemLongClickListener != null && getAdapterPosition() != NO_POSITION) {
                return onItemLongClickListener.onItemLongClick(getItem(), v);
            }
            return false;
        }
    };

    public ViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Item item, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        this.item = item;

        // Only set the top-level click listeners if a) the adapter has one, and b) the item has
        // click enabled.  This ensures we don't interfere with user-set click listeners.

        // It would be nice to keep our listener always attached and set it only once on creating
        // the viewholder, but different items of the same layout type may not have the same click
        // listener or even agree on whether they are clickable.
        if (onItemClickListener != null && item.isClickable()) {
            binding.getRoot().setOnClickListener(onClickListener);
            this.onItemClickListener = onItemClickListener;
        }

        if (onItemLongClickListener != null && item.isClickable()) {
            binding.getRoot().setOnLongClickListener(onLongClickListener);
            this.onItemLongClickListener = onItemLongClickListener;
        }
    }

    public void unbind() {
        // Only set the top-level click listener to null if we had previously set it ourselves.

        // This avoids undoing any click listeners the user may set which might be persistent for
        // the life of the viewholder. (It's up to the user to make sure that's correct behavior.)
        if (onItemClickListener != null && item.isClickable()) {
            binding.getRoot().setOnClickListener(null);
        }
        this.item = null;
        this.onItemClickListener = null;
    }

    public Map<String, Object> getExtras() {
        return item.getExtras();
    }

    public int getSwipeDirs() {
        return item.getSwipeDirs();
    }

    public int getDragDirs() {
        return item.getDragDirs();
    }

    public Item getItem() {
        return item;
    }
}

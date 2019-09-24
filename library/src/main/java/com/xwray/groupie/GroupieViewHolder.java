package com.xwray.groupie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.Map;

public class GroupieViewHolder extends RecyclerView.ViewHolder {
    private Item item;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(@NonNull View v) {
            // Discard click if the viewholder has been removed, but was still in the process of
            // animating its removal while clicked (unlikely, but technically possible)
            if (onItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(getItem(), v);
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(@NonNull View v) {
            // Discard long click if the viewholder has been removed, but was still in the process of
            // animating its removal while long clicked (unlikely, but technically possible)
            if (onItemLongClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                return onItemLongClickListener.onItemLongClick(getItem(), v);
            }
            return false;
        }
    };

    public GroupieViewHolder(@NonNull View rootView) {
        super(rootView);
    }

    public void bind(@NonNull Item item, @Nullable OnItemClickListener onItemClickListener, @Nullable OnItemLongClickListener onItemLongClickListener) {
        this.item = item;

        // Only set the top-level click listeners if a) they exist, and b) the item has
        // clicks enabled.  This ensures we don't interfere with user-set click listeners.

        // It would be nice to keep our listeners always attached and set them only once on creating
        // the viewholder, but different items of the same layout type may not have the same click
        // listeners or even agree on whether they are clickable.
        if (onItemClickListener != null && item.isClickable()) {
            itemView.setOnClickListener(onClickListener);
            this.onItemClickListener = onItemClickListener;
        }

        if (onItemLongClickListener != null && item.isLongClickable()) {
            itemView.setOnLongClickListener(onLongClickListener);
            this.onItemLongClickListener = onItemLongClickListener;
        }
    }

    public void unbind() {
        // Only set the top-level click listener to null if we had previously set it ourselves.

        // This avoids undoing any click listeners the user may set which might be persistent for
        // the life of the viewholder. (It's up to the user to make sure that's correct behavior.)
        if (onItemClickListener != null && item.isClickable()) {
            itemView.setOnClickListener(null);
        }
        if (onItemLongClickListener != null && item.isLongClickable()) {
            itemView.setOnLongClickListener(null);
        }
        this.item = null;
        this.onItemClickListener = null;
        this.onItemLongClickListener = null;
    }

    public @NonNull Map<String, Object> getExtras() {
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

    public View getRoot() {
        return itemView;
    }
}

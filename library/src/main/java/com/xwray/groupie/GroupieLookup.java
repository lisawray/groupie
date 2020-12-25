package com.xwray.groupie;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class GroupieLookup extends ItemDetailsLookup<Long> {

    public final static String GROUPIE_SELECTION_ID = "groupie-selection";
    private RecyclerView recyclerView;

    public GroupieLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof GroupieViewHolder) {
                return ((GroupieViewHolder) viewHolder).getItemDetails();
            }
        }

        return null;
    }
}

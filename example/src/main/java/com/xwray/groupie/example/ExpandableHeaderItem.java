package com.xwray.groupie.example;

import android.graphics.drawable.Animatable;
import android.support.annotation.StringRes;
import android.view.View;

import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.ExpandableItem;
import com.xwray.groupie.example.item.HeaderItem;
import com.xwray.groupie.example.viewholder.HeaderViewHolder;

public class ExpandableHeaderItem extends HeaderItem implements ExpandableItem {

    private ExpandableGroup expandableGroup;

    public ExpandableHeaderItem(@StringRes int titleStringResId, @StringRes int subtitleResId) {
        super(titleStringResId, subtitleResId);
    }

    @Override
    public void bind(final HeaderViewHolder holder, int position) {
        super.bind(holder, position);

        // Initial icon state -- not animated.
        holder.icon.setVisibility(View.VISIBLE);
        holder.icon.setImageResource(expandableGroup.isExpanded() ? R.drawable.collapse : R.drawable.expand);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                expandableGroup.onToggleExpanded();
                bindIcon(holder);
            }
        });
    }

    private void bindIcon(HeaderViewHolder holder) {
        holder.icon.setVisibility(View.VISIBLE);
        holder.icon.setImageResource(expandableGroup.isExpanded() ? R.drawable.collapse_animated : R.drawable.expand_animated);
        Animatable drawable = (Animatable) holder.icon.getDrawable();
        drawable.start();
    }

    @Override public void setExpandableGroup(ExpandableGroup onToggleListener) {
        this.expandableGroup = onToggleListener;
    }
}

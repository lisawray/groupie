package com.xwray.groupie.example.item;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;

import com.xwray.groupie.Item;
import com.xwray.groupie.example.R;
import com.xwray.groupie.example.viewholder.HeaderViewHolder;

public class HeaderItem extends Item<HeaderViewHolder> {

    @StringRes private int titleStringResId;
    @StringRes private int subtitleResId;
    @DrawableRes private int iconResId;
    private View.OnClickListener onIconClickListener;

    public HeaderItem(@StringRes int titleStringResId) {
        this(titleStringResId, 0);
    }

    public HeaderItem(@StringRes int titleStringResId, @StringRes int subtitleResId) {
        this(titleStringResId, subtitleResId, 0, null);
    }

    public HeaderItem(@StringRes int titleStringResId, @StringRes int subtitleResId, @DrawableRes int iconResId, View.OnClickListener onIconClickListener) {
        this.titleStringResId = titleStringResId;
        this.subtitleResId = subtitleResId;
        this.iconResId = iconResId;
        this.onIconClickListener = onIconClickListener;
    }

    @Override public int getLayout() {
        return R.layout.item_header;
    }

    @Override
    public HeaderViewHolder createViewHolder(View itemView) {
        return new HeaderViewHolder(itemView);
    }

    @Override
    public void bind(HeaderViewHolder viewHolder, int position) {
        viewHolder.title.setText(titleStringResId);
        if (subtitleResId > 0) {
            viewHolder.subtitle.setText(subtitleResId);
        }
        viewHolder.subtitle.setVisibility(subtitleResId > 0 ? View.VISIBLE : View.GONE);

        if (iconResId > 0) {
            viewHolder.icon.setImageResource(iconResId);
            viewHolder.icon.setOnClickListener(onIconClickListener);
        }
        viewHolder.icon.setVisibility(iconResId > 0 ? View.VISIBLE : View.GONE);
    }
}

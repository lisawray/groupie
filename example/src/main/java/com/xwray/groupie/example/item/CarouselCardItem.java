package com.xwray.groupie.example.item;

import android.support.annotation.ColorInt;
import android.view.View;

import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;
import com.xwray.groupie.example.R;

/**
 * A card item with a fixed width so it can be used with a horizontal layout manager.
 */
public class CarouselCardItem extends Item<ViewHolder> {

    @ColorInt private int colorRes;

    public CarouselCardItem(@ColorInt int colorRes) {
        this.colorRes = colorRes;
    }

    @Override public int getLayout() {
        return R.layout.item_square_card;
    }

    @Override
    public ViewHolder createViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bind(ViewHolder viewHolder, int position) {
        //viewHolder.getRoot().setBackgroundResource(colorRes);
    }
}

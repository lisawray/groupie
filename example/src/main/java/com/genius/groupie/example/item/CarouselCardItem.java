package com.genius.groupie.example.item;

import android.support.annotation.ColorRes;

import com.genius.groupie.Item;
import com.genius.groupie.example.databinding.ItemSquareCardBinding;

/**
 * A card item with a fixed width so it can be used with a horizontal layout manager.
 */
public class CarouselCardItem extends Item<ItemSquareCardBinding> {

    @ColorRes private int colorRes;

    public CarouselCardItem(@ColorRes int colorRes) {
        this.colorRes = colorRes;
    }

    @Override public int getLayout() {
        return com.genius.groupie.example.R.layout.item_square_card;
    }

    @Override public void bind(ItemSquareCardBinding viewBinding, int position) {
        //viewBinding.getRoot().setBackgroundResource(colorRes);
    }
}

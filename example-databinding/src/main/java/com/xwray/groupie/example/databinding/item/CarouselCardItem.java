package com.xwray.groupie.example.databinding.item;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.xwray.groupie.databinding.BindableItem;
import com.xwray.groupie.example.databinding.R;
import com.xwray.groupie.example.databinding.databinding.ItemSquareCardBinding;

/**
 * A card item with a fixed width so it can be used with a horizontal layout manager.
 */
public class CarouselCardItem extends BindableItem<ItemSquareCardBinding> {

    @ColorInt private int colorInt;

    public CarouselCardItem(@ColorInt int colorInt) {
        this.colorInt = colorInt;
    }

    @Override public int getLayout() {
        return R.layout.item_square_card;
    }

    @Override public void bind(@NonNull ItemSquareCardBinding viewBinding, int position) {
        viewBinding.getRoot().setBackgroundColor(colorInt);
    }
}

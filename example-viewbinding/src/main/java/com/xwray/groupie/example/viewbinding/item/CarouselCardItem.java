package com.xwray.groupie.example.viewbinding.item;

import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.xwray.groupie.viewbinding.BindableItem;
import com.xwray.groupie.example.viewbinding.R;
import com.xwray.groupie.example.viewbinding.databinding.ItemSquareCardBinding;

/**
 * A card item with a fixed width so it can be used with a horizontal layout manager.
 */
public class CarouselCardItem extends BindableItem<ItemSquareCardBinding> {

    @ColorInt private int colorRes;

    public CarouselCardItem(@ColorInt int colorRes) {
        this.colorRes = colorRes;
    }

    @Override public int getLayout() {
        return R.layout.item_square_card;
    }

    @NonNull
    @Override
    protected ItemSquareCardBinding initializeViewBinding(@NonNull View view) {
        return ItemSquareCardBinding.bind(view);
    }

    @Override public void bind(@NonNull ItemSquareCardBinding viewBinding, int position) {
        viewBinding.getRoot().setBackgroundColor(colorRes);
    }
}

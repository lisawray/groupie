package com.genius.groupie.example.item;

import android.support.annotation.ColorInt;

import com.genius.groupie.Item;
import com.genius.groupie.example.databinding.ItemCardBinding;

import static com.genius.groupie.example.MainActivity.INSET;
import static com.genius.groupie.example.MainActivity.INSET_TYPE_KEY;

public class CardItem extends Item<ItemCardBinding> {

    @ColorInt private int colorRes;
    private CharSequence text;

    public CardItem(@ColorInt int colorRes) {
        this(colorRes, "");
    }

    public CardItem(@ColorInt int colorRes, CharSequence text) {
        this.colorRes = colorRes;
        this.text = text;
        getExtras().put(INSET_TYPE_KEY, INSET);
    }

    @Override public int getLayout() {
        return com.genius.groupie.example.R.layout.item_card;
    }

    @Override public void bind(ItemCardBinding viewBinding, int position) {
        //viewBinding.getRoot().setBackgroundColor(colorRes);
        viewBinding.text.setText(text);
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public CharSequence getText() {
        return text;
    }
}

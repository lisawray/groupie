package com.xwray.groupie.example.databinding.item;

import android.support.annotation.ColorInt;

import com.xwray.groupie.databinding.Item;
import com.xwray.groupie.example.databinding.R;
import com.xwray.groupie.example.databinding.databinding.ItemCardBinding;

import static com.xwray.groupie.example.databinding.MainActivity.INSET;
import static com.xwray.groupie.example.databinding.MainActivity.INSET_TYPE_KEY;

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
        return R.layout.item_card;
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

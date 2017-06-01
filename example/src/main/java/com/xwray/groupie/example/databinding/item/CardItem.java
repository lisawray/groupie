package com.xwray.groupie.example.item;

import android.support.annotation.ColorInt;

import com.xwray.groupie.Item;
import com.xwray.groupie.example.databinding.ItemCardBinding;
import com.xwray.groupie.example.R;

import static com.xwray.groupie.example.MainActivity.INSET;
import static com.xwray.groupie.example.MainActivity.INSET_TYPE_KEY;

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

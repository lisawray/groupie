package com.xwray.groupie.example.item;

import android.support.annotation.ColorInt;
import android.view.View;

import com.xwray.groupie.Item;
import com.xwray.groupie.example.R;
import com.xwray.groupie.example.viewholder.CardViewHolder;

import static com.xwray.groupie.example.MainActivity.INSET;
import static com.xwray.groupie.example.MainActivity.INSET_TYPE_KEY;

public class CardItem extends Item<CardViewHolder> {

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

    public void setText(CharSequence text) {
        this.text = text;
    }

    public CharSequence getText() {
        return text;
    }

    @Override
    public CardViewHolder createViewHolder(View itemView) {
        return new CardViewHolder(itemView);
    }

    @Override
    public void bind(CardViewHolder viewHolder, int position) {
        //viewBinding.getRoot().setBackgroundColor(colorRes);
        viewHolder.text.setText(text);
    }
}

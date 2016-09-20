package com.genius.groupie.example.item;

import android.support.annotation.ColorRes;

public class SmallCardItem extends CardItem {

    public SmallCardItem(@ColorRes int colorRes) {
        super(colorRes);
    }

    public SmallCardItem(@ColorRes int colorRes, CharSequence text) {
        super(colorRes, text);
    }

    @Override public int getSpanSize(int spanCount, int position) {
        return spanCount / 3;
    }
}

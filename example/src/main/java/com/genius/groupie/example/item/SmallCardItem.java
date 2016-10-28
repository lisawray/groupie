package com.genius.groupie.example.item;

import android.support.annotation.ColorInt;

public class SmallCardItem extends CardItem {

    public SmallCardItem(@ColorInt int colorRes) {
        super(colorRes);
    }

    public SmallCardItem(@ColorInt int colorRes, CharSequence text) {
        super(colorRes, text);
    }

    @Override public int getSpanSize(int spanCount, int position) {
        return spanCount / 3;
    }
}

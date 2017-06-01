package com.xwray.groupie.example.databinding.item;

import android.support.annotation.ColorInt;

public class SmallCardItem extends com.xwray.groupie.example.item.CardItem {

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

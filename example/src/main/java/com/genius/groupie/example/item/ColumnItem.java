package com.genius.groupie.example.item;

import android.support.annotation.ColorInt;

import com.genius.groupie.example.MainActivity;

public class ColumnItem extends CardItem {

    public ColumnItem(@ColorInt int colorRes, int index) {
        super(colorRes, String.valueOf(index));
        getExtras().put(MainActivity.INSET_TYPE_KEY, MainActivity.INSET);
    }

    @Override public int getSpanSize(int spanCount, int position) {
        return spanCount / 2;
    }
}

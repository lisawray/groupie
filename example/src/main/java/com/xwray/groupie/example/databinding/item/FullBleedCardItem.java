package com.xwray.groupie.example.item;

import android.support.annotation.ColorRes;

public class FullBleedCardItem extends CardItem {

    public FullBleedCardItem(@ColorRes int colorRes) {
        super(colorRes);
        getExtras().put(MainActivity.INSET_TYPE_KEY, MainActivity.INSET);
    }
}

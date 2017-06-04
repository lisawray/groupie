package com.xwray.groupie.example.databinding.item;

import android.support.annotation.ColorRes;

import com.xwray.groupie.example.databinding.MainActivity;

public class FullBleedCardItem extends CardItem {

    public FullBleedCardItem(@ColorRes int colorRes) {
        super(colorRes);
        getExtras().put(MainActivity.INSET_TYPE_KEY, MainActivity.INSET);
    }
}

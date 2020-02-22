package com.xwray.groupie.example.viewbinding.item;

import androidx.annotation.ColorRes;

import com.xwray.groupie.example.viewbinding.MainActivity;

public class FullBleedCardItem extends CardItem {

    public FullBleedCardItem(@ColorRes int colorRes) {
        super(colorRes);
        getExtras().remove(MainActivity.INSET_TYPE_KEY);
    }
}

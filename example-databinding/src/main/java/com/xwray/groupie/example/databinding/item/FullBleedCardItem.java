package com.xwray.groupie.example.databinding.item;

import androidx.annotation.ColorRes;

import com.xwray.groupie.example.databinding.MainActivity;

public class FullBleedCardItem extends CardItem {

    public FullBleedCardItem(@ColorRes int colorRes) {
        super(colorRes);
        getExtras().remove(MainActivity.INSET_TYPE_KEY);
    }
}

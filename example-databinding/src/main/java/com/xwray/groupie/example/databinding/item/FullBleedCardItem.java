package com.xwray.groupie.example.databinding.item;

import com.xwray.groupie.example.databinding.MainActivity;

public class FullBleedCardItem extends CardItem {

    public FullBleedCardItem() {
        super();
        getExtras().remove(MainActivity.INSET_TYPE_KEY);
    }
}

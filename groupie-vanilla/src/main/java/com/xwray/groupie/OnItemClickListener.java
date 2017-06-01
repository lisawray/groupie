package com.xwray.groupie;

import android.view.View;

import com.xwray.groupie.core.BaseOnItemClickListener;

public interface OnItemClickListener extends BaseOnItemClickListener<Item> {

    void onItemClick(Item item, View view);

}

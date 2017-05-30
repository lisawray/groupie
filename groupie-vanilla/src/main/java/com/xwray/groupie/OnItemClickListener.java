package com.xwray.groupie;

import android.view.View;

public interface OnItemClickListener extends BaseOnItemClickListener<Item> {

    void onItemClick(Item item, View view);

}

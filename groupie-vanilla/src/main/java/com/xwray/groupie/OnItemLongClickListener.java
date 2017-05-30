package com.xwray.groupie;

import android.view.View;

public interface OnItemLongClickListener extends BaseOnItemLongClickListener<Item> {

    boolean onItemLongClick(Item item, View view);

}

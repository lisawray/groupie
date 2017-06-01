package com.xwray.groupie.databinding;

import android.view.View;

import com.xwray.groupie.core.BaseOnItemLongClickListener;

public interface OnItemLongClickListener extends BaseOnItemLongClickListener<Item> {

    boolean onItemLongClick(Item item, View view);

}

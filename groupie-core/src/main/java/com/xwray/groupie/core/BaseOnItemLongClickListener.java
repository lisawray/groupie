package com.xwray.groupie.core;

import android.view.View;

public interface BaseOnItemLongClickListener<I extends BaseItem> {

    boolean onItemLongClick(I item, View view);

}

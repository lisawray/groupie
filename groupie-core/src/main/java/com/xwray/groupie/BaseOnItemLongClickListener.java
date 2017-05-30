package com.xwray.groupie;

import android.view.View;

public interface BaseOnItemLongClickListener<I extends BaseItem> {

    boolean onItemLongClick(I item, View view);

}

package com.xwray.groupie.core;

import android.view.View;

public interface BaseOnItemClickListener<I extends BaseItem> {

    void onItemClick(I item, View view);

}

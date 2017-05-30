package com.xwray.groupie;

import android.view.View;

public interface BaseOnItemClickListener<I extends BaseItem> {

    void onItemClick(I item, View view);

}

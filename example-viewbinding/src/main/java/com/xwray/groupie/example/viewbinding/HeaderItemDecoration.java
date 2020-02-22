package com.xwray.groupie.example.viewbinding;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;

public class HeaderItemDecoration extends com.xwray.groupie.example.core.decoration.HeaderItemDecoration {
    public HeaderItemDecoration(@ColorInt int background, int sidePaddingPixels) {
        super(background, sidePaddingPixels, R.layout.item_header);
    }
}

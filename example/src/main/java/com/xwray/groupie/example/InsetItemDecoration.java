package com.xwray.groupie.example;

import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;

public class InsetItemDecoration extends com.xwray.groupie.example.core.decoration.InsetItemDecoration {
    public InsetItemDecoration(@ColorInt int backgroundColor, @Dimension int padding) {
        super(backgroundColor, padding, MainActivity.INSET_TYPE_KEY, MainActivity.INSET);
    }
}

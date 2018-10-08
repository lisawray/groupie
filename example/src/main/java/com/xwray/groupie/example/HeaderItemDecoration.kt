package com.xwray.groupie.example

import androidx.annotation.ColorInt

class HeaderItemDecoration(@ColorInt background: Int, sidePaddingPixels: Int)
    : com.xwray.groupie.example.core.decoration.HeaderItemDecoration(
        background, sidePaddingPixels, R.layout.item_header)

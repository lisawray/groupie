package com.xwray.groupie.example.viewbinding

import androidx.annotation.ColorInt
import com.xwray.groupie.example.core.decoration.HeaderItemDecoration

class HeaderItemDecoration(
    @ColorInt background: Int,
    sidePaddingPixels: Int
) : HeaderItemDecoration(background, sidePaddingPixels, R.layout.item_header)

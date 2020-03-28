package com.xwray.groupie.example.viewbinding

import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.xwray.groupie.example.core.decoration.InsetItemDecoration

class InsetItemDecoration(
    @ColorInt backgroundColor: Int,
    @Dimension padding: Int
) : InsetItemDecoration(backgroundColor, padding, INSET_TYPE_KEY, INSET)

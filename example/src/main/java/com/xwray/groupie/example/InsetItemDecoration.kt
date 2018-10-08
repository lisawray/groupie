package com.xwray.groupie.example

import androidx.annotation.ColorInt
import androidx.annotation.Dimension

class InsetItemDecoration(@ColorInt backgroundColor: Int, @Dimension padding: Int) :
        com.xwray.groupie.example.core.decoration.InsetItemDecoration(backgroundColor, padding, INSET_TYPE_KEY, INSET)

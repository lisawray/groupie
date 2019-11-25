package com.xwray.groupie.example.item

import androidx.annotation.ColorRes
import com.xwray.groupie.example.INSET_TYPE_KEY

class FullBleedCardItem(@ColorRes colorInt: Int) : CardItem(colorInt) {

    init {
        extras.remove(INSET_TYPE_KEY)
    }
}

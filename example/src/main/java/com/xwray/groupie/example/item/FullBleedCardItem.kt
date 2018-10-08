package com.xwray.groupie.example.item

import androidx.annotation.ColorRes
import com.xwray.groupie.example.INSET_TYPE_KEY

class FullBleedCardItem(@ColorRes colorRes: Int) : CardItem(colorRes) {

    init {
        extras.remove(INSET_TYPE_KEY)
    }
}

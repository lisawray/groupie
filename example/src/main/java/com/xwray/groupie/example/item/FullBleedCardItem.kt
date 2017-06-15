package com.xwray.groupie.example.item

import android.support.annotation.ColorRes
import com.xwray.groupie.example.INSET
import com.xwray.groupie.example.INSET_TYPE_KEY

class FullBleedCardItem(@ColorRes colorRes: Int) : CardItem(colorRes) {

    init {
        extras.put(INSET_TYPE_KEY, INSET)
    }
}

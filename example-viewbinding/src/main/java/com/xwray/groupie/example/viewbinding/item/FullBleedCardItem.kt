package com.xwray.groupie.example.viewbinding.item

import androidx.annotation.ColorInt
import com.xwray.groupie.example.viewbinding.INSET_TYPE_KEY

class FullBleedCardItem(@ColorInt colorInt: Int) : CardItem(colorInt) {
  init {
    extras.remove(INSET_TYPE_KEY)
  }
}

package com.xwray.groupie.example.viewbinding.item

import android.view.View
import androidx.annotation.ColorInt
import com.xwray.groupie.example.viewbinding.R
import com.xwray.groupie.example.viewbinding.databinding.ItemSquareCardBinding
import com.xwray.groupie.viewbinding.BindableItem

/**
 * A card item with a fixed width so it can be used with a horizontal layout manager.
 */
class CarouselCardItem(
    @ColorInt private val colorInt: Int
) : BindableItem<ItemSquareCardBinding?>() {

  override fun getLayout(): Int = R.layout.item_square_card

  override fun initializeViewBinding(view: View): ItemSquareCardBinding =
      ItemSquareCardBinding.bind(view)

  override fun bind(viewBinding: ItemSquareCardBinding, position: Int) {
    viewBinding.root.setBackgroundColor(colorInt)
  }
}

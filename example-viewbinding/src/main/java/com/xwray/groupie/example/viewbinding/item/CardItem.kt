package com.xwray.groupie.example.viewbinding.item

import android.view.View
import androidx.annotation.ColorInt
import com.xwray.groupie.example.viewbinding.INSET
import com.xwray.groupie.example.viewbinding.INSET_TYPE_KEY
import com.xwray.groupie.example.viewbinding.R
import com.xwray.groupie.example.viewbinding.databinding.ItemCardBinding
import com.xwray.groupie.viewbinding.BindableItem

open class CardItem @JvmOverloads constructor(
    @ColorInt private val colorInt: Int,
    val text: CharSequence = ""
) : BindableItem<ItemCardBinding>() {

  init {
    extras[INSET_TYPE_KEY] = INSET
  }

  override fun getLayout(): Int = R.layout.item_card

  override fun initializeViewBinding(view: View): ItemCardBinding = ItemCardBinding.bind(view)

  override fun bind(viewBinding: ItemCardBinding, position: Int) {
    viewBinding.root.setBackgroundColor(colorInt)
    viewBinding.text.text = text
  }
}

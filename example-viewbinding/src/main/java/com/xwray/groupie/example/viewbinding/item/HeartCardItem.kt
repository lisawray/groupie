package com.xwray.groupie.example.viewbinding.item

import android.graphics.drawable.Animatable
import android.view.View
import androidx.annotation.ColorInt
import com.xwray.groupie.example.viewbinding.INSET
import com.xwray.groupie.example.viewbinding.INSET_TYPE_KEY
import com.xwray.groupie.example.viewbinding.R
import com.xwray.groupie.example.viewbinding.databinding.ItemHeartCardBinding
import com.xwray.groupie.viewbinding.BindableItem

class HeartCardItem(
    @ColorInt private val colorInt: Int,
    id: Long,
    private val onFavoriteListener: (item: HeartCardItem, favorite: Boolean) -> Unit
) : BindableItem<ItemHeartCardBinding>(id) {

  companion object {
    const val FAVORITE = "FAVORITE"
  }

  private var checked = false
  private var inProgress = false

  init {
    extras[INSET_TYPE_KEY] = INSET
  }

  override fun getLayout(): Int = R.layout.item_heart_card

  override fun initializeViewBinding(view: View): ItemHeartCardBinding =
      ItemHeartCardBinding.bind(view)

  override fun bind(binding: ItemHeartCardBinding, position: Int) {
    binding.root.setBackgroundColor(colorInt)
    bindHeart(binding)
    binding.text.text = (id + 1).toString()
    binding.favorite.setOnClickListener {
      inProgress = true
      animateProgress(binding)
      onFavoriteListener(this@HeartCardItem, !checked)
    }
  }

  private fun bindHeart(binding: ItemHeartCardBinding) {
    if (inProgress) {
      animateProgress(binding)
    } else {
      binding.favorite.setImageResource(R.drawable.favorite_state_list)
    }
    binding.favorite.isChecked = checked
  }

  private fun animateProgress(binding: ItemHeartCardBinding) {
    binding.favorite.setImageResource(R.drawable.avd_favorite_progress)
    (binding.favorite.drawable as Animatable).start()
  }

  fun setFavorite(favorite: Boolean) {
    inProgress = false
    checked = favorite
  }

  override fun bind(binding: ItemHeartCardBinding, position: Int, payloads: List<Any>) {
    if (payloads.contains(FAVORITE)) {
      bindHeart(binding)
    } else {
      bind(binding, position)
    }
  }

  override fun isClickable(): Boolean = false
}

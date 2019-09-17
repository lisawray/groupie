package com.xwray.groupie.example.item

import android.graphics.drawable.Animatable
import androidx.annotation.ColorInt
import com.xwray.groupie.example.INSET
import com.xwray.groupie.example.INSET_TYPE_KEY
import com.xwray.groupie.example.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_heart_card.*

val FAVORITE = "FAVORITE"

class HeartCardItem(@ColorInt private val colorRes: Int, id: Long,
                    private val onFavoriteListener: (item: HeartCardItem, favorite: Boolean) -> Unit) :
        Item(id) {

    private var checked = false
    private var inProgress = false

    init {
        extras.put(INSET_TYPE_KEY, INSET)
    }

    override val layoutRes: Int = R.layout.item_heart_card

    private fun bindHeart(holder: GroupieViewHolder) {
        if (inProgress) {
            animateProgress(holder)
        } else {
            holder.favorite.setImageResource(R.drawable.favorite_state_list)
        }
        holder.favorite.isChecked = checked
    }

    private fun animateProgress(holder: GroupieViewHolder) {
        holder.favorite.apply {
            setImageResource(R.drawable.avd_favorite_progress)
            (drawable as Animatable).start()
        }
    }

    fun setFavorite(favorite: Boolean) {
        inProgress = false
        checked = favorite
    }

    override val isClickable: Boolean = false

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //viewHolder.getRoot().setBackgroundColor(colorRes);
        bindHeart(viewHolder)
        viewHolder.text.text = (id + 1).toString()

        viewHolder.favorite.setOnClickListener {
            inProgress = true
            animateProgress(viewHolder)
            onFavoriteListener(this@HeartCardItem, !checked)
        }
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.contains(FAVORITE)) {
            bindHeart(viewHolder)
        } else {
            bind(viewHolder, position)
        }
    }
}

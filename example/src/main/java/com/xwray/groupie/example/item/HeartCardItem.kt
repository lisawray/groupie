package com.xwray.groupie.example.item

import android.graphics.drawable.Animatable
import android.support.annotation.ColorInt
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.example.INSET
import com.xwray.groupie.example.INSET_TYPE_KEY
import com.xwray.groupie.example.R
import kotlinx.android.synthetic.main.item_heart_card.view.*

val FAVORITE = "FAVORITE"

class HeartCardItem(@param:ColorInt private val colorRes: Int, id: Long,
                    private val onFavoriteListener: (item: HeartCardItem, favorite: Boolean) -> Unit) :
        Item<ViewHolder>(id) {

    private var checked = false
    private var inProgress = false

    init {
        extras.put(INSET_TYPE_KEY, INSET)
    }

    override fun getLayout(): Int {
        return R.layout.item_heart_card
    }

    private fun bindHeart(holder: ViewHolder) {
        if (inProgress) {
            animateProgress(holder)
        } else {
            holder.itemView.favorite.setImageResource(R.drawable.favorite_state_list)
        }
        holder.itemView.favorite.isChecked = checked
    }

    private fun animateProgress(holder: ViewHolder) {
        holder.itemView.favorite.apply {
            setImageResource(R.drawable.avd_favorite_progress)
            (drawable as Animatable).start()
        }
    }

    fun setFavorite(favorite: Boolean) {
        inProgress = false
        checked = favorite
    }

    override fun isClickable(): Boolean {
        return false
    }

    override fun bind(holder: ViewHolder, position: Int) {
        //holder.getRoot().setBackgroundColor(colorRes);
        bindHeart(holder)
        holder.itemView.text.text = (id + 1).toString()

        holder.itemView.favorite.setOnClickListener {
            inProgress = true
            animateProgress(holder)
            onFavoriteListener(this@HeartCardItem, !checked)
        }
    }

    override fun bind(holder: ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.contains(FAVORITE)) {
            bindHeart(holder)
        } else {
            bind(holder, position)
        }
    }
}

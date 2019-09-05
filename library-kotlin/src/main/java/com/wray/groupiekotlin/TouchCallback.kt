package com.wray.groupiekotlin

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class TouchCallback : ItemTouchHelper.SimpleCallback(0, 0) {

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return (viewHolder as GroupieViewHolder).swipeDirs
    }

    override fun getDragDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return (viewHolder as GroupieViewHolder).dragDirs
    }
}

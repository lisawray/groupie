package com.xwray.groupie.kotlinandroidextensions

import android.view.View
import com.xwray.groupie.Item

abstract class Item : Item<GroupieViewHolder> {

    constructor() : super()
    constructor(id: Long) : super(id)

    override fun createViewHolder(itemView: View): GroupieViewHolder {
        return GroupieViewHolder(itemView)
    }
}
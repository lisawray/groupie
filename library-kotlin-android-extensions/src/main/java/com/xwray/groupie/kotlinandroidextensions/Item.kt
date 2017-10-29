package com.xwray.groupie.kotlinandroidextensions

import android.view.View
import com.xwray.groupie.Item

abstract class Item : Item<ViewHolder> {

    constructor() : super()
    constructor(id: Long) : super(id)

    override fun createViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }
}
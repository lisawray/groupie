package com.xwray.groupie.kotlinandroidextensions

import android.view.View
import com.xwray.groupie.Item

@Deprecated(message = "Kotlin-Android-Extensions is deprecated since 1.4.21, therefore so is the kotlin-android-extensions module in Groupie. Use `groupie-viewbinding` instead.")
abstract class Item : Item<GroupieViewHolder> {

    constructor() : super()
    constructor(id: Long) : super(id)

    override fun createViewHolder(itemView: View): GroupieViewHolder {
        return GroupieViewHolder(itemView)
    }
}
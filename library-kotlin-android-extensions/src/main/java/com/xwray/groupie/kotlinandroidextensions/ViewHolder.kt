package com.xwray.groupie.kotlinandroidextensions

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer

class ViewHolder(override val containerView: View) : com.xwray.groupie.ViewHolder(containerView),
        LayoutContainer {
}
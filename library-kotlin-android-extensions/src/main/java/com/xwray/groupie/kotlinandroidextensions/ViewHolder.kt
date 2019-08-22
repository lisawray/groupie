package com.xwray.groupie.kotlinandroidextensions

import android.view.View
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer

@ContainerOptions(cache = CacheImplementation.HASH_MAP)
class ViewHolder(override val containerView: View) : com.xwray.groupie.ViewHolder(containerView),
        LayoutContainer
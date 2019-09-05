package com.wray.groupiekotlin

import android.view.View

interface OnItemLongClickListener {

    fun onItemLongClick(item: AnyItem, view: View): Boolean
}

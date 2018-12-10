package com.xwray.groupie.example.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.xwray.groupie.example.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_live_data.*

class LiveDataItem(private val data: LiveData<String>) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        data.observe(viewHolder, Observer {
            viewHolder.text.text = it
        })
    }

    override fun getLayout(): Int = R.layout.item_live_data
}

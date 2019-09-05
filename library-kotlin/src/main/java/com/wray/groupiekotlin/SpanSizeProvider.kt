package com.wray.groupiekotlin

interface SpanSizeProvider {
    fun getSpanSize(spanCount: Int, position: Int): Int
}

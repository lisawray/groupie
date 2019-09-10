package com.wray.groupiekotlin

import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback

/**
 * Listener that provides a callback when
 * async update methods complete.
 */
interface OnAsyncUpdateListener {
    fun onUpdateComplete()
}

/**
 * A wrapper around [DiffUtil] that calculates diff in a background thread
 */
internal class AsyncDiffUtil(val asyncDiffUtilCallback: Callback) {
    var maxScheduledGeneration: Int = 0
        private set

    lateinit var groups: Collection<Group>
        private set

    internal interface Callback : ListUpdateCallback {
        /**
         * Called on the main thread before DiffUtil dispatches the result
         */
        @MainThread
        fun onDispatchAsyncResult(newGroups: Collection<Group>)
    }

    fun calculateDiff(newGroups: Collection<Group>,
                      diffUtilCallback: DiffUtil.Callback,
                      onAsyncUpdateListener: OnAsyncUpdateListener?,
                      detectMoves: Boolean) {
        groups = newGroups
        // incrementing generation means any currently-running diffs are discarded when they finish
        val runGeneration = ++maxScheduledGeneration
        DiffTask(this, diffUtilCallback, runGeneration, detectMoves, onAsyncUpdateListener).execute()
    }
}

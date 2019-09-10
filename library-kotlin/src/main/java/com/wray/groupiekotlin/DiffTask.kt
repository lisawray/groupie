package com.wray.groupiekotlin

import android.os.AsyncTask
import androidx.recyclerview.widget.DiffUtil
import java.lang.ref.WeakReference

/**
 * An async task implementation that runs [DiffUtil.calculateDiff]
 * in a background thread. This task will call [AsyncDiffUtil.Callback.onDispatchAsyncResult]
 * passing the new list just before dispatching the diff result to the provided
 * [DiffUtil.Callback] so that the new list.
 *
 * This task is executed via [AsyncDiffUtil.calculateDiff].
 */
internal class DiffTask(
    asyncDiffUtil: AsyncDiffUtil,
    private val diffCallback: DiffUtil.Callback,
    private val runGeneration: Int,
    private val detectMoves: Boolean,
    onAsyncUpdateListener: OnAsyncUpdateListener?
) : AsyncTask<Void, Void, DiffUtil.DiffResult>() {

    private val asyncListDiffer: WeakReference<AsyncDiffUtil> = WeakReference(asyncDiffUtil)
    private var onAsyncUpdateListener: WeakReference<OnAsyncUpdateListener>? = null
    private var backgroundException: Exception? = null

    init {
        if (onAsyncUpdateListener != null) {
            this.onAsyncUpdateListener = WeakReference(onAsyncUpdateListener)
        }
    }

    override fun doInBackground(vararg voids: Void): DiffUtil.DiffResult? =
        try {
            DiffUtil.calculateDiff(diffCallback, detectMoves)
        } catch (e: Exception) {
            backgroundException = e
            null
        }


    override fun onPostExecute(diffResult: DiffUtil.DiffResult?) {
        if (backgroundException != null) {
            throw RuntimeException(backgroundException)
        }
        val async = asyncListDiffer.get()
        if (shouldDispatchResult(diffResult, async)) {
            async?.asyncDiffUtilCallback?.let { callback ->
                callback.onDispatchAsyncResult(async.groups)
                diffResult?.dispatchUpdatesTo(callback)
            }

            onAsyncUpdateListener?.get()?.onUpdateComplete()
        }
    }

    private fun shouldDispatchResult(diffResult: DiffUtil.DiffResult?, async: AsyncDiffUtil?): Boolean =
        diffResult != null && async != null && runGeneration == async.maxScheduledGeneration

}

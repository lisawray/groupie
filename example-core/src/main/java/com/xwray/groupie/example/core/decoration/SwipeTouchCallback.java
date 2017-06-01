package com.xwray.groupie.example.core.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.xwray.groupie.core.TouchCallback;

public abstract class SwipeTouchCallback extends TouchCallback {

    private final Paint paint;
    @ColorInt private final int backgroundColor;

    public SwipeTouchCallback(@ColorInt int backgroundColor) {
        super();
        this.backgroundColor = backgroundColor;
        paint = new Paint();
    }

    @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (ItemTouchHelper.ACTION_STATE_SWIPE == actionState) {
            View child = viewHolder.itemView;
            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

            // Fade out the item
            child.setAlpha(1 - (Math.abs(dX) / (float) child.getWidth()));
//
//            // Draw gray "behind" the item, if it's being moved horizontally (swiped)
//            paint.setColor(backgroundColor);
//            if (dX > 0) {
//                c.drawRect(
//                        lm.getDecoratedLeft(child),
//                        lm.getDecoratedTop(child),
//                        lm.getDecoratedLeft(child) + dX,
//                        lm.getDecoratedBottom(child), paint);
//            } else if (dX < 0) {
//                c.drawRect(
//                        lm.getDecoratedRight(child) + dX,
//                        lm.getDecoratedTop(child),
//                        lm.getDecoratedRight(child),
//                        lm.getDecoratedBottom(child), paint);
//            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}

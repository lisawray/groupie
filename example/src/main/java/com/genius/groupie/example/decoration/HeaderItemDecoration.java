package com.genius.groupie.example.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.genius.groupie.example.R;

public class HeaderItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint;
    private int sidePaddingPixels;

    public HeaderItemDecoration(@ColorInt int background, int sidePaddingPixels) {
        this.sidePaddingPixels = sidePaddingPixels;
        paint = new Paint();
        paint.setColor(background);
    }

    public boolean isHeader(View child, RecyclerView parent) {
        int viewType = parent.getLayoutManager().getItemViewType(child);
        return viewType == R.layout.item_header;
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (!isHeader(view, parent)) return;

        outRect.left = sidePaddingPixels;
        outRect.right = sidePaddingPixels;
    }

    @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (!isHeader(child, parent)) continue;

            RecyclerView.LayoutManager lm = parent.getLayoutManager();

            float top = lm.getDecoratedTop(child) + child.getTranslationY();
            float bottom = lm.getDecoratedBottom(child) + child.getTranslationY();
            if (i == parent.getChildCount() - 1) {
                // Draw to bottom if last item
                bottom = Math.max(parent.getHeight(), bottom);
            }
            float right = lm.getDecoratedRight(child) + child.getTranslationX();
            float left = lm.getDecoratedLeft(child) + child.getTranslationX();
            c.drawRect(left, top, right, bottom, paint);
        }
    }
}

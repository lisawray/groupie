package com.xwray.groupie.example.core.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class CarouselItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint grayBackgroundPaint;
    private final int padding;

    public CarouselItemDecoration(@ColorInt int backgroundColor, int paddingPixelSize) {
        grayBackgroundPaint = new Paint();
        grayBackgroundPaint.setColor(backgroundColor);
        padding = paddingPixelSize;
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = padding;
    }

    @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            int right = (int) (lm.getDecoratedRight(child) + child.getTranslationX());
            if (i == childCount - 1) {
                // Last item
                right = Math.max(right, parent.getWidth());
            }

            // Right border
            c.drawRect(child.getRight() + child.getTranslationX(), 0, right, parent.getHeight(), grayBackgroundPaint);
        }
    }
}

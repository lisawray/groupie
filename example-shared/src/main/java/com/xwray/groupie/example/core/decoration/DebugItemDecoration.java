package com.xwray.groupie.example.core.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xwray.groupie.example.core.Prefs;
import com.xwray.groupie.example.core.R;

public class DebugItemDecoration extends RecyclerView.ItemDecoration {

    private int decoratedLeft, decoratedTop, decoratedRight, decoratedBottom;
    private int left, top, right, bottom;
    private Paint paint = new Paint();
    private Prefs prefs;
    private int leftColor, topColor, rightColor, bottomColor;

    public DebugItemDecoration(Context context) {
        prefs = Prefs.get(context);
        leftColor = ContextCompat.getColor(context, R.color.red_200);
        topColor = ContextCompat.getColor(context, R.color.pink_200);
        rightColor = ContextCompat.getColor(context, R.color.purple_200);
        bottomColor = ContextCompat.getColor(context, R.color.indigo_200);
    }

    @Override public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!(prefs.getShowBounds() || prefs.getShowOffsets())) return;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutManager lm = parent.getLayoutManager();
            decoratedLeft = lm.getDecoratedLeft(child) + (int) child.getTranslationX();
            decoratedTop = lm.getDecoratedTop(child) + (int) child.getTranslationY();
            decoratedRight = lm.getDecoratedRight(child) + (int) child.getTranslationX();
            decoratedBottom = lm.getDecoratedBottom(child) + (int) child.getTranslationY();

            left = child.getLeft() + (int) child.getTranslationX();
            top = child.getTop() + (int) child.getTranslationY();
            right = child.getRight() + (int) child.getTranslationX();
            bottom = child.getBottom() + (int) child.getTranslationY();

            if (prefs.getShowBounds()) {
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(1);
                c.drawRect(decoratedLeft, decoratedTop, decoratedRight, decoratedBottom, paint);
            }

            if (prefs.getShowOffsets()) {
                paint.setStyle(Paint.Style.FILL);

                paint.setColor(leftColor);
                c.drawRect(decoratedLeft, decoratedTop, left, decoratedBottom, paint);

                paint.setColor(topColor);
                c.drawRect(decoratedLeft, decoratedTop, decoratedRight, top, paint);

                paint.setColor(rightColor);
                c.drawRect(right, decoratedTop, decoratedRight, decoratedBottom, paint);

                paint.setColor(bottomColor);
                c.drawRect(decoratedLeft, bottom, decoratedRight, decoratedBottom, paint);
            }
        }
    }
}

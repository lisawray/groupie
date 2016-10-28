package com.genius.groupie.example.item;

import android.support.annotation.ColorInt;

import com.genius.groupie.example.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class ColumnItem extends CardItem {

    public ColumnItem(@ColorInt int colorRes, int index) {
        super(colorRes, String.valueOf(index));
    }

    @Override public int getSpanSize(int spanCount, int position) {
        return spanCount / 2;
    }

    @Override public Map<String, Object> getExtras() {
        Map<String, Object> map = new HashMap<>();
        map.put(MainActivity.INSET_TYPE_KEY, MainActivity.INSET);
        return map;
    }
}

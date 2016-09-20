package com.genius.groupie.example.item;

import android.support.annotation.ColorRes;

import com.genius.groupie.Item;
import com.genius.groupie.example.MainActivity;
import com.genius.groupie.example.databinding.ItemCardBinding;

import java.util.HashMap;
import java.util.Map;

import static com.genius.groupie.example.MainActivity.*;

public class CardItem extends Item<ItemCardBinding> {

    @ColorRes private int colorRes;
    private CharSequence text;

    public CardItem(@ColorRes int colorRes) {
        this(colorRes, "");
    }

    public CardItem(@ColorRes int colorRes, CharSequence text) {
        this.colorRes = colorRes;
        this.text = text;
    }

    @Override public int getLayout() {
        return com.genius.groupie.example.R.layout.item_card;
    }

    @Override public void bind(ItemCardBinding viewBinding, int position) {
        //viewBinding.getRoot().setBackgroundResource(colorRes);
        viewBinding.text.setText(text);
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    @Override public Map<String, Object> getExtras() {
        Map<String, Object> map = new HashMap<>();
        map.put(INSET_TYPE_KEY, INSET);
        return map;
    }
}

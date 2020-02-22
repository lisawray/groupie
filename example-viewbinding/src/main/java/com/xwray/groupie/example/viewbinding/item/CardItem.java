package com.xwray.groupie.example.viewbinding.item;

import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.xwray.groupie.viewbinding.BindableItem;
import com.xwray.groupie.example.viewbinding.R;
import com.xwray.groupie.example.viewbinding.databinding.ItemCardBinding;

import static com.xwray.groupie.example.viewbinding.MainActivity.INSET;
import static com.xwray.groupie.example.viewbinding.MainActivity.INSET_TYPE_KEY;

public class CardItem extends BindableItem<ItemCardBinding> {

    @ColorInt private int colorRes;
    private CharSequence text;

    public CardItem(@ColorInt int colorRes) {
        this(colorRes, "");
    }

    public CardItem(@ColorInt int colorRes, CharSequence text) {
        this.colorRes = colorRes;
        this.text = text;
        getExtras().put(INSET_TYPE_KEY, INSET);
    }

    @Override public int getLayout() {
        return R.layout.item_card;
    }

    @NonNull
    @Override
    protected ItemCardBinding initializeViewBinding(@NonNull View view) {
        return ItemCardBinding.bind(view);
    }

    @Override public void bind(@NonNull ItemCardBinding viewBinding, int position) {
        //viewBinding.getRoot().setBackgroundColor(colorRes);
        viewBinding.text.setText(text);
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public CharSequence getText() {
        return text;
    }
}

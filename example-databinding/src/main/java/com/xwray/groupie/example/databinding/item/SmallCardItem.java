package com.xwray.groupie.example.databinding.item;

public class SmallCardItem extends CardItem {

    public SmallCardItem() {
        super();
    }

    public SmallCardItem(CharSequence text) {
        super(text);
    }

    @Override public int getSpanSize(int spanCount, int position) {
        return spanCount / 3;
    }
}

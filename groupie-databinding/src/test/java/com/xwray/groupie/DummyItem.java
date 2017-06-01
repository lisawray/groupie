package com.xwray.groupie;

import android.databinding.ViewDataBinding;

import com.xwray.groupie.databinding.Item;

class DummyItem extends Item {

    @Override public int getLayout() {
        return 0;
    }

    @Override public void bind(ViewDataBinding viewBinding, int position) {

    }
}

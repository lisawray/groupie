package com.genius.groupie;

import android.databinding.ViewDataBinding;

class DummyItem extends Item {

    @Override public int getLayout() {
        return 0;
    }

    @Override public void bind(ViewDataBinding viewBinding, int position) {

    }
}

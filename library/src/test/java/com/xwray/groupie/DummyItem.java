package com.xwray.groupie;

import android.view.View;

import androidx.annotation.NonNull;

class DummyItem extends Item {

    DummyItem() {
        super();
    }

    DummyItem(long id) {
        super(id);
    }

    @Override public int getLayout() {
        return 0;
    }

    @NonNull
    @Override
    public GroupieViewHolder createViewHolder(@NonNull View itemView) {
        return null;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {

    }
}

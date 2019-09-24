package com.xwray.groupie;

import android.view.View;

class DummyItem extends Item {

    public DummyItem() {
        super();
    }

    protected DummyItem(long id) {
        super(id);
    }

    @Override public int getLayout() {
        return 0;
    }

    @Override
    public GroupieViewHolder createViewHolder(View itemView) {
        return null;
    }

    @Override
    public void bind(GroupieViewHolder viewHolder, int position) {

    }
}

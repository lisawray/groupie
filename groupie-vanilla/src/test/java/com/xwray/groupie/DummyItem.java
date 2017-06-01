package com.xwray.groupie;

import android.view.View;

class DummyItem extends Item {

    @Override public int getLayout() {
        return 0;
    }

    @Override
    public ViewHolder createViewHolder(View itemView) {
        return null;
    }

    @Override
    public void bind(ViewHolder viewHolder, int position) {

    }
}

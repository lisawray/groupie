package com.xwray.groupie.example.viewholder;

import android.view.View;

import com.xwray.groupie.example.R;
import com.xwray.groupie.example.core.widget.CheckableImageView;

public class HeartCardViewHolder extends CardViewHolder {

    public final CheckableImageView favorite;

    public HeartCardViewHolder(View rootView) {
        super(rootView);
        favorite = (CheckableImageView) rootView.findViewById(R.id.favorite);
    }
}

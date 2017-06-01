package com.xwray.groupie.example.viewholder;

import android.view.View;
import android.widget.TextView;

import com.xwray.groupie.ViewHolder;
import com.xwray.groupie.example.R;

public class CardViewHolder extends ViewHolder {

    public final TextView text;

    public CardViewHolder(View rootView) {
        super(rootView);
        text = (TextView) rootView.findViewById(R.id.text);
    }
}

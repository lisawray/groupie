package com.xwray.groupie.example.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xwray.groupie.ViewHolder;
import com.xwray.groupie.example.R;

public class HeaderViewHolder extends ViewHolder {

    public final TextView title;
    public final TextView subtitle;
    public final ImageView icon;

    public HeaderViewHolder(View rootView) {
        super(rootView);
        title = (TextView) rootView.findViewById(R.id.title);
        subtitle = (TextView) rootView.findViewById(R.id.subtitle);
        icon = (ImageView) rootView.findViewById(R.id.icon);
    }
}

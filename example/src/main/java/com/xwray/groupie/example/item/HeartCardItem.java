package com.xwray.groupie.example.item;

import android.graphics.drawable.Animatable;
import android.support.annotation.ColorInt;
import android.view.View;

import com.xwray.groupie.Item;
import com.xwray.groupie.example.MainActivity;
import com.xwray.groupie.example.R;
import com.xwray.groupie.example.viewholder.HeartCardViewHolder;

import java.util.List;

public class HeartCardItem extends Item<HeartCardViewHolder> {

    public static final String FAVORITE = "FAVORITE";

    @ColorInt private int colorRes;
    private OnFavoriteListener onFavoriteListener;
    private boolean checked = false;
    private boolean inProgress = false;

    public HeartCardItem(@ColorInt int colorRes, long id, OnFavoriteListener onFavoriteListener) {
        super(id);
        this.colorRes = colorRes;
        this.onFavoriteListener = onFavoriteListener;
        getExtras().put(MainActivity.INSET_TYPE_KEY, MainActivity.INSET);
    }

    @Override
    public int getLayout() {
        return R.layout.item_heart_card;
    }


    private void bindHeart(HeartCardViewHolder holder) {
        if (inProgress) {
            animateProgress(holder);
        } else {
            holder.favorite.setImageResource(R.drawable.favorite_state_list);
        }
        holder.favorite.setChecked(checked);
    }

    private void animateProgress(HeartCardViewHolder holder) {
        holder.favorite.setImageResource(R.drawable.avd_favorite_progress);
        ((Animatable) holder.favorite.getDrawable()).start();
    }

    public void setFavorite(boolean favorite) {
        inProgress = false;
        checked = favorite;
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public HeartCardViewHolder createViewHolder(View itemView) {
        return new HeartCardViewHolder(itemView);
    }

    @Override
    public void bind(final HeartCardViewHolder holder, int position) {
        //holder.getRoot().setBackgroundColor(colorRes);
        bindHeart(holder);
        holder.text.setText(String.valueOf(getId() + 1));

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inProgress = true;
                animateProgress(holder);

                onFavoriteListener.onFavorite(HeartCardItem.this, !checked);
            }
        });
    }

    @Override
    public void bind(HeartCardViewHolder holder, int position, List<Object> payloads) {
        if (payloads.contains(FAVORITE)) {
            bindHeart(holder);
        } else {
            bind(holder, position);
        }
    }

    public interface OnFavoriteListener {
        void onFavorite(HeartCardItem item, boolean favorite);
    }
}

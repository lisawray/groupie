package com.xwray.groupie.example.databinding.item;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.view.View;

import com.xwray.groupie.databinding.BindableItem;
import com.xwray.groupie.example.databinding.R;
import com.xwray.groupie.example.databinding.databinding.ItemHeaderBinding;

public class HeaderItem extends BindableItem<ItemHeaderBinding> {

    @StringRes private final int titleStringResId;
    @StringRes private final int subtitleResId;
    @DrawableRes private final int iconResId;
    private final View.OnClickListener onIconClickListener;

    public HeaderItem(@StringRes int titleStringResId) {
        this(titleStringResId, 0);
    }

    public HeaderItem(@StringRes int titleStringResId, @StringRes int subtitleResId) {
        this(titleStringResId, subtitleResId, 0, null);
    }

    public HeaderItem(@StringRes int titleStringResId, @StringRes int subtitleResId, @DrawableRes int iconResId, View.OnClickListener onIconClickListener) {
        this.titleStringResId = titleStringResId;
        this.subtitleResId = subtitleResId;
        this.iconResId = iconResId;
        this.onIconClickListener = onIconClickListener;
    }

    @Override public int getLayout() {
        return R.layout.item_header;
    }

    @Override public void bind(@NonNull ItemHeaderBinding viewBinding, int position) {
        viewBinding.title.setText(titleStringResId);
        if (subtitleResId != 0) {
            viewBinding.subtitle.setText(subtitleResId);
        }
        viewBinding.subtitle.setVisibility(subtitleResId != 0 ? View.VISIBLE : View.GONE);

        if (iconResId != 0) {
            viewBinding.icon.setImageResource(iconResId);
            viewBinding.icon.setOnClickListener(onIconClickListener);
        }
        viewBinding.icon.setVisibility(iconResId != 0 ? View.VISIBLE : View.GONE);
    }
}

package com.xwray.groupie.example;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import com.xwray.groupie.ViewHolder;

public class StickyViewHolder<B extends ViewDataBinding> extends ViewHolder<B> {
  @NonNull private View contentView;

  public StickyViewHolder(B binding, RecyclerView.LayoutManager layoutManager) {
    // Since itemView is declared "final", the split is done before the View is initialized
    super(new FrameLayout(binding.getRoot().getContext()), binding);

    itemView.setLayoutParams(
        layoutManager.generateLayoutParams(binding.getRoot().getLayoutParams()));
    ((FrameLayout) itemView).addView(binding.getRoot()); //Add View after setLayoutParams
    float elevation = ViewCompat.getElevation(binding.getRoot());
    if (elevation > 0) {
      ViewCompat.setBackground(itemView, binding.getRoot().getBackground());
      ViewCompat.setElevation(itemView, elevation);
    }
    contentView = binding.getRoot();
  }

  /**
   * In case this ViewHolder represents a Header Item, this method returns the contentView of the
   * FrameLayout, otherwise it returns the basic itemView.
   *
   * @return the real contentView
   */
  public final View getContentView() {
    return contentView;
  }
}

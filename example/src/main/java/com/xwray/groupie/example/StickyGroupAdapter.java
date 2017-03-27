package com.xwray.groupie.example;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.StickyHeaderHelper;
import com.xwray.groupie.ViewHolder;

public class StickyGroupAdapter extends GroupAdapter {

  private final ArraySet<Integer> stickyHeadersLayouts = new ArraySet<>();
  private RecyclerView recyclerView;
  private StickyHeaderHelper stickyHeaderHelper;

  public StickyGroupAdapter(@LayoutRes int... stickyHeadersLayouts) {
    if (stickyHeadersLayouts != null) {
      for (int stickyHeadersLayout : stickyHeadersLayouts) {
        this.stickyHeadersLayouts.add(stickyHeadersLayout);
      }
    }
  }

  @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    this.recyclerView = recyclerView;
    if (stickyHeaderHelper == null) {
      stickyHeaderHelper = new StickyHeaderHelper(this);
    }
    stickyHeaderHelper.attachToRecyclerView(recyclerView);
  }

  @Override public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    stickyHeaderHelper.detachFromRecyclerView();
    super.onDetachedFromRecyclerView(recyclerView);
    this.recyclerView = null;
  }

  @Override public ViewHolder<? extends ViewDataBinding> onCreateViewHolder(ViewGroup parent,
      int layoutResId) {
    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    final ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutResId, parent, false);
    return stickyHeadersLayouts.contains(layoutResId) ? new StickyViewHolder<>(binding,
        recyclerView.getLayoutManager()) : new ViewHolder<>(binding);
  }

  public boolean isStickyHeader(@LayoutRes int layoutRes) {
    return stickyHeadersLayouts.contains(layoutRes);
  }
}

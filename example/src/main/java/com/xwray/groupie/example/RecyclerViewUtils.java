package com.xwray.groupie.example;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public final class RecyclerViewUtils {

  private RecyclerViewUtils() {
    throw new UnsupportedOperationException("no instances");
  }

  /**
   * Finds the layout orientation of the RecyclerView, no matter which LayoutManager is in use.
   *
   * @param layoutManager the LayoutManager instance in use by the RV
   * @return one of {@link OrientationHelper#HORIZONTAL}, {@link OrientationHelper#VERTICAL}
   */
  public static int getOrientation(RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof LinearLayoutManager) {
      return ((LinearLayoutManager) layoutManager).getOrientation();
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
    }
    return OrientationHelper.HORIZONTAL;
  }

  /**
   * Helper method to retrieve the number of the columns (span count) of the given LayoutManager.
   * <p>All Layouts are supported.</p>
   *
   * @param layoutManager the layout manager to check
   * @return the span count
   */
  public static int getSpanCount(RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof GridLayoutManager) {
      return ((GridLayoutManager) layoutManager).getSpanCount();
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
    }
    return 1;
  }

  /**
   * Helper method to find the adapter position of the <b>first completely</b> visible view
   * [for each span], no matter which Layout is.
   *
   * @param layoutManager the layout manager in use
   * @return the adapter position of the <b>first fully</b> visible item or {@code
   * RecyclerView.NO_POSITION}
   * if there aren't any visible items.
   * @see #findFirstVisibleItemPosition(RecyclerView.LayoutManager)
   */
  public static int findFirstCompletelyVisibleItemPosition(
      RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof StaggeredGridLayoutManager) {
      return ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(
          null)[0];
    } else {
      return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
    }
  }

  /**
   * Helper method to find the adapter position of the <b>first partially</b> visible view
   * [for each span], no matter which Layout is.
   *
   * @param layoutManager the layout manager in use
   * @return the adapter position of the <b>first partially</b> visible item or {@code
   * RecyclerView.NO_POSITION}
   * if there aren't any visible items.
   * @see #findFirstCompletelyVisibleItemPosition(RecyclerView.LayoutManager)
   */
  public static int findFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof StaggeredGridLayoutManager) {
      return ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null)[0];
    } else {
      return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
    }
  }

  /**
   * Helper method to find the adapter position of the <b>last completely</b> visible view
   * [for each span], no matter which Layout is.
   *
   * @param layoutManager the layout manager in use
   * @return the adapter position of the <b>last fully</b> visible item or {@code
   * RecyclerView.NO_POSITION}
   * if there aren't any visible items.
   * @see #findLastVisibleItemPosition(RecyclerView.LayoutManager)
   */
  public static int findLastCompletelyVisibleItemPosition(
      RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof StaggeredGridLayoutManager) {
      return ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(
          null)[0];
    } else {
      return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
    }
  }

  /**
   * Helper method to find the adapter position of the <b>last partially</b> visible view
   * [for each span], no matter which Layout is.
   *
   * @param layoutManager the layout manager in use
   * @return the adapter position of the <b>last partially</b> visible item or {@code
   * RecyclerView.NO_POSITION}
   * if there aren't any visible items.
   * @see #findLastCompletelyVisibleItemPosition(RecyclerView.LayoutManager)
   */
  public static int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof StaggeredGridLayoutManager) {
      return ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null)[0];
    } else {
      return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }
  }
}
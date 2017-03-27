package com.xwray.groupie;

import android.animation.Animator;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import com.xwray.groupie.example.StickyGroupAdapter;
import com.xwray.groupie.example.StickyViewHolder;

import static com.xwray.groupie.example.RecyclerViewUtils.findFirstVisibleItemPosition;
import static com.xwray.groupie.example.RecyclerViewUtils.getOrientation;

public final class StickyHeaderHelper extends OnScrollListener {

  private static final String TAG = StickyHeaderHelper.class.getSimpleName();
  public static boolean DEBUG = false;

  private StickyGroupAdapter mAdapter;
  private RecyclerView mRecyclerView;
  private ViewGroup mStickyHolderLayout;
  private StickyViewHolder mStickyHeaderViewHolder;
  private int mHeaderPosition = RecyclerView.NO_POSITION;
  private boolean displayWithAnimation = false;
  private float mElevation;

  public StickyHeaderHelper(StickyGroupAdapter adapter) {
    mAdapter = adapter;
  }

  @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    displayWithAnimation = mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE;
    updateOrClearHeader();
  }

  public void attachToRecyclerView(RecyclerView parent) {
    if (mRecyclerView != null) {
      mRecyclerView.removeOnScrollListener(this);
      clearHeader();
    }
    if (parent == null) {
      throw new IllegalStateException(
          "Adapter is not attached to RecyclerView. Enable sticky headers after setting adapter to RecyclerView.");
    }
    mRecyclerView = parent;
    mRecyclerView.addOnScrollListener(this);
    initStickyHeadersHolder();
  }

  public void detachFromRecyclerView() {
    mRecyclerView.removeOnScrollListener(this);
    mRecyclerView = null;
    clearHeaderWithAnimation();
    if (DEBUG) Log.i(TAG, "StickyHolderLayout detached");
  }

  private FrameLayout createContainer(int width, int height) {
    FrameLayout frameLayout = new FrameLayout(mRecyclerView.getContext());
    frameLayout.setLayoutParams(new ViewGroup.LayoutParams(width, height));
    return frameLayout;
  }

  private ViewGroup getParent(View view) {
    return (ViewGroup) view.getParent();
  }

  private void initStickyHeadersHolder() {
    if (mStickyHolderLayout == null) {
      // Create stickyContainer for shadow elevation
      FrameLayout stickyContainer =
          createContainer(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      ViewGroup oldParentLayout = getParent(mRecyclerView);
      if (oldParentLayout == null) {
        return;
      }

      oldParentLayout.addView(stickyContainer);
      // Initialize Holder Layout
      mStickyHolderLayout = (ViewGroup) LayoutInflater.from(mRecyclerView.getContext())
          .inflate(com.xwray.groupie.example.R.layout.sticky_header_layout, stickyContainer);
      if (DEBUG) Log.i(TAG, "Default StickyHolderLayout initialized");
    } else if (DEBUG) {
      Log.i(TAG, "User defined StickyHolderLayout initialized");
    }
    // Show sticky header if exists already
    updateOrClearHeader();
  }

  public int getStickyPosition() {
    return mHeaderPosition;
  }

  private boolean hasStickyHeaderTranslated(int position) {
    RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForAdapterPosition(position);
    return vh != null && (vh.itemView.getX() < 0 || vh.itemView.getY() < 0);
  }

  public void updateOrClearHeader() {
    if (mAdapter.getItemCount() == 0) {
      clearHeaderWithAnimation();
      return;
    }
    int firstHeaderPosition = getStickyPosition(RecyclerView.NO_POSITION);
    Log.i(TAG, "sticky position is " + firstHeaderPosition);
    if (firstHeaderPosition >= 0) {
      updateHeader(firstHeaderPosition);
    } else {
      clearHeader();
    }
  }

  private void updateHeader(int headerPosition) {
    // Check if there is a new header to be sticky
    if (mHeaderPosition != headerPosition) {
      // #244 - Don't animate if header is already visible at the first layout position
      int firstVisibleItemPosition = findFirstVisibleItemPosition(mRecyclerView.getLayoutManager());
      // Animate if headers were hidden, but don't if configuration changed (rotation)
      if (displayWithAnimation
          && mHeaderPosition == RecyclerView.NO_POSITION
          && headerPosition != firstVisibleItemPosition) {
        displayWithAnimation = false;
        mStickyHolderLayout.setAlpha(0);
        mStickyHolderLayout.animate().alpha(1).start();
      } else {
        mStickyHolderLayout.setAlpha(1);
      }
      mHeaderPosition = headerPosition;
      StickyViewHolder holder = getHeaderViewHolder(headerPosition);
      if (DEBUG) Log.d(TAG, "swapHeader newHeaderPosition=" + mHeaderPosition);
      swapHeader(holder);
    }
    translateHeader();
  }

  private void configureLayoutElevation() {
    // 1. Take elevation from header item layout (most important)
    mElevation = ViewCompat.getElevation(mStickyHeaderViewHolder.getContentView());
    if (mElevation == 0f) {
      // 2. Take elevation settings
      mElevation = 20f;//mAdapter.getStickyHeaderElevation();
    }
    if (mElevation > 0) {
      // Needed to elevate the view
      ViewCompat.setBackground(mStickyHolderLayout,
          mStickyHeaderViewHolder.getContentView().getBackground());
    }
  }

  private void translateHeader() {
    // Sticky at zero offset (no translation)
    int headerOffsetX = 0, headerOffsetY = 0;
    // Get calculated elevation
    float elevation = mElevation;

    // Search for the position where the next header item is found and translate the new offset
    for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
      final View nextChild = mRecyclerView.getChildAt(i);
      if (nextChild != null) {
        int adapterPos = mRecyclerView.getChildAdapterPosition(nextChild);
        int nextHeaderPosition = getStickyPosition(adapterPos);
        Log.i(TAG, "next header position is " + nextHeaderPosition);
        if (mHeaderPosition != nextHeaderPosition) {
          if (getOrientation(mRecyclerView.getLayoutManager()) == OrientationHelper.HORIZONTAL) {
            if (nextChild.getLeft() > 0) {
              int headerWidth = mStickyHolderLayout.getMeasuredWidth();
              int nextHeaderOffsetX = nextChild.getLeft() - headerWidth;
              headerOffsetX = Math.min(nextHeaderOffsetX, 0);
              // Early remove the elevation/shadow to match with the next view
              if (nextHeaderOffsetX < 5) elevation = 0f;
              if (headerOffsetX < 0) break;
            }
          } else {
            if (nextChild.getTop() > 0) {
              int headerHeight = mStickyHolderLayout.getMeasuredHeight();
              int nextHeaderOffsetY = nextChild.getTop() - headerHeight;
              headerOffsetY = Math.min(nextHeaderOffsetY, 0);
              // Early remove the elevation/shadow to match with the next view
              if (nextHeaderOffsetY < 5) elevation = 0f;
              if (headerOffsetY < 0) break;
            }
          }
        }
      }
    }
    // Apply the user elevation to the sticky container
    ViewCompat.setElevation(mStickyHolderLayout, elevation);
    // Apply translation (pushed up by another header)
    mStickyHolderLayout.setTranslationX(headerOffsetX);
    mStickyHolderLayout.setTranslationY(headerOffsetY);
    //Log.v(TAG, "TranslationX=" + headerOffsetX + " TranslationY=" + headerOffsetY);
  }

  private void swapHeader(StickyViewHolder newHeader) {
    if (mStickyHeaderViewHolder != null) {
      resetHeader(mStickyHeaderViewHolder);
    }
    mStickyHeaderViewHolder = newHeader;
    if (mStickyHeaderViewHolder != null) {
      mStickyHeaderViewHolder.setIsRecyclable(false);
      ensureHeaderParent();
    }
  }

  private void ensureHeaderParent() {
    final View view = mStickyHeaderViewHolder.getContentView();
    // #121 - Make sure the measured height (width for horizontal layout) is kept if
    // WRAP_CONTENT has been set for the Header View
    mStickyHeaderViewHolder.itemView.getLayoutParams().width = view.getMeasuredWidth();
    mStickyHeaderViewHolder.itemView.getLayoutParams().height = view.getMeasuredHeight();
    // Ensure the itemView is hidden to avoid double background
    //mStickyHeaderViewHolder.itemView.setVisibility(View.INVISIBLE);
    // #139 - Copy xml params instead of Measured params
    ViewGroup.LayoutParams params = mStickyHolderLayout.getLayoutParams();
    params.width = view.getLayoutParams().width;
    params.height = view.getLayoutParams().height;
    removeViewFromParent(view);
    mStickyHolderLayout.addView(view);
    configureLayoutElevation();
  }

  private void resetHeader(StickyViewHolder header) {
    // Clean the header container
    final View view = header.getContentView();
    removeViewFromParent(view);
    // Reset translation on removed header
    view.setTranslationX(0);
    view.setTranslationY(0);
    if (!header.itemView.equals(view)) ((ViewGroup) header.itemView).addView(view);
    header.setIsRecyclable(true);
    // #294 - Expandable header is not resized / redrawn on automatic configuration change when sticky headers are enabled
    header.itemView.getLayoutParams().width = view.getLayoutParams().width;
    header.itemView.getLayoutParams().height = view.getLayoutParams().height;
  }

  private void clearHeader() {
    if (mStickyHeaderViewHolder != null) {
      if (DEBUG) Log.d(TAG, "clearHeader");
      resetHeader(mStickyHeaderViewHolder);
      mStickyHolderLayout.setAlpha(0);
      mStickyHolderLayout.animate().cancel();
      mStickyHolderLayout.animate().setListener(null);
      mStickyHeaderViewHolder = null;
      mHeaderPosition = RecyclerView.NO_POSITION;
    }
  }

  public void clearHeaderWithAnimation() {
    if (mStickyHeaderViewHolder != null && mHeaderPosition != RecyclerView.NO_POSITION) {
      mStickyHolderLayout.animate().setListener(new Animator.AnimatorListener() {
        @Override public void onAnimationStart(Animator animation) {
          mHeaderPosition = RecyclerView.NO_POSITION;
        }

        @Override public void onAnimationEnd(Animator animation) {
          displayWithAnimation = true; //This helps after clearing filter
          mStickyHolderLayout.setAlpha(0);
          clearHeader();
        }

        @Override public void onAnimationCancel(Animator animation) {
        }

        @Override public void onAnimationRepeat(Animator animation) {
        }
      });
      mStickyHolderLayout.animate().alpha(0).start();
    }
  }

  private static void removeViewFromParent(final View view) {
    final ViewParent parent = view.getParent();
    if (parent instanceof ViewGroup) {
      ((ViewGroup) parent).removeView(view);
    }
  }

  @SuppressWarnings("unchecked") private int getStickyPosition(int adapterPosHere) {
    if (adapterPosHere == RecyclerView.NO_POSITION) {
      adapterPosHere = findFirstVisibleItemPosition(mRecyclerView.getLayoutManager());
      if (adapterPosHere == 0 && !hasStickyHeaderTranslated(0)) {
        return RecyclerView.NO_POSITION;
      }
    }
    Item header = getHeader(adapterPosHere);
    // Header cannot be sticky if it's also an Expandable in collapsed status, RV will raise an exception
    if (header == null || isCollapsed(header)) {
      return RecyclerView.NO_POSITION;
    }
    if (DEBUG) {
      Log.i(TAG, String.format("getStickyPosition(%d) - %s", adapterPosHere, header.toString()));
    }
    return mAdapter.getAdapterPosition(header);
  }

  private Item getHeader(int position) {
    final Item item = mAdapter.getItem(position);
    final Group group = mAdapter.getGroup(item);

    if (group instanceof Section) {
      final Section section = (Section) group;
      return getSectionHeader(section);
    }

    if (group instanceof ExpandableGroup && !isCollapsed(group)) {
      final ExpandableGroup expandableGroup = (ExpandableGroup) group;
      return getExpandableGroupHeader(expandableGroup);
    }

    return null;
  }

  @Nullable private Item getExpandableGroupHeader(ExpandableGroup expandableGroup) {
    if (!(getHeader(expandableGroup) instanceof Item)) {
      return null;
    }

    return (Item) getHeader(expandableGroup);
  }

  @Nullable private Item getSectionHeader(Section section) {
    if (!section.isHeaderShown() || section.isEmpty()) {
      return null;
    }

    if (!(getHeader(section) instanceof Item)) {
      return null;
    }

    return (Item) getHeader(section);
  }

  private Group getHeader(ExpandableGroup expandableGroup) {
    return expandableGroup.getGroup(0);
  }

  private Group getHeader(Section section) {
    return section.getGroup(0);
  }

  private boolean isCollapsed(Group header) {
    if (!(header instanceof ExpandableGroup)) {
      return false;
    }

    final ExpandableGroup expandable = (ExpandableGroup) header;
    return !expandable.isExpanded();
  }

  /**
   * Gets the header view for the associated header position. If it doesn't exist yet, it will
   * be created, measured, and laid out.
   *
   * @param position the adapter position to get the header view
   * @return ViewHolder of type ViewHolder of the associated header position
   */
  @SuppressWarnings("unchecked") private StickyViewHolder getHeaderViewHolder(int position) {
    // Find existing ViewHolder
    StickyViewHolder holder =
        (StickyViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
    if (holder == null) {
      // Create and binds a new ViewHolder
      final int itemViewType = mAdapter.getItemViewType(position);
      if (!mAdapter.isStickyHeader(itemViewType)) {
        return null;
      }
      holder = (StickyViewHolder) mAdapter.createViewHolder(mRecyclerView, itemViewType);
      mAdapter.bindViewHolder(holder, position);

      // Calculate width and height
      int widthSpec;
      int heightSpec;
      if (getOrientation(mRecyclerView.getLayoutManager()) == OrientationHelper.VERTICAL) {
        widthSpec =
            View.MeasureSpec.makeMeasureSpec(mRecyclerView.getWidth(), View.MeasureSpec.EXACTLY);
        heightSpec = View.MeasureSpec.makeMeasureSpec(mRecyclerView.getHeight(),
            View.MeasureSpec.UNSPECIFIED);
      } else {
        widthSpec = View.MeasureSpec.makeMeasureSpec(mRecyclerView.getWidth(),
            View.MeasureSpec.UNSPECIFIED);
        heightSpec =
            View.MeasureSpec.makeMeasureSpec(mRecyclerView.getHeight(), View.MeasureSpec.EXACTLY);
      }

      // Measure and Layout the stickyView
      final View headerView = holder.getContentView();
      int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
          mRecyclerView.getPaddingLeft() + mRecyclerView.getPaddingRight(),
          headerView.getLayoutParams().width);
      int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
          mRecyclerView.getPaddingTop() + mRecyclerView.getPaddingBottom(),
          headerView.getLayoutParams().height);

      headerView.measure(childWidth, childHeight);
      headerView.layout(0, 0, headerView.getMeasuredWidth(), headerView.getMeasuredHeight());
    }
    return holder;
  }
}
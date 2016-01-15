package com.everseat.paginaterecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * A RecyclerView that can paginate its content
 */
public class PaginateRecyclerView extends RecyclerView implements GestureDetector.OnGestureListener {
  private int orientation;

  // View configs
  private int touchSlop;
  private int flingSlop;

  private int paginateItemCount = -1;
  private int lastScrollPosition;
  private PaginateLayoutManager layoutManager;
  private GestureDetector gestureDetector;

  private static final String TAG = "PaginateRecyclerView";

  public PaginateRecyclerView(Context context) {
    super(context);
    init(context);
  }

  public PaginateRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PaginateRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private void init(Context context) {
    ViewConfiguration vc = ViewConfiguration.get(context);
    flingSlop = 1000;
    gestureDetector = new GestureDetector(context, this);
    touchSlop = vc.getScaledTouchSlop();
  }

  /**
   * Sets the number of items to page by. By default, we paginate a page at a time
   * based on the first N visible items in the RecyclerView.
   * @param paginateItemCount The number of items to page by
   */
  public void setPaginateItemCount(int paginateItemCount) {
    this.paginateItemCount = paginateItemCount;
  }

  /**
   * Sets the orientation of the LayoutManager
   * @param orientation Orientation of the LayoutManager
   */
  public void setOrientation(int orientation) {
    this.orientation = orientation;
    setLayoutManager(new PaginateLayoutManager(getContext(), orientation, false));
  }

  public int getOrientation() {
    return orientation;
  }

  @Override
  public void setAdapter(Adapter adapter) {
    lastScrollPosition = 0;
    super.setAdapter(adapter);
  }

  @Override
  public void setLayoutManager(LayoutManager layout) {
    if (!(layout instanceof PaginateLayoutManager)) {
      throw new IllegalArgumentException("PaginateRecyclerView needs a PaginateLayoutManager!");
    }
    layoutManager = (PaginateLayoutManager) layout;
    super.setLayoutManager(layout);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent e) {
    getParent().requestDisallowInterceptTouchEvent(true);
    return super.onInterceptTouchEvent(e);
  }

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    gestureDetector.onTouchEvent(e);
    int action = MotionEventCompat.getActionMasked(e);
    switch (action) {
      case MotionEvent.ACTION_UP:
        smoothScrollToPosition(lastScrollPosition);
        break;
    }
    return true;
  }

  /////////////////////////////////////////////////////////////////////////////
  // Public API
  /////////////////////////////////////////////////////////////////////////////

  public void centerItem(int position) {
    int orientation = getOrientation();
    ViewHolder viewHolder = findViewHolderForAdapterPosition(position);
    View itemView = viewHolder.itemView;

    if (orientation == HORIZONTAL) {
      centerItemHorizontally(itemView);
    } else {
      centerItemVertically(itemView);
    }
  }

  /////////////////////////////////////////////////////////////////////////////
  // Helper methods
  /////////////////////////////////////////////////////////////////////////////

  private void centerItemHorizontally(View itemView) {
    int width = getWidth();
    int x = itemView.getLeft() - ((width / 2) - (itemView.getWidth() / 2));
    smoothScrollBy(x, 0);
  }

  private void centerItemVertically(View itemView) {
    int height = getHeight();
    int y = itemView.getTop() - ((height / 2) - (itemView.getHeight() / 2));
    smoothScrollBy(0, y);
  }

  /////////////////////////////////////////////////////////////////////////////
  // GestureDetector.OnGestureListener methods
  /////////////////////////////////////////////////////////////////////////////

  @Override
  public boolean onDown(MotionEvent e) {
    return false;
  }

  @Override
  public void onShowPress(MotionEvent e) {/* No op */}

  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    return false;
  }

  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    boolean canScrollHorizontally = getLayoutManager().canScrollHorizontally();
    boolean canScrollVertically = getLayoutManager().canScrollVertically();

    int dX = (int) distanceX;
    int dY = (int) distanceY;

    if (canScrollHorizontally) {
      scrollBy(dX, 0);
    }

    if (canScrollVertically) {
      scrollBy(0, dY);
    }
    return false;
  }

  @Override
  public void onLongPress(MotionEvent e) {/* No op */}

  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    int skipCount;

    boolean canScrollHorizontally = getLayoutManager().canScrollHorizontally();
    boolean canScrollVertically = getLayoutManager().canScrollVertically();

    int direction = 0;
    if (canScrollHorizontally && Math.abs(velocityX) > flingSlop) {
      direction = velocityX < 0 ? 1 : -1;
    }
    if (canScrollVertically && Math.abs(velocityY) > flingSlop) {
      direction = velocityY < 0 ? 1 : -1;
    }

    if (paginateItemCount == -1) {
      int averageChildSize = layoutManager.getAverageChildSize();
      int size = canScrollHorizontally ? getWidth() : getHeight();
      skipCount = (size / averageChildSize) * direction;
    } else {
      skipCount = paginateItemCount * direction;
    }

    int scrollToPosition = lastScrollPosition + skipCount;
    smoothScrollToPosition(scrollToPosition);
    lastScrollPosition = scrollToPosition < 0 ? 0 : scrollToPosition;
    return false;
  }
}

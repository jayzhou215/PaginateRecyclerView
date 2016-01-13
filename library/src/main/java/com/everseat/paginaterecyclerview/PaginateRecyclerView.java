package com.everseat.paginaterecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * A RecyclerView that can paginate its content
 */
public class PaginateRecyclerView extends RecyclerView {
  private int initialTouchX, initialTouchY;
  private int lastTouchX, lastTouchY;
  private int scrollPointerId;
  private int orientation;

  // View configs
  private int maxFlingVelocity;
  private int velocitySlop;
  private int touchSlop;

  private int paginateItemCount = -1;
  private int lastScrollPosition;
  private VelocityTracker velocityTracker;
  private boolean shouldPage;
  private PaginateLayoutManager layoutManager;

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
    velocityTracker = VelocityTracker.obtain();
    maxFlingVelocity = getMaxFlingVelocity();
    velocitySlop = 200;
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
    initialTouchX = lastTouchX = (int) e.getX();
    initialTouchY = lastTouchY = (int) e.getY();
    return super.onInterceptTouchEvent(e);
  }

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    final int action = MotionEventCompat.getActionMasked(e);

    final boolean canScrollHorizontally = getLayoutManager().canScrollHorizontally();
    final boolean canScrollVertically = getLayoutManager().canScrollVertically();

    switch (action) {
      case MotionEvent.ACTION_DOWN: {
        shouldPage = false;
        initialTouchX = lastTouchX = (int) e.getX();
        initialTouchY = lastTouchY = (int) e.getY();
        scrollPointerId = MotionEventCompat.getPointerId(e, 0);
      } break;
      case MotionEvent.ACTION_MOVE: {
        velocityTracker.addMovement(e);

        // Get pointer index
        final int index = MotionEventCompat.findPointerIndex(e, scrollPointerId);
        if (index < 0) {
          Log.e(TAG, "Error processing scroll; pointer index for id " +
              scrollPointerId + " not found. Did any MotionEvents get skipped?");
          return false;
        }

        final int x = (int) MotionEventCompat.getX(e, index);
        final int y = (int) MotionEventCompat.getY(e, index);
        final int dx = lastTouchX - x;
        final int dy = lastTouchY - y;

        velocityTracker.computeCurrentVelocity(1000, maxFlingVelocity);
        float xVeclocity = VelocityTrackerCompat.getXVelocity(velocityTracker, scrollPointerId);
        float yVelocity = VelocityTrackerCompat.getYVelocity(velocityTracker, scrollPointerId);

        if (canScrollHorizontally && Math.abs(dx) > touchSlop) {
          scrollBy(dx, 0);
          shouldPage = Math.abs(xVeclocity) > velocitySlop;
          lastTouchX = x;
        }

        if (canScrollVertically && Math.abs(dy) > touchSlop) {
          scrollBy(0, dy);
          shouldPage = Math.abs(yVelocity) > velocitySlop;
          lastTouchY = y;
        }
      } break;
      case MotionEvent.ACTION_UP: {
        // Get pointer index
        final int index = MotionEventCompat.findPointerIndex(e, scrollPointerId);
        if (index < 0) {
          Log.e(TAG, "Error processing scroll; pointer index for id " +
              scrollPointerId + " not found. Did any MotionEvents get skipped?");
          return false;
        }

        final int x = (int) MotionEventCompat.getX(e, index);
        final int y = (int) MotionEventCompat.getY(e, index);
        int dx = initialTouchX - x;
        int dy = initialTouchY - y;

        int direction = 0;
        if (canScrollHorizontally && Math.abs(dx) > touchSlop) {
          direction = dx / Math.abs(dx);
        }
        if (canScrollVertically && Math.abs(dy) > touchSlop) {
          direction = dy / Math.abs(dy);
        }

        int scrollToPosition;
        if (shouldPage) {
          int skipCount;
          if (paginateItemCount == -1) {
            int averageChildSize = layoutManager.getAverageChildSize();
            skipCount = (getWidth() / averageChildSize) * direction;
          } else {
            skipCount = paginateItemCount * direction;
          }
          scrollToPosition = (lastScrollPosition + skipCount);
        } else {
          scrollToPosition = lastScrollPosition;
        }

        smoothScrollToPosition(scrollToPosition);
        lastScrollPosition = scrollToPosition;
      } break;
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
}

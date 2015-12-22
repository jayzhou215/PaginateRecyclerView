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
import android.view.ViewConfiguration;

/**
 * A RecyclerView that can paginate its content
 */
public class PaginateRecyclerView extends RecyclerView {
  private int initialTouchX, initialTouchY;
  private int lastTouchX, lastTouchY;
  private int scrollPointerId;

  // View configs
  private int touchSlop;
  private int maxFlingVelocity;
  private int velocitySlop;

  private int paginateItemCount = 3;
  private int lastScrollPosition = 0;
  private VelocityTracker velocityTracker;
  private boolean shouldPage;

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
    ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
    velocityTracker = VelocityTracker.obtain();
    touchSlop = viewConfiguration.getScaledTouchSlop();
    maxFlingVelocity = getMaxFlingVelocity();
    velocitySlop = 200;
  }

  /**
   * Sets the number of items to page by. Default is 3.
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
    setLayoutManager(new PaginateLayoutManager(getContext(), orientation, false));
  }

  @Override
  public void setLayoutManager(LayoutManager layout) {
    if (!(layout instanceof PaginateLayoutManager)) {
      throw new IllegalArgumentException("PaginateRecyclerView needs a PaginateLayoutManager!");
    }
    super.setLayoutManager(layout);
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
          shouldPage = Math.abs(xVeclocity) > velocitySlop;
          scrollBy(dx, 0);
          lastTouchX = x;
        }

        if (canScrollVertically && Math.abs(dy) > touchSlop) {
          shouldPage = Math.abs(yVelocity) > velocitySlop;
          scrollBy(0, dy);
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
        if (canScrollHorizontally) {
          direction = dx / Math.abs(dx);
        }
        if (canScrollVertically) {
          direction = dy / Math.abs(dy);
        }

        int scrollToPosition = lastScrollPosition;

        if (shouldPage) {
          scrollToPosition = (lastScrollPosition + (direction * paginateItemCount));
        }

        smoothScrollToPosition(scrollToPosition);
        lastScrollPosition = scrollToPosition;
      } break;
    }
    return true;
  }
}

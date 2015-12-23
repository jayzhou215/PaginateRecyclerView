package com.everseat.paginaterecyclerview;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PaginateLayoutManager extends LinearLayoutManager {
  private int averageChildSize;

  public PaginateLayoutManager(Context context, int orientation, boolean reverseLayout) {
    super(context, orientation, reverseLayout);
  }

  @Override
  public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
    LinearSmoothScroller linearSmoothScroller =
        new PaginateSmoothScroller(recyclerView.getContext()) {
          @Override
          public PointF computeScrollVectorForPosition(int targetPosition) {
            return PaginateLayoutManager.this
                .computeScrollVectorForPosition(targetPosition);
          }
        };
    linearSmoothScroller.setTargetPosition(position);
    startSmoothScroll(linearSmoothScroller);
  }

  @Override
  public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
    super.onLayoutChildren(recycler, state);
    int size = getChildCount();
    if (size > 0) {
      final boolean canScrollHorizontally = canScrollHorizontally();
      final boolean canScrollVertically = canScrollVertically();
      int totalSize = 0;

      for (int i = 0; i < size; i++) {
        View view = getChildAt(i);
        if (canScrollHorizontally) {
          totalSize += view.getWidth();
        }
        if (canScrollVertically) {
          totalSize += view.getHeight();
        }
      }

      averageChildSize = totalSize / size;
    }
  }

  public int getAverageChildSize() {
    return averageChildSize;
  }
}

package com.everseat.paginaterecyclerview;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;

public class PaginateLayoutManager extends LinearLayoutManager {
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
}

package com.everseat.paginaterecyclerview;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;
import android.util.DisplayMetrics;

public abstract class PaginateSmoothScroller extends LinearSmoothScroller {
  public PaginateSmoothScroller(Context context) {
    super(context);
  }

  @Override
  protected int getVerticalSnapPreference() {
    return SNAP_TO_START;
  }

  @Override
  protected int getHorizontalSnapPreference() {
    return SNAP_TO_START;
  }

  @Override
  protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
    return 80f / displayMetrics.densityDpi;
  }
}

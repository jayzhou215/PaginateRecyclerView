package com.everseat.paginaterecyclerview.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CheeseNameHorizontalAdapter extends CheeseNameAdapter {
  public CheeseNameHorizontalAdapter(List<String> cheeseNames) {
    super(cheeseNames);
  }

  @Override
  public CheeseNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.row_main_horizontal, parent, false);
    return new CheeseNameViewHolder(itemView);
  }
}

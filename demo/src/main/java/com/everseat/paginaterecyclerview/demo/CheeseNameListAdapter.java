package com.everseat.paginaterecyclerview.demo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CheeseNameListAdapter extends RecyclerView.Adapter<CheeseNameViewHolder> {
  private List<String> cheeseNames;

  public CheeseNameListAdapter(List<String> cheeseNames) {
    this.cheeseNames = cheeseNames;
  }

  @Override
  public CheeseNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.row_main, parent, false);
    return new CheeseNameViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(CheeseNameViewHolder holder, int position) {
    String name = cheeseNames.get(position);
    holder.descriptionTextView.setText(name);
  }

  @Override
  public int getItemCount() {
    return cheeseNames.size();
  }
}

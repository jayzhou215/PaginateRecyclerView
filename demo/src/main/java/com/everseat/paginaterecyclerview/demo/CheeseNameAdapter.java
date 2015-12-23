package com.everseat.paginaterecyclerview.demo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public abstract class CheeseNameAdapter extends RecyclerView.Adapter<CheeseNameViewHolder> {
  private List<String> cheeseNames;

  public CheeseNameAdapter(List<String> cheeseNames) {
    this.cheeseNames = cheeseNames;
  }

  @Override
  public void onBindViewHolder(CheeseNameViewHolder holder, int position) {
    String name = cheeseNames.get(position);
    holder.descriptionTextView.setText(name);
    if (position % 2 == 0) {
      holder.itemView.setBackgroundColor(Color.parseColor("#FC9F9F"));
    } else {
      holder.itemView.setBackgroundColor(Color.parseColor("#9FDCFC"));
    }
  }

  @Override
  public int getItemCount() {
    return cheeseNames.size();
  }
}

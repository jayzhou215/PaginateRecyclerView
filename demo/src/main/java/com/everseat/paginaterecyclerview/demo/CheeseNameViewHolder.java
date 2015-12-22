package com.everseat.paginaterecyclerview.demo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class CheeseNameViewHolder extends RecyclerView.ViewHolder {
  public final TextView descriptionTextView;
  public CheeseNameViewHolder(View itemView) {
    super(itemView);
    descriptionTextView = (TextView) itemView.findViewById(R.id.name);
  }
}

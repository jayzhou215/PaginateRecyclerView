package com.everseat.paginaterecyclerview.demo;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public abstract class CheeseNameAdapter extends RecyclerView.Adapter<CheeseNameViewHolder> {
  public interface ItemClickListener {
    void onItemClicked(String name, int position);
  }

  private final List<String> cheeseNames;
  @Nullable private ItemClickListener itemClickListener;

  public CheeseNameAdapter(List<String> cheeseNames) {
    this.cheeseNames = cheeseNames;
  }

  @Override
  public void onBindViewHolder(CheeseNameViewHolder holder, final int position) {
    final String name = cheeseNames.get(position);
    holder.descriptionTextView.setText(name);
    if (position % 2 == 0) {
      holder.itemView.setBackgroundColor(Color.parseColor("#FC9F9F"));
    } else {
      holder.itemView.setBackgroundColor(Color.parseColor("#9FDCFC"));
    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (itemClickListener != null) {
          itemClickListener.onItemClicked(name, position);
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return cheeseNames.size();
  }

  public void setItemClickListener(@Nullable ItemClickListener listener) {
    itemClickListener = listener;
  }
}

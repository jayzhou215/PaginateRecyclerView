package com.everseat.paginaterecyclerview.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.everseat.paginaterecyclerview.PaginateRecyclerView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    PaginateRecyclerView recyclerView = (PaginateRecyclerView) findViewById(R.id.recycler_view);
    recyclerView.setOrientation(RecyclerView.VERTICAL);
    recyclerView.setPaginateItemCount(10);

    CheeseNameListAdapter adapter = new CheeseNameListAdapter(Cheeses.asList());
    recyclerView.setAdapter(adapter);
  }
}

package com.everseat.paginaterecyclerview.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.everseat.paginaterecyclerview.PaginateRecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CheeseNameAdapter.ItemClickListener {
  private CheeseNameAdapter adapter;
  private PaginateRecyclerView recyclerView;
  private List<String> cheeses = Cheeses.asList();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerView = (PaginateRecyclerView) findViewById(R.id.recycler_view);
    recyclerView.setOrientation(RecyclerView.VERTICAL);

    adapter = new CheeseNameVerticalAdapter(cheeses);
    adapter.setItemClickListener(this);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.toggle) {
      switch (recyclerView.getOrientation()) {
        case RecyclerView.VERTICAL:
          item.setTitle("Vertical");
          item.setIcon(R.drawable.ic_view_stream);
          adapter = new CheeseNameHorizontalAdapter(cheeses);
          adapter.setItemClickListener(this);
          recyclerView.setOrientation(RecyclerView.HORIZONTAL);
          recyclerView.setAdapter(adapter);
          return true;
        case RecyclerView.HORIZONTAL:
          item.setTitle("Horizontal");
          item.setIcon(R.drawable.ic_view_column);
          adapter = new CheeseNameVerticalAdapter(cheeses);
          adapter.setItemClickListener(this);
          recyclerView.setOrientation(RecyclerView.VERTICAL);
          recyclerView.setAdapter(adapter);
          return true;
      }
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onItemClicked(String name, int position) {
    Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    recyclerView.centerItem(position);
  }
}

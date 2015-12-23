# PaginateRecyclerView
A RecyclerView that can paginate it's content.

![demo](http://i.giphy.com/JDBXYNEVWgkOk.gif)

# Usage
Simply include in your layout like so...

```xml
<com.everseat.paginaterecyclerview.PaginateRecyclerView
  android:id="@+id/recycler_view"
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"/>
```

Then, set it up in code...

```java
 PaginateRecyclerView recyclerView = (PaginateRecyclerView) findViewById(R.id.recycler_view);
 recyclerView.setOrientation(RecyclerView.VERTICAL); // A convienence method to setting a LayoutManager
 recyclerView.setAdapter(new MyCoolAdapter());
```
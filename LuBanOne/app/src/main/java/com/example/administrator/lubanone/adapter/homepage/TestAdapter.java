package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import com.example.administrator.lubanone.R;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import java.util.List;

/**
 * Created by admistrator on 2017/7/21.
 */

public class TestAdapter extends CustomAdapter<String> {

  public TestAdapter(Context context, List<String> list) {
    super(context, list);
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.test_layout;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, String item, int position) {
    Button view = viewHolder.getView(R.id.btn);
    view.setText(item);

  }
}

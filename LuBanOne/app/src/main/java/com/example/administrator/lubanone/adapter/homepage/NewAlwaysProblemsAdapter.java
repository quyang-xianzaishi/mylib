package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.AlwaysProblesResulBean.ResultBean;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.DebugUtils;
import java.util.List;

/**
 * Created by admistrator on 2017/8/18.
 */

public class NewAlwaysProblemsAdapter extends CustomAdapter<ResultBean> {

  public NewAlwaysProblemsAdapter(Context context, List<ResultBean> list) {
    super(context, list);
  }


  @Override
  public int getListViewLayoutId() {
    return R.layout.new_always_problems_item;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, ResultBean item, int position) {
    DebugUtils.setStringValue(item.getTitle(), "", (TextView) viewHolder.getView(R.id.tv_title));
  }
}

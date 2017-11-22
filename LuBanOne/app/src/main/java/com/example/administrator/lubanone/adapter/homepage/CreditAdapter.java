package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.CreditListResultBean.CreditDetailsBean;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.DebugUtils;
import java.util.List;

/**
 * Created by quyang on 2017/7/3.
 */

public class CreditAdapter extends CustomAdapter<CreditDetailsBean> {

  public CreditAdapter(Context context, List<CreditDetailsBean> list) {
    super(context, list);
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.activity_credit_item;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, CreditDetailsBean item, int position) {

    TextView time = viewHolder.getView(R.id.tv_time);
    TextView method = viewHolder.getView(R.id.tv_method);
    TextView credit = viewHolder.getView(R.id.tv_credit);
    View root = viewHolder.getView(R.id.root);

    DebugUtils.setStringValue(item.getGetdate(), "", time);
    DebugUtils.setStringValue(item.getCode(), "", credit);
    DebugUtils.setStringValue(item.getNote(), "", method);
    if (position % 2 == 0) {
      root.setBackgroundColor(Color.parseColor("#FFFFFF"));
    } else {
      root.setBackgroundColor(Color.parseColor("#F9F9F9"));
    }

  }
}

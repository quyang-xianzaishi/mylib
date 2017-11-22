package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.finance.CuiHuaJiResultBean.DeallistBean;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.DebugUtils;
import java.util.List;

/**
 * Created by quyang on 2017/6/28.
 */

public class CuiHuaJiAdapter extends CustomAdapter<DeallistBean> {

  public CuiHuaJiAdapter(Context context, List<DeallistBean> list) {
    super(context, list);
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.cuihuaji_lv_item;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, DeallistBean item, int position) {
    View root = viewHolder.getView(R.id.root);

    TextView tvDate = viewHolder.getView(R.id.tv_date);
    TextView tvTask = viewHolder.getView(R.id.tv_task);
    TextView tvDCount = viewHolder.getView(R.id.tv_count);

    tvDate.setText(DebugUtils.convert(item.getDate(), ""));
    tvTask.setText(DebugUtils.convert(item.getTask(), ""));
    tvDCount.setText(DebugUtils.convert(item.getCount(), ""));
//    if ("0".equals(item.getColor())) {
//      tvDate.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
//      tvTask.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
//      tvDCount.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
//    } else if ("1".equals(item.getColor())) {
//      tvDate.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
//      tvTask.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
//      tvDCount.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
//    }

    if(position%2==0){
      root.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    }else {
      root.setBackgroundColor(mContext.getResources().getColor(R.color.cf9f9f9));
    }

  }
}

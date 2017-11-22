package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.finance.ActiveCodeResultBean.DeallistBean;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import java.util.List;

/**
 * Created by 激活码 on 2017/6/28.
 */

public class ActiveCodeAdapter extends CustomAdapter<DeallistBean> {

  public ActiveCodeAdapter(Context context, List<DeallistBean> list) {
    super(context, list);
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.active_code_item;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, DeallistBean item, int position) {

    View root = viewHolder.getView(R.id.root_normal);
    TextView tvDate = viewHolder.getView(R.id.tv_date);
    TextView tvTradeObj = viewHolder.getView(R.id.tv_trade_obj);
    TextView tvTradeSize = viewHolder.getView(R.id.tv_trade_size);

    DebugUtils.setStringValue(item.getDate(), "", tvDate);
    DebugUtils.setStringValue(item.getTradeobject(), "", tvTradeObj);
    DebugUtils.setStringValue(item.getCount(), "", tvTradeSize);

    if ("0".equals(item.getColor())) {
      tvDate.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
      tvTradeObj.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
      tvTradeSize.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
    } else if ("1".equals(item.getColor())) {
      tvDate.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
      tvTradeObj.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
      tvTradeSize.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
    }

    if(position%2==0){
      root.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    }else {
      root.setBackgroundColor(mContext.getResources().getColor(R.color.cf6f6f6));
    }

  }

}

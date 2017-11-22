package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.CreditRecordBean.ResultBean;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.StringUtil;
import java.util.List;

/**
 * Created by admistrator on 2017/7/19.
 */

public class CreditRecordAdapter extends CustomAdapter<ResultBean> {

  public CreditRecordAdapter(Context context,
      List<ResultBean> list) {
    super(context, list);
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.credit_record_item_layout_new;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, ResultBean item, int position) {

    TextView tv_vip = viewHolder.getView(R.id.tv_vip);
    TextView tv_pingfeng = viewHolder.getView(R.id.tv_pingfeng);
    TextView tv_order_id = viewHolder.getView(R.id.tv_order_id);
    TextView tv_content = viewHolder.getView(R.id.tv_content);
    TextView tv_date = viewHolder.getView(R.id.tv_date);

    DebugUtils.setStringValue(item.getTrademember(), "", tv_vip);
    DebugUtils.setStringValue(mContext.getString(R.string.order_id) + ": " + item.getOrderid(), "",
        tv_order_id);
    DebugUtils.setStringValue(item.getEvaluatetext(), mContext.getString(R.string.pingjia_tips), tv_content);
    DebugUtils.setStringValue(item.getDate(), "", tv_date);

    StringUtil.setTextSizeNewOne(
        item.getEvaluate() + "\n" + mContext.getString(R.string.xiong_yong_ping_jia), tv_pingfeng,
        24,
        11, mContext.getString(R.string.xiong_yong_ping_jia).charAt(0),
        mContext.getString(R.string.xiong_yong_ping_jia).charAt(0),
        ColorUtil.getColor(R.color._888888, mContext));

  }
}

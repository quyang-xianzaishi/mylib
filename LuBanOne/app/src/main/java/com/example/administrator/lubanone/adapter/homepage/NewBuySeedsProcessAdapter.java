package com.example.administrator.lubanone.adapter.homepage;

import android.widget.TextView;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BuyMatchListResultBean.TgbzlistBean;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.StringUtil;
import java.util.List;

/**
 * Created by quyang on 2017/6/30.
 */

public class NewBuySeedsProcessAdapter extends CustomAdapter<TgbzlistBean> {

  private String mPrice;

  public NewBuySeedsProcessAdapter(List<TgbzlistBean> list, String price) {
    super(MyApplication.getContext(), list);
    this.mPrice = price;
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.activity_buy_seeds_lv_item_new;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, TgbzlistBean item, int position) {

    TextView tvOrderId = viewHolder.getView(R.id.tv_order_id);
    TextView tvOrderTime = viewHolder.getView(R.id.tv_order_time);
    TextView tvOrderCount = viewHolder.getView(R.id.tv_seeds_count);
    TextView tvOrderStatus = viewHolder.getView(R.id.tv_order_status);

    //订单编号
    tvOrderId.setText(StringUtil.getBufferString(mContext.getString(R.string.order_id), " :  ",
        DebugUtils.convert(item.getOrderid(), "")));

    //种子数量
    String s = DebugUtils.convert(item.getSeedcount(), "0") + "PCS";
    StringUtil.setTextSize(s, tvOrderTime, 25, 9);

    //下单时间
    tvOrderCount.setText(
        StringUtil.getBufferString(mContext.getString(R.string.order_create_time_new), " :  ",
            DebugUtils.convert(item.getOrdertime(), "")));

    String multile = DemicalUtil
        .multile(DebugUtils.convert(item.getSeedcount(), "0"),
            DebugUtils.convert(mPrice, "0"));
    //合计
    String s1 = mContext.getString(R.string.heji) + " " + StringUtil.getThreeString(multile);
    StringUtil.setTextSizeNewOne(s1, tvOrderStatus, 11, 15, '¥', '¥',
        ColorUtil.getColor(R.color.c333, mContext));
  }
}

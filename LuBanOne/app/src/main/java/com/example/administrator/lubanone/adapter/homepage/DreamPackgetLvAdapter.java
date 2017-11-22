package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.finance.DreamPackageResultBean.DeallistBean;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.DebugUtils;
import java.util.List;

/**
 * Created by quyang on 2017/6/28.
 */

public class DreamPackgetLvAdapter extends CustomAdapter<DeallistBean> {

  public DreamPackgetLvAdapter(Context context, List<DeallistBean> list) {
    super(context, list);
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.dream_package_lv_item;
  }


  @Override
  public void setData2Views(ViewHolder viewHolder, DeallistBean item, int position) {

    View root = viewHolder.getView(R.id.root);
    //日期
    TextView tvDate = viewHolder.getView(R.id.tv_date);
    //订单id
    TextView tvOrderId = viewHolder.getView(R.id.tv_order_id);
    //交易类型
    TextView tvTradeType = viewHolder.getView(R.id.tv_trade_type);
    //交易会员
    TextView tvTradeVIP = viewHolder.getView(R.id.tv_trade_vip);
    //播种数
    TextView tvSeeds = viewHolder.getView(R.id.tv_seeds);
    //成长中的种子
    TextView tvGrowingSeeds = viewHolder.getView(R.id.tv_growing_seeds);
    //交易的种子
    TextView tvTradeSeeds = viewHolder.getView(R.id.tv_trade_seeds);

    tvDate.setText(DebugUtils.convert(item.getDate(), ""));
    tvOrderId.setText(DebugUtils.convert(item.getOrderid(), ""));
    tvTradeVIP.setText(DebugUtils.convert(item.getTrademember(), ""));
    tvTradeType.setText(DebugUtils.convert(item.getTradetype(), ""));
    tvSeeds.setText(DebugUtils.convert(item.getSowseed(), ""));
    tvGrowingSeeds.setText(DebugUtils.convert(item.getGrowget(), ""));
    tvTradeSeeds.setText(DebugUtils.convert(item.getTradeseed(), ""));
//
//    if (ColorType.NORMAL.equals(item.getColor())) {
////      tvDate.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
////      tvOrderId.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
////      tvTradeVIP.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
//
//      tvTradeType.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
//
////      tvSeeds.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
////      tvGrowingSeeds.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
////      tvTradeSeeds.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
//    } else if (ColorType.RED.equals(item.getColor())) {
////      tvDate.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
////      tvOrderId.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
////      tvTradeVIP.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
//
//      tvTradeType.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
//
////      tvSeeds.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
////      tvGrowingSeeds.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
////      tvTradeSeeds.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
//    }

    if (position % 2 == 0) {
      root.setBackgroundColor(Color.parseColor("#FFFFFF"));
    } else {
      root.setBackgroundColor(Color.parseColor("#F9F9F9"));
    }

  }
}

package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.finance.MoneyPackageResultBean.DeallistBean;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.DebugUtils;
import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */

public class MoneyPackageLvAdapter extends CustomAdapter<DeallistBean> {

  public MoneyPackageLvAdapter(Context context, List<DeallistBean> list) {
    super(context, list);
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.money_package_lv_item;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, DeallistBean item, int position) {

    View root = viewHolder.getView(R.id.root);

    //日期
    TextView tvDate = viewHolder.getView(R.id.tv_date);
    //订单id
    TextView tvOrderId = viewHolder.getView(R.id.tv_order_id);
    //交易会员
    TextView tvTradeVIP = viewHolder.getView(R.id.tv_trade_vip);
    //奖金类型
    TextView tvTradeType = viewHolder.getView(R.id.tv_trade_type);

    TextView tvSeeds = viewHolder.getView(R.id.tv_seeds);
//    tvSeeds.setVisibility(View.VISIBLE);

    tvDate.setText(DebugUtils.convert(item.getDate(), "0"));
    tvOrderId.setText(DebugUtils.convert(item.getOrderid(), "0"));
    tvTradeVIP.setText(DebugUtils.convert(item.getTrademember(), "0"));
    tvTradeType.setText(DebugUtils.convert(item.getRewardtype(), "0"));
    tvSeeds.setText(DebugUtils.convert(item.getTradeseeds(), "0"));

    if ("1".equals(item.getColor())) {
//      tvDate.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
//      tvOrderId.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
//      tvTradeVIP.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));

//      tvTradeType.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));

//      tvSeeds.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
    } else if ("0".equals(item.getColor())) {
//      tvDate.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
//      tvOrderId.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
//      tvTradeVIP.setTextColor(ColorUtil.getColor(R.color.c666, mContext));

//      tvTradeType.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));

//      tvSeeds.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
    }

    if (position % 2 == 0) {
      root.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    } else {
      root.setBackgroundColor(mContext.getResources().getColor(R.color.cf9f9f9));
    }

  }
}

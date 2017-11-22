package com.example.administrator.lubanone.adapter.homepage;

import android.widget.TextView;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BuyMatchListResultBean.TgbzlistBean;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.StringUtil;
import java.util.List;

/**
 * Created by quyang on 2017/6/30.
 */

public class BuySeedsProcessAdapter extends CustomAdapter<TgbzlistBean> {

  public BuySeedsProcessAdapter(List<TgbzlistBean> list) {
    super(MyApplication.getContext(), list);
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.activity_buy_seeds_lv_item;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, TgbzlistBean item, int position) {

    TextView tvOrderId = viewHolder.getView(R.id.tv_order_id);
    TextView tvOrderTime = viewHolder.getView(R.id.tv_order_time);
    TextView tvOrderCount = viewHolder.getView(R.id.tv_seeds_count);
    TextView tvOrderStatus = viewHolder.getView(R.id.tv_order_status);

    tvOrderId.setText(StringUtil.getBufferString(mContext.getString(R.string.order_code),
        DebugUtils.convert(item.getOrderid(), "")));
    tvOrderTime.setText(StringUtil.getBufferString(mContext.getString(R.string.order_create_time),
        DebugUtils.convert(item.getOrdertime(), "")));
    tvOrderCount.setText(
        StringUtil.getBufferString(mContext.getString(R.string.seed_count),
            DebugUtils.convert(item.getSeedcount(), "")));
    tvOrderStatus.setText(mContext.getString(R.string.buy_seed_process_match));

  }
}

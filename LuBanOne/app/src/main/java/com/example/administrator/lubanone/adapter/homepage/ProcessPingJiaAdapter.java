package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BuyPingJiaResultBean.PDpingjialistBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.StringUtil;
import java.util.List;

/**
 * Created by quyang on 2017/6/30.
 */

public class ProcessPingJiaAdapter extends CustomAdapter<PDpingjialistBean> {


  private OnListViewItemListener mListener;

  public ProcessPingJiaAdapter(Context context, List<PDpingjialistBean> list,
      OnListViewItemListener listener) {
    super(context, list);
    this.mListener = listener;
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.pingjia_lv_item;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, PDpingjialistBean item, int position) {

    TextView tvOrderId = viewHolder.getView(R.id.tv_order_id);//订单编号
    TextView tvSeedsCount = viewHolder.getView(R.id.tv_seeds_count);//种子数量
    TextView tvSellVIP = viewHolder.getView(R.id.tv_match_time);//卖方会员
    TextView tvOrderStatus = viewHolder.getView(R.id.tv_seller);//订单状态
    TextView tvPinLun = viewHolder.getView(R.id.tv_upload_proof);//评论

    tvOrderId.setText(StringUtil.getBufferString(mContext.getString(R.string.order_code),
        DebugUtils.convert(item.getOrderid(), "")));
    tvSeedsCount.setText(StringUtil.getBufferString(mContext.getString(R.string.seed_count),
        DebugUtils.convert(item.getSeedcount(), "")));

    tvSellVIP.setText(StringUtil.getBufferString(mContext.getString(R.string.seller_vip),
        DebugUtils.convert(item.getSellmember(), "")));
    tvOrderStatus.setText(mContext.getString(R.string.buy_seed_record_no_pingjia));

    tvPinLun.setOnClickListener(new MyOnClickListener(position));

  }

  private class MyOnClickListener implements View.OnClickListener {

    private int position;

    public MyOnClickListener(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      mListener.onItem(getItem(position), position);
    }
  }
}

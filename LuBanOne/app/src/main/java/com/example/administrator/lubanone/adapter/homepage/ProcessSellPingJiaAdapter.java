package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.SellPingJiaResultBean.GDpingjialistBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
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

public class ProcessSellPingJiaAdapter extends CustomAdapter<GDpingjialistBean> {


  private OnListViewItemListener mListener;
  private String mPrice;

  public ProcessSellPingJiaAdapter(Context context, List<GDpingjialistBean> list,
      OnListViewItemListener listener,String price) {
    super(context, list);
    this.mListener = listener;
    this.mPrice = price;
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.pingjia_lv_item_last;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, GDpingjialistBean item, int position) {

    TextView tvOrderId = viewHolder.getView(R.id.tv_order_id);//订单编号
    TextView tvSeedsCount = viewHolder.getView(R.id.tv_seeds_count);//购买会员
    TextView tvPinLun = viewHolder.getView(R.id.tv_upload_proof);//评论
    TextView heji = viewHolder.getView(R.id.tv_order_status);//合计
    TextView tv_order_time = viewHolder.getView(R.id.tv_order_time);//种子数量

    //种子数量
    String s = DebugUtils.convert(item.getSeedcount(),"0") + "PCS";
    StringUtil.setTextSize(s, tv_order_time, 25, 9);


    tvOrderId.setText(StringUtil.getBufferString(mContext.getString(R.string.order_code),
        DebugUtils.convert(item.getOrderid(), "")));
    tvSeedsCount.setText(StringUtil.getBufferString(mContext.getString(R.string.buy_vip),
        DebugUtils.convert(item.getBuymember(), "")));

    //合计
    String multile = DemicalUtil
        .multile(DebugUtils.convert(item.getSeedcount(), "0"),
            DebugUtils.convert(mPrice, "0"));
    String s1 = mContext.getString(R.string.heji) + " " + StringUtil.getThreeString(multile);
    StringUtil.setTextSizeNewOne(s1, heji, 11, 15, '¥', '¥',
        ColorUtil.getColor(R.color.c333, mContext));

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

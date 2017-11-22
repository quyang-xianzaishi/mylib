package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BuyPingJiaResultBean.PDpingjialistBean;
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

public class ProcessPingJiaAdapterNew extends CustomAdapter<PDpingjialistBean> {


  private OnListViewItemListener mListener;
  private String mPrice;

  public ProcessPingJiaAdapterNew(Context context, List<PDpingjialistBean> list,
      OnListViewItemListener listener, String price) {
    super(context, list);
    this.mListener = listener;
    this.mPrice = price;
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.pingjia_lv_item_new;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, PDpingjialistBean item, int position) {

    TextView tvOrderId = viewHolder.getView(R.id.tv_order_id);//订单编号
    TextView tvSeedsCount = viewHolder.getView(R.id.tv_seeds_count);//种子数量
    TextView tvSeller = viewHolder.getView(R.id.tv_seller);//订单状态
    TextView tvPinLun = viewHolder.getView(R.id.tv_upload_proof);//评论
    TextView tv_heji = viewHolder.getView(R.id.tv_heji);//合计

    tvOrderId.setText(StringUtil.getBufferString(mContext.getString(R.string.order_code),
        DebugUtils.convert(item.getOrderid(), "")));

    StringUtil
        .setTextSize(DebugUtils.convert(item.getSeedcount(), "0") + " PCS", tvSeedsCount, 20, 9);

    //合计
    String multile = DemicalUtil
        .multile(DebugUtils.convert(item.getSeedcount(), "0"),
            DebugUtils.convert(mPrice, "0"));
    String s1 = mContext.getString(R.string.heji) + " " + StringUtil.getThreeString(multile);
    StringUtil.setTextSizeNewOne(s1, tv_heji, 11, 15, '¥', '¥',
        ColorUtil.getColor(R.color.c333, mContext));


    //卖方会员
    tvSeller.setText(StringUtil
        .getBufferString(mContext.getString(R.string.seller_vip_new), item.getSellmember()));

    //评价listener
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

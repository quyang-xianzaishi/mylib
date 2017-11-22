package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.SellConfirmResultBean.ComplaintType;
import com.example.administrator.lubanone.bean.homepage.SellConfirmResultBean.GConfirmlistBean;
import com.example.administrator.lubanone.interfaces.OnItemListener;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DpUtil;
import com.example.qlibrary.utils.DrableUtil;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.ViewUtil;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ProcessSellConfirmAdapter extends CustomAdapter<GConfirmlistBean> {

  private OnItemListener mListener;
  private OnListViewItemListener mReceverListener;
  private OnMoneyPayListener<GConfirmlistBean> mVIPListener;


  public ProcessSellConfirmAdapter(Context context, List<GConfirmlistBean> list,
      OnItemListener listener, OnMoneyPayListener<GConfirmlistBean> vipListener,
      OnListViewItemListener receverListener) {
    super(context, list);
    this.mListener = listener;
    this.mVIPListener = vipListener;
    this.mReceverListener = receverListener;
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.sell_comfrim_layout;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, final GConfirmlistBean item,
      final int position) {

    View container = viewHolder.getView(R.id.ll_container);
    container.setVisibility(View.VISIBLE);

    TextView tvOrderId = viewHolder.getView(R.id.tv_order_id);
    TextView tvApplyTime = viewHolder.getView(R.id.tv_apply_time);
    TextView tvSeedsCount = viewHolder.getView(R.id.tv_seeds_count);
    TextView tvOrderTime = viewHolder.getView(R.id.tv_match_time);
    TextView tvOrderSeller = viewHolder.getView(R.id.tv_seller);
    TextView tvHaveTime = viewHolder.getView(R.id.tv_have_time);
    TextView tvTips = viewHolder.getView(R.id.tv_tips);
    TextView tvOrderStatus = viewHolder.getView(R.id.tv_order_status);
    final TextView tvPayLongTime = viewHolder.getView(R.id.tv_pay_long_time);
    final TextView tvUploadProof = viewHolder.getView(R.id.tv_upload_proof);
    tvUploadProof.setVisibility(View.VISIBLE);

    tvPayLongTime.setText("确认收款");
    tvUploadProof.setText("未收到款，我要投诉");
    tvTips.setText("请第一时间确认是否已经收到款，让购买会员尽快拿到种子播种哦！");

    tvPayLongTime.setTextColor(ColorUtil.getColor(R.color.blue, mContext));
//    tvPayLongTime.setBackgroundDrawable(
//        DrableUtil
//            .getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5), DpUtil.dp2px(mContext, 1),
//                R.color.blue));
    ViewUtil.setBackground(tvPayLongTime,
        DrableUtil
            .getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5), DpUtil.dp2px(mContext, 1),
                R.color.blue));

    if (ComplaintType.GRAY.equals(item.getComplaintbutton())) {
      tvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, mContext));
//      tvUploadProof.setBackgroundDrawable(
//          DrableUtil
//              .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5)));
      ViewUtil.setBackground(tvUploadProof,
          DrableUtil
              .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5)));

      tvUploadProof.setEnabled(false);
    } else if (ComplaintType.NORMAL.equals(item.getComplaintbutton())) {
      tvUploadProof.setTextColor(ColorUtil.getColor(R.color.cEA5514, mContext));
//      tvUploadProof.setBackgroundDrawable(
//          DrableUtil
//              .getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5),
//                  DpUtil.dp2px(mContext, 1),
//                  R.color.cEA5514));
      ViewUtil.setBackground(tvUploadProof,
          DrableUtil
              .getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5),
                  DpUtil.dp2px(mContext, 1),
                  R.color.cEA5514));
      tvUploadProof.setEnabled(true);
    }

    tvOrderId.setText(
        StringUtil.getBufferString(mContext.getString(R.string.order_code), item.getOrderid()));
    tvApplyTime.setText(
        StringUtil
            .getBufferString(mContext.getString(R.string.apply_time), item.getApplytime() + ""));
    tvSeedsCount.setText(
        StringUtil.getBufferString(mContext.getString(R.string.seed_count), item.getSeedcount()));
    tvOrderTime.setText(
        StringUtil.getBufferString(mContext.getString(R.string.pay_time), item.getPaytime()));
    tvOrderSeller.setText(
        StringUtil.getBufferString(mContext.getString(R.string.buy_vip), item.getBuymember()));
    tvHaveTime.setText(
        StringUtil
            .getBufferString(mContext.getString(R.string.shengyu_time), item.getTimeout() + ""));
    tvOrderStatus.setText(mContext.getString(R.string.sell_seeds_order_status));

    //会员
    tvOrderSeller.setOnClickListener(new MyOnClickListener(position));

    //投诉
    tvUploadProof.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mListener.onItem(getItem(position), position, 0);
      }
    });

    //确认收款
    tvPayLongTime.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mReceverListener.onItem(getItem(position), position);
      }
    });

  }

  //会员信息
  private class MyOnClickListener implements OnClickListener {

    private int position;

    public MyOnClickListener(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      mVIPListener.onCuiPayClick(getItem(position), position);
    }
  }
}

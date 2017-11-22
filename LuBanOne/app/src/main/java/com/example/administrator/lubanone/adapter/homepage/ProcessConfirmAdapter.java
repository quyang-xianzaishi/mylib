package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.CuiType;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.PConfirmlistBean;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.ProofType;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.StatusType;
import com.example.administrator.lubanone.interfaces.OnItemListener;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DpUtil;
import com.example.qlibrary.utils.DrableUtil;
import com.example.qlibrary.utils.ViewUtil;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ProcessConfirmAdapter extends CustomAdapter<PConfirmlistBean> {

  private OnItemListener mListener;
  private OnMoneyPayListener<PConfirmlistBean> mVIPListener;


  public ProcessConfirmAdapter(Context context, List<PConfirmlistBean> list,
      OnItemListener listener, OnMoneyPayListener<PConfirmlistBean> vipListener) {
    super(context, list);
    this.mListener = listener;
    this.mVIPListener = vipListener;
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.no_money_layout;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, final PConfirmlistBean item,
      final int position) {

    View container = viewHolder.getView(R.id.ll_container);
    container.setVisibility(View.VISIBLE);

    TextView tvOrderId = viewHolder.getView(R.id.tv_order_id);
    TextView tvSeedsCount = viewHolder.getView(R.id.tv_seeds_count);
    TextView tvOrderTime = viewHolder.getView(R.id.tv_match_time);
    TextView tvOrderSeller = viewHolder.getView(R.id.tv_seller);
    TextView tvHaveTime = viewHolder.getView(R.id.tv_have_time);//订单状态
    TextView tvTips = viewHolder.getView(R.id.tv_tips);
    TextView tvOrderStatus = viewHolder.getView(R.id.tv_order_status);
    final TextView tvPayLongTime = viewHolder.getView(R.id.tv_pay_long_time);
    final TextView tvUploadProof = viewHolder.getView(R.id.tv_upload_proof);

    tvUploadProof.setVisibility(View.VISIBLE);
    tvPayLongTime.setVisibility(View.GONE);

    tvOrderSeller.setOnClickListener(new MyOnClickListener(position));

    String buypressbutton = item.getBuypressbutton();
    String showpic2button = item.getShowpic2button();

    tvOrderId.setText(new StringBuilder().append(mContext.getString(R.string.order_code))
        .append(item.getOrderid()));
    tvSeedsCount.setText(new StringBuilder().append(mContext.getString(R.string.seed_count))
        .append(item.getSeedcount()));
    tvOrderTime.setText(new StringBuilder().append(mContext.getString(R.string.pay_time))
        .append(item.getPaytime()));
    tvOrderSeller.setText(new StringBuilder().append(mContext.getString(R.string.seller_vip))
        .append(item.getSellmember()));

    if (StatusType.NO_COMPLAIN.equals(item.getStatus())) {//没有被投诉
      tvTips.setVisibility(View.VISIBLE);
      tvTips.setText(mContext.getString(R.string.buy_confim_tips));

      updatePressButton(tvUploadProof, buypressbutton, tvOrderStatus);

      tvHaveTime.setTextColor(ColorUtil.getColor(R.color.c000, mContext));//剩余确认时间
      tvHaveTime.setText("剩余确认时间 ：" + item.getTimedifferent());
    }
    if (StatusType.COMPLAINTED.equals(item.getStatus())) {//被投诉
      updatePic(tvUploadProof, showpic2button, tvTips, tvHaveTime);

      tvOrderStatus.setTextColor(ColorUtil.getColor(R.color.cEA5514, mContext));//投诉时间
      tvOrderStatus.setText(mContext.getString(R.string.complain_time) + item.getTs_time());

    }

    tvUploadProof.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //催确认
        mListener.onItem(getItem(position), position, Integer.parseInt(item.getStatus()));
      }
    });
  }

  //上传付款视频
  private void updatePic(TextView tvUploadProof, String showpic2button, TextView tvTips,
      TextView orderStatus) {

    if (ProofType.GRAY.equals(showpic2button)) {
      tvUploadProof.setVisibility(View.VISIBLE);

      tvTips.setText(mContext.getString(R.string.conplaint_proof_tips));
      tvTips.setVisibility(View.VISIBLE);

      orderStatus
          .setText(new StringBuilder().append(mContext.getString(R.string.order_status))
              .append(mContext.getString(R.string.complainted)));
      orderStatus.setTextColor(ColorUtil.getColor(R.color.cEA5514, mContext));

    }
    if (ProofType.NORMAL.equals(showpic2button)) {
      tvTips.setText(mContext.getString(R.string.conplaint_tips));
      tvTips.setVisibility(View.VISIBLE);

      tvUploadProof.setVisibility(View.VISIBLE);
      tvUploadProof.setEnabled(true);
      tvUploadProof.setText(mContext.getString(R.string.upload_proof_video));
      tvUploadProof
          .setTextColor(ColorUtil.getColor(R.color.cEA5514, mContext));
      ViewUtil.setBackground(tvUploadProof,
          DrableUtil.getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5),
              DpUtil.dp2px(mContext, 1), R.color.cEA5514));

      orderStatus
          .setText(new StringBuilder().append(mContext.getString(R.string.order_status))
              .append(mContext.getString(R.string.complainted)));
      orderStatus.setTextColor(ColorUtil.getColor(R.color.cEA5514, mContext));
    }
  }


  //崔确认按钮
  private void updatePressButton(TextView tvUploadProof, String buypressbutton,
      TextView tvOrderStatus) {

    tvUploadProof.setText(mContext.getString(R.string.cui_confirm));

    tvOrderStatus.setText(new StringBuilder().append(mContext.getString(R.string.order_status))
        .append(mContext.getString(R.string.wait_confrim)));
    tvOrderStatus.setTextColor(ColorUtil.getColor(R.color.c000, mContext));

    if (CuiType.GRAY.equals(buypressbutton)) {//灰色
      tvUploadProof.setVisibility(View.VISIBLE);
      tvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, mContext));
      ViewUtil.setBackground(tvUploadProof, DrableUtil
          .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5)));
      tvUploadProof.setEnabled(false);

    }

    if (CuiType.NORMAL.equals(buypressbutton)) {
      tvUploadProof.setEnabled(true);
      tvUploadProof.setVisibility(View.VISIBLE);
      ViewUtil.setBackground(tvUploadProof,
          DrableUtil.getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5),
              DpUtil.dp2px(mContext, 1), R.color.blue));
      tvUploadProof.setEnabled(true);
      tvUploadProof
          .setTextColor(ColorUtil.getColor(R.color.blue, mContext));


    }
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

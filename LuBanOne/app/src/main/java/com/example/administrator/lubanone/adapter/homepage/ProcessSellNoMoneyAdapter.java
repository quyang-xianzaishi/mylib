package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.GPaylistBean;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.StatusType;
import com.example.administrator.lubanone.interfaces.OnBuyVIPListener;
import com.example.administrator.lubanone.interfaces.OnItemListener;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DpUtil;
import com.example.qlibrary.utils.DrableUtil;
import com.example.qlibrary.utils.ViewUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ProcessSellNoMoneyAdapter extends CustomAdapter<GPaylistBean> {

  private OnMoneyPayListener<GPaylistBean> mPayListener;
  private OnBuyVIPListener mVipListener;
  private OnItemListener itemListener;

  public ProcessSellNoMoneyAdapter(Context context, List<GPaylistBean> list,
      OnMoneyPayListener<GPaylistBean> payListener,
      OnBuyVIPListener vipListener, OnItemListener itemListener) {
    super(context, list);
    this.mPayListener = payListener;
    this.mVipListener = vipListener;
    this.itemListener = itemListener;
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.sell_no_money_layout;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, GPaylistBean item, final int position) {

    View container = viewHolder.getView(R.id.ll_container);
    View rlCui = viewHolder.getView(R.id.rl_cuiyicui);
    View rlLongPayTime = viewHolder.getView(R.id.rl_long_pay_time);

    TextView tvOrderId = viewHolder.getView(R.id.tv_order_id);
    TextView tvApplyTime = viewHolder.getView(R.id.tv_apply_time);
    TextView tvSeedsCount = viewHolder.getView(R.id.tv_seeds_count);
    TextView tvOrderTime = viewHolder.getView(R.id.tv_match_time);
    TextView tvOrderSeller = viewHolder.getView(R.id.tv_seller);
    TextView tvHaveTime = viewHolder.getView(R.id.tv_have_time);
    TextView tvTips = viewHolder.getView(R.id.tv_tips);
    TextView tvOrderStatus = viewHolder.getView(R.id.tv_order_status);

    TextView tvPayLongTime = viewHolder.getView(R.id.tv_pay_long_time);//催一催

    //被投诉
    TextView tvUploadProof = viewHolder.getView(R.id.tv_upload_proof);

    TextView tvTouSu = viewHolder.getView(R.id.tv_tousu);
    tvTouSu.setVisibility(View.GONE);

    //延长时间
    TextView tvYanChangPay = viewHolder.getView(R.id.tv_yangchang);

    //投诉
    String complaintbutton = item.getComplaintbutton();
    updateTouSu(complaintbutton, tvUploadProof);

    //催一崔
    String sellpressbutton = item.getSellpressbutton();
    updateCui(sellpressbutton, tvPayLongTime);

    //延长
    String prolongbutton = item.getProlongbutton();
    updateLong(prolongbutton, tvYanChangPay);

    tvPayLongTime.setText(mContext.getString(R.string.cui_yi_cui));//催一催
    tvUploadProof.setText(mContext.getString(R.string.i_want_complaint));//我要投诉
    tvYanChangPay.setText(mContext.getString(R.string.long_pay_time_one));//延长打款时间

    String status = item.getStatus();
    if (StatusType.COMPLAINTED.equals(status)) {//投诉
      rlLongPayTime.setVisibility(View.GONE);//隐藏延长打款时间按钮
      tvTips.setVisibility(View.GONE);
      rlCui.setVisibility(View.VISIBLE);
      tvUploadProof.setVisibility(View.VISIBLE);//显示投诉
      tvPayLongTime.setVisibility(View.VISIBLE);//隐藏催一催
    } else if (StatusType.LONG_PAY.equals(status)) {//延长打款
      rlLongPayTime.setVisibility(View.VISIBLE);
      tvPayLongTime.setVisibility(View.VISIBLE);//显示催一催
      tvTips.setVisibility(View.VISIBLE);
      container.setVisibility(View.VISIBLE);
      rlCui.setVisibility(View.VISIBLE);
      tvTips.setText("匹配12小时候后，才能催对方打款哦");

      LayoutParams layoutParams = tvUploadProof.getLayoutParams();
      layoutParams.width = 1;
      layoutParams.height = 1;
      tvUploadProof.setLayoutParams(layoutParams);
      tvUploadProof.setVisibility(View.VISIBLE);// 隐藏投诉按钮

    } else if (StatusType.CUI.equals(status)) {//单纯催一崔
      rlLongPayTime.setVisibility(View.GONE);//隐藏延长打款时间按钮
      tvPayLongTime.setVisibility(View.VISIBLE);//显示催一催
      container.setVisibility(View.VISIBLE);
      rlCui.setVisibility(View.VISIBLE);
      tvTips.setVisibility(View.VISIBLE);
      tvTips.setText("匹配12小时候后，才能催对方打款哦");

      LayoutParams layoutParams = tvUploadProof.getLayoutParams();
      layoutParams.width = 1;
      layoutParams.height = 1;
      tvUploadProof.setLayoutParams(layoutParams);
      tvUploadProof.setVisibility(View.VISIBLE);//隐藏投诉

    }

    tvOrderId.setText("订单编号 ：" + item.getOrderid());
    tvApplyTime.setText("申请时间 ：" + item.getApplytime());
    tvSeedsCount.setText("种子数量 ：" + item.getSeedcount());
    tvOrderTime.setText("匹配时间 ：" + item.getMatchtime());
    tvOrderSeller.setText("买方会员 ：" + item.getBuymember());
    tvHaveTime.setText("剩余付款时间 ：" + item.getTimeout());

    //点击事件
    tvUploadProof.setOnClickListener(new MyOnClickListener(1, getItem(position), position));
    tvYanChangPay.setOnClickListener(new MyOnClickListener(2, getItem(position), position));

    //会员
    tvOrderSeller.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mVipListener.onVIPItemClick(getItem(position), position);
      }
    });

    //催一崔
    tvPayLongTime.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mPayListener.onCuiPayClick(getItem(position), position);
      }
    });
  }

  private void updateLong(String prolongbutton, TextView tvYanChangPay) {
    if ("0".equals(prolongbutton)) {
      tvYanChangPay.setVisibility(View.GONE);
    } else if ("1".equals(prolongbutton)) {
      tvYanChangPay.setVisibility(View.VISIBLE);
      tvYanChangPay.setEnabled(true);
      tvYanChangPay.setText(mContext.getResources().getString(R.string.longer_pay_time));

//      tvYanChangPay.setBackgroundDrawable(
//          DrableUtil.getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5),
//              DpUtil.dp2px(mContext, 1), R.color.blue));
      ViewUtil.setBackground(tvYanChangPay,
          DrableUtil.getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5),
              DpUtil.dp2px(mContext, 1), R.color.blue));
      tvYanChangPay.setTextColor(ColorUtil.getColor(R.color.blue, mContext));

    }
//    else if ("2".equals(prolongbutton)) {
//      tvYanChangPay.setVisibility(View.VISIBLE);
//      tvYanChangPay.setEnabled(false);
//      tvYanChangPay.setText(mContext.getResources().getString(R.string.agreed_long));
//      tvYanChangPay
//          .setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.gray_shape));
//    }
  }

  private void updateCui(String sellpressbutton, TextView tvUploadProof) {
    if ("0".equals(sellpressbutton)) {
      tvUploadProof.setVisibility(View.GONE);
    } else if ("1".equals(sellpressbutton)) {
      tvUploadProof.setVisibility(View.VISIBLE);
      tvUploadProof.setEnabled(true);

//      tvUploadProof.setBackgroundDrawable(
//          DrableUtil.getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5),
//              DpUtil.dp2px(mContext, 1), R.color.blue));
      ViewUtil.setBackground(tvUploadProof,
          DrableUtil.getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5),
              DpUtil.dp2px(mContext, 1), R.color.blue));
      tvUploadProof.setTextColor(ColorUtil.getColor(R.color.blue, mContext));

    } else if ("2".equals(sellpressbutton)) {
      tvUploadProof.setVisibility(View.VISIBLE);
      tvUploadProof.setEnabled(false);

//      tvUploadProof.setBackgroundDrawable(
//          DrableUtil.getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5)));
      ViewUtil.setBackground(tvUploadProof,
          DrableUtil.getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5)));
      tvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, mContext));
    }
  }

  private void updateTouSu(String complaintbutton, TextView tvTousu) {

    if ("0".equals(complaintbutton)) {//隐藏
      tvTousu.setVisibility(View.GONE);
    } else if ("1".equals(complaintbutton)) {//显示
      tvTousu.setVisibility(View.VISIBLE);
      tvTousu.setEnabled(true);

    } else if ("2".equals(complaintbutton)) {//灰色
      tvTousu.setVisibility(View.VISIBLE);
      tvTousu.setEnabled(false);
    }

  }

  private class MyOnClickListener implements OnClickListener {

    private int i;
    private GPaylistBean item;
    private int position;

    public MyOnClickListener(int i, GPaylistBean item, int position) {
      this.i = i;
      this.item = item;
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      switch (i) {
        case 1://投诉
          itemListener.onItem(item, position, 1);
          break;
        case 2://延长打款
          itemListener.onItem(item, position, 2);
          break;
      }
    }
  }
}

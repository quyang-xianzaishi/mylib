package com.example.administrator.lubanone.adapter.homepage;

import static com.example.administrator.lubanone.R.id.tv_pay_long_time;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BuyMoneyListResultBean.PPaylistBean;
import com.example.administrator.lubanone.bean.homepage.BuyMoneyListResultBean.ShowButtonType;
import com.example.administrator.lubanone.interfaces.OnBuyVIPListener;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
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

public class ProcessNoMoneyAdapter extends CustomAdapter<PPaylistBean> {

  private OnListViewItemListener mListener;
  private OnMoneyPayListener<PPaylistBean> mPayListener;
  private OnBuyVIPListener mVipListener;


  public ProcessNoMoneyAdapter(Context context, List<PPaylistBean> list,
      OnListViewItemListener listener, OnMoneyPayListener<PPaylistBean> payListener,
      OnBuyVIPListener vipListener) {
    super(context, list);
    this.mListener = listener;
    this.mPayListener = payListener;
    this.mVipListener = vipListener;
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.no_money_layout;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, PPaylistBean item, final int position) {

    View container = viewHolder.getView(R.id.ll_container);

    TextView tvOrderId = viewHolder.getView(R.id.tv_order_id);
    TextView tvSeedsCount = viewHolder.getView(R.id.tv_seeds_count);
    TextView tvOrderTime = viewHolder.getView(R.id.tv_match_time);
    TextView tvOrderSeller = viewHolder.getView(R.id.tv_seller);
    TextView tvHaveTime = viewHolder.getView(R.id.tv_have_time);
    TextView tvTips = viewHolder.getView(R.id.tv_tips);//提示语
    TextView tvOrderStatus = viewHolder.getView(R.id.tv_order_status);
    TextView tvPayLongTime = viewHolder.getView(tv_pay_long_time);//延长打款
    TextView tvUploadProof = viewHolder.getView(R.id.tv_upload_proof);

    tvOrderId.setText("订单编号 ：" + item.getOrderid());
    tvSeedsCount.setText("种子数量 ：" + item.getSeedcount());
    tvOrderTime.setText("匹配时间 ：" + item.getMatchtime());
    tvOrderSeller.setText("卖方会员 ：" + item.getSellmember());
    tvHaveTime.setText("剩余付款时间 ：" + item.getTimedifferent() / 60 + "分钟");
    tvOrderStatus.setText("订单状态 ：待付款");
    tvTips.setText(mContext.getResources().getString(R.string.buy_seeds_list_tips));
    tvOrderSeller.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mVipListener.onVIPItemClick(getItem(position), position);
      }
    });

    if (item.getShowButton() == ShowButtonType.SHOW_LONG_TIME) {
      tvTips.setVisibility(View.VISIBLE);
      tvPayLongTime.setVisibility(View.VISIBLE);
      tvPayLongTime.setEnabled(true);
      tvPayLongTime.setText(mContext.getString(R.string.long_pay_time));
//      tvPayLongTime.setBackgroundDrawable(
//          DrableUtil.getStateListDrawable(mContext, R.drawable.round_all_blue_shape_one,
//              R.drawable.round_line_blue_shape));
      GradientDrawable shape = DrableUtil
          .getShape(mContext, R.drawable.round_line_blue_shape, DpUtil.dp2px(mContext, 5),
              DpUtil.dp2px(mContext, 1), R.color.blue);
//      tvPayLongTime.setBackgroundDrawable(shape);
      ViewUtil.setBackground(tvPayLongTime,shape);
      tvPayLongTime
          .setTextColor(ColorUtil.getColor(R.color.blue, mContext));
    } else if (item.getShowButton() == ShowButtonType.HIDE_LONG_TIME) {
      tvTips.setVisibility(View.GONE);
      tvPayLongTime.setVisibility(View.GONE);
    } else if (item.getShowButton() == ShowButtonType.GRAY_AGREE_LONG_TIME) {
      //卖家已同意
      tvTips.setVisibility(View.GONE);
      tvPayLongTime.setVisibility(View.VISIBLE);
      tvPayLongTime.setTextColor(ColorUtil.getColor(R.color.white, mContext));
      tvPayLongTime.setBackgroundColor(ColorUtil.getColor(R.color.cBBB, mContext));
     /* tvPayLongTime
          .setBackgroundDrawable(DrableUtil
              .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5),
                  DpUtil.dp2px(mContext, 1), R.color.cBBB));*/
      ViewUtil.setBackground(tvPayLongTime,DrableUtil
          .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5),
              DpUtil.dp2px(mContext, 1), R.color.cBBB));
      tvPayLongTime.setEnabled(false);
      tvPayLongTime.setText(mContext.getString(R.string.seller_agree));

    } else if (item.getShowButton() == ShowButtonType.GRAY_AGREE_LONG_TIME) {
      //卖家没响应
      tvTips.setVisibility(View.GONE);
      tvPayLongTime.setVisibility(View.VISIBLE);
      tvPayLongTime.setTextColor(ColorUtil.getColor(R.color.white, mContext));
      tvPayLongTime.setBackgroundColor(ColorUtil.getColor(R.color.cBBB, mContext));
//      tvPayLongTime
//          .setBackgroundDrawable(DrableUtil
//              .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5),
//                  DpUtil.dp2px(mContext, 1), R.color.cBBB));
      ViewUtil.setBackground(tvPayLongTime,DrableUtil
          .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5),
              DpUtil.dp2px(mContext, 1), R.color.cBBB));
      tvPayLongTime.setEnabled(false);
      tvPayLongTime.setText(mContext.getString(R.string.seller_no_respose));

    }

    tvPayLongTime.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mListener.onItem(getItem(position), position);
      }
    });

    tvUploadProof.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mPayListener.onCuiPayClick(getItem(position), position);
      }
    });
  }

}

package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.SellConfirmResultBean.ComplaintType;
import com.example.administrator.lubanone.bean.homepage.SellConfirmResultBean.GConfirmlistBean;
import com.example.administrator.lubanone.interfaces.OnItemListener;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DateUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DpUtil;
import com.example.qlibrary.utils.DrableUtil;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.ViewUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ProcessSellConfirmTimerAdapter extends ArrayAdapter<GConfirmlistBean> {

  private OnItemListener mListener;
  private OnListViewItemListener mReceverListener;
  private OnMoneyPayListener<GConfirmlistBean> mVIPListener;


  private Context mContext;

  private LayoutInflater lf;
  private List<ViewHolder> lstHolders;
  private Handler mHandler = new Handler();
  private Runnable updateRemainingTimeRunnable = new Runnable() {
    @Override
    public void run() {
      synchronized (lstHolders) {
        long currentTime = System.currentTimeMillis();
        for (ViewHolder holder : lstHolders) {
          holder.updateTimeRemaining(currentTime);
        }
      }
    }
  };


  public ProcessSellConfirmTimerAdapter(Context context, List<GConfirmlistBean> list,
      OnItemListener listener, OnMoneyPayListener<GConfirmlistBean> vipListener,
      OnListViewItemListener receverListener) {
    super(context, 0, list);
    this.mListener = listener;
    this.mVIPListener = vipListener;
    this.mReceverListener = receverListener;

    mContext = context;
    lf = LayoutInflater.from(context);
    lstHolders = new ArrayList<>();
    startUpdateTimer();
  }


  private void startUpdateTimer() {
    Timer tmr = new Timer();
    tmr.schedule(new TimerTask() {
      @Override
      public void run() {
        mHandler.post(updateRemainingTimeRunnable);
      }
    }, 1000, 1000);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ViewHolder holder = null;
    if (null == convertView) {

      holder = new ViewHolder();

      convertView = lf.inflate(R.layout.sell_comfrim_layout, null);

      holder.container = convertView.findViewById(R.id.ll_container);
      holder.tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
      holder.tvApplyTime = (TextView) convertView.findViewById(R.id.tv_apply_time);
      holder.tvSeedsCount = (TextView) convertView.findViewById(R.id.tv_seeds_count);
      holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_match_time);
      holder.tvOrderSeller = (TextView) convertView.findViewById(R.id.tv_seller);
      holder.tvHaveTime = (TextView) convertView.findViewById(R.id.tv_have_time);
      holder.tvTips = (TextView) convertView.findViewById(R.id.tv_tips);
      holder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tv_order_status);
      holder.tvPayLongTime = (TextView) convertView.findViewById(R.id.tv_pay_long_time);
      holder.tvUploadProof = (TextView) convertView.findViewById(R.id.tv_upload_proof);

      convertView.setTag(holder);

      synchronized (lstHolders) {
        lstHolders.add(holder);
      }

    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.setData(getItem(position), position);

    return convertView;
  }


  public class ViewHolder {

    View container;// =   viewHolder.getView(R.id.ll_container);
    TextView tvOrderId;//=    viewHolder.getView(R.id.tv_order_id);
    TextView tvApplyTime;//  =  viewHolder.getView(R.id.tv_apply_time);
    TextView tvSeedsCount;//   = viewHolder.getView(R.id.tv_seeds_count);
    TextView tvOrderTime;//  =  viewHolder.getView(R.id.tv_match_time);
    TextView tvOrderSeller;// =   viewHolder.getView(R.id.tv_seller);
    TextView tvHaveTime;// =   viewHolder.getView(R.id.tv_have_time);
    TextView tvTips;// =   viewHolder.getView(R.id.tv_tips);
    TextView tvOrderStatus;// =   viewHolder.getView(R.id.tv_order_status);
    TextView tvPayLongTime;     // =   viewHolder.getView(R.id.tv_pay_long_time);
    TextView tvUploadProof;     //=   viewHolder.getView(R.id.tv_upload_proof);


    GConfirmlistBean mProduct;

    public void setData(GConfirmlistBean item, final int position) {
      mProduct = item;
      updateTimeRemaining(System.currentTimeMillis());

      tvPayLongTime.setText(mContext.getString(R.string.confrim_receive_money));
      tvTips.setText(mContext.getString(R.string.tips));

      tvPayLongTime.setTextColor(ColorUtil.getColor(R.color.blue, mContext));
      ViewUtil.setBackground(tvPayLongTime,
          DrableUtil
              .getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5),
                  DpUtil.dp2px(mContext, 1),
                  R.color.blue));

      if (ComplaintType.GRAY.equals(item.getComplaintbutton())) {
        tvUploadProof.setTextColor(ColorUtil.getColor(R.color.white, mContext));

        tvUploadProof.setText(mContext.getString(R.string.has_complainted));
        ViewUtil.setBackground(tvUploadProof,
            DrableUtil
                .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5)));

        tvUploadProof.setEnabled(false);
      } else if (ComplaintType.NORMAL.equals(item.getComplaintbutton())) {
        tvUploadProof.setTextColor(ColorUtil.getColor(R.color.cEA5514, mContext));
        Drawable drawable = DrableUtil.getDrawable(mContext, R.drawable.round_line_orange_shape);
        ViewUtil.setBackground(tvUploadProof, drawable
        );
        tvUploadProof.setText(mContext.getString(R.string.no_money_compainted));

        tvUploadProof.setEnabled(true);
      }

      tvOrderId.setText(
          StringUtil.getBufferString(mContext.getString(R.string.order_code),
              DebugUtils.convert(item.getOrderid(), "")));
      tvApplyTime.setText(
          StringUtil
              .getBufferString(mContext.getString(R.string.apply_time),
                  DebugUtils.convert(item.getApplytime(), "")));
      tvSeedsCount.setText(
          StringUtil.getBufferString(mContext.getString(R.string.seed_count),
              DebugUtils.convert(item.getSeedcount(), "")));
      tvOrderTime.setText(
          StringUtil.getBufferString(mContext.getString(R.string.pay_time),
              DebugUtils.convert(item.getPaytime(), "")));
      tvOrderSeller.setText(
          StringUtil.getBufferString(mContext.getString(R.string.buy_vip),
              DebugUtils.convert(item.getBuymember(), "")));
//      tvHaveTime.setText("剩余确认/投诉时间 ： " + item.getTimeout());
      tvOrderStatus.setText(mContext.getString(R.string.sell_seeds_order_status));

      tvUploadProof.setVisibility(View.VISIBLE);
      container.setVisibility(View.VISIBLE);

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


    public void updateTimeRemaining(long currentTime) {
      if (TextUtils.isEmpty(mProduct.getEndtime()) || null == DateUtil
          .parseDateString(mProduct.getEndtime())) {
        tvHaveTime.setText(mContext.getString(R.string.no_time));
        return;
      }
      long timeDiff = DateUtil.parseDateString(mProduct.getEndtime()) - currentTime;
      if (timeDiff > 0) {
        int seconds = (int) (timeDiff / 1000) % 60;
        int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
        int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);

        if (null != tvHaveTime) {
          tvHaveTime.setText(StringUtil
              .getBufferString(mContext.getString(R.string.remain_pay_time), hours + "",
                  mContext.getString(hours), minutes + "",
                  mContext.getString(minutes),
                  seconds + "", mContext.getString(seconds)));
        }
      } else {
        if (null != tvHaveTime) {
          tvHaveTime.setText(mContext.getString(R.string.no_time));
        }
      }
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

package com.example.administrator.lubanone.adapter.homepage;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BuyMoneyListResultBean.PPaylistBean;
import com.example.administrator.lubanone.bean.homepage.BuyMoneyListResultBean.ShowButtonType;
import com.example.administrator.lubanone.interfaces.OnBuyVIPListener;
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

public class ProcessNoMoneyTimerAdapter extends ArrayAdapter<PPaylistBean> {

  private OnListViewItemListener mListener;
  private OnMoneyPayListener<PPaylistBean> mPayListener;
  private OnBuyVIPListener mVipListener;

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
          System.out.println("ProcessNoMoneyTimerAdapter.run");
          holder.updateTimeRemaining(currentTime);
        }
      }
    }
  };


  public ProcessNoMoneyTimerAdapter(Context context, List<PPaylistBean> list,
      OnListViewItemListener listener, OnMoneyPayListener<PPaylistBean> payListener,
      OnBuyVIPListener vipListener) {
    super(context, 0, list);
    this.mListener = listener;
    this.mPayListener = payListener;
    this.mVipListener = vipListener;

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

      convertView = lf.inflate(R.layout.no_money_layout, null);

      holder.tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
      holder.tvSeedsCount = (TextView) convertView.findViewById(R.id.tv_seeds_count);
      holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_match_time);
      holder.tvOrderSeller = (TextView) convertView.findViewById(R.id.tv_seller);
      holder.tvHaveTime = (TextView) convertView.findViewById(R.id.tv_have_time);
      holder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tv_order_status);
      holder.ll_container = (LinearLayout) convertView.findViewById(R.id.ll_container);
      holder.tvTips = (TextView) convertView.findViewById(R.id.tv_tips);
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


  private class ViewHolder {

    TextView tvOrderId;
    TextView tvSeedsCount;
    TextView tvOrderTime;
    TextView tvOrderSeller;
    TextView tvHaveTime;
    TextView tvOrderStatus;
    LinearLayout ll_container;
    TextView tvTips;
    TextView tvPayLongTime;
    TextView tvUploadProof;


    PPaylistBean mProduct;

    public void setData(PPaylistBean item, final int position) {
      mProduct = item;
      updateTimeRemaining(System.currentTimeMillis());

      tvOrderId.setText(
          StringUtil
              .getBufferString(mContext.getString(R.string.order_code),
                  DebugUtils.convert(mProduct.getOrderid(), "")));
      tvSeedsCount
          .setText(StringUtil
              .getBufferString(mContext.getString(R.string.seed_count),
                  DebugUtils.convert(mProduct.getSeedcount(), "")));
      tvOrderTime.setText(StringUtil
          .getBufferString(mContext.getString(R.string.match_time),
              DebugUtils.convert(mProduct.getMatchtime(), "")));
      tvOrderSeller.setText(StringUtil
          .getBufferString(mContext.getString(R.string.seller_vip),
              DebugUtils.convert(mProduct.getSellmember(), "")));
      tvOrderStatus.setText(mContext.getString(R.string.order_no_pay));
      tvTips.setText(mContext.getResources().getString(R.string.buy_seeds_list_tips));

      tvOrderSeller.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mVipListener.onVIPItemClick(getItem(position), position);
        }
      });

      if (ShowButtonType.SHOW_LONG_TIME.equals(mProduct.getShowButton())) {//正常显示延长打款时间按钮
        tvPayLongTime.setVisibility(View.VISIBLE);
        tvPayLongTime.setEnabled(true);
        tvPayLongTime.setText(mContext.getString(R.string.long_pay_time));
        GradientDrawable shape = DrableUtil
            .getShape(mContext, R.color.white, DpUtil.dp2px(mContext, 5),
                DpUtil.dp2px(mContext, 1), R.color.colorPrimaryDark);
//        tvPayLongTime.setBackgroundDrawable(shape);
        ViewUtil.setBackground(tvPayLongTime, shape);
        tvPayLongTime
            .setTextColor(ColorUtil.getColor(R.color.blue, mContext));
      } else if (mProduct.getShowButton() == ShowButtonType.HIDE_LONG_TIME) {//隐藏延长打款时间按钮
        tvPayLongTime.setVisibility(View.GONE);
      } else if (ShowButtonType.GRAY_AGREE_LONG_TIME.equals(mProduct.getShowButton())) {//置灰延长打款时间按钮
        //卖家已同意
        tvPayLongTime.setVisibility(View.VISIBLE);
        tvPayLongTime.setTextColor(ColorUtil.getColor(R.color.white, mContext));
//        tvPayLongTime
//            .setBackgroundDrawable(DrableUtil
//                .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5)));
        ViewUtil.setBackground(tvPayLongTime, DrableUtil
            .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5)));
        tvPayLongTime.setEnabled(false);
        tvPayLongTime.setText(mContext.getString(R.string.seller_agree));

      } else if (ShowButtonType.GRAY_LONG_TIME.equals(mProduct.getShowButton())) {//置灰延长打款时间按钮
        //卖家没响应
        tvPayLongTime.setVisibility(View.VISIBLE);
        tvPayLongTime.setTextColor(ColorUtil.getColor(R.color.white, mContext));
//        tvPayLongTime
//            .setBackgroundDrawable(DrableUtil
//                .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5)));
        ViewUtil.setBackground(tvPayLongTime, DrableUtil
            .getShape(mContext, R.color.cBBB, DpUtil.dp2px(mContext, 5)));
        tvPayLongTime.setEnabled(false);
        tvPayLongTime.setText(mContext.getString(R.string.seller_no_respose));

      }

      tvPayLongTime.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mListener.onItem(getItem(position), position);
        }
      });

      tvUploadProof.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mPayListener.onCuiPayClick(getItem(position), position);
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

        if (timeDiff < 1000 * 60 * 60) {
          if (null != tvTips) {
            tvTips.setVisibility(View.VISIBLE);
          }
        } else {
          if (null != tvTips) {
            tvTips.setVisibility(View.GONE);
          }
        }
      } else {
        if (null != tvHaveTime) {
          tvHaveTime.setText(mContext.getString(R.string.no_time));
        }
        if (null != tvTips) {
          tvTips.setVisibility(View.VISIBLE);
        }
      }
    }
  }

}

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
import com.example.qlibrary.utils.DemicalUtil;
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

public class ProcessSellConfirmTimerAdapterNew extends ArrayAdapter<GConfirmlistBean> {

  private OnItemListener mListener;
  private OnListViewItemListener mReceverListener;
  private OnMoneyPayListener<GConfirmlistBean> mVIPListener;


  private Context mContext;
  private String mPrice;

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


  public ProcessSellConfirmTimerAdapterNew(Context context, List<GConfirmlistBean> list,
      OnItemListener listener, OnMoneyPayListener<GConfirmlistBean> vipListener,
      OnListViewItemListener receverListener, String price) {
    super(context, 0, list);
    this.mListener = listener;
    this.mVIPListener = vipListener;
    this.mReceverListener = receverListener;
    this.mPrice = price;

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

      convertView = lf.inflate(R.layout.sell_comfrim_layout_new, null);

      holder.container = convertView.findViewById(R.id.ll_container);
      holder.tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
      holder.tvApplyTime = (TextView) convertView.findViewById(R.id.tv_apply_time);
      holder.tvSeedsCount = (TextView) convertView.findViewById(R.id.tv_seeds_count);
      holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_match_time);
      holder.tvOrderSeller = (TextView) convertView.findViewById(R.id.tv_seller);
      holder.tvHaveTime = (TextView) convertView.findViewById(R.id.tv_have_time);
      holder.tvPayLongTime = (TextView) convertView.findViewById(R.id.tv_pay_long_time);//确认收款
      holder.tvUploadProof = (TextView) convertView.findViewById(R.id.tv_upload_proof);//我要投诉
      holder.tv_heji = (TextView) convertView.findViewById(R.id.tv_heji);
      holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
      holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);

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
    TextView tvPayLongTime;     // =   viewHolder.getView(R.id.tv_pay_long_time);
    TextView tvUploadProof;     //=   viewHolder.getView(R.id.tv_upload_proof);
    TextView tv_heji;     //=   viewHolder.getView(R.id.tv_upload_proof);
    TextView tv_status;     //=   viewHolder.getView(R.id.tv_upload_proof);
    TextView tv_title;     //=   viewHolder.getView(R.id.tv_upload_proof);


    GConfirmlistBean mProduct;

    public void setData(GConfirmlistBean item, final int position) {

      tvUploadProof.setText(mContext.getString(R.string.no_money_compainted));

      mProduct = item;
      updateTimeRemaining(System.currentTimeMillis());

//      Long aLong = DateUtil.parseDateString(item.getEndtime());
//      long timeMillis = System.currentTimeMillis();
//      if (null != aLong && aLong - timeMillis < 0) {
//        tv_status.setText(mContext.getString(R.string.than_time));
//      }

      tvPayLongTime.setText(mContext.getString(R.string.confrim_receive_money));//确认收款按钮

      //隐藏未收到款，我要投诉
      if (ComplaintType.GRAY.equals(item.getComplaintbutton())) {
        tvUploadProof.setVisibility(View.GONE);//我要投诉按钮
        tv_status.setText(mContext.getString(R.string.has_complainted_new));
        tvPayLongTime.setVisibility(View.GONE);//确认收款gone

        tv_title.setText(mContext.getString(R.string.shenyu_time_new));

      } else if (ComplaintType.NORMAL.equals(item.getComplaintbutton())) {
        tvUploadProof.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
        Drawable drawable = DrableUtil.getDrawable(mContext, R.drawable.round_line_orange_shape);
        ViewUtil.setBackground(tvUploadProof, drawable);
        tvUploadProof.setText(mContext.getString(R.string.no_money_compainted));
        tvUploadProof.setEnabled(true);

        tv_title.setText(mContext.getString(R.string.shenyu_time));

        tvUploadProof.setVisibility(View.VISIBLE);
        tvPayLongTime.setVisibility(View.VISIBLE);
      }

      //合计
      String multile = DemicalUtil
          .multile(DebugUtils.convert(item.getSeedcount(), "0"),
              DebugUtils.convert(mPrice, "0"));
      String s1 = mContext.getString(R.string.heji) + " " + StringUtil.getThreeString(multile);
      StringUtil.setTextSizeNewOne(s1, tv_heji, 11, 15, '¥', '¥',
          ColorUtil.getColor(R.color.c333, mContext));

      //种子数量
      String s = item.getSeedcount() + "PCS";
      StringUtil.setTextSize(s, tvSeedsCount, 25, 9);

      tvOrderId.setText(
          StringUtil.getBufferString(mContext.getString(R.string.order_code),
              DebugUtils.convert(item.getOrderid(), "")));
      tvApplyTime.setText(
          StringUtil
              .getBufferString(mContext.getString(R.string.apply_time),
                  DebugUtils.convert(item.getApplytime(), "")));

      tvOrderTime.setText(
          StringUtil.getBufferString(mContext.getString(R.string.pay_time),
              DebugUtils.convert(item.getPaytime(), "")));
      tvOrderSeller.setText(
          StringUtil.getBufferString(mContext.getString(R.string.buy_vip),
              DebugUtils.convert(item.getBuymember(), "")));

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

      //投诉过
      if (ComplaintType.GRAY.equals(mProduct.getComplaintbutton())) {
        if (TextUtils.isEmpty(mProduct.getDealendtime()) || null == DateUtil
            .parseDateString(mProduct.getDealendtime())) {
          if (null != tvHaveTime) {
            tvHaveTime.setText(mContext.getString(R.string.zero_time));
          }
          return;
        }
        long timeDiff = DateUtil.parseDateString(mProduct.getDealendtime()) - currentTime;
        if (timeDiff > 0) {
          int seconds = (int) (timeDiff / 1000) % 60;
          int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
          int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);

          if (null != tvHaveTime) {
            tvHaveTime.setText(StringUtil
                .getBufferString(String.valueOf(hours < 10 ? "0" + hours : hours), ":",
                    (minutes < 10 ? "0" + minutes : minutes) + ":",
                    (seconds < 10 ? "0" + seconds : seconds) + ""));
          }

        } else {
          if (null != tvHaveTime) {
            tvHaveTime.setText(mContext.getString(R.string.zero_time));
          }

          //超时
          if (null != tvUploadProof) {
            tvUploadProof.setVisibility(View.GONE);
          }
          if (null != tvPayLongTime) {
            tvPayLongTime.setVisibility(View.GONE);
          }

//        tv_status.setText(mContext.getString(R.string.than_time));

//        tvUploadProof.setEnabled(false);
//        tvPayLongTime.setEnabled(false);
//
//        ViewsUtils.grayBgWhiteTextRound4(tvUploadProof, mContext);
//        ViewsUtils.grayBgWhiteTextRound4(tvPayLongTime, mContext);
        }
      } else {
        if (TextUtils.isEmpty(mProduct.getEndtime()) || null == DateUtil
            .parseDateString(mProduct.getEndtime())) {
          if (tvHaveTime != null) {
            tvHaveTime.setText(mContext.getString(R.string.zero_time));
          }
          return;
        }
        long timeDiff = DateUtil.parseDateString(mProduct.getEndtime()) - currentTime;
        if (timeDiff > 0) {
          int seconds = (int) (timeDiff / 1000) % 60;
          int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
          int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);

          if (null != tvHaveTime) {
            tvHaveTime.setText(StringUtil
                .getBufferString(String.valueOf(hours < 10 ? "0" + hours : hours), ":",
                    (minutes < 10 ? "0" + minutes : minutes) + ":",
                    (seconds < 10 ? "0" + seconds : seconds) + ""));
          }
        } else {
          if (null != tvHaveTime) {
            tvHaveTime.setText(mContext.getString(R.string.zero_time));
          }
          //超时
          if (null != tvUploadProof) {
            tvUploadProof.setVisibility(View.GONE);
          }
          if (null != tvUploadProof) {
            tvPayLongTime.setVisibility(View.GONE);
          }

//        tv_status.setText(mContext.getString(R.string.than_time));

//        tvUploadProof.setEnabled(false);
//        tvPayLongTime.setEnabled(false);
//
//        ViewsUtils.grayBgWhiteTextRound4(tvUploadProof, mContext);
//        ViewsUtils.grayBgWhiteTextRound4(tvPayLongTime, mContext);
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

package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.CuiType;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.PConfirmlistBean;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.ProofType;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.StatusType;
import com.example.administrator.lubanone.interfaces.OnItemListener;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
import com.example.qlibrary.interfaces.OnClickItemListener;
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
 * Created by quyang on 2017/6/30.
 */

public class ProcessConfirmTimerAdapter extends ArrayAdapter<PConfirmlistBean> {

  private OnItemListener mListener;
  private OnMoneyPayListener<PConfirmlistBean> mVIPListener;


  private Context mContext;
  private LayoutInflater lf;
  private List<ViewHolder> lstHolders;
  private OnClickItemListener mLianXiListener;

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


  public ProcessConfirmTimerAdapter(Context context, List<PConfirmlistBean> list,
      OnItemListener listener, OnMoneyPayListener<PConfirmlistBean> vipListener,
      OnClickItemListener mLianXiListener) {
    super(context, 0, list);
    this.mListener = listener;
    this.mVIPListener = vipListener;
    this.mLianXiListener = mLianXiListener;

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

      holder.container = convertView.findViewById(R.id.ll_container);
      holder.tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
      holder.tvSeedsCount = (TextView) convertView.findViewById(R.id.tv_seeds_count);
      holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_match_time);
      holder.tvOrderSeller = (TextView) convertView.findViewById(R.id.tv_seller);
      holder.tvHaveTime = (TextView) convertView.findViewById(R.id.tv_have_time);//订单状态
      holder.tvTips = (TextView) convertView.findViewById(R.id.tv_tips);
      holder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tv_order_status);
      holder.tvPayLongTime = (TextView) convertView.findViewById(R.id.tv_pay_long_time);
      holder.tvUploadProof = (TextView) convertView.findViewById(R.id.tv_upload_proof);
      holder.tv_shengyu_pay_time = (TextView) convertView.findViewById(R.id.tv_shengyu_pay_time);

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

  //上传付款视频
  private void updatePic(TextView tvUploadProof, String showpic2button, TextView tvTips,
      TextView orderStatus, int position) {

    if (ProofType.GRAY.equals(showpic2button)) {//灰色
      tvUploadProof.setVisibility(View.VISIBLE);
      tvUploadProof.setText(mContext.getString(R.string.has_upload_proof_video));
      tvUploadProof.setTextColor(Color.WHITE);
      tvUploadProof.setEnabled(false);
      Drawable drawable = DrableUtil.getDrawable(mContext, R.drawable.gray_white_shape);
      ViewUtil.setBackground(tvUploadProof, drawable);

//      tvTips.setText(mContext.getString(R.string.conplaint_proof_tips));
      tvTips.setVisibility(View.VISIBLE);
      StringUtil
          .setPartColorAndClickableInList(mContext.getString(R.string.conplaint_proof_tips), tvTips,
              mContext.getString(R.string.conplaint_proof_tips).length() - 5,
              mContext.getString(R.string.conplaint_proof_tips).length() - 1, mLianXiListener,
              position, ColorUtil.getColor(R.color.blue, mContext));

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
      Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.round_line_orange_shape);
      ViewUtil.setBackground(tvUploadProof,
          drawable);

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


  private class ViewHolder {

    View container;
    TextView tvOrderId;
    TextView tvSeedsCount;
    TextView tvOrderTime;
    TextView tvOrderSeller;
    TextView tvHaveTime;
    TextView tvTips;
    TextView tvOrderStatus;
    TextView tvPayLongTime;
    TextView tvUploadProof;
    TextView tv_shengyu_pay_time;

    PConfirmlistBean mProduct;


    public void setData(final PConfirmlistBean item, final int position) {
      mProduct = item;
//      updateTimeRemaining(System.currentTimeMillis());

      container.setVisibility(View.VISIBLE);
      tvUploadProof.setVisibility(View.VISIBLE);
      tvPayLongTime.setVisibility(View.GONE);

      tvOrderSeller.setOnClickListener(new MyOnClickListener(position));

      String buypressbutton = item.getBuypressbutton();
      String showpic2button = item.getShowpic2button();

      tvOrderId.setText(new StringBuilder().append(mContext.getString(R.string.order_code))
          .append(DebugUtils.convert(item.getOrderid(), "")));
      tvSeedsCount.setText(new StringBuilder().append(mContext.getString(R.string.seed_count))
          .append(DebugUtils.convert(item.getSeedcount(), "")));
      tvOrderTime.setText(new StringBuilder().append(mContext.getString(R.string.pay_time))
          .append(DebugUtils.convert(item.getPaytime(), "")));
      tvOrderSeller.setText(new StringBuilder().append(mContext.getString(R.string.seller_vip))
          .append(DebugUtils.convert(item.getSellmember(), "")));

      if (StatusType.NO_COMPLAIN.equals(item.getStatus())) {//没有被投诉
        tvTips.setVisibility(View.VISIBLE);
        tvTips.setText(mContext.getString(R.string.buy_confim_tips));

        updatePressButton(tvUploadProof, buypressbutton, tvOrderStatus);

        tvHaveTime.setTextColor(ColorUtil.getColor(R.color.c000, mContext));//剩余确认时间
//        tvHaveTime.setText("剩余确认时间 ：" + item.getTimedifferent());

        updateTimeRemaining(System.currentTimeMillis());
      }
      if (StatusType.COMPLAINTED.equals(item.getStatus())) {//被投诉
        updatePic(tvUploadProof, showpic2button, tvTips, tvHaveTime, position);

        tvOrderStatus.setTextColor(ColorUtil.getColor(R.color.cEA5514, mContext));//投诉时间
        tvOrderStatus.setText(StringUtil
            .getBufferString(mContext.getString(R.string.complain_time),
                DebugUtils.convert(item.getTs_time(), "")));

      }

      tvUploadProof.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          //催确认
          mListener.onItem(getItem(position), position, Integer.parseInt(item.getStatus()));
        }
      });

    }


    public void updateTimeRemaining(long currentTime) {
      String status = mProduct.getStatus();
      if (StatusType.COMPLAINTED.equals(status)) {
        tvHaveTime.setText(new StringBuilder().append(mContext.getString(R.string.order_status))
            .append(mContext.getString(R.string.complainted)));
      } else {
        if (TextUtils.isEmpty(mProduct.getEndtime())
            || DateUtil.parseDateString(mProduct.getEndtime()) == null) {
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
                    mContext.getString(R.string.hours), minutes + "",
                    mContext.getString(R.string.minutes),
                    seconds + "", mContext.getString(R.string.seconds)));
          }

        } else {
          if (null != tvHaveTime) {
            tvHaveTime.setText(mContext.getString(R.string.no_time));
          }
        }
      }

    }
  }


}

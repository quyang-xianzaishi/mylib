package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
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
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.ComplainType;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.CuiType;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.GPaylistBean;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.LongPayType;
import com.example.administrator.lubanone.bean.homepage.SellMoneyListResultBean.StatusType;
import com.example.administrator.lubanone.interfaces.OnBuyVIPListener;
import com.example.administrator.lubanone.interfaces.OnItemListener;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
import com.example.administrator.lubanone.utils.ViewsUtils;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DateUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ProcessSellNoMoneyTimerAdapterNew extends ArrayAdapter<GPaylistBean> {

  private OnMoneyPayListener<GPaylistBean> mPayListener;
  private OnBuyVIPListener mVipListener;
  private OnItemListener itemListener;


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

  public ProcessSellNoMoneyTimerAdapterNew(Context context, List<GPaylistBean> list,
      OnMoneyPayListener<GPaylistBean> payListener,
      OnBuyVIPListener vipListener, OnItemListener itemListener, String price) {
    super(context, 0, list);
    this.mPayListener = payListener;
    this.mVipListener = vipListener;
    this.itemListener = itemListener;
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

      convertView = lf.inflate(R.layout.sell_no_money_layout_new, null);

      holder.tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
      holder.tvApplyTime = (TextView) convertView.findViewById(R.id.tv_apply_time);
      holder.tvSeedsCount = (TextView) convertView.findViewById(R.id.tv_seeds_count);
      holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_match_time);
      holder.tvOrderSeller = (TextView) convertView.findViewById(R.id.tv_seller);
      holder.tvHaveTime = (TextView) convertView.findViewById(R.id.tv_have_time);
      holder.tvCui = (TextView) convertView.findViewById(R.id.tv_pay_long_time);// 催一催
      holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
      holder.tv_tousu = (TextView) convertView.findViewById(R.id.tv_tousu);//我要投诉
      holder.tvYanChangPay = (TextView) convertView.findViewById(R.id.tv_yangchang);//延长打款
      holder.tv_heji = (TextView) convertView.findViewById(R.id.tv_heji);
      holder.tv_lianxi = (TextView) convertView.findViewById(R.id.tv_lianxi);

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

    TextView tvOrderId;
    TextView tvApplyTime;
    TextView tvSeedsCount;
    TextView tvOrderTime;
    TextView tvOrderSeller;
    TextView tvHaveTime;
    TextView tvCui;
    TextView tv_tousu;
    TextView tvUploadProof;
    TextView tv_status;
    TextView tvYanChangPay;
    TextView tv_heji;
    TextView tv_lianxi;


    GPaylistBean mProduct;

    public void setData(GPaylistBean item, final int position) {
      mProduct = item;
      updateTimeRemaining(System.currentTimeMillis());

//      Long aLong = DateUtil.parseDateString(item.getEndtime());
//      long timeMillis = System.currentTimeMillis();
//      if (null != aLong && aLong - timeMillis < 0) {
//        tv_status.setText(mContext.getString(R.string.than_time));
//      }
      tvCui.setVisibility(View.GONE);
      tv_tousu.setVisibility(View.GONE);
      tvYanChangPay.setVisibility(View.GONE);

      //合计
      String multile = DemicalUtil
          .multile(DebugUtils.convert(item.getSeedcount(), "0"),
              DebugUtils.convert(mPrice, "0"));
      String s1 = mContext.getString(R.string.heji) + " " + StringUtil.getThreeString(multile);
      StringUtil.setTextSizeNewOne(s1, tv_heji, 11, 15, '¥', '¥',
          ColorUtil.getColor(R.color.c333, mContext));

      //超时
      if (StatusType.COMPLAINTED.equals(item.getStatus())) {
        //只显示我要投诉
        //投诉
        String complaintbutton = item.getComplaintbutton();
        updateTouSu(complaintbutton, tv_tousu, tv_status);

      } else {
        //催一崔
        String sellpressbutton = item.getSellpressbutton();
        tvCui.setText(mContext.getString(R.string.cui_yi_cui));//催一催
        updateCui(sellpressbutton, tvCui, tv_status);

        //延长
        String prolongbutton = item.getProlongbutton();
        tvYanChangPay.setText(mContext.getString(R.string.long_pay));//延长打款时间
        updateLong(prolongbutton, tvYanChangPay);
      }

      tvOrderId.setText(StringUtil.getBufferString(mContext.getString(R.string.order_code),
          DebugUtils.convert(item.getOrderid(), "")));
      tvApplyTime.setText(StringUtil.getBufferString(mContext.getString(R.string.apply_time),
          DebugUtils.convert((String) item.getApplytime(), "")));

      //种子数量
      String s = DebugUtils.convert(item.getSeedcount(), "0") + "PCS";
      StringUtil.setTextSize(s, tvSeedsCount, 25, 9);

      tvOrderTime.setText(StringUtil.getBufferString(mContext.getString(R.string.match_time),
          DebugUtils.convert(item.getMatchtime(), "")));
      tvOrderSeller.setText(StringUtil.getBufferString(mContext.getString(R.string.buy_vip),
          DebugUtils.convert(item.getBuymember(), "")));

      //点击事件
      tv_tousu.setOnClickListener(new MyOnClickListener(1, getItem(position), position));
      tvYanChangPay.setOnClickListener(new MyOnClickListener(2, getItem(position), position));

      //催一崔
      tvCui.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mPayListener.onCuiPayClick(getItem(position), position);
        }
      });
    }


    public void updateTimeRemaining(long currentTime) {
      if (TextUtils.isEmpty(mProduct.getEndtime()) || null == DateUtil
          .parseDateString(mProduct.getEndtime())) {
        if (null != tvHaveTime) {
          tvHaveTime.setText(mContext
              .getString(R.string.zero_time));
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
              .getBufferString((hours < 10 ? "0" + hours : hours) + ":",
                  (minutes < 10 ? "0" + minutes : minutes) + ":",
                  (seconds < 10 ? "0" + seconds : seconds) + ""));
        }

      } else {
        if (null != tvHaveTime) {
          tvHaveTime.setText(mContext
              .getString(R.string.zero_time));

        }
      }
    }
  }

  private void updateLong(String prolongbutton, TextView tvYanChangPay) {
    if (LongPayType.GONE.equals(prolongbutton)) {
      tvYanChangPay.setVisibility(View.GONE);
    } else if (LongPayType.DISPLAY_CAN_CLICK.equals(prolongbutton)) {
      tvYanChangPay.setVisibility(View.VISIBLE);
      tvYanChangPay.setEnabled(true);
      tvYanChangPay.setText(mContext.getResources().getString(R.string.longer_pay_time));

      ViewsUtils.grayBgLineGrayTextRound4(tvYanChangPay, mContext);
    }
  }

  private void updateCui(String sellpressbutton, TextView tvCui, TextView tv_status) {
    if (CuiType.GONE.equals(sellpressbutton)) {//gone
      tvCui.setVisibility(View.GONE);
    } else if (CuiType.DISPLAY_CAN_CLICK.equals(sellpressbutton)) {//显示可点击
      tvCui.setVisibility(View.VISIBLE);
      tvCui.setEnabled(true);

      //灰色边框 灰色文字
      ViewsUtils.grayBgLineGrayTextRound4(tvCui, mContext);

    } else if (CuiType.DISPLAY_NO_CLICK.equals(sellpressbutton)) {//显示不可点击
      tvCui.setVisibility(View.GONE);
      tv_status.setText(mContext.getString(R.string.has_cui));
    }
  }

  private void updateTouSu(String complaintbutton, TextView tv_tousu, TextView status) {

    if (ComplainType.GONE.equals(complaintbutton)) {//隐藏
      tv_tousu.setVisibility(View.GONE);
    } else if (ComplainType.DISPLAY.equals(complaintbutton)) {//显示
      tv_tousu.setVisibility(View.VISIBLE);
      tv_tousu.setEnabled(true);
      ViewsUtils.organeBorderBgOrganeTextRound4(tv_tousu, mContext);

    }
//    else if (ComplainType.GRAY.equals(complaintbutton)) {//灰色
//      tv_tousu.setVisibility(View.GONE);
//      status.setText(mContext.getString(R.string.has_complainted_new));
//    }

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

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
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ProcessNoMoneyTimerAdapterNew extends ArrayAdapter<PPaylistBean> {

  private OnListViewItemListener mListener;
  private OnMoneyPayListener<PPaylistBean> mPayListener;
  private OnBuyVIPListener mVipListener;

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
          System.out.println("ProcessNoMoneyTimerAdapter.run");
          holder.updateTimeRemaining(currentTime);
        }
      }
    }
  };


  public ProcessNoMoneyTimerAdapterNew(Context context, List<PPaylistBean> list,
      OnListViewItemListener listener, OnMoneyPayListener<PPaylistBean> payListener,
      OnBuyVIPListener vipListener, String price) {
    super(context, 0, list);
    this.mListener = listener;
    this.mPayListener = payListener;
    this.mVipListener = vipListener;
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

      convertView = lf.inflate(R.layout.no_money_layout_new, null);

      holder.tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
      holder.tvSeedsCount = (TextView) convertView.findViewById(R.id.tv_seeds_count);
      holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_match_time);
      holder.tvOrderSeller = (TextView) convertView.findViewById(R.id.tv_seller);
      holder.tvHaveTime = (TextView) convertView.findViewById(R.id.tv_have_time);
      holder.tv_lianxi = (TextView) convertView.findViewById(R.id.tv_lianxi);
      holder.ll_container = (LinearLayout) convertView.findViewById(R.id.ll_container);
      holder.tvPayLongTime = (TextView) convertView.findViewById(R.id.tv_pay_long_time);
      holder.tvUploadProof = (TextView) convertView.findViewById(R.id.tv_upload_proof);
      holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
      holder.tv_heji = (TextView) convertView.findViewById(R.id.tv_heji);
      holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

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
    TextView tv_lianxi;
    LinearLayout ll_container;
    TextView tvTips;
    TextView tvPayLongTime;
    TextView tvUploadProof;
    TextView tv_status;
    TextView tv_heji;
    TextView tv_time;


    PPaylistBean mProduct;

    public void setData(PPaylistBean item, final int position) {
      mProduct = item;
      updateTimeRemaining(System.currentTimeMillis());

//      if (TextUitl.isNotEmpty(mProduct.getEndtime())) {
//        Long aLong = DateUtil.parseDateString(mProduct.getEndtime());
//        if (null != aLong && aLong - System.currentTimeMillis() < 0) {
//          tv_status.setText(mContext.getString(R.string.than_time));
//        }
//      }

      tvOrderId.setText(
          StringUtil
              .getBufferString(mContext.getString(R.string.order_code),
                  DebugUtils.convert(mProduct.getOrderid(), "")));

      //种子数量
      String s = DebugUtils.convert(item.getSeedcount(), "0") + "PCS";
      StringUtil.setTextSize(s, tvSeedsCount, 25, 9);

      tvOrderTime.setText(StringUtil
          .getBufferString(mContext.getString(R.string.match_time),
              DebugUtils.convert(mProduct.getMatchtime(), "")));
      tvOrderSeller.setText(StringUtil
          .getBufferString(mContext.getString(R.string.seller_vip),
              DebugUtils.convert(mProduct.getSellmember(), "")));

      //合计
      String multile = DemicalUtil.multile(DebugUtils.convert(item.getSeedcount(), "0"),
          DebugUtils.convert(mPrice, "0"));
      String s1 = mContext.getString(R.string.heji) + " " + StringUtil.getThreeString(multile);
      StringUtil.setTextSizeNewOne(s1, tv_heji, 11, 15, '¥', '¥',
          ColorUtil.getColor(R.color.c333, mContext));

      tv_lianxi.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          mVipListener.onVIPItemClick(getItem(position), position);
        }
      });

      if (ShowButtonType.SHOW_LONG_TIME.equals(mProduct.getShowButton())) {//正常显示延长打款时间按钮
        tvPayLongTime.setVisibility(View.VISIBLE);
        tvPayLongTime.setEnabled(true);

      } else if (ShowButtonType.HIDE_LONG_TIME.equals(mProduct.getShowButton())) {//隐藏延长打款时间按钮
        tvPayLongTime.setVisibility(View.GONE);
      } else if (ShowButtonType.GRAY_AGREE_LONG_TIME.equals(mProduct.getShowButton())) {//置灰延长打款时间按钮
        //卖家已同意
        tvPayLongTime.setVisibility(View.GONE);
        tv_status.setText(mContext.getString(R.string.seller_agree));

      } else if (ShowButtonType.GRAY_LONG_TIME.equals(mProduct.getShowButton())) {//置灰延长打款时间按钮
        //卖家没响应
        tvPayLongTime.setVisibility(View.GONE);
        tv_status.setText(mContext.getString(R.string.seller_no_respose_one));
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
        tv_time.setText(mContext.getString(R.string.zero_time));
        return;
      }
      long timeDiff = DateUtil.parseDateString(mProduct.getEndtime()) - currentTime;
      if (timeDiff > 0) {
        int seconds = (int) (timeDiff / 1000) % 60;
        int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
        int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);

        if (null != tv_time) {
          tv_time.setText(StringUtil
              .getBufferString((hours < 10 ? "0" + hours : "" + hours) + ":",
                  (minutes < 10 ? "0" + minutes : "" + minutes) + ":",
                  (seconds < 10 ? "0" + seconds : "" + seconds)));
        }

      } else {
        if (null != tv_time) {
          tv_time.setText(mContext.getString(R.string.zero_time));
        }
      }
    }
  }

}

package com.example.administrator.lubanone.adapter.homepage;

import static com.example.administrator.lubanone.R.id.tv_order_status;

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
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TimeUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by quyang on 2017/6/30.
 */

public class ProcessConfirmTimerAdapterNew extends ArrayAdapter<PConfirmlistBean> {

  private OnItemListener mListener;
  private OnMoneyPayListener<PConfirmlistBean> mVIPListener;


  private Context mContext;
  private LayoutInflater lf;
  private List<ViewHolder> lstHolders;
  private OnClickItemListener mLianXiListener;
  private String mPrice;

  private Handler mHandler = new Handler();
  private Runnable updateRemainingTimeRunnable = new Runnable() {
    @Override
    public void run() {
      synchronized (lstHolders) {
        long currentTime = System.currentTimeMillis();
        for (ViewHolder holder : lstHolders) {
          synchronized (this) {
            holder.updateTimeRemaining(currentTime);
          }
        }
      }
    }
  };


  public ProcessConfirmTimerAdapterNew(Context context, List<PConfirmlistBean> list,
      OnItemListener listener, OnMoneyPayListener<PConfirmlistBean> vipListener,
      OnClickItemListener mLianXiListener, String price) {
    super(context, 0, list);
    this.mListener = listener;
    this.mVIPListener = vipListener;
    this.mLianXiListener = mLianXiListener;
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

      convertView = lf.inflate(R.layout.no_money_layout_new2, null);

      holder.container = convertView.findViewById(R.id.ll_container);
      holder.tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
      holder.tvSeedsCount = (TextView) convertView.findViewById(R.id.tv_seeds_count);
      holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tv_match_time);
      holder.tvOrderSeller = (TextView) convertView.findViewById(R.id.tv_seller);
      holder.tvHaveTime = (TextView) convertView.findViewById(R.id.tv_have_time);
      holder.tv_order_status = (TextView) convertView.findViewById(tv_order_status);
      holder.tvUploadProof = (TextView) convertView.findViewById(R.id.tv_upload_proof);
      holder.tvHours = (TextView) convertView.findViewById(R.id.tv_hours);
      holder.tvMinus = (TextView) convertView.findViewById(R.id.tv_minus);
      holder.tvSeconds = (TextView) convertView.findViewById(R.id.tv_seconds);
      holder.tv_heji = (TextView) convertView.findViewById(R.id.tv_heji);
      holder.tv_lianxi = (TextView) convertView.findViewById(R.id.tv_lianxi);
      holder.tv_cui = (TextView) convertView.findViewById(R.id.tv_cui);//上传视频凭证

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
  private void updatePic(TextView tvUploadProof, String showpic2button,
      TextView orderStatus, int position, TextView tvHaveTime) {

    tvHaveTime.setText(mContext.getString(R.string.shengyu_time_proof_one));
    orderStatus.setVisibility(View.VISIBLE);

    if (ProofType.GRAY.equals(showpic2button)) {//灰色
      tvUploadProof.setVisibility(View.GONE);
      orderStatus
          .setText(new StringBuilder().append(mContext.getString(R.string.has_upload_proof_video)));

      //剩余处理时间
//      tvHaveTime.setText(mContext.getString(R.string.shengyu_time_proof_one));
    }

    if (ProofType.NORMAL.equals(showpic2button)) {

      tvUploadProof.setVisibility(View.VISIBLE);
      tvUploadProof.setEnabled(true);
      tvUploadProof.setText(mContext.getString(R.string.upload_proof_video));

      //剩余时间
//      tvHaveTime.setText(mContext.getString(R.string.shenyu_time));

      orderStatus
          .setText(new StringBuilder().append(mContext.getString(R.string.complainted)));
    }

    if (ProofType.DOWN_TIME.equals(showpic2button)) {

      tvUploadProof.setVisibility(View.GONE);

      //剩余处理时间
//      tvHaveTime.setText(mContext.getString(R.string.shengyu_time_proof_one));

      orderStatus
          .setText(new StringBuilder().append(mContext.getString(R.string.complainted_new)));
    }
  }


  //崔确认按钮
  private void updatePressButton(TextView tvUploadProof, String buypressbutton,
      TextView tv_order_status, TextView tv_cui, String status) {

    if (CuiType.GRAY.equals(buypressbutton)) {//灰色
      tv_cui.setVisibility(View.GONE);
      tvUploadProof.setVisibility(View.GONE);
      tv_order_status.setText(mContext.getString(R.string.has_cui));
    }

    if (CuiType.NORMAL.equals(buypressbutton)) {
      tvUploadProof.setEnabled(true);
      tvUploadProof.setVisibility(View.VISIBLE);

      tvUploadProof.setText(mContext.getString(R.string.cui_confirm));

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
    TextView tvUploadProof;
    TextView tv_order_status;
    TextView tvHours;
    TextView tvMinus;
    TextView tvSeconds;
    TextView tv_heji;
    TextView tv_lianxi;
    TextView tv_cui;

    PConfirmlistBean mProduct;


    public void setData(final PConfirmlistBean item, final int position) {
      mProduct = item;
//      updateTimeRemaining(System.currentTimeMillis());

      container.setVisibility(View.VISIBLE);
      tvUploadProof.setVisibility(View.VISIBLE);

      String buypressbutton = item.getBuypressbutton();
      String showpic2button = item.getShowpic2button();

      tvOrderId.setText(new StringBuilder().append(mContext.getString(R.string.order_code))
          .append(DebugUtils.convert(item.getOrderid(), "")));
      //种子数量
      String s = DebugUtils.convert(item.getSeedcount(), "0") + "PCS";
      StringUtil.setTextSize(s, tvSeedsCount, 25, 9);

      tvOrderTime.setText(new StringBuilder().append(mContext.getString(R.string.pay_time))
          .append(DebugUtils.convert(item.getPaytime(), "")));
      tvOrderSeller.setText(new StringBuilder().append(mContext.getString(R.string.seller_vip))
          .append(DebugUtils.convert(item.getSellmember(), "")));

      //合计
      String multile = DemicalUtil.multile(DebugUtils.convert(item.getSeedcount(), "0"),
          DebugUtils.convert(mPrice, "0"));
      String s1 = mContext.getString(R.string.heji) + " " + StringUtil.getThreeString(multile);
      StringUtil.setTextSizeNewOne(s1, tv_heji, 11, 15, '¥', '¥',
          ColorUtil.getColor(R.color.c333, mContext));

      //被投诉
      if (StatusType.COMPLAINTED.equals(item.getStatus())) {
        tvUploadProof.setVisibility(View.GONE);
        //上传付款凭证
        updatePic(tv_cui, showpic2button, tv_order_status, position, tvHaveTime);
      }

      if (StatusType.NO_COMPLAIN.equals(item.getStatus())) {
        tv_cui.setVisibility(View.GONE);
        tvHaveTime.setText(mContext.getString(R.string.shenyu_time));
        //崔确认
        updatePressButton(tvUploadProof, buypressbutton, tv_order_status, tv_cui, item.getStatus());
      }

      tvUploadProof.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          //催确认
          mListener.onItem(getItem(position), position, 0);
        }
      });

      tv_cui.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          //上传视频凭证
          mListener.onItem(getItem(position), position, 1);
        }
      });

    }


    public void updateTimeRemaining(long currentTime) {

      String status = mProduct.getStatus();

      //没有投诉
      if (StatusType.NO_COMPLAIN.equals(status)) {
        if (TextUtils.isEmpty(mProduct.getEndtime())
            || DateUtil.parseDateString(mProduct.getEndtime()) == null) {
          tvHours.setText("00");
          tvMinus.setText("00");
          tvSeconds.setText("00");
          return;
        }

        String endtime = mProduct.getEndtime();
        long timeDiff = DateUtil.parseDateString(endtime) - currentTime;

        if (timeDiff > 0) {
          List<Integer> formatDiffTimeString = TimeUtil.getFormatDiffTimeString(timeDiff);

          Integer hours = formatDiffTimeString.get(0);
          Integer minutes = formatDiffTimeString.get(1);
          Integer seconds = formatDiffTimeString.get(2);

          if (null != tvHours && null != tvMinus && null != tvSeconds) {
            tvHours.setText(String.valueOf((hours < 10 ? "0" + hours : hours)));
            tvMinus.setText(String.valueOf(minutes < 10 ? "0" + minutes : minutes));
            tvSeconds.setText(String.valueOf(seconds < 10 ? "0" + seconds : seconds));
          }
        } else {
          if (null != tvHours && null != tvMinus && null != tvSeconds) {
            tvHours.setText("00");
            tvMinus.setText("00");
            tvSeconds.setText("00");
          }

        }
      } else {

        String endtime = null;

        //只要被投诉了就使用dealendtime

        //不是隐状态都是用dealendtime
//        if (ProofType.NORMAL.equals(mProduct.getShowpic2button())) {
////          tvHaveTime.setText(mContext.getString(R.string.shenyu_time));
//          endtime = mProduct.getEndtime();
//        } else {
////          tvHaveTime.setText(mContext.getString(R.string.shengyu_time_proof_one));
//          endtime = mProduct.getDealendtime();
//        }

        endtime = mProduct.getDealendtime();

        if (TextUtils.isEmpty(endtime)
            || DateUtil.parseDateString(mProduct.getDealendtime()) == null) {

          if (null != tvHours && null != tvMinus && null != tvSeconds) {
            tvHours.setText("00");
            tvMinus.setText("00");
            tvSeconds.setText("00");
          }
          return;
        }

        long timeDiff = DateUtil.parseDateString(endtime) - currentTime;

        if (timeDiff > 0) {
          List<Integer> formatDiffTimeString = TimeUtil.getFormatDiffTimeString(timeDiff);

          Integer hours = formatDiffTimeString.get(0);
          Integer minutes = formatDiffTimeString.get(1);
          Integer seconds = formatDiffTimeString.get(2);

          if (null != tvHours && null != tvMinus && null != tvSeconds) {
            tvHours.setText(String.valueOf((hours < 10 ? "0" + hours : hours)));
            tvMinus.setText(String.valueOf(minutes < 10 ? "0" + minutes : minutes));
            tvSeconds.setText(String.valueOf(seconds < 10 ? "0" + seconds : seconds));
          }


        } else {
          if (null != tvHours && null != tvMinus && null != tvSeconds) {
            tvHours.setText("00");
            tvMinus.setText("00");
            tvSeconds.setText("00");
          }
        }
      }
    }

  }
}



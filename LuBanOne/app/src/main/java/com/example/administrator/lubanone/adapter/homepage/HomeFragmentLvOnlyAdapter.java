package com.example.administrator.lubanone.adapter.homepage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BuySellBean;
import com.example.administrator.lubanone.bean.homepage.HomeFragmentResultBean.UserjjlistBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.OnViewClickListener;
import java.util.List;

/**
 * Created by quyang on 2017/7/4.
 */

public class HomeFragmentLvOnlyAdapter extends BaseAdapter {

  public static final int TYPE_TITLE = 0;

  public static final int TYPE_COMPANY = 1;

  private Activity mActivity;

  private List<UserjjlistBean> mList;

  private BuySellBean buySellBean;

  private OnListViewItemListener mListener;

  private OnViewClickListener mViewClickListener;

  public HomeFragmentLvOnlyAdapter(Activity activity, List<UserjjlistBean> strings,
      BuySellBean buySellBean, OnListViewItemListener listener, OnViewClickListener mViewListener) {
    this.mActivity = activity;
    this.mList = strings;
    this.buySellBean = buySellBean;
    this.mListener = listener;
    this.mViewClickListener = mViewListener;
  }

  @Override
  public int getItemViewType(int position) {
    if (0 == position) {
      return TYPE_TITLE;
    } else {
      return TYPE_COMPANY;
    }
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public int getCount() {
    return mList.size() + 1;
  }

  @Override
  public Object getItem(int position) {
    if (0 == position) {
      return buySellBean;
    } else {
      return mList.get(position - 1);
    }
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    TitleViewHolder titleHolder = null;
    ComViewHolder comHolder = null;

    int type = getItemViewType(position);

    switch (type) {
      case TYPE_TITLE:
        if (null == convertView) {
          convertView = LayoutInflater.from(mActivity).inflate(R.layout.home_page_lv_head, null);

          titleHolder = new TitleViewHolder();

          ViewGroup buySellBtn = (ViewGroup) convertView.findViewById(R.id.buy_sell_btn);
          ViewGroup buyLayout = (ViewGroup) convertView.findViewById(R.id.buy_layout);
          ViewGroup sellLayout = (ViewGroup) convertView.findViewById(R.id.sell_layout);

          titleHolder.buyBtn = (Button) buySellBtn.findViewById(R.id.buy_btn);
          titleHolder.sellBtn = (Button) buySellBtn.findViewById(R.id.sell_btn);

          titleHolder.rbMatch_buy = (RadioButton) buyLayout.findViewById(R.id.rb_match);
          titleHolder.rbNoMoney_buy = (RadioButton) buyLayout.findViewById(R.id.rb_no_money);
          titleHolder.rbComfirm_buy = (RadioButton) buyLayout.findViewById(R.id.rb_comfirm);
          titleHolder.rbEvaluate_buy = (RadioButton) buyLayout.findViewById(R.id.rb_evaluate);

          titleHolder.matchDot_buy = (TextView) buyLayout.findViewById(R.id.match_dot);
          titleHolder.noMoneyDot_buy = (TextView) buyLayout.findViewById(R.id.no_money_dot);
          titleHolder.confirmDot_buy = (TextView) buyLayout.findViewById(R.id.confirm_dot);
          titleHolder.evaluateDot_buy = (TextView) buyLayout.findViewById(R.id.evaluate_dot);

          //卖出
          titleHolder.rbMatch_sell = (RadioButton) sellLayout.findViewById(R.id.rb_match);
          titleHolder.rbNoMoney_sell = (RadioButton) sellLayout.findViewById(R.id.rb_no_money);
          titleHolder.rbComfirm_sell = (RadioButton) sellLayout.findViewById(R.id.rb_comfirm);
          titleHolder.rbEvaluate_sell = (RadioButton) sellLayout.findViewById(R.id.rb_evaluate);

          titleHolder.matchDot_sell = (TextView) sellLayout.findViewById(R.id.match_dot);
          titleHolder.noMoneyDot_sell = (TextView) sellLayout.findViewById(R.id.no_money_dot);
          titleHolder.confirmDot_sell = (TextView) sellLayout.findViewById(R.id.confirm_dot);
          titleHolder.evaluateDot_sell = (TextView) sellLayout.findViewById(R.id.evaluate_dot);

          convertView.setTag(titleHolder);
        } else {
          titleHolder = (TitleViewHolder) convertView.getTag();
        }

        titleHolder.buyBtn.setOnClickListener(new MyViewClickListener(0));
        titleHolder.sellBtn.setOnClickListener(new MyViewClickListener(1));

        titleHolder.rbMatch_buy.setOnClickListener(new MyViewClickListener(2));
        titleHolder.rbNoMoney_buy.setOnClickListener(new MyViewClickListener(3));
        titleHolder.rbComfirm_buy.setOnClickListener(new MyViewClickListener(4));
        titleHolder.rbEvaluate_buy.setOnClickListener(new MyViewClickListener(5));
        titleHolder.matchDot_buy.setText(buySellBean.getBuyMatch());
        titleHolder.noMoneyDot_buy.setText(buySellBean.getBuyNoMoney());
        titleHolder.confirmDot_buy.setText(buySellBean.getBuyConfirm());
        titleHolder.evaluateDot_buy.setText(buySellBean.getBuyPingJia());
        if (isZero(buySellBean.getBuyMatch())) {
          titleHolder.matchDot_buy.setVisibility(View.INVISIBLE);
        }
        if (isZero(buySellBean.getBuyNoMoney())) {
          titleHolder.noMoneyDot_buy.setVisibility(View.INVISIBLE);
        }
        if (isZero(buySellBean.getBuyConfirm())) {
          titleHolder.confirmDot_buy.setVisibility(View.INVISIBLE);
        }
        if (isZero(buySellBean.getBuyPingJia())) {
          titleHolder.evaluateDot_buy.setVisibility(View.INVISIBLE);
        }

        //卖出
        titleHolder.rbMatch_sell.setOnClickListener(new MyViewClickListener(6));
        titleHolder.rbNoMoney_sell.setOnClickListener(new MyViewClickListener(7));
        titleHolder.rbComfirm_sell.setOnClickListener(new MyViewClickListener(8));
        titleHolder.rbEvaluate_sell.setOnClickListener(new MyViewClickListener(9));
        titleHolder.matchDot_sell.setText(buySellBean.getSellMatch());
        titleHolder.noMoneyDot_sell.setText(buySellBean.getSellNoMoney());
        titleHolder.confirmDot_sell.setText(buySellBean.getSellConfirm());
        titleHolder.evaluateDot_sell.setText(buySellBean.getSellPingJia());
        if (isZero(buySellBean.getBuyMatch())) {
          titleHolder.matchDot_sell.setVisibility(View.INVISIBLE);
        }
        if (isZero(buySellBean.getBuyNoMoney())) {
          titleHolder.noMoneyDot_sell.setVisibility(View.INVISIBLE);
        }
        if (isZero(buySellBean.getBuyConfirm())) {
          titleHolder.confirmDot_sell.setVisibility(View.INVISIBLE);
        }
        if (isZero(buySellBean.getBuyPingJia())) {
          titleHolder.evaluateDot_sell.setVisibility(View.INVISIBLE);
        }

        break;

      case TYPE_COMPANY:

        if (null == convertView) {
          convertView = LayoutInflater.from(mActivity).inflate(R.layout.home_page_lv_item, null);

          comHolder = new ComViewHolder();

          comHolder.tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
          comHolder.tvSeedCount = (TextView) convertView.findViewById(R.id.tv_seed_count);
          comHolder.tvPeiSongTime = (TextView) convertView.findViewById(R.id.tv_peisong_time);
          comHolder.tvGiveMoneyTime = (TextView) convertView.findViewById(R.id.tv_give_money_time);
          comHolder.tvReceiver = (TextView) convertView.findViewById(R.id.tv_receiver);
          comHolder.tvGetPingfeng = (TextView) convertView.findViewById(R.id.tv_get_pingfeng);
          comHolder.tvGivePingfeng = (TextView) convertView.findViewById(R.id.tv_give_pingfeng);
          comHolder.tvCompleteTime = (TextView) convertView.findViewById(R.id.tv_complete_time);
          comHolder.tvSeedStatus = (TextView) convertView.findViewById(R.id.tv_seed_status);
          comHolder.tvGrowingDays = (TextView) convertView.findViewById(R.id.tv_growing_days);
          comHolder.tvGrowingGetSeeds = (TextView) convertView
              .findViewById(R.id.tv_growing_get_seeds);
          comHolder.btnReap = (Button) convertView.findViewById(R.id.btn_reap);

          convertView.setTag(comHolder);
        } else {
          comHolder = (ComViewHolder) convertView.getTag();
        }

        UserjjlistBean item = (UserjjlistBean) getItem(position);
        comHolder.tvOrderId.setText(item.getOrderid());
        comHolder.tvSeedCount.setText(item.getJb());
        comHolder.tvPeiSongTime.setText(item.getDate());
        comHolder.tvGiveMoneyTime.setText(item.getDate_hk());
        comHolder.tvReceiver.setText(item.getG_user());
        comHolder.tvGetPingfeng.setText(item.getGpingjia());
        comHolder.tvGivePingfeng.setText(item.getPpingjia());
        comHolder.tvCompleteTime.setText(item.getDate_su());
        comHolder.tvSeedStatus.setText(item.getZt());
        comHolder.tvGrowingDays.setText(item.getUser_jj_ts());
        comHolder.tvGrowingGetSeeds.setText(item.getUser_jj_lx());

        comHolder.btnReap.setVisibility(item.getIsshouge() == 1 ? View.VISIBLE : View.GONE);
        comHolder.btnReap.setOnClickListener(new MyViewClickListener(10));

        comHolder.btnReap.setOnClickListener(new MyClickListener(getItem(position), position));

        break;
    }

    return convertView;
  }


  static class TitleViewHolder {

    public Button buyBtn;
    public Button sellBtn;

    public RadioButton rbMatch_buy;
    public RadioButton rbNoMoney_buy;
    public RadioButton rbComfirm_buy;
    public RadioButton rbEvaluate_buy;

    public TextView matchDot_buy;
    public TextView noMoneyDot_buy;
    public TextView confirmDot_buy;
    public TextView evaluateDot_buy;

    public RadioButton rbMatch_sell;
    public RadioButton rbNoMoney_sell;
    public RadioButton rbComfirm_sell;
    public RadioButton rbEvaluate_sell;

    public TextView matchDot_sell;
    public TextView noMoneyDot_sell;
    public TextView confirmDot_sell;
    public TextView evaluateDot_sell;


  }

  static class ComViewHolder {

    public TextView tvOrderId;
    public TextView tvSeedCount;
    public TextView tvPeiSongTime;
    public TextView tvGiveMoneyTime;
    public TextView tvReceiver;
    public TextView tvGetPingfeng;
    public TextView tvGivePingfeng;
    public TextView tvCompleteTime;
    public TextView tvSeedStatus;
    public TextView tvGrowingDays;
    public TextView tvGrowingGetSeeds;
    public Button btnReap;

  }


  private class MyClickListener implements View.OnClickListener {

    private int position;
    private Object item;

    MyClickListener(Object item, int position) {
      this.position = position;
      this.item = item;
    }

    @Override
    public void onClick(View v) {
      mListener.onItem(item, position);
    }
  }

  private class MyViewClickListener implements View.OnClickListener {

    private int position;

    MyViewClickListener(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      mViewClickListener.onView(position);
    }
  }

  public boolean isZero(String string) {
    if ("0".equals(string)) {
      return true;
    }
    return false;
  }

}

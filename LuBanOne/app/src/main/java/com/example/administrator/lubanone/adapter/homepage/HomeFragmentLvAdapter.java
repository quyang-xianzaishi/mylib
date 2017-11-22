package com.example.administrator.lubanone.adapter.homepage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BuySellBean;
import com.example.administrator.lubanone.bean.homepage.HomeFragmentLvBean;
import com.example.administrator.lubanone.bean.homepage.HomeFragmentLvBean.ReapType;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.OnViewClickListener;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import java.util.List;

/**
 * Created by quyang on 2017/7/4.
 */

public class HomeFragmentLvAdapter extends BaseAdapter {

  public static final int TYPE_TITLE = 0;

  public static final int TYPE_COMPANY = 1;

  private Activity mActivity;

  private List<HomeFragmentLvBean> mList;

  private BuySellBean buySellBean;

  private OnListViewItemListener mListener;//收割的回调

  private OnViewClickListener mViewClickListener;//8个图标的回调

  public HomeFragmentLvAdapter(Activity activity, List<HomeFragmentLvBean> strings,
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
    if (CollectionUtils.isEmpty(mList)) {
      return 1;
    }
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

          titleHolder.emptyLayout = (RelativeLayout) convertView.findViewById(R.id.empty_layout);
          titleHolder.tip_buy = (TextView) buyLayout.findViewById(R.id.tip_text);

          titleHolder.buyBtn = (Button) buySellBtn.findViewById(R.id.buy_btn);
          titleHolder.sellBtn = (Button) buySellBtn.findViewById(R.id.sell_btn);

          titleHolder.rbMatch_buy = (ImageView) buyLayout.findViewById(R.id.rb_match);
          titleHolder.rbNoMoney_buy = (ImageView) buyLayout.findViewById(R.id.rb_no_money);
          titleHolder.rbComfirm_buy = (ImageView) buyLayout.findViewById(R.id.rb_comfirm);
          titleHolder.rbEvaluate_buy = (ImageView) buyLayout.findViewById(R.id.rb_evaluate);

          titleHolder.matchDot_buy = (TextView) buyLayout.findViewById(R.id.match_dot);
          titleHolder.noMoneyDot_buy = (TextView) buyLayout.findViewById(R.id.no_money_dot);
          titleHolder.confirmDot_buy = (TextView) buyLayout.findViewById(R.id.confirm_dot);
          titleHolder.evaluateDot_buy = (TextView) buyLayout.findViewById(R.id.evaluate_dot);

          titleHolder.tv_buy_one = (TextView) buyLayout.findViewById(R.id.tv_one);
          titleHolder.tv_buy_two = (TextView) buyLayout.findViewById(R.id.tv_two);
          titleHolder.tv_buy_three = (TextView) buyLayout.findViewById(R.id.tv_three);
          titleHolder.tv_buy_four = (TextView) buyLayout.findViewById(R.id.tv_four);

          //---------------------------------------------------------------------------------------

          //卖出
          titleHolder.rbMatch_sell = (ImageView) sellLayout.findViewById(R.id.rb_match);
          titleHolder.rbNoMoney_sell = (ImageView) sellLayout.findViewById(R.id.rb_no_money);
          titleHolder.rbComfirm_sell = (ImageView) sellLayout.findViewById(R.id.rb_comfirm);
          titleHolder.rbEvaluate_sell = (ImageView) sellLayout.findViewById(R.id.rb_evaluate);

          titleHolder.matchDot_sell = (TextView) sellLayout.findViewById(R.id.match_dot);
          titleHolder.noMoneyDot_sell = (TextView) sellLayout.findViewById(R.id.no_money_dot);
          titleHolder.confirmDot_sell = (TextView) sellLayout.findViewById(R.id.confirm_dot);
          titleHolder.evaluateDot_sell = (TextView) sellLayout.findViewById(R.id.evaluate_dot);

          titleHolder.tv_sell_one = (TextView) sellLayout.findViewById(R.id.tv_one);
          titleHolder.tv_sell_two = (TextView) sellLayout.findViewById(R.id.tv_two);
          titleHolder.tv_sell_three = (TextView) sellLayout.findViewById(R.id.tv_three);
          titleHolder.tv_sell_four = (TextView) sellLayout.findViewById(R.id.tv_four);

          titleHolder.tip_sell = (TextView) sellLayout.findViewById(R.id.tip_text);

          convertView.setTag(titleHolder);
        } else {
          titleHolder = (TitleViewHolder) convertView.getTag();
        }

        if (CollectionUtils.isEmpty(mList)) {
          titleHolder.emptyLayout.setVisibility(View.VISIBLE);
        } else {
          titleHolder.emptyLayout.setVisibility(View.GONE);
        }

        titleHolder.buyBtn.setOnClickListener(new MyViewClickListener(0));
        titleHolder.sellBtn.setOnClickListener(new MyViewClickListener(1));

        titleHolder.rbMatch_buy.setOnClickListener(new MyViewClickListener(2));
        titleHolder.tv_buy_one.setOnClickListener(new MyViewClickListener(2));
        titleHolder.rbNoMoney_buy.setOnClickListener(new MyViewClickListener(3));
        titleHolder.tv_buy_two.setOnClickListener(new MyViewClickListener(3));
        titleHolder.rbComfirm_buy.setOnClickListener(new MyViewClickListener(4));
        titleHolder.tv_buy_three.setOnClickListener(new MyViewClickListener(4));
        titleHolder.rbEvaluate_buy.setOnClickListener(new MyViewClickListener(5));
        titleHolder.tv_buy_four.setOnClickListener(new MyViewClickListener(5));
        titleHolder.matchDot_buy.setText(buySellBean.getBuyMatch());
        titleHolder.noMoneyDot_buy.setText(buySellBean.getBuyNoMoney());
        titleHolder.confirmDot_buy.setText(buySellBean.getBuyConfirm());
        titleHolder.evaluateDot_buy.setText(buySellBean.getBuyPingJia());

        titleHolder.tv_buy_two.setText(mActivity.getString(R.string.buy_text_two));
        titleHolder.tv_sell_two.setText(mActivity.getString(R.string.no_receive_money));

        titleHolder.tip_buy.setText(mActivity.getString(R.string.buy));
        titleHolder.tip_sell.setText(mActivity.getString(R.string.sell));

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
        titleHolder.tv_sell_one.setOnClickListener(new MyViewClickListener(6));
        titleHolder.rbNoMoney_sell.setOnClickListener(new MyViewClickListener(7));
        titleHolder.tv_sell_two.setOnClickListener(new MyViewClickListener(7));
        titleHolder.rbComfirm_sell.setOnClickListener(new MyViewClickListener(8));
        titleHolder.tv_sell_three.setOnClickListener(new MyViewClickListener(8));
        titleHolder.rbEvaluate_sell.setOnClickListener(new MyViewClickListener(9));
        titleHolder.tv_sell_four.setOnClickListener(new MyViewClickListener(9));
        titleHolder.matchDot_sell.setText(buySellBean.getSellMatch());
        titleHolder.noMoneyDot_sell.setText(buySellBean.getSellNoMoney());
        titleHolder.confirmDot_sell.setText(buySellBean.getSellConfirm());
        titleHolder.evaluateDot_sell.setText(buySellBean.getSellPingJia());
        if (isZero(buySellBean.getSellMatch())) {
          titleHolder.matchDot_sell.setVisibility(View.INVISIBLE);
        }
        if (isZero(buySellBean.getSellNoMoney())) {
          titleHolder.noMoneyDot_sell.setVisibility(View.INVISIBLE);
        }
        if (isZero(buySellBean.getSellConfirm())) {
          titleHolder.confirmDot_sell.setVisibility(View.INVISIBLE);
        }
        if (isZero(buySellBean.getSellPingJia())) {
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

        HomeFragmentLvBean item = (HomeFragmentLvBean) getItem(position);

        DebugUtils.setStringValue(item.getOrderid(), "", comHolder.tvOrderId);
        DebugUtils.setStringValue(item.getJb(), "0", comHolder.tvSeedCount);
        DebugUtils.setStringValue(item.getDate(), "", comHolder.tvPeiSongTime);
        DebugUtils.setStringValue(item.getDate_hk(), "", comHolder.tvGiveMoneyTime);
        DebugUtils.setStringValue(item.getG_user(), "", comHolder.tvReceiver);
        DebugUtils.setStringValue(item.getGpingjia(), "0", comHolder.tvGetPingfeng);
        DebugUtils.setStringValue(item.getPpingjia(), "0", comHolder.tvGivePingfeng);
        DebugUtils.setStringValue(item.getDate_su(), "", comHolder.tvCompleteTime);
        DebugUtils.setStringValue(item.getZt(), "", comHolder.tvSeedStatus);
        DebugUtils.setStringValue(item.getUser_jj_ts(), "", comHolder.tvGrowingDays);
        DebugUtils.setStringValue(item.getUser_jj_lx(), "", comHolder.tvGrowingGetSeeds);

        if (ReapType.SHOW.equals(item.getIsshouge())) {
          comHolder.btnReap.setVisibility(View.VISIBLE);
        }
        if (ReapType.HIDE.equals(item.getIsshouge())) {
          comHolder.btnReap.setVisibility(View.GONE);
        }

        comHolder.btnReap
            .setOnClickListener(new MyClickListener(getItem(position), position));

        break;
    }

    return convertView;
  }


  static class TitleViewHolder {

    public Button buyBtn;
    public Button sellBtn;

    public ImageView rbMatch_buy;
    public ImageView rbNoMoney_buy;
    public ImageView rbComfirm_buy;
    public ImageView rbEvaluate_buy;

    public TextView matchDot_buy;
    public TextView noMoneyDot_buy;
    public TextView confirmDot_buy;
    public TextView evaluateDot_buy;


    public TextView tv_buy_one;
    public TextView tv_buy_two;
    public TextView tv_buy_three;
    public TextView tv_buy_four;

    //----------------------------------

    public ImageView rbMatch_sell;
    public ImageView rbNoMoney_sell;
    public ImageView rbComfirm_sell;
    public ImageView rbEvaluate_sell;

    public TextView matchDot_sell;
    public TextView noMoneyDot_sell;
    public TextView confirmDot_sell;
    public TextView evaluateDot_sell;

    public TextView tv_sell_one;
    public TextView tv_sell_two;
    public TextView tv_sell_three;
    public TextView tv_sell_four;

    public TextView tip_buy;
    public TextView tip_sell;

    public RelativeLayout emptyLayout;


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
    if ("0".equals(string) || "".equals(string)) {
      return true;
    }
    return false;
  }

}

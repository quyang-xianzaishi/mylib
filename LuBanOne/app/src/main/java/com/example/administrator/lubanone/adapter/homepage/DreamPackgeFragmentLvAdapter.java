package com.example.administrator.lubanone.adapter.homepage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.finance.DreamPackageResultBean.DeallistBean;
import com.example.administrator.lubanone.interfaces.OnViewClickListener;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import java.util.List;

/**
 * Created by quyang on 2017/7/4.
 */

public class DreamPackgeFragmentLvAdapter extends BaseAdapter {

  public static final int TYPE_TITLE = 0;

  public static final int TYPE_COMPANY = 1;

  private Activity mActivity;

  private List<DeallistBean> mList;

  private String seedsCount;

  private OnViewClickListener mViewClickListener;

  public DreamPackgeFragmentLvAdapter(Activity activity, List<DeallistBean> strings,
      String buySellBean, OnViewClickListener mViewListener) {
    this.mActivity = activity;
    this.mList = strings;
    this.seedsCount = buySellBean;
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
      return seedsCount;
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
          convertView = LayoutInflater.from(mActivity).inflate(R.layout.dream_package_head, null);

          titleHolder = new TitleViewHolder();

          titleHolder.tv_dream_package_seeds = (TextView) convertView
              .findViewById(R.id.tv_dream_package_seeds);
          titleHolder.sell_seeds_btn = (Button) convertView.findViewById(R.id.sell_seeds_btn);

          convertView.setTag(titleHolder);
        } else {
          titleHolder = (TitleViewHolder) convertView.getTag();
        }

        titleHolder.sell_seeds_btn.setOnClickListener(new MyViewClickListener(0));
        titleHolder.tv_dream_package_seeds.setText(DebugUtils.convert(seedsCount, "0"));



        break;

      case TYPE_COMPANY:

        if (null == convertView) {
          convertView = LayoutInflater.from(mActivity)
              .inflate(R.layout.dream_package_lv_item, null);

          comHolder = new ComViewHolder();

          comHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
          comHolder.tv_growing_seeds = (TextView) convertView.findViewById(R.id.tv_growing_seeds);
          comHolder.tv_order_id = (TextView) convertView.findViewById(R.id.tv_order_id);
          comHolder.tv_seeds = (TextView) convertView.findViewById(R.id.tv_seeds);
          comHolder.tv_trade_seeds = (TextView) convertView.findViewById(R.id.tv_trade_seeds);
          comHolder.tv_trade_type = (TextView) convertView.findViewById(R.id.tv_trade_type);
          comHolder.tv_trade_vip = (TextView) convertView.findViewById(R.id.tv_trade_vip);

          convertView.setTag(comHolder);
        } else {
          comHolder = (ComViewHolder) convertView.getTag();
        }

        DeallistBean item = (DeallistBean) getItem(position);

        DebugUtils.setStringValue(item.getOrderid(), "", comHolder.tv_order_id);
        DebugUtils.setStringValue(item.getDate(), "", comHolder.tv_date);
        DebugUtils.setStringValue(item.getTrademember(), "", comHolder.tv_trade_vip);
        DebugUtils.setStringValue(item.getTradetype(), "", comHolder.tv_trade_type);
        DebugUtils.setStringValue(item.getSowseed(), "0", comHolder.tv_seeds);
        DebugUtils.setStringValue(item.getGrowget(), "0", comHolder.tv_growing_seeds);
        DebugUtils.setStringValue(item.getTradeseed(), "", comHolder.tv_trade_seeds);

        break;
    }

    return convertView;
  }


  static class TitleViewHolder {

    public TextView tv_dream_package_seeds;
    public Button sell_seeds_btn;
  }

  static class ComViewHolder {

    //日期
    public TextView tv_date;
    //订单id
    public TextView tv_order_id;
    //交易类型
    public TextView tv_trade_type;
    //交易会员
    public TextView tv_trade_vip;
    //播种数
    public TextView tv_seeds;
    //成长中的种子
    public TextView tv_growing_seeds;
    //交易的种子
    public TextView tv_trade_seeds;

  }


  private class MyViewClickListener implements View.OnClickListener {

    private int position;

    MyViewClickListener(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      mViewClickListener.onView(0);
    }
  }

  public boolean isZero(String string) {
    if ("0".equals(string) || "".equals(string)) {
      return true;
    }
    return false;
  }

}

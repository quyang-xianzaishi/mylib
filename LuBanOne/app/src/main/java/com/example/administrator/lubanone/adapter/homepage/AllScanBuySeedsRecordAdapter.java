package com.example.administrator.lubanone.adapter.homepage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BuyRecordResultBean.ResultBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.StringUtil;
import java.util.List;

/**
 * Created by quyang on 2017/7/4.
 */

public class AllScanBuySeedsRecordAdapter extends BaseAdapter {


  private Activity mActivity;

  private List<ResultBean> mList;

  private OnListViewItemListener mListener;//收割的回调


  public AllScanBuySeedsRecordAdapter(Activity activity, List<ResultBean> strings,
      OnListViewItemListener listener) {
    this.mActivity = activity;
    this.mList = strings;
    this.mListener = listener;
  }

  @Override
  public int getCount() {
    return mList == null ? 0 : mList.size();
  }

  @Override
  public ResultBean getItem(int position) {
    return mList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ComViewHolder comHolder = null;

    if (null == convertView) {
      convertView = LayoutInflater.from(mActivity).inflate(R.layout.home_page_new_lv_item, null);
//      convertView = LayoutInflater.from(mActivity).inflate(R.layout.home_page_lv_item, null);

      comHolder = new ComViewHolder();

      comHolder.tv_buy_time = (TextView) convertView.findViewById(R.id.tv_buy_time);
      comHolder.tv_right = (TextView) convertView.findViewById(R.id.tv_right);

      convertView.setTag(comHolder);
    } else {
      comHolder = (ComViewHolder) convertView.getTag();
    }

    ResultBean item = getItem(position);
    comHolder.tv_buy_time
        .setText(DebugUtils
            .convert(item.getDate(), ""));

    if ("0".equals(item.getStatus())) {
      String s = "+" + item.getUser_jj_lx() + "\n" + DebugUtils.convert(item.getZt(), "");
      StringUtil.setTextPartColor(s,
          ColorUtil.getColor(mActivity, R.color.red),
          s.indexOf(DebugUtils.convert(item.getZt(), "").charAt(0)), s.length(),
          comHolder.tv_right);
    } else {
      String s = "+" + item.getUser_jj_lx() + "\n" + DebugUtils.convert(item.getZt(), "");
      StringUtil.setTextPartColor(s,
          ColorUtil.getColor(mActivity, R.color._888888),
          s.indexOf(DebugUtils.convert(item.getZt(), "").charAt(0)), s.length(),
          comHolder.tv_right);
    }

    comHolder.tv_right
        .setOnClickListener(new MyClickListener(getItem(position), position));

    return convertView;
  }

  static class ComViewHolder {

    public TextView tv_buy_time;
    public TextView tv_right;

  }


  private class MyClickListener implements View.OnClickListener {

    private int position;
    private Object item;

    MyClickListener(ResultBean item, int position) {
      this.position = position;
      this.item = item;
    }

    @Override
    public void onClick(View v) {
      mListener.onItem(item, position);
    }
  }

  public boolean isZero(String string) {
    if ("0".equals(string)) {
      return true;
    }
    return false;
  }

}

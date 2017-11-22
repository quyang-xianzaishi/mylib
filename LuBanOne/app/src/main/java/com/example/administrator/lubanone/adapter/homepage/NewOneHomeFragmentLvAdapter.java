package com.example.administrator.lubanone.adapter.homepage;

import static com.example.administrator.lubanone.R.id.tv_left;
import static com.example.administrator.lubanone.R.id.tv_right;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BewHomeResultBean.ReapseedbuttonType;
import com.example.administrator.lubanone.bean.homepage.NewHomeFragmentResultBean.GrowseedslistBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DrableUtil;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.ViewUtil;
import java.util.List;

/**
 * Created by quyang on 2017/7/4.
 */

public class NewOneHomeFragmentLvAdapter extends BaseAdapter {


  private Activity mActivity;

  private List<GrowseedslistBean> mList;


  private OnListViewItemListener mListener;//收割的回调


  public NewOneHomeFragmentLvAdapter(Activity activity, List<GrowseedslistBean> strings,
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
  public GrowseedslistBean getItem(int position) {
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
      convertView = LayoutInflater.from(mActivity).inflate(R.layout.new_home_page_ll_item, null);

      comHolder = new ComViewHolder();

      comHolder.tv_left = (TextView) convertView.findViewById(tv_left);
      comHolder.tv_mid = (TextView) convertView.findViewById(R.id.tv_mid);
      comHolder.tv_right = (TextView) convertView.findViewById(tv_right);

      convertView.setTag(comHolder);
    } else {
      comHolder = (ComViewHolder) convertView.getTag();
    }

    System.out.println("NewOneHomeFragmentLvAdapter.getView=" + position);

    GrowseedslistBean item = getItem(position);

    StringUtil
        .setTextSize(item.getUser_jj_lx() + " " + mActivity.getString(R.string.pcs),
            comHolder.tv_left, 23, 9);
    DebugUtils
        .setStringValue(mActivity.getString(R.string.growing_days) + " " + item.getUser_jj_ts(),
            "0",
            comHolder.tv_mid);

    if (ReapseedbuttonType.HIDE.equals(item.getIsshouge())) {

      comHolder.tv_right.setText(mActivity.getString(R.string.wait_reap));
      comHolder.tv_right.setVisibility(View.VISIBLE);
      comHolder.tv_right.setEnabled(false);
      comHolder.tv_right.setTextColor(ColorUtil.getColor(R.color.c888, mActivity));

      Drawable drawable = DrableUtil
          .getDrawable(mActivity, R.drawable.home_growing_seeds_gray_line);
      ViewUtil.setBackground(comHolder.tv_right, drawable);

    } else {
      comHolder.tv_right.setVisibility(View.VISIBLE);
      comHolder.tv_right.setEnabled(true);
      comHolder.tv_right.setText(mActivity.getString(R.string.reap_seeds));

      comHolder.tv_right.setTextColor(ColorUtil.getColor(R.color.c1e257c, mActivity));

      Drawable drawable = DrableUtil
          .getDrawable(mActivity, R.drawable.blue_shape_one);
      ViewUtil.setBackground(comHolder.tv_right, drawable);
    }

    comHolder.tv_right
        .setOnClickListener(new MyClickListener(getItem(position), position));

    return convertView;
  }


  static class ComViewHolder {

    public TextView tv_left;
    public TextView tv_mid;
    public TextView tv_right;

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


}

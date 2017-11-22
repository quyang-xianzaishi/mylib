package com.example.administrator.lubanone.adapter.homepage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.HomeFragmentLvBean.ReapType;
import com.example.administrator.lubanone.bean.homepage.NewHomeFragmentResultBean.GrowseedslistBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.qlibrary.utils.DebugUtils;
import java.util.List;

/**
 * Created by quyang on 2017/7/4.
 */

public class NewHomeFragmentLvAdapter extends BaseAdapter {


  private Activity mActivity;

  private List<GrowseedslistBean> mList;


  private OnListViewItemListener mListener;//收割的回调


  public NewHomeFragmentLvAdapter(Activity activity, List<GrowseedslistBean> strings,
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

    GrowseedslistBean item =getItem(position);

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

    return convertView;
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


}

package com.example.administrator.lubanone.adapter.homepage;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.home.SellSeedsRecordDetailActivity;
import com.example.administrator.lubanone.bean.homepage.SellSeedsRecordsresultBean.ResultBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.OnViewClickListener;
import com.example.administrator.lubanone.mvp.contract.CircleContract;
import com.example.qlibrary.utils.DebugUtils;
import java.util.List;

/**
 * Created by quyang on 2017/7/4.
 */

public class AllScanSellSeedsRecordAdapter extends BaseAdapter implements View.OnClickListener{


  private Activity mActivity;

  private List<ResultBean> mList;

  private OnListViewItemListener mListener;//收割的回调


  public AllScanSellSeedsRecordAdapter(Activity activity, List<ResultBean> strings,
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
  public View getView(final int position, View convertView, ViewGroup parent) {
    ComViewHolder comHolder = null;

    if (null == convertView) {
      //convertView = LayoutInflater.from(mActivity).inflate(R.layout.sell_record_lv_item, null);
      convertView = LayoutInflater.from(mActivity).inflate(R.layout.sell_record_item_new, null);

      comHolder = new ComViewHolder();

      /*comHolder.tvOrderId = (TextView) convertView.findViewById(R.id.tv_order_id);
      comHolder.tvSeedCount = (TextView) convertView.findViewById(R.id.tv_seed_count);
      comHolder.tvPeiSongTime = (TextView) convertView.findViewById(R.id.tv_peisong_time);
      comHolder.tvGiveMoneyTime = (TextView) convertView.findViewById(R.id.tv_give_money_time);
      comHolder.tvReceiver = (TextView) convertView.findViewById(R.id.tv_receiver);
      comHolder.tvGetPingfeng = (TextView) convertView.findViewById(R.id.tv_get_pingfeng);
      comHolder.tvGivePingfeng = (TextView) convertView.findViewById(R.id.tv_give_pingfeng);
      comHolder.tvCompleteTime = (TextView) convertView.findViewById(R.id.tv_complete_time);*/

      comHolder.recordItem =  (LinearLayout) convertView.findViewById(R.id.sell_seeds_record_item);
      comHolder.recordItemTime = (TextView) convertView.findViewById(R.id.sell_seeds_record_item_time);
      comHolder.recordItemCount = (TextView) convertView.findViewById(R.id.sell_seeds_record_item_count);
      comHolder.recordItemDetail = (TextView) convertView.findViewById(R.id.sell_seeds_record_item_detail);

      convertView.setTag(comHolder);
    } else {
      comHolder = (ComViewHolder) convertView.getTag();
    }

    ResultBean item = getItem(position);
    /*comHolder.tvOrderId.setText(DebugUtils.convert(item.getOrderid(), ""));
    comHolder.tvSeedCount.setText(DebugUtils.convert(item.getApplytime(), ""));
    comHolder.tvPeiSongTime.setText(DebugUtils.convert(item.getSeedcount(), ""));
    comHolder.tvGiveMoneyTime.setText(DebugUtils.convert(item.getBuymember(), ""));
    comHolder.tvReceiver.setText(DebugUtils.convert(item.getDealtime(), ""));
    comHolder.tvGetPingfeng.setText(mActivity.getString(R.string.completed));
    comHolder.tvGivePingfeng.setText(DebugUtils.convert(item.getGetscore(), "0"));
    comHolder.tvCompleteTime.setText(DebugUtils.convert(item.getGivescore(), "0"));*/

    comHolder.recordItemTime.setText(DebugUtils.convert(item.getDealtime(), ""));
    comHolder.recordItemCount.setText(DebugUtils.convert("-"+item.getSeedcount(), ""));
    comHolder.recordItem.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        forwardActivity(position);
      }
    });
    comHolder.recordItemDetail.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        forwardActivity(position);
      }
    });
    return convertView;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.sell_seeds_record_item:
      case R.id.sell_seeds_record_item_detail:
        //查看卖出种子记录详情页面
        break;
      default:
        break;
    }

  }
  private void forwardActivity(int position){
    Intent intent = new Intent();
    intent.putExtra("orderId",getItem(position).getOrderid());
    intent.putExtra("orderCount",getItem(position).getSeedcount());
    intent.putExtra("orderTime",getItem(position).getDealtime());
    intent.putExtra("orderMember",getItem(position).getBuymember());
    intent.putExtra("orderScore",getItem(position).getGetscore());
    intent.setClass(mActivity, SellSeedsRecordDetailActivity.class);
    mActivity.startActivity(intent);
  }

  static class ComViewHolder {

    /*public TextView tvOrderId;
    public TextView tvSeedCount;
    public TextView tvPeiSongTime;
    public TextView tvGiveMoneyTime;
    public TextView tvReceiver;
    public TextView tvGetPingfeng;
    public TextView tvGivePingfeng;
    public TextView tvCompleteTime;*/

    public LinearLayout recordItem;
    public TextView recordItemTime;
    public TextView recordItemCount;
    public TextView recordItemDetail;

  }

//  private class MyClickListener implements View.OnClickListener {
//
//    private int position;
//    private Object item;
//
//    MyClickListener(SellRecords item, int position) {
//      this.position = position;
//      this.item = item;
//    }
//
//    @Override
//    public void onClick(View v) {
//      mListener.onItem(item, position);
//    }
//  }
//
//  public boolean isZero(String string) {
//    if ("0".equals(string)) {
//      return true;
//    }
//    return false;
//  }

}

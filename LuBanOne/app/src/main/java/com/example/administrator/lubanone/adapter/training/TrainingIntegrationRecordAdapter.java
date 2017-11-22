package com.example.administrator.lubanone.adapter.training;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.training.IntegrationRecordBean;
import com.example.administrator.lubanone.bean.training.TrainingBean;
import java.util.List;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class TrainingIntegrationRecordAdapter extends BaseAdapter {

  Context context;
  List<IntegrationRecordBean> datas;

  public TrainingIntegrationRecordAdapter(Context context, List<IntegrationRecordBean> datas) {
    this.context = context;
    this.datas = datas;
  }

  @Override
  public int getCount() {
    return datas.size();
  }

  @Override
  public Object getItem(int position) {
    return datas.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.item_training_integration_record, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.integrationRecordItemTime = (TextView) convertView.findViewById(R.id.integration_record_item_time);
      viewHolder.integrationRecordItemWay = (TextView) convertView.findViewById(R.id.integration_record_item_way);
      viewHolder.integrationRecordItemScore = (TextView) convertView.findViewById(R.id.integration_record_item_score);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    viewHolder.integrationRecordItemTime.setText(datas.get(position).getTime());
    viewHolder.integrationRecordItemWay.setText(datas.get(position).getWay());
    viewHolder.integrationRecordItemScore.setText(datas.get(position).getScore());
    return convertView;
  }

  class ViewHolder {
    TextView integrationRecordItemTime;
    TextView integrationRecordItemWay;
    TextView integrationRecordItemScore;
  }
}

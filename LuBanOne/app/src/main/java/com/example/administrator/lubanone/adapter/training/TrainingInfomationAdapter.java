package com.example.administrator.lubanone.adapter.training;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.training.activity.TrainingInfoDetailActivity;
import com.example.administrator.lubanone.bean.training.TrainingInfoBean;
import java.util.List;

/**
 * Created by Administrator on 2017\7\13 0013.
 */

public class TrainingInfomationAdapter extends BaseAdapter {

  Context context;
  List<TrainingInfoBean> datas;

  public TrainingInfomationAdapter(Context context, List<TrainingInfoBean> datas) {
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
      convertView = LayoutInflater.from(context).inflate(R.layout.training_info_item_layout, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.trainInfoItemImage = (ImageView) convertView.findViewById(R.id.training_info_item_image);
      viewHolder.trainInfoItemTitle = (TextView) convertView.findViewById(R.id.training_info_item_title);
      viewHolder.trainInfoItemSummary = (TextView) convertView.findViewById(R.id.training_info_item_content);
      viewHolder.trainInfoItemDetail = (ImageView) convertView.findViewById(R.id.training_info_item_detail);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    viewHolder.trainInfoItemTitle.setText(datas.get(position).getTrainingInfoTitle());
    viewHolder.trainInfoItemSummary.setText(datas.get(position).getTrainingInfoSummary());
    Glide.with(context)
        .load(datas.get(position).getTrainingInfoImgUrl())
        .placeholder(R.mipmap.b)
        .error(R.mipmap.b)
        .diskCacheStrategy(DiskCacheStrategy.ALL).
        into(viewHolder.trainInfoItemImage);
    convertView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //培训资料item点击进入详情页
        Intent intent = new Intent();
        intent.setClass(context,TrainingInfoDetailActivity.class);
        context.startActivity(intent);
      }
    });
    return convertView;
  }

  class ViewHolder {
    ImageView trainInfoItemImage;
    TextView trainInfoItemTitle;
    TextView trainInfoItemSummary;
    ImageView trainInfoItemDetail;
  }

}

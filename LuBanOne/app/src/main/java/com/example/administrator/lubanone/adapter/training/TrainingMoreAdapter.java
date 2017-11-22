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
import com.example.administrator.lubanone.bean.training.TrainingBean;
import java.util.List;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class TrainingMoreAdapter extends BaseAdapter {

  Context context;
  List<TrainingBean> datas;

  public TrainingMoreAdapter(Context context, List<TrainingBean> datas) {
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
      convertView = LayoutInflater.from(context).inflate(R.layout.training_item_layout, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.trainingItemImage = (ImageView) convertView.findViewById(R.id.training_item_image);
      viewHolder.trainingItemTitle = (TextView) convertView.findViewById(R.id.training_item_title);
      viewHolder.trainingItemContent = (TextView) convertView.findViewById(R.id.training_item_content);
      viewHolder.trainingItemDetail = (ImageView) convertView.findViewById(R.id.training_item_detail);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    viewHolder.trainingItemTitle.setText(datas.get(position).getTitle());
    viewHolder.trainingItemContent.setText(datas.get(position).getContent());
    Glide.with(context)
        .load(datas.get(position).getImgUrl())
        .placeholder(R.mipmap.b)
        .error(R.mipmap.b)
        .diskCacheStrategy(DiskCacheStrategy.ALL).
        into(viewHolder.trainingItemImage);
    convertView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //正在培训中item点击进入培训直播页面
      }
    });
    return convertView;
  }

  class ViewHolder {
    ImageView trainingItemImage;
    TextView trainingItemTitle;
    TextView trainingItemContent;
    ImageView trainingItemDetail;
  }
}

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
import com.example.administrator.lubanone.activity.training.activity.TrainingBackPlayActivity;
import com.example.administrator.lubanone.bean.training.TrainPlayBackBean;
import java.util.List;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class TrainPlayBackAdapter extends BaseAdapter {

  Context context;
  List<TrainPlayBackBean> datas;

  public TrainPlayBackAdapter(Context context, List<TrainPlayBackBean> datas) {
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
      convertView = LayoutInflater.from(context).inflate(R.layout.training_back_item_layout, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.trainingBackItemTitle = (TextView) convertView.findViewById(R.id.training_back_item_title);
      viewHolder.trainingBackItemPlay = (TextView) convertView.findViewById(R.id.training_back_item_play);
      viewHolder.trainingBackItemAudience = (TextView) convertView.findViewById(R.id.training_back_item_audience);
      viewHolder.trainingBackItemPointCount = (TextView) convertView.findViewById(R.id.training_back_item_point_count);
      viewHolder.trainingBackItemImage = (ImageView) convertView.findViewById(R.id.training_back_item_image);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    viewHolder.trainingBackItemTitle.setText(datas.get(position).getTitle());
    viewHolder.trainingBackItemPlay.setText("回放");
    viewHolder.trainingBackItemAudience.setText(datas.get(position).getAudience()+
        context.getResources().getString(R.string.training_audience));
    viewHolder.trainingBackItemPointCount.setText(datas.get(position).getPoint()
        +context.getResources().getString(R.string.training_point_count));
    //holder.trainingBackItemImage.setImageBitmap(trainingPreList.get(position).get(""));
    Glide.with(context)
        .load(datas.get(position).getImgUrl())
        .placeholder(R.mipmap.b)
        .error(R.mipmap.b)
        .diskCacheStrategy(DiskCacheStrategy.ALL).
        into(viewHolder.trainingBackItemImage);

    viewHolder.trainingBackItemPlay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //培训回放点击回放
        Intent intent = new Intent();
        intent.setClass(context,TrainingBackPlayActivity.class);
        context.startActivity(intent);
      }
    });
    return convertView;
  }

  class ViewHolder {
    TextView trainingBackItemTitle;
    TextView trainingBackItemPlay;
    TextView trainingBackItemAudience;
    TextView trainingBackItemPointCount;
    ImageView trainingBackItemImage;

  }
}

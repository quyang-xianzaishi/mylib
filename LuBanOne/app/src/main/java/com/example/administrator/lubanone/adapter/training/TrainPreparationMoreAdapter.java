package com.example.administrator.lubanone.adapter.training;

import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.training.TrainPreparationBean;
import com.example.administrator.lubanone.bean.training.TrainingBean;
import java.util.List;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class TrainPreparationMoreAdapter extends BaseAdapter {

  Context context;
  List<TrainPreparationBean> datas;

  public TrainPreparationMoreAdapter(Context context, List<TrainPreparationBean> datas) {
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

  @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {

    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.training_pre_item_layout, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.trainingPreItemImage = (ImageView) convertView.findViewById(R.id.training_pre_item_image);
      viewHolder.trainingPreItemTitle = (TextView) convertView.findViewById(R.id.training_pre_item_title);
      viewHolder.trainingPreItemTime = (TextView) convertView.findViewById(R.id.training_pre_item_time);
      viewHolder.trainingPreItemRemind = (TextView) convertView.findViewById(R.id.training_pre_item_remind);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    viewHolder.trainingPreItemTitle.setText(datas.get(position).getTitle());
    viewHolder.trainingPreItemTime.setText(datas.get(position).getTime());
    //holder.trainingPreItemRemind.setText(trainingPreList.get(position).get(""));
    String str = datas.get(position).getRemindState();
    if(str!=null&&str.equals("开播提醒")){
      viewHolder.trainingPreItemRemind.setBackground(context.getResources().
          getDrawable(R.drawable.training_play_notify_bg));
    }else if(str!=null&&str.equals("取消提醒")){
      viewHolder.trainingPreItemRemind.setBackground(context.getResources().
          getDrawable(R.drawable.training_play_cancle_notify_bg));
    }
    viewHolder.trainingPreItemRemind.setText(datas.get(position).getRemindState());
    Glide.with(context)
        .load(datas.get(position).getImgUrl())
        .placeholder(R.mipmap.b)
        .error(R.mipmap.b)
        .diskCacheStrategy(DiskCacheStrategy.ALL).
        into(viewHolder.trainingPreItemImage);
    viewHolder.trainingPreItemRemind.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String str = (String) ((TextView)v).getText();
        if(str!=null&&str.equals("开播提醒")){
          //培训预告点击开播提醒通知伞下会员
          ((TextView)v).setText("取消提醒");
          datas.get(position).setRemindState("取消提醒");
          ((TextView)v).setBackground(context.getResources().
              getDrawable(R.drawable.training_play_cancle_notify_bg));
        }else if(str!=null&&str.equals("取消提醒")){
          //取消提醒
          ((TextView)v).setText("开播提醒");
          datas.get(position).setRemindState("开播提醒");
          ((TextView)v).setBackground(context.getResources().
              getDrawable(R.drawable.training_play_notify_bg));
        }
      }
    });
    return convertView;
  }

  class ViewHolder {
    ImageView trainingPreItemImage;
    TextView trainingPreItemTitle;
    TextView trainingPreItemTime;
    TextView trainingPreItemRemind;
  }
}

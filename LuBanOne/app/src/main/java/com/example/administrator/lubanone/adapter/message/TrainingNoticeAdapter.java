package com.example.administrator.lubanone.adapter.message;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.training.activity.ApplyTrainingActivity;
import com.example.administrator.lubanone.activity.training.activity.MyTeamActivity;
import com.example.administrator.lubanone.bean.message.TrainingNoticeBean;
import java.util.List;

/**
 * Created by Administrator on 2017\7\6 0006.
 */

public class TrainingNoticeAdapter extends BaseAdapter{

  Context context;
  List<TrainingNoticeBean> datas;

  public TrainingNoticeAdapter(Context context, List<TrainingNoticeBean> datas) {
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
  public View getView(final int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.training_notice_item, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.trainingNoticeItemTitle = (TextView) convertView.findViewById(
          R.id.training_notice_item_title);
      viewHolder.trainingNoticeItemTime = (TextView) convertView.findViewById(
          R.id.training_notice_item_time);
      viewHolder.trainingNoticeItemContent = (TextView) convertView.findViewById(
          R.id.training_notice_item_content);
      viewHolder.trainingNoticeItemTrainingTheme = (TextView) convertView.findViewById(
          R.id.training_notice_item_training_theme);
      viewHolder.trainingNoticeItemTrainingSummary = (TextView) convertView.findViewById(
          R.id.training_notice_item_training_summary);
      viewHolder.trainingNoticeItemTrainingTime = (TextView) convertView.findViewById(
          R.id.training_notice_item_training_time);
      viewHolder.trainingNoticeItemClick = (TextView) convertView.findViewById(
          R.id.training_notice_item_click);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    if(datas.get(position).getTitle()!=null&&datas.get(position).getTitle().length()>0){
      viewHolder.trainingNoticeItemTitle.setText(datas.get(position).getTitle());
    }
    if(datas.get(position).getTime()!=null&&datas.get(position).getTime().length()>0){
      viewHolder.trainingNoticeItemTime.setVisibility(View.VISIBLE);
      viewHolder.trainingNoticeItemTime.setText(datas.get(position).getTime());
    }else {
      viewHolder.trainingNoticeItemTime.setVisibility(View.GONE);
    }
    if(datas.get(position).getContent()!=null&&datas.get(position).getContent().length()>0){
      viewHolder.trainingNoticeItemContent.setText(datas.get(position).getContent());
    }
    if(datas.get(position).getTrainingTheme()!=null&&datas.get(position).getTrainingTheme().length()>0){
      viewHolder.trainingNoticeItemTrainingTheme.setVisibility(View.VISIBLE);
      viewHolder.trainingNoticeItemTrainingTheme.setText(
          context.getResources().getString(R.string.training_theme)+datas.get(position).getTrainingTheme());
    }else {
      viewHolder.trainingNoticeItemTrainingTheme.setVisibility(View.GONE);
    }
    if(datas.get(position).getTrainingSummary()!=null&&datas.get(position).getTrainingSummary().length()>0){
      viewHolder.trainingNoticeItemTrainingSummary.setVisibility(View.VISIBLE);
      viewHolder.trainingNoticeItemTrainingSummary.setText(
          context.getResources().getString(R.string.training_summary)+datas.get(position).getTrainingSummary());
    }else {
      viewHolder.trainingNoticeItemTrainingSummary.setVisibility(View.GONE);
    }
    if(datas.get(position).getTrainingTime()!=null&&datas.get(position).getTrainingTime().length()>0){
      viewHolder.trainingNoticeItemTrainingTime.setVisibility(View.VISIBLE);
      viewHolder.trainingNoticeItemTrainingTime.setText(
          context.getResources().getString(R.string.training_time)+datas.get(position).getTrainingTime());
    }else {
      viewHolder.trainingNoticeItemTrainingTime.setVisibility(View.GONE);
    }
    final String str = datas.get(position).getType();
    viewHolder.trainingNoticeItemClick.setText(datas.get(position).getClick());
    if(str!=null&&str.length()>0){
      if(str.equals("3")){
        viewHolder.trainingNoticeItemClick.setVisibility(View.GONE);
      }else {
        viewHolder.trainingNoticeItemClick.setVisibility(View.VISIBLE);
        viewHolder.trainingNoticeItemClick.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            farwardActivity(str);
          }
        });
      }
    }else {
      viewHolder.trainingNoticeItemClick.setVisibility(View.GONE);
    }

    return convertView;

  }

  class ViewHolder {
    TextView trainingNoticeItemTitle;
    TextView trainingNoticeItemTime;
    TextView trainingNoticeItemContent;
    TextView trainingNoticeItemTrainingTheme;
    TextView trainingNoticeItemTrainingSummary;
    TextView trainingNoticeItemTrainingTime;
    TextView trainingNoticeItemClick;
  }

  private void farwardActivity(String type){
    if(type.equals("1")){
      //开播提醒，进入直播页面
      Toast.makeText(context,"进入培训直播页面",Toast.LENGTH_LONG).show();
    }
    if(type.equals("2")){
      //培训邀请，查看预告
      Toast.makeText(context,"查看直播预告",Toast.LENGTH_LONG).show();
    }
    if(type.equals("4")){
      //审核通过，通知伞下会员
      Intent intent = new Intent();
      intent.setClass(context, MyTeamActivity.class);
      context.startActivity(intent);
    }
    if(type.equals("5")){
      //申请被拒，申请新的培训
      Intent intent = new Intent();
      intent.setClass(context, ApplyTrainingActivity.class);
      context.startActivity(intent);
    }


  }

}

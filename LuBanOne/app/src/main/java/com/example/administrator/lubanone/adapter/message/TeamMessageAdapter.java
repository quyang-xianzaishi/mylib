package com.example.administrator.lubanone.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.message.TeamMessageBean;
import com.example.administrator.lubanone.bean.message.TrainingNoticeBean;
import java.util.List;

/**
 * Created by Administrator on 2017\7\6 0006.
 */

public class TeamMessageAdapter extends BaseAdapter{

  Context context;
  List<TeamMessageBean> datas;

  public TeamMessageAdapter(Context context, List<TeamMessageBean> datas) {
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
      convertView = LayoutInflater.from(context).inflate(R.layout.team_message_item, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.teamMessageItemTitle = (TextView) convertView.findViewById(
          R.id.team_message_item_title);
      viewHolder.teamMessageItemTime = (TextView) convertView.findViewById(
          R.id.team_message_item_time);
      viewHolder.teamMessageItemContent = (TextView) convertView.findViewById(
          R.id.team_message_item_content);
      viewHolder.teamMessageItemTrainingTheme = (TextView) convertView.findViewById(
          R.id.team_message_item_training_theme);
      viewHolder.teamMessageItemTrainingSummary = (TextView) convertView.findViewById(
          R.id.team_message_item_training_summary);
      viewHolder.teamMessageItemTrainingTime = (TextView) convertView.findViewById(
          R.id.team_message_item_training_time);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    if(datas.get(position).getTitle()!=null&&datas.get(position).getTitle().length()>0){
      viewHolder.teamMessageItemTitle.setText(datas.get(position).getTitle());
    }
    if(datas.get(position).getTime()!=null&&datas.get(position).getTime().length()>0){
      viewHolder.teamMessageItemTime.setVisibility(View.VISIBLE);
      viewHolder.teamMessageItemTime.setText(datas.get(position).getTime());
    }else {
      viewHolder.teamMessageItemTime.setVisibility(View.GONE);
    }
    if(datas.get(position).getContent()!=null&&datas.get(position).getContent().length()>0){
      viewHolder.teamMessageItemContent.setText(datas.get(position).getContent());
    }
    if(datas.get(position).getTrainingTheme()!=null&&datas.get(position).getTrainingTheme().length()>0){
      viewHolder.teamMessageItemTrainingTheme.setVisibility(View.VISIBLE);
      viewHolder.teamMessageItemTrainingTheme.setText(
          context.getResources().getString(R.string.training_theme)+datas.get(position).getTrainingTheme());
    }else {
      viewHolder.teamMessageItemTrainingTheme.setVisibility(View.GONE);
    }
    if(datas.get(position).getTrainingSummary()!=null&&datas.get(position).getTrainingSummary().length()>0){
      viewHolder.teamMessageItemTrainingSummary.setVisibility(View.VISIBLE);
      viewHolder.teamMessageItemTrainingSummary.setText(datas.get(position).getTrainingSummary());
    }else {
      viewHolder.teamMessageItemTrainingSummary.setVisibility(View.GONE);
    }
    if(datas.get(position).getTrainingTime()!=null&&datas.get(position).getTrainingTime().length()>0){
      viewHolder.teamMessageItemTrainingTime.setVisibility(View.VISIBLE);
      viewHolder.teamMessageItemTrainingTime.setText(
          context.getResources().getString(R.string.training_time)+datas.get(position).getTrainingTime());
    }else {
      viewHolder.teamMessageItemTrainingTime.setVisibility(View.GONE);
    }

    return convertView;

  }

  class ViewHolder {
    TextView teamMessageItemTitle;
    TextView teamMessageItemTime;
    TextView teamMessageItemContent;
    TextView teamMessageItemTrainingTheme;
    TextView teamMessageItemTrainingSummary;
    TextView teamMessageItemTrainingTime;
  }

}

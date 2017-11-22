package com.example.administrator.lubanone.adapter.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.TaskCollectBean;

import java.util.List;

/**
 * Created by hou on 2017/6/30.
 */

public class TaskCollectLvAdapter extends BaseAdapter {

  Context context;
  List<TaskCollectBean.TaskCollectList> datas;

  public TaskCollectLvAdapter(Context context, List<TaskCollectBean.TaskCollectList> datas) {
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
    ViewHolder vh;
    if (convertView == null) {
      convertView = LayoutInflater.from(context)
          .inflate(R.layout.item_task_fragment_common_list, null);
      vh = new ViewHolder();
      vh.itemImage = (ImageView) convertView.findViewById(R.id.task_list_item_image);
      vh.itemTitle = (TextView) convertView.findViewById(R.id.task_list_item_title);
      vh.itemReward = (TextView) convertView.findViewById(R.id.task_list_item_reward);
      convertView.setTag(vh);
    } else {
      vh = (ViewHolder) convertView.getTag();
    }

    Glide.with(context).load(datas.get(position).getIf_thumimg()).placeholder(R.drawable.example)
        .into(vh.itemImage);
    vh.itemTitle.setText(datas.get(position).getIf_title());
    vh.itemReward.setText(datas.get(position).getCollect_time());
    vh.itemReward.setTextColor(context.getResources().getColor(R.color.gray_b6b6b6));
    return convertView;
  }

  class ViewHolder {

    ImageView itemImage;
    TextView itemTitle;
    TextView itemReward;
  }

}

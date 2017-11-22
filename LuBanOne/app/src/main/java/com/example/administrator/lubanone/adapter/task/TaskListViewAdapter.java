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
import com.example.administrator.lubanone.bean.TasksBean;

import java.util.List;

/**
 * Created by hou on 2017/6/22.
 */

public class TaskListViewAdapter extends BaseAdapter {

  Context context;
  List<TasksBean> datas;

  public TaskListViewAdapter(Context context, List<TasksBean> datas) {
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
      convertView = LayoutInflater.from(context).inflate(R.layout.item_tasks_list, null);
      viewHolder = new ViewHolder();
      viewHolder.mImg = (ImageView) convertView.findViewById(R.id.task_list_item_img);
      viewHolder.taskName = (TextView) convertView.findViewById(R.id.task_list_item_name);
      viewHolder.taskStatus = (TextView) convertView.findViewById(R.id.task_list_item_status);
      viewHolder.catalystsNum = (TextView) convertView
          .findViewById(R.id.task_list_item_catalystsNum);
      viewHolder.clickNum = (TextView) convertView.findViewById(R.id.task_list_item_click_tv);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    viewHolder.taskName.setText(datas.get(position).getIf_title());
    switch (datas.get(position).getStatus()){
      case "0":
        viewHolder.taskStatus.setText("未完成");
        break;
      case "1":
        viewHolder.taskStatus.setText("审核中");
        break;
      case "2":
        viewHolder.taskStatus.setText("已完成");
        break;
//      case "3":
//        viewHolder.taskStatus.setText("审核不通过");
//        break;
    }
    viewHolder.catalystsNum.setText(datas.get(position).getIf_cataindex());
    viewHolder.clickNum.setText(datas.get(position).getClicknum());
    Glide.with(context).load(datas.get(position).getIf_thumimg()).into(viewHolder.mImg);
    return convertView;
  }

  class ViewHolder {

    ImageView mImg;
    TextView taskName;
    TextView taskStatus;
    TextView catalystsNum;
    TextView clickNum;
  }
}

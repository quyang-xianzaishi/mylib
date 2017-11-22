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
import com.example.administrator.lubanone.bean.model.TaskModel;
import com.example.administrator.lubanone.utils.GlideRoundTransform;
import java.util.List;

/**
 * Created by hou on 2017/8/25.
 */

public class TaskCommonListAdapter extends BaseAdapter {

  private Context context;
  private List<TaskModel.TaskList> datas;

  public TaskCommonListAdapter(Context context,
      List<TaskModel.TaskList> datas) {
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
      vh.finishImage = (ImageView) convertView.findViewById(R.id.task_list_item_finish_image);
      vh.reviewingImage = (ImageView) convertView.findViewById(R.id.task_list_item_reviewing_image);
      convertView.setTag(vh);
    } else {
      vh = (ViewHolder) convertView.getTag();
    }

    Glide.with(context)
        .load(datas.get(position).getThumimg())
//        .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504861721181&di=9b1ce1e6135a5295c6b04791b544504c&imgtype=0&src=http%3A%2F%2Fpic.jj20.com%2Fup%2Fallimg%2F911%2F021616153629%2F160216153629-1.jpg")
        .placeholder(R.drawable.example)
        .transform(new GlideRoundTransform(context, 4))
        .into(vh.itemImage);
    vh.itemTitle.setText(datas.get(position).getTitle());
    vh.itemReward.setText(
        context.getText(R.string.task_list_item_reward1) + datas.get(position).getCatalyst()
            + context.getText(R.string.task_list_item_reward2));
    switch (datas.get(position).getStatus()) {
      case "0":

        break;
      case "1":
        vh.reviewingImage.setVisibility(View.VISIBLE);
        break;
      case "2":
        vh.finishImage.setVisibility(View.VISIBLE);
        break;
      case "3":
        vh.finishImage.setImageResource(R.drawable.task_list_item_gray_finish);
        vh.finishImage.setVisibility(View.VISIBLE);
        break;
      default:
        vh.itemTitle.setText(
            context.getText(R.string.task_list_status_fail) + datas.get(position).getStatus());
        break;
    }
    return convertView;
  }

  class ViewHolder {

    ImageView itemImage;
    TextView itemTitle;
    TextView itemReward;
    ImageView finishImage;
    ImageView reviewingImage;
  }
}

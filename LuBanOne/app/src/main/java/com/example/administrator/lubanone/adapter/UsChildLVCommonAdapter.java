package com.example.administrator.lubanone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.model.UsChildLVCommonBean;

import com.example.administrator.lubanone.utils.GlideRoundTransform;
import java.util.List;

/**
 * 关于我们，各个子fragment中listview适配器
 * <p>
 * Created by hou on 2017/7/4.
 */

public class UsChildLVCommonAdapter extends BaseAdapter {

  private List<UsChildLVCommonBean.UsChildLvList> datas;
  private Context context;

  public UsChildLVCommonAdapter(List<UsChildLVCommonBean.UsChildLvList> datas, Context context) {
    this.datas = datas;
    this.context = context;
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

    Glide
        .with(context)
        .load(datas.get(position).getThumimg())
        .placeholder(R.drawable.example)
        .transform(new GlideRoundTransform(context, 4))
        .into(vh.itemImage);
    vh.itemTitle.setText(datas.get(position).getTitle());
    vh.itemReward.setText(datas.get(position).getDatetime());
    vh.itemReward.setTextColor(context.getResources().getColor(R.color.gray_888888));
    return convertView;
  }

  class ViewHolder {

    ImageView itemImage;
    TextView itemTitle;
    TextView itemReward;
  }
}

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
import com.example.administrator.lubanone.activity.message.MemberInfoActivity;
import com.example.administrator.lubanone.bean.message.RecommendFriendBean;
import java.util.List;

/**
 * Created by Administrator on 2017\8\8 0008.
 */

public class RecommendFriendAdapter extends BaseAdapter {

  Context context;
  List<RecommendFriendBean> datas;

  public RecommendFriendAdapter(Context context,
      List<RecommendFriendBean> datas) {
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
      convertView = LayoutInflater.from(context).inflate(R.layout.recommend_friend_list_item, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.recommendFriendListItemTitle = (TextView) convertView.findViewById(
          R.id.recommend_friend_list_item_title);
      viewHolder.recommendFriendListItemTime = (TextView) convertView.findViewById(
          R.id.recommend_friend_list_item_time);
      viewHolder.recommendFriendListItemContent = (TextView) convertView.findViewById(
          R.id.recommend_friend_item_content);
      viewHolder.recommendFriendListItemFriendText = (TextView) convertView.findViewById(
          R.id.recommend_friend_item_friend_text);
      viewHolder.recommendFriendListItemFriend = (TextView) convertView.findViewById(
          R.id.recommend_friend_item_friend);
      viewHolder.detail = (TextView) convertView.findViewById(R.id.recommend_message_item_detail);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    viewHolder.recommendFriendListItemTitle.setText(datas.get(position).getTitle());
    viewHolder.recommendFriendListItemTime.setText(datas.get(position).getTime());
    //viewHolder.recommendFriendListItemContent.setText(datas.get(position).getContent());
    String str1 ="";
    String str2 ="";
    if (datas.get(position).getContent() != null && datas.get(position).getContent().length() > 6) {
      str1 = datas.get(position).getContent().toString().substring(0, 6);
      str2 = datas.get(position).getContent().toString()
          .substring(6, datas.get(position).getContent().length() - 1);
      viewHolder.recommendFriendListItemContent.setText(str1+"\n"+
          str2);
    }else {
      viewHolder.recommendFriendListItemContent.setText(datas.get(position).getContent());
    }
    viewHolder.recommendFriendListItemFriend.setText(datas.get(position).getBerecommendname());
    viewHolder.recommendFriendListItemFriend.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("userId",datas.get(position).getBerecommendid());
        intent.setClass(context, MemberInfoActivity.class);
        context.startActivity(intent);
      }
    });
    viewHolder.detail.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("userId",datas.get(position).getBerecommendid());
        intent.setClass(context, MemberInfoActivity.class);
        context.startActivity(intent);
      }
    });
    return convertView;
  }

  class ViewHolder {
    TextView recommendFriendListItemTitle;
    TextView recommendFriendListItemTime;
    TextView recommendFriendListItemContent;
    TextView recommendFriendListItemFriendText;
    TextView recommendFriendListItemFriend;
    TextView detail;
  }
}

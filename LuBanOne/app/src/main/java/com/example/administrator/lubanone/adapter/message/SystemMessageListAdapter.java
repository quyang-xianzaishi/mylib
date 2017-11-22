package com.example.administrator.lubanone.adapter.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.message.CustomMessageActivity;
import com.example.administrator.lubanone.bean.message.SystemMessageBean;
import com.example.administrator.lubanone.bean.message.SystemMessageListBean;
import java.util.List;

/**
 * Created by Administrator on 2017\7\6 0006.
 */

public class SystemMessageListAdapter extends BaseAdapter{

  Context context;
  List<SystemMessageListBean> datas;

  public SystemMessageListAdapter(Context context, List<SystemMessageListBean> datas) {
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
      convertView = LayoutInflater.from(context).inflate(R.layout.system_message_list_item, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.systemMessageListItemTitle = (TextView) convertView.findViewById(
          R.id.system_message_list_item_title);
      viewHolder.systemMessageListItemTime = (TextView) convertView.findViewById(
          R.id.system_message_list_item_time);
      viewHolder.systemMessageListItemContent = (TextView) convertView.findViewById(
          R.id.system_message_list_item_content);
      viewHolder.systemMessageListItemEnterDetail = (TextView) convertView.findViewById(
          R.id.system_message_list_item_enter_detail);
      viewHolder.systemMessageListItemEnterCustom = (TextView) convertView.findViewById(
          R.id.system_message_list_item_enter_custom);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    viewHolder.systemMessageListItemTitle.setText(datas.get(position).getTitle());
    viewHolder.systemMessageListItemTime.setText(datas.get(position).getTime());
    //viewHolder.systemMessageListItemContent.setText(datas.get(position).getContent());
    String str1 = "";
    String str2 = "";
    if (datas.get(position).getContent() != null && datas.get(position).getContent().length() > 6) {
      str1 = datas.get(position).getContent().toString().substring(0, 6);
      str2 = datas.get(position).getContent().toString()
          .substring(6, datas.get(position).getContent().length() - 1);
      if(str1.equals(context.getString(R.string.system_message_hint1))||
          str1.equals(context.getString(R.string.system_message_hint2))||
          str1.equals(context.getString(R.string.system_message_hint3))||
          str1.equals(context.getString(R.string.system_message_hint4))){
        viewHolder.systemMessageListItemContent.setText(str1+"\n"+
            str2);
      }else {
        viewHolder.systemMessageListItemContent.setText(str1+ str2);
      }

    }else {
      viewHolder.systemMessageListItemContent.setText(datas.get(position).getContent());
    }

    /*String str = datas.get(position).getType();
    if(str!=null&&str.length()>0){
      if(str.equals("3")){
        viewHolder.systemMessageListItemEnterCustom.setVisibility(View.VISIBLE);
      }else {
        viewHolder.systemMessageListItemEnterCustom.setVisibility(View.GONE);
      }
      if(str.equals("3")||str.equals("2")){
        viewHolder.systemMessageListItemEnterDetail.setVisibility(View.VISIBLE);
        viewHolder.systemMessageListItemTitle.setTextColor(context.getResources().getColor(R.color.red));
      }else {
        viewHolder.systemMessageListItemEnterDetail.setVisibility(View.GONE);
        viewHolder.systemMessageListItemTitle.setTextColor(context.getResources().getColor(R.color.black));
      }
    }else {
      viewHolder.systemMessageListItemEnterCustom.setVisibility(View.GONE);
      viewHolder.systemMessageListItemEnterDetail.setVisibility(View.GONE);
      viewHolder.systemMessageListItemTitle.setTextColor(context.getResources().getColor(R.color.black));
    }*/
    viewHolder.systemMessageListItemEnterDetail.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
      }
    });
    viewHolder.systemMessageListItemEnterCustom.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(context, CustomMessageActivity.class);
        context.startActivity(intent);
      }
    });
    return convertView;

  }

  class ViewHolder {
    TextView systemMessageListItemTitle;
    TextView systemMessageListItemTime;
    TextView systemMessageListItemContent;
    TextView systemMessageListItemEnterDetail;
    TextView systemMessageListItemEnterCustom;
  }

  /**
   * 字符串高亮显示部分文字
   * @param textView  控件
   * @param str1      要高亮显示的文字（输入的关键词）
   * @param str2      包含高亮文字的字符串
   */
  private void setTextHighLight(TextView textView, String str1, String str2) {

    SpannableString sp = new SpannableString(str2);
    // 遍历要显示的文字
    for (int i = 0 ; i < str1.length() ; i ++){
      // 得到单个文字
      String s1 = str1.charAt(i) + "";
      // 判断字符串是否包含高亮显示的文字
      if (str2.contains(s1)){
        // 查找文字在字符串的下标
        int index = str2.indexOf(s1);
        // 循环查找字符串中所有该文字并高亮显示
        while (index != -1) {
          ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
          sp.setSpan(colorSpan, index, index + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
          // s1从这个索引往后开始第一个出现的位置
          index = str2.indexOf(s1, index + 1);
        }
      }
    }
    // 设置控件
    textView.setText(sp);
  }

}

package com.example.administrator.lubanone.adapter.message;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.home.BuySeedProcessActivity;
import com.example.administrator.lubanone.activity.home.CreditGradeActivity;
import com.example.administrator.lubanone.activity.home.SellSeedsProcessActivity;
import com.example.administrator.lubanone.activity.message.MemberInfoActivity;
import com.example.administrator.lubanone.bean.message.OrderMessageBean;
import java.util.List;

/**
 * Created by Administrator on 2017\7\26 0026.
 */

public class OrderMessageAdapter extends BaseAdapter {

  Context context;
  List<OrderMessageBean> datas;

  public OrderMessageAdapter(Context context, List<OrderMessageBean> datas) {
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
      convertView = LayoutInflater.from(context).inflate(R.layout.order_message_item, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.orderMessageItemTitle = (TextView) convertView.findViewById(
          R.id.order_message_item_title);
      viewHolder.orderMessageItemTime = (TextView) convertView.findViewById(
          R.id.order_message_item_time);
      viewHolder.orderMessageItemContent = (TextView) convertView.findViewById(
          R.id.order_message_item_content);
      viewHolder.orderMessageItemMemberType = (TextView) convertView.findViewById(
          R.id.order_message_item_member_type);
      viewHolder.orderMessageItemMember = (TextView) convertView.findViewById(
          R.id.order_message_item_member);
      viewHolder.orderMessageItemOrderNumber = (TextView) convertView.findViewById(
          R.id.order_message_item_order_number);
      viewHolder.orderMessageItemExpectTime = (TextView) convertView.findViewById(
          R.id.order_message_item_expect_time);
      viewHolder.orderMessageItemExpectTimeText = (TextView) convertView.findViewById(
          R.id.order_message_item_expect_time_text);
      viewHolder.orderMessageItemClick = (TextView) convertView.findViewById(
          R.id.order_message_item_click);
      viewHolder.orderMessageItemMemberLinear = (LinearLayout) convertView.findViewById(R.id.order_message_item_member_linear);
      viewHolder.itemClickLinear = (LinearLayout) convertView.findViewById(R.id.item_click_linear);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    viewHolder.orderMessageItemTitle.setText(datas.get(position).getTitle());
    viewHolder.orderMessageItemTime.setText(datas.get(position).getPushtime());
    String str1 ="";
    String str2 ="";
    if (datas.get(position).getContent() != null && datas.get(position).getContent().length() > 6) {
      str1 = datas.get(position).getContent().toString().substring(0, 6);
      str2 = datas.get(position).getContent().toString()
          .substring(6, datas.get(position).getContent().length() - 1);
      viewHolder.orderMessageItemContent.setText(str1+"\n"+
          str2);
    }else {
      viewHolder.orderMessageItemContent.setText(datas.get(position).getContent());
    }
    String string = datas.get(position).getOrdertype().toString();
    if(string!=null&&string.length()>0){
      if(string.equals("1")||string.equals("9")){
        viewHolder.orderMessageItemMemberLinear.setVisibility(View.GONE);
      }else {
        viewHolder.orderMessageItemMemberLinear.setVisibility(View.VISIBLE);
      }
    }else {
      viewHolder.orderMessageItemMemberLinear.setVisibility(View.VISIBLE);
    }
    viewHolder.orderMessageItemMember.setText(datas.get(position).getMember());
    viewHolder.orderMessageItemMemberType.setText(getMemberType(position));
    viewHolder.orderMessageItemOrderNumber.setText(datas.get(position).getOrderid());
    viewHolder.orderMessageItemExpectTimeText.setText(datas.get(position).getOtherdescription()+"：");
    viewHolder.orderMessageItemExpectTime.setText(datas.get(position).getTime());
    if(getClickType(position)!=null&&getClickType(position).length()>0){
      viewHolder.orderMessageItemClick.setVisibility(View.VISIBLE);
      viewHolder.itemClickLinear.setVisibility(View.VISIBLE);
      viewHolder.orderMessageItemClick.setText(getClickType(position));
    }else {
      viewHolder.orderMessageItemClick.setVisibility(View.GONE);
      viewHolder.itemClickLinear.setVisibility(View.GONE);
    }
    viewHolder.orderMessageItemClick.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String str = ((TextView)v).getText().toString();
        if(str!=null&&str.length()>0){
          if(str.equals("查看订单详情")){
            farwardActivity(position);
          }else if(str.equals("查看信用评分")){
            Intent intent = new Intent();
            intent.setClass(context, CreditGradeActivity.class);
            context.startActivity(intent);
          }
        }
      }
    });
    viewHolder.orderMessageItemMember.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        //点击查看会员信息
        Intent intent = new Intent();
        intent.putExtra("userId",datas.get(position).getMemberid());
        intent.setClass(context, MemberInfoActivity.class);
        context.startActivity(intent);
      }
    });

    return convertView;

  }

  class ViewHolder {
    TextView orderMessageItemTitle;
    TextView orderMessageItemTime;
    TextView orderMessageItemContent;
    TextView orderMessageItemMemberType;
    TextView orderMessageItemMember;
    TextView orderMessageItemOrderNumber;
    TextView orderMessageItemExpectTimeText;
    TextView orderMessageItemExpectTime;
    TextView orderMessageItemClick;
    LinearLayout orderMessageItemMemberLinear;
    LinearLayout itemClickLinear;
  }


  private String getMemberType(int position){
    String memberType = null;
    String str = datas.get(position).getOrdertype().toString();
    if(str!=null&&str.length()>0){
      switch(Integer.parseInt(str)){
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 17:
        case 18:
        case 19:
        case 20:
        case 21:
        case 27:
          memberType = "卖方会员：";
        break;
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 16:
        case 22:
        case 23:
        case 24:
        case 25:
        case 26:
          memberType = "买方会员：";
          break;
        default:
          break;
      }
    }
    return memberType;
  }

  private String getClickType(int position){
    String clickType = null;
    String str = datas.get(position).getOrdertype().toString();
    if(str!=null&&str.length()>0){
      if(str.equals("5")||str.equals("6")||str.equals("13")||str.equals("14")){
        clickType = "查看信用评分";
      }else if(str.equals("27")){
        clickType = "";
      }else {
        clickType = "查看订单详情";
      }
    }else {
      clickType = "";
    }
    return clickType;
  }

  private void farwardActivity(int position){
    String str = datas.get(position).getOrdertype().toString();
    if(str!=null&&str.length()>0){
      if(str.equals("1")){
        //买入待匹配
        Intent intent = new Intent(context, BuySeedProcessActivity.class);
        intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_match_from_home_fragment);
        context.startActivity(intent);
      }else if(str.equals("2")||str.equals("7")||str.equals("8")||str.equals("17")||str.equals("20")){
        //买入待付款
        Intent intent = new Intent(context, BuySeedProcessActivity.class);
        intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_money_from_home_fragment);
        context.startActivity(intent);
      }else if(str.equals("3")||str.equals("18")||str.equals("19")||str.equals("21")){
        //买入待确认
        Intent intent = new Intent(context, BuySeedProcessActivity.class);
        intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_comfrim_from_home_fragment);
        context.startActivity(intent);
      }else if(str.equals("4")){
        //买入待评价
        Intent intent = new Intent(context, BuySeedProcessActivity.class);
        intent.putExtra(Config.BUY_PROCESS_KEY, Config.buy_process_pingjia_from_home_fragment);
        context.startActivity(intent);
      }else if(str.equals("9")){
        //卖出待匹配
        Intent intent = new Intent(context, SellSeedsProcessActivity.class);
        intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_MATCH_FROM_HOME_FRAGMENT);
        context.startActivity(intent);
      }else if(str.equals("10")||str.equals("15")||str.equals("16")||str.equals("22")||str.equals("25")){
        //卖出待付款
        Intent intent = new Intent(context, SellSeedsProcessActivity.class);
        intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_MONEY_FROM_HOME_FRAGMENT);
        context.startActivity(intent);
      }else if(str.equals("11")||str.equals("23")||str.equals("24")||str.equals("26")){
        //卖出待确认
        Intent intent = new Intent(context, SellSeedsProcessActivity.class);
        intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_COMFRIM_FROM_HOME_FRAGMENT);
        context.startActivity(intent);
      }else if(str.equals("12")){
        //卖出待评价
        Intent intent = new Intent(context, SellSeedsProcessActivity.class);
        intent.putExtra(Config.SELL_PROCESS_KEY, Config.SELL_PROCESS_PINGJIA_FROM_HOME_FRAGMENT);
        context.startActivity(intent);
      }
    }
  }


}

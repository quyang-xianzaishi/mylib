package com.example.administrator.lubanone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.model.UploadRecord;
import java.util.List;

/**
 * 关于我们，我要推广，上传推广资料，我的上传记录适配器
 *
 * Created by hou on 2017/7/4.
 */

public class UsSpreadUploadRecordAdapter extends BaseAdapter {

  private List<UploadRecord> datas;
  private Context context;
  private boolean isMore = false;

  public UsSpreadUploadRecordAdapter(List<UploadRecord> datas, Context context, boolean isMore) {
    this.datas = datas;
    this.context = context;
    this.isMore = isMore;
  }

  @Override
  public int getCount() {
    if (isMore) {
      return datas.size();
    }
    if (datas.size() < 5) {
      return datas.size();
    }
    return 5;
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

    ViewHolder vh;
    if (convertView == null) {
      convertView = LayoutInflater.from(context)
          .inflate(R.layout.item_us_spread_upload_record_lv, null);
      vh = new ViewHolder();
      vh.mName = (TextView) convertView.findViewById(R.id.us_spread_upload_record_item_title);
      vh.mDate = (TextView) convertView.findViewById(R.id.us_spread_upload_record_item_date);
      vh.mStatus = (TextView) convertView.findViewById(R.id.us_spread_upload_record_item_status);
      vh.clickBtn = (ImageView) convertView
          .findViewById(R.id.us_spread_upload_record_right);
      vh.notifyMember = (TextView) convertView
          .findViewById(R.id.us_spread_upload_item_notify_next_member);
      vh.notifiedMember = (TextView) convertView
          .findViewById(R.id.us_spread_upload_item_notified_next_member);
      convertView.setTag(vh);
    } else {
      vh = (ViewHolder) convertView.getTag();
    }
    String codeNum = datas.get(position).getIf_jhm();//激活码个数
    vh.mName.setText(datas.get(position).getIf_title());
    vh.mDate.setText(datas.get(position).getIf_time());
    vh.mStatus.setText(datas.get(position).getState());
    switch (datas.get(position).getState()) {
      case "0":
        vh.clickBtn.setImageResource(R.drawable.back_right_white);
        vh.mStatus.setTextColor(context.getResources().getColor(R.color.gray_888888));
        vh.mStatus.setText(context.getText(R.string.train_data_applying));
        vh.notifiedMember.setVisibility(View.GONE);
        vh.notifyMember.setVisibility(View.GONE);
        break;
      case "1":
        vh.mStatus.setTextColor(context.getResources().getColor(R.color.cEA5412));
        vh.mStatus.setText(
            context.getText(R.string.pass_apply_and_activation_code1) + codeNum + context
                .getText(R.string.pass_apply_and_activation_code2) + datas.get(position)
                .getCatalyst() + context.getText(R.string.pass_apply_and_activation_code3));
        vh.clickBtn.setImageResource(R.mipmap.next_gray_2x_5);
        switch (datas.get(position).getIs_notice()) {
          case "0"://未通知
            vh.notifyMember.setVisibility(View.VISIBLE);
            vh.notifiedMember.setVisibility(View.GONE);
            break;
          case "1"://已通知
            vh.notifyMember.setVisibility(View.GONE);
            vh.notifiedMember.setVisibility(View.VISIBLE);
            break;
          default:

            break;
        }
        break;
      case "2":
        vh.mStatus.setTextColor(context.getResources().getColor(R.color.gray_888888));
        vh.mStatus.setText(
            context.getText(R.string.train_data_no_pass_apply1) + datas.get(position).getReason()
                + context.getText(R.string.train_data_no_pass_apply2));
        vh.clickBtn.setImageResource(R.drawable.back_right_white);
        vh.notifiedMember.setVisibility(View.GONE);
        vh.notifyMember.setVisibility(View.GONE);
        break;
      default:
        vh.mStatus
            .setText(context.getText(R.string.unknown_status) + datas.get(position).getState());
        vh.mStatus.setTextColor(context.getResources().getColor(R.color.cEA5412));
        vh.notifiedMember.setVisibility(View.GONE);
        vh.notifyMember.setVisibility(View.GONE);
        break;
    }

    vh.clickBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onClickBtnListener.onClickBtn(position);
      }
    });

    vh.notifyMember.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onItemNotifyMember.onNotifyMember(position);
      }
    });

    return convertView;
  }

  //============下===============
  public interface onItemNotifyMember {

    void onNotifyMember(int position);
  }

  private onItemNotifyMember onItemNotifyMember;

  public void setOnItemNotifyMemberListener(onItemNotifyMember onItemNotifyMemberListener) {
    this.onItemNotifyMember = onItemNotifyMemberListener;
  }

  //============上=================


  //=============下================
  public interface OnClickBtnListener {

    void onClickBtn(int position);
  }

  private OnClickBtnListener onClickBtnListener;

  public void setOnClickBtnListener(OnClickBtnListener onClickBtnListener) {
    this.onClickBtnListener = onClickBtnListener;
  }
//================上================

  class ViewHolder {

    TextView mName;
    TextView mStatus;
    TextView mDate;
    ImageView clickBtn;
    TextView notifyMember;
    TextView notifiedMember;
  }
}

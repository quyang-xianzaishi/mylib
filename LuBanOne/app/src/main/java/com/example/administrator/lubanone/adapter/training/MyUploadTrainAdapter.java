package com.example.administrator.lubanone.adapter.training;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.model.TrainMyUploadModel;
import com.example.administrator.lubanone.bean.model.TrainMyUploadModel.MyUploadInfo;
import java.util.List;

/**
 * Created by hou on 2017/8/5.
 */

public class MyUploadTrainAdapter extends BaseAdapter {

  private Context context;
  private List<TrainMyUploadModel.MyUploadInfo> datas;
  private boolean isMore = false;

  public MyUploadTrainAdapter(Context context,
      List<MyUploadInfo> datas, boolean isMore) {
    this.context = context;
    this.datas = datas;
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
      convertView = LayoutInflater.from(context).inflate(R.layout.my_training_item, null);
      vh = new ViewHolder();
      vh.trainTheme = (TextView) convertView.findViewById(R.id.my_training_item_theme);
      vh.applyTime = (TextView) convertView.findViewById(R.id.my_training_item_apply_time);
      vh.trainStatus = (TextView) convertView.findViewById(R.id.my_training_item_training_state);
      vh.clickBtn = (ImageView) convertView.findViewById(R.id.my_training_item_training_notify);
      vh.notifyMember = (TextView) convertView
          .findViewById(R.id.train_upload_item_notify_next_member);
      vh.notifiedMember = (TextView) convertView
          .findViewById(R.id.train_upload_item_notified_next_member);
      convertView.setTag(vh);
    } else {
      vh = (ViewHolder) convertView.getTag();
    }
    //数据处理
    vh.trainTheme.setText(datas.get(position).getTheme());
    vh.applyTime.setText(datas.get(position).getTraintime());
    switch (datas.get(position).getStatus()) {
      case "0"://审核中
        vh.trainStatus.setText(context.getResources().getString(R.string.train_data_applying));
        vh.trainStatus.setTextColor(context.getResources().getColor(R.color.gray_888888));
        vh.clickBtn.setImageResource(R.drawable.back_right_white);
        vh.notifiedMember.setVisibility(View.GONE);
        vh.notifyMember.setVisibility(View.GONE);
        break;
      case "1"://审核通过
        vh.trainStatus.setText(context.getResources().getString(R.string.train_data_pass_apply));
        vh.trainStatus.setTextColor(context.getResources().getColor(R.color.cEA5412));
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
      case "2"://审核未通过
        vh.trainStatus.setText(
            context.getResources().getString(R.string.train_data_no_pass_apply1) + datas
                .get(position).getReason() + context.getResources()
                .getString(R.string.train_data_no_pass_apply2));
        vh.trainStatus.setTextColor(context.getResources().getColor(R.color.gray_888888));
        vh.clickBtn.setImageResource(R.drawable.back_right_white);
        vh.notifiedMember.setVisibility(View.GONE);
        vh.notifyMember.setVisibility(View.GONE);
        break;
      default:
        vh.trainStatus
            .setText(context.getText(R.string.unknown_status) + datas.get(position).getStatus());
        vh.trainStatus.setTextColor(context.getResources().getColor(R.color.cEA5412));
        vh.clickBtn.setImageResource(R.drawable.back_right_white);
        vh.notifiedMember.setVisibility(View.GONE);
        vh.notifyMember.setVisibility(View.GONE);
        break;
    }

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

  //============下=================
  public interface OnItemDeleteLister {

    void onDeleteClick(int position);
  }

  private OnItemDeleteLister onItemDeleteLister;

  public void setOnItemDeleteClickListener(OnItemDeleteLister onItemDeleteLister) {
    this.onItemDeleteLister = onItemDeleteLister;
  }

  //============上=================
  class ViewHolder {

    TextView trainTheme;
    TextView applyTime;
    TextView trainStatus;
    ImageView clickBtn;
    TextView notifyMember;
    TextView notifiedMember;
  }
}

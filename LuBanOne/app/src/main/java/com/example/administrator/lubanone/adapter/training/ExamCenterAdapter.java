package com.example.administrator.lubanone.adapter.training;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.model.TrainTestCenterModel;
import java.util.List;

/**
 * Created by Administrator on 2017\7\13 0013.
 */

public class ExamCenterAdapter extends BaseAdapter {

  Context context;
  List<TrainTestCenterModel> datas;

  public ExamCenterAdapter(Context context, List<TrainTestCenterModel> datas) {
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
      convertView = LayoutInflater.from(context).inflate(R.layout.exam_center_item, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.examCenterItemImage = (ImageView) convertView
          .findViewById(R.id.exam_center_item_image);
      viewHolder.examCenterItemTitle = (TextView) convertView
          .findViewById(R.id.exam_center_item_title);
      viewHolder.examCenterItemIntegration = (TextView) convertView
          .findViewById(R.id.exam_center_item_integration);
      viewHolder.examCenterItemState = (TextView) convertView
          .findViewById(R.id.exam_center_item_state);
      viewHolder.mScore = (TextView) convertView.findViewById(R.id.item_exam_center_score);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    viewHolder.examCenterItemTitle.setText(datas.get(position).getTheme());
    viewHolder.examCenterItemIntegration
        .setText(context.getResources().getString(R.string.exam_integration)
            + datas.get(position).getCode());
    viewHolder.mScore.setText(datas.get(position).getCode() + context.getText(R.string.score));
    switch (datas.get(position).getStatus()) {
      case "0":
        viewHolder.examCenterItemState
            .setText(context.getResources().getString(R.string.train_test_no_pass));
        viewHolder.examCenterItemState
            .setTextColor(context.getResources().getColor(R.color.gray_888888));
        break;
      case "1":
        viewHolder.examCenterItemState
            .setText(context.getResources().getString(R.string.train_test_pass));
        viewHolder.examCenterItemState
            .setTextColor(context.getResources().getColor(R.color.cEA5412));
        break;
      default:
        break;
    }

    Glide
        .with(context)
        .load(datas.get(position).getImage())
        .placeholder(R.drawable.loading_fail)
        .into(viewHolder.examCenterItemImage);

    return convertView;
  }

  class ViewHolder {

    ImageView examCenterItemImage;
    TextView examCenterItemTitle;
    TextView examCenterItemIntegration;
    TextView examCenterItemState;
    TextView mScore;
  }


}

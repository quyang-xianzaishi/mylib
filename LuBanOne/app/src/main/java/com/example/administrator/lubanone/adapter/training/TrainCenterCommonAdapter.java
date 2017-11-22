package com.example.administrator.lubanone.adapter.training;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.model.TrainCenterCommonModel;
import com.example.administrator.lubanone.utils.GlideRoundTransform;
import com.example.administrator.lubanone.utils.HouLog;
import java.util.List;

/**
 * Created by hou on 2017/8/3.
 */

public class TrainCenterCommonAdapter extends BaseAdapter {

  private List<TrainCenterCommonModel> datas;
  private Context mContext;
  private boolean needTopFire = false;

  public TrainCenterCommonAdapter(
      List<TrainCenterCommonModel> datas, Context mContext, boolean needTopFire) {
    this.datas = datas;
    this.mContext = mContext;
    this.needTopFire = needTopFire;
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
      convertView = LayoutInflater.from(mContext).inflate(R.layout.item_train_center_common, null);
      vh = new ViewHolder();
      vh.imageView = (ImageView) convertView.findViewById(R.id.item_train_center_common_image);
      vh.theme = (TextView) convertView.findViewById(R.id.item_train_center_common_theme);
      vh.status = (TextView) convertView.findViewById(R.id.item_train_center_common_status);
      vh.status2 = (TextView) convertView.findViewById(R.id.item_train_center_common_status2);
      vh.topImage = (ImageView) convertView.findViewById(R.id.item_train_center_common_top);
      vh.fireImage = (ImageView) convertView.findViewById(R.id.item_train_center_common_fire);
      vh.hotImage = (ImageView) convertView.findViewById(R.id.item_train_center_common_hot);
      vh.notice = (TextView) convertView.findViewById(R.id.item_train_center_common_content);
      vh.score = (TextView) convertView.findViewById(R.id.item_train_center_common_score);
      convertView.setTag(vh);
    } else {
      vh = (ViewHolder) convertView.getTag();
    }

//// 把图片生成的ID加入img标签中 <img src='123'>
//    String htmlFor02 =  "<img src='" + R.drawable.ico_hot + "'>" + datas.get(position)
//        .getTheme() +"<img src='"+ R.drawable.ico_fir + "'>";
//    vh.theme.setText(Html.fromHtml(htmlFor02, new Html.ImageGetter() {
//      @Override
//      public Drawable getDrawable(String source) {
//        HouLog.d("项目图片测试_source:" + source);
//        int id = Integer.parseInt(source);
//        Drawable drawable = mContext.getResources().getDrawable(id);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth() ,
//            drawable.getIntrinsicHeight());
//        return drawable;
//      }
//    }, null));

    vh.score.setText(datas.get(position).getCode() + mContext.getText(R.string.score));

    vh.notice.setText(datas.get(position).getNotice());

    if (needTopFire) {
      switch (datas.get(position).getFire()) {
        case "0":
          vh.topImage.setVisibility(View.GONE);
          vh.hotImage.setVisibility(View.GONE);
          vh.fireImage.setVisibility(View.GONE);
          vh.theme.setText(datas.get(position)
              .getTheme());
          break;
        case "1"://置顶
          vh.topImage.setVisibility(View.VISIBLE);
          vh.hotImage.setVisibility(View.GONE);
          vh.fireImage.setVisibility(View.GONE);
          vh.theme.setText("            "+datas.get(position)
              .getTheme());
          break;
        case "2"://热门
          vh.topImage.setVisibility(View.GONE);
          vh.hotImage.setVisibility(View.VISIBLE);
          vh.fireImage.setVisibility(View.GONE);
          vh.theme.setText("       "+datas.get(position)
              .getTheme());
          break;
        default:
          vh.topImage.setVisibility(View.GONE);
          vh.hotImage.setVisibility(View.GONE);
          vh.fireImage.setVisibility(View.GONE);
          vh.theme.setText(datas.get(position)
              .getTheme());
          break;
      }
    } else {
      //不需要置顶，火，热，简介
      vh.topImage.setVisibility(View.GONE);
      vh.hotImage.setVisibility(View.GONE);
      vh.fireImage.setVisibility(View.GONE);
      vh.notice.setVisibility(View.GONE);
      vh.theme.setText(datas.get(position)
          .getTheme());
    }

    if (!TextUtils.isEmpty(datas.get(position).getStatus())) {
      switch (datas.get(position).getStatus()) {
        case "0"://未完成培训
          vh.status2.setVisibility(View.GONE);
          vh.status.setVisibility(View.VISIBLE);
          vh.status.setText(mContext.getResources().getString(R.string.train_test_no_finish));
          break;
        case "1"://已完成，并获得培训积分
          vh.status.setVisibility(View.GONE);
          vh.status2.setVisibility(View.VISIBLE);
          vh.status2.setText(mContext.getResources().getString(R.string.train_test_finish_score));
          break;
        case "2"://已完成，未获得培训积分
          vh.status.setVisibility(View.GONE);
          vh.status2.setVisibility(View.VISIBLE);
          vh.status2.setText(
              mContext.getResources().getString(R.string.train_test_finish_score) + mContext
                  .getResources().getString(R.string.train_test_finish_no_score));
          break;
        default:
          break;
      }
    } else {
      HouLog.d("状态为空");

    }
    Glide
        .with(mContext)
        .load(datas.get(position).getImage())
        .placeholder(R.drawable.example)
        .transform(new GlideRoundTransform(mContext, 4))
        .into(vh.imageView);

    return convertView;
  }

  class ViewHolder {

    ImageView imageView;
    TextView theme;
    TextView status;
    TextView status2;
    ImageView topImage;
    ImageView hotImage;
    ImageView fireImage;
    TextView notice;
    TextView score;
  }

}

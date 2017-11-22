package com.example.administrator.lubanone.adapter.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.message.TrainMessageActivity;
import com.example.administrator.lubanone.activity.train.UploadTrainActivity;
import com.example.administrator.lubanone.activity.training.activity.ApplyTrainingActivity;
import com.example.administrator.lubanone.activity.training.activity.MyTeamActivity;
import com.example.administrator.lubanone.activity.training.activity.TrainDetailsActivity;
import com.example.administrator.lubanone.activity.training.activity.TrainUploadActivity;
import com.example.administrator.lubanone.bean.message.TrainMessageBean;
import com.example.administrator.lubanone.bean.message.TrainingNoticeBean;
import com.example.administrator.lubanone.bean.model.TrainVipGrade;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.qlibrary.utils.SPUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017\7\6 0006.
 */

public class TrainMessageAdapter extends BaseAdapter{

  Context context;
  List<TrainMessageBean> datas;

  public TrainMessageAdapter(Context context, List<TrainMessageBean> datas) {
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
      convertView = LayoutInflater.from(context).inflate(R.layout.training_message_item, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.trainMessageItemTitle = (TextView) convertView.findViewById(
          R.id.train_message_item_title);
      viewHolder.trainMessageItemTime = (TextView) convertView.findViewById(
          R.id.train_message_item_time);
      viewHolder.trainMessageItemContent = (TextView) convertView.findViewById(
          R.id.train_message_item_content);
      viewHolder.trainMessageItemTrainTheme = (TextView) convertView.findViewById(
          R.id.train_message_item_traintheme);
      viewHolder.trainMessageItemUploadtime = (TextView) convertView.findViewById(
          R.id.train_message_item_uploadtime);
      viewHolder.trainMessageItemObtainintegration = (TextView) convertView.findViewById(
          R.id.train_message_item_obtainintegration);
      viewHolder.trainMessageItemClick = (TextView) convertView.findViewById(
          R.id.train_message_item_click);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    if(datas.get(position).getTitle()!=null&&datas.get(position).getTitle().length()>0){
      viewHolder.trainMessageItemTitle.setText(datas.get(position).getTitle());
    }
    if(datas.get(position).getTime()!=null&&datas.get(position).getTime().length()>0){
      viewHolder.trainMessageItemTime.setText(datas.get(position).getTime());
    }
   /* if(datas.get(position).getContent()!=null&&datas.get(position).getContent().length()>0){
      viewHolder.trainMessageItemContent.setText(datas.get(position).getContent());
    }*/
    String str1 ="";
    String str2 ="";
    if (datas.get(position).getContent() != null && datas.get(position).getContent().length() > 6) {
      str1 = datas.get(position).getContent().toString().substring(0, 6);
      str2 = datas.get(position).getContent().toString()
          .substring(6, datas.get(position).getContent().length() - 1);
      viewHolder.trainMessageItemContent.setText(str1+"\n"+
          str2);
    }else {
      viewHolder.trainMessageItemContent.setText(datas.get(position).getContent());
    }
    if(datas.get(position).getTraintheme()!=null&&datas.get(position).getTraintheme().length()>0){
      viewHolder.trainMessageItemTrainTheme.setVisibility(View.VISIBLE);
      viewHolder.trainMessageItemTrainTheme.setText(datas.get(position).getTraintheme());
    }
    if(datas.get(position).getUploadtime()!=null&&datas.get(position).getUploadtime().length()>0){
      viewHolder.trainMessageItemUploadtime.setVisibility(View.VISIBLE);
      viewHolder.trainMessageItemUploadtime.setText(
          datas.get(position).getUploadtime());
    }
    if(datas.get(position).getObtainintegration()!=null&&datas.get(position).getObtainintegration().length()>0){
      viewHolder.trainMessageItemObtainintegration.setVisibility(View.VISIBLE);
      viewHolder.trainMessageItemObtainintegration.setText(
          datas.get(position).getObtainintegration());
    }
    final String str = datas.get(position).getType();
    viewHolder.trainMessageItemClick.setText(getClickStr(str));
    if(str!=null&&str.length()>0){
      if(str.equals("3")){
        viewHolder.trainMessageItemClick.setVisibility(View.GONE);
      }else {
        viewHolder.trainMessageItemClick.setVisibility(View.VISIBLE);
        viewHolder.trainMessageItemClick.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            farwardActivity(str,position);
          }
        });
      }
    }else {
      viewHolder.trainMessageItemClick.setVisibility(View.GONE);
    }

    return convertView;

  }

  class ViewHolder {
    TextView trainMessageItemTitle;
    TextView trainMessageItemTime;
    TextView trainMessageItemContent;
    TextView trainMessageItemTrainTheme;
    TextView trainMessageItemUploadtime;
    TextView trainMessageItemObtainintegration;
    TextView trainMessageItemClick;
  }

  private void farwardActivity(String type,int position){
    /*if(type.equals("1")){
      //开播提醒，进入直播页面
      Toast.makeText(context,"进入培训直播页面",Toast.LENGTH_LONG).show();
    }
    if(type.equals("2")){
      //培训邀请，查看预告
      Toast.makeText(context,"查看直播预告",Toast.LENGTH_LONG).show();
    }
    if(type.equals("4")){
      //审核通过，通知伞下会员
      Intent intent = new Intent();
      intent.setClass(context, MyTeamActivity.class);
      context.startActivity(intent);
    }
    if(type.equals("5")){
      //申请被拒，申请新的培训
      Intent intent = new Intent();
      intent.setClass(context, ApplyTrainingActivity.class);
      context.startActivity(intent);
    }*/
    if(type.equals("0")){
      //审核不通过
      /*Intent intent = new Intent();
      //intent.setClass(context, TrainUploadActivity.class);
      intent.setClass(context, UploadTrainActivity.class);
      context.startActivity(intent);*/
      getVipGrade();
    }
    if(type.equals("1")){
      //审核通过
      Intent intent = new Intent();
      intent.putExtra("trainId",datas.get(position).getTrainid());
      intent.putExtra("theme",datas.get(position).getTraintheme());
      intent.putExtra("imageUrl","");
      intent.setClass(context, TrainDetailsActivity.class);
      context.startActivity(intent);
    }

  }

  private String getClickStr(String type){
    String clickStr = null;
    if(type!=null&&type.length()>0){
      int clicktype = Integer.parseInt(type);
      switch (clicktype){
        case 0:
          //审核不通过
          clickStr = context.getString(R.string.re_upload_train_information);
          break;
        case 1:
          //审核通过
          clickStr = context.getString(R.string.train_information_detail);
          break;
        default:
          break;
      }
    }else {
      clickStr = "";
    }
    return clickStr;
  }

  /**
   * 获取会员等级
   */
  private void getVipGrade() {
    Subscriber subscriber = new MySubscriber<TrainVipGrade>((Activity) context) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
      }

      @Override
      public void onNext(TrainVipGrade trainVipGrade) {
        if (trainVipGrade != null) {
          if (Integer.parseInt(trainVipGrade.getLevel()) < 4) {
            HouToast.showLongToast(context, context.getString(R.string.vip_grade_tips));
          } else {
            Intent intent = new Intent(context, UploadTrainActivity.class);
            context.startActivity(intent);
          }
        }
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils
        .getStringValue(context, Config.USER_INFO, Config.TOKEN, ""));
    MyApplication.rxNetUtils.getTrainService()
        .getVipGrade(params)
        .map(new BaseModelFunc<TrainVipGrade>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

}

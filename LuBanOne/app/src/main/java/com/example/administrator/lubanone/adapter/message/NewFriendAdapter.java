package com.example.administrator.lubanone.adapter.message;

import static com.umeng.facebook.FacebookSdk.getApplicationContext;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.bean.message.NewFriendBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.GlideRoundTransform;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.example.qlibrary.utils.WindoswUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\7\31 0031.
 */

public class NewFriendAdapter extends BaseAdapter {

  Context context;
  List<NewFriendBean> datas;
  MyApplication mMyApplication;

  public NewFriendAdapter(Context context, List<NewFriendBean> datas) {
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

  @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.new_friend_item, null);
      viewHolder = new ViewHolder();
      //实例化
      viewHolder.applyImg = (ImageView) convertView.findViewById(R.id.new_friend_item_apply_img);
      viewHolder.applyName = (TextView) convertView.findViewById(R.id.new_friend_item_apply_name);
      viewHolder.applyMess = (TextView) convertView.findViewById(R.id.new_friend_item_apply_mess);
      viewHolder.applyState = (TextView) convertView.findViewById(R.id.new_friend_item_state);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //设置资源
    Glide.with(context)
        .load(datas.get(position).getUserimg())
        .transform(new GlideRoundTransform(context, 4))
        .diskCacheStrategy(DiskCacheStrategy.ALL).
        into(viewHolder.applyImg);
    viewHolder.applyName.setText(datas.get(position).getUsername());
    viewHolder.applyMess.setText(datas.get(position).getApplymess());
    final String str = datas.get(position).getZt();
    if(str!=null&&str.length()>0){
      if(str.equals("0")){
        viewHolder.applyState.setText(context.getString(R.string.agree_friend_apply));
        viewHolder.applyState.setClickable(true);
        //viewHolder.applyState.setBackgroundColor(context.getResources().getColor(R.color.blue));
        viewHolder.applyState.setBackground(context.getResources().getDrawable(R.drawable.apply_friend_agree));
        viewHolder.applyState.setTextColor(context.getResources().getColor(R.color.white));
        viewHolder.applyState.setClickable(true);
      }else if(str.equals("1")){
        viewHolder.applyState.setText(context.getString(R.string.agreed_friend_apply));
        viewHolder.applyState.setBackgroundColor(context.getResources().getColor(R.color.white));
        viewHolder.applyState.setTextColor(context.getResources().getColor(R.color.c999));
        //viewHolder.applyState.setBackground(context.getResources().getDrawable(R.drawable.apply_friend_agreed));
        viewHolder.applyState.setClickable(false);
      }else {
        viewHolder.applyState.setText(context.getString(R.string.agree_friend_apply));
        //viewHolder.applyState.setBackgroundColor(context.getResources().getColor(R.color.blue));
        viewHolder.applyState.setBackground(context.getResources().getDrawable(R.drawable.apply_friend_agree));
        viewHolder.applyState.setTextColor(context.getResources().getColor(R.color.white));
        viewHolder.applyState.setClickable(true);
      }
    }else {
      viewHolder.applyState.setText(context.getString(R.string.agree_friend_apply));
      //viewHolder.applyState.setBackgroundColor(context.getResources().getColor(R.color.blue));
      viewHolder.applyState.setBackground(context.getResources().getDrawable(R.drawable.apply_friend_agree));
      viewHolder.applyState.setTextColor(context.getResources().getColor(R.color.white));
      viewHolder.applyState.setClickable(true);
    }
    viewHolder.applyState.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if(str!=null&&str.length()>0&&str.equals("0")){
          addFriend(datas.get(position).getUserid());
        }else {
          //Toast.makeText(context,"添加该好友到通讯录",Toast.LENGTH_LONG).show();
        }
      }
    });
    return convertView;
  }

  class ViewHolder {
    ImageView applyImg;
    TextView applyName;
    TextView applyMess;
    TextView applyState;

  }

  private RequestListener addFriendListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        commitResult(result);
      } catch (Exception e) {
        ToastUtil.showShort(context.getResources().getString(R.string.add_friend_fail),
            context.getApplicationContext());
      }

    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(context.getResources().getString(R.string.add_friend_fail),
          context.getApplicationContext());
    }
  };


  private void addFriend(String userId){
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(context.getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsUserId = new RequestParams("userid", userId);
    list.add(paramsToken);
    list.add(paramsUserId);
    RequestNet requestNet = new RequestNet(mMyApplication, (Activity) context, list,
        Urls.AGREE_APPLY_FRIEND, addFriendListener, RequestNet.POST);
  }

  private void commitResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showSuccessDialog();
    } else {
      Toast.makeText(context, DebugUtils.convert(ResultUtil.getErrorMsg(result),
          context.getString(R.string.add_friend_fail)),Toast.LENGTH_LONG).show();
    }
  }


  private void showSuccessDialog() {

    final Dialog dialog = new Dialog(context, R.style.MyDialog);
    View view = ((Activity) context).getLayoutInflater().inflate(R.layout.dialog_layout, null);
    TextView tv = (TextView) view.findViewById(R.id.tv);
    tv.setText(context.getResources().getString(R.string.add_friend_success));
    dialog.setContentView(view);
    dialog.getWindow()
        .setLayout(WindoswUtil.getWindowWidth((Activity) context) - 200, WindowManager.LayoutParams.WRAP_CONTENT);
    dialog.show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
        ((Activity) context).finish();
      }
    }, 2000);

  }


}

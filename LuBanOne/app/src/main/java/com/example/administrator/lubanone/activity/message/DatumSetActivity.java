package com.example.administrator.lubanone.activity.message;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.bean.message.DatumSetResultBean;
import com.example.administrator.lubanone.customview.ToggleButtonView.ToggleButton;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.example.qlibrary.utils.WindoswUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation.ConversationType;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\7\3 0003.
 */

public class DatumSetActivity extends AppCompatActivity implements View.OnClickListener,ToggleButton.OnToggleChanged{

    private TextView back;
    private LinearLayout rewardSet;
    private LinearLayout recommendFriend;
    private ToggleButton setIdentificationFriend;
    private ToggleButton shieldMyMoments;
    private ToggleButton shieldHisMoments;
    private ToggleButton joinBlacklist;
    private LinearLayout complain;
    private TextView deteleFriend;
    private String userId;
    private boolean joinBlack = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_datum_set);

        if(getIntent()!=null&&getIntent().hasExtra("userId")){
            userId = getIntent().getStringExtra("userId");
        }

        initView();
        getSetInfo();
    }

    private void initView(){

        back = (TextView) this.findViewById(R.id.back);
        rewardSet = (LinearLayout) this.findViewById(R.id.reward_set);
        recommendFriend = (LinearLayout) this.findViewById(R.id.recommend_friend);
        setIdentificationFriend = (ToggleButton) this.findViewById(R.id.set_identification_friend);
        shieldMyMoments = (ToggleButton) this.findViewById(R.id.shield_my_moments);
        shieldHisMoments = (ToggleButton) this.findViewById(R.id.shield_his_moments);
        joinBlacklist = (ToggleButton) this.findViewById(R.id.join_blacklist);
        complain = (LinearLayout) this.findViewById(R.id.complain);
        deteleFriend = (TextView) this.findViewById(R.id.detele_friend);
        setIdentificationFriend.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    setStar("1");
                }else {
                    setStar("0");
                }
            }
        });
        shieldMyMoments.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                }else {
                }
            }
        });
        shieldHisMoments.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                }else {
                }
            }
        });
        joinBlacklist.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    joinBlackList("1");
                    joinBlack = true;
                }else {
                    joinBlackList("0");
                    joinBlack = false;
                }
            }
        });

        back.setOnClickListener(this);
        rewardSet.setOnClickListener(this);
        recommendFriend.setOnClickListener(this);
        complain.setOnClickListener(this);
        deteleFriend.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                DatumSetActivity.this.finish();
                break;
            case R.id.reward_set:
                //设置备注
                break;
            case R.id.recommend_friend:
                //推荐好友
                Intent intent1 = new Intent();
                if(userId!=null&&userId.length()>0){
                    intent1.putExtra("targetId",userId);
                }
                List<String> memberList = new ArrayList<>();
                memberList.add(userId);
                intent1.putStringArrayListExtra("memberList", (ArrayList<String>) memberList);
                intent1.putExtra("type","recommend");
                intent1.setClass(DatumSetActivity.this,ChooseFriendActivity.class);
                DatumSetActivity.this.startActivity(intent1);
                break;
            case R.id.complain:
                //投诉
                Intent intent = new Intent();
                if(userId!=null&&userId.length()>0){
                    intent.putExtra("targetId",userId);
                }
                intent.putExtra("type","friend");
                intent.setClass(DatumSetActivity.this,ComplainActivity.class);
                DatumSetActivity.this.startActivity(intent);
                break;
            case R.id.detele_friend:
                //删除好友
                deleteFriend();
                break;
            default:
                break;

        }

    }

    @Override
    public void onToggle(boolean on) {

    }

    private RequestListener deleteFriendListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
                commitResult(result);
            } catch (Exception e) {
                ToastUtil.showShort(getResources().getString(R.string.delete_friend_fail),
                    getApplicationContext());
            }
        }
        @Override
        public void onFail(String errorMsf) {
            ToastUtil.showShort(getResources().getString(R.string.delete_friend_fail),
                getApplicationContext());
        }
    };

    private void deleteFriend(){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsUserId = new RequestParams("userid", userId);
        list.add(paramsToken);
        list.add(paramsUserId);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), DatumSetActivity.this, list,
            Urls.DELETE_FRIEND, deleteFriendListener, RequestNet.POST);

    }

    private void commitResult(Result<Object> result) {
        if (ResultUtil.isSuccess(result)) {
            if(RongIM.getInstance()!=null){
                RongIM.getInstance().removeConversation(ConversationType.PRIVATE, userId,
                    new ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            //清除会话列表成功
                        }
                        @Override
                        public void onError(ErrorCode errorCode) {
                            //清除会话列表失败
                        }
                    });
            }
            showSuccessDialog();
        } else {
            Toast.makeText(this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                getString(R.string.delete_friend_fail)),Toast.LENGTH_LONG).show();
        }
    }


    private void showSuccessDialog() {

        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText(getResources().getString(R.string.delete_friend_success));
        dialog.setContentView(view);
        dialog.getWindow()
            .setLayout(WindoswUtil.getWindowWidth(this) - 200, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                finish();
            }
        }, 2000);

    }

    private RequestListener getSetInfoListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<DatumSetResultBean> result = GsonUtil.processJson(jsonObject, DatumSetResultBean.class);
                if(result!=null){
                    if(result.getResult().getBlack()!=null&&result.getResult().getBlack().equals("1")){
                        //已经加入黑名单
                        joinBlacklist.setToggleOn();
                    }else {
                        //未加入黑名单
                        joinBlacklist.setToggleOff();
                    }
                    if(result.getResult().getStar()!=null&&result.getResult().getStar().equals("1")){
                        //已经设为星标
                        setIdentificationFriend.setToggleOn();
                    }else {
                        //未设置星标
                        setIdentificationFriend.setToggleOff();
                    }
                }
            } catch (Exception e) {
                ToastUtil.showShort(getResources().getString(R.string.get_set_info_fail),
                    DatumSetActivity.this);
            }
        }
        @Override
        public void onFail(String errorMsf) {
            ToastUtil.showShort(getResources().getString(R.string.get_set_info_fail),
                DatumSetActivity.this);
        }
    };

    private void getSetInfo(){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsUserId = new RequestParams("userid", userId);
        list.add(paramsToken);
        list.add(paramsUserId);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), DatumSetActivity.this, list,
            Urls.GET_SET_INFO, getSetInfoListener, RequestNet.POST);
    }


    private RequestListener setStarInfoListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
                if (ResultUtil.isSuccess(result)) {
                    ToastUtil.showShort(getResources().getString(R.string.set_star_success),
                        getApplicationContext());
                } else {
                    Toast.makeText(DatumSetActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                        getString(R.string.set_star_fail)),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                ToastUtil.showShort(getResources().getString(R.string.set_star_fail),
                    getApplicationContext());
            }
        }
        @Override
        public void onFail(String errorMsf) {
            Toast.makeText(DatumSetActivity.this, DebugUtils.convert(errorMsf,
                getString(R.string.set_star_fail)),Toast.LENGTH_LONG).show();
        }
    };
    private void setStar(String starstate){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsUserId = new RequestParams("userid", userId);
        RequestParams paramsStar = new RequestParams("starstate", starstate);
        list.add(paramsToken);
        list.add(paramsUserId);
        list.add(paramsStar);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), DatumSetActivity.this, list,
            Urls.SET_STAR_STATE, setStarInfoListener, RequestNet.POST);

    }

    private RequestListener joinBlackListListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
                if (ResultUtil.isSuccess(result)) {
                    if(joinBlack){
                        //加入黑名单成功
                        ToastUtil.showShort(getResources().getString(R.string.join_black_list_success),
                            getApplicationContext());
                    }else {
                        ToastUtil.showShort(getResources().getString(R.string.remove_black_list_success),
                            getApplicationContext());
                    }
                } else {
                    if(joinBlack){
                        //加入黑名单失败
                        Toast.makeText(DatumSetActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                            getString(R.string.join_black_list_fail)),Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(DatumSetActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                            getString(R.string.remove_black_list_fail)),Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                if(joinBlack){
                    //加入黑名单失败
                    Toast.makeText(DatumSetActivity.this, getString(R.string.join_black_list_fail)
                        ,Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(DatumSetActivity.this, getString(R.string.remove_black_list_fail)
                        ,Toast.LENGTH_LONG).show();
                }
            }
        }
        @Override
        public void onFail(String errorMsf) {
            Toast.makeText(DatumSetActivity.this, DebugUtils.convert(errorMsf,
                getString(R.string.set_star_fail)),Toast.LENGTH_LONG).show();
        }
    };
    private void joinBlackList(String blackstate){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsUserId = new RequestParams("userid", userId);
        RequestParams paramsBlack = new RequestParams("blackstate", blackstate);
        list.add(paramsToken);
        list.add(paramsUserId);
        list.add(paramsBlack);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), DatumSetActivity.this, list,
            Urls.JOIN_BLACK_LIST, joinBlackListListener, RequestNet.POST);
    }
}

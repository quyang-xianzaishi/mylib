package com.example.administrator.lubanone.activity.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.bean.message.GetGroupCountBean;
import com.example.administrator.lubanone.bean.message.GroupInfoBean;
import com.example.administrator.lubanone.bean.message.UserInfoBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\6\30 0030.
 */

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back;
    private TextView groupInfo;
    private TextView titleTx;
    private String targetId;
    private String titleStr;
    private String groupCount;
    private ConversationType conversationType;
    public static ConversationActivity instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_conversation);
        instance = this;

        initViews();
        setRongProvider();
    }

    private void initViews(){

        back = (TextView)this.findViewById(R.id.back);
        titleTx = (TextView)this.findViewById(R.id.activity_title);
        groupInfo = (TextView) this.findViewById(R.id.group_info);

        back.setOnClickListener(this);
        groupInfo.setOnClickListener(this);
        Uri uri= this.getIntent().getData();
        targetId = uri.getQueryParameter("targetId");
        titleStr = uri.getQueryParameter("title");
        conversationType = ConversationType.valueOf(this.getIntent().getData()
            .getLastPathSegment().toUpperCase(Locale.US));
        if(titleStr!=null){
            if(conversationType!=null){
                if(conversationType.equals(Conversation.ConversationType.GROUP)){
                    //群聊
                    titleTx.setText(titleStr);
                    groupInfo.setVisibility(View.VISIBLE);
                    //getGroupCount();
                }else if(conversationType.equals(ConversationType.PRIVATE)){
                    //单聊
                    titleTx.setText(titleStr);
                    groupInfo.setVisibility(View.INVISIBLE);
                }else if(conversationType.equals(ConversationType.CUSTOMER_SERVICE)){
                    //客服
                    titleTx.setText("客服");
                    groupInfo.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                ConversationActivity.this.finish();
                break;
            case R.id.group_info:
                Intent intent = new Intent();
                intent.putExtra("targetId",targetId);
                intent.setClass(this,ConversationInfoActivity.class);
                this.startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //getGroupCount();
        if(conversationType!=null) {
            if (conversationType.equals(Conversation.ConversationType.GROUP)) {
                //群聊
                getGroupInfo();
            }
        }
    }

    private RequestListener getUserInfoListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }

        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<UserInfoBean> result = GsonUtil.processJson(jsonObject, UserInfoBean.class);
                if(result!=null){
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(result.getResult().getUserid(),
                        result.getResult().getUsername(), Uri.parse(result.getResult().getUserimg())));
                }
            } catch (Exception e) {
                ToastUtil.showShort(getResources().getString(R.string.get_user_info_fail),
                    getApplicationContext());
            }

        }

        @Override
        public void onFail(String errorMsf) {
            ToastUtil.showShort(getResources().getString(R.string.get_user_info_fail),
                getApplicationContext());
        }
    };

    private void setRongProvider(){
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userId) {
                UserInfo userInfo = null;
                List<RequestParams> list = new ArrayList<>();
                RequestParams paramsToken = new RequestParams(Config.TOKEN,
                    SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
                RequestParams paramsUserId = new RequestParams("userid", userId);
                list.add(paramsToken);
                list.add(paramsUserId);
                RequestNet requestNet = new RequestNet((MyApplication) getApplication(),
                    ConversationActivity.this, list, Urls.GET_USER_INFO,getUserInfoListener, RequestNet.POST);
                return null;
            }
        }, true);
    }

    private RequestListener getGroupCountListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<GetGroupCountBean> result = GsonUtil.processJson(jsonObject, GetGroupCountBean.class);
                    if (ResultUtil.isSuccess(result)&&result!=null) {
                        if(result.getResult()!=null&&result.getResult().getState()!=null
                            &&result.getResult().getState().length()>0){
                            if(result.getResult().getState().equals("0")){
                                //不在该群
                                groupInfo.setVisibility(View.INVISIBLE);
                            }else if(result.getResult().getState().equals("1")){
                                groupInfo.setVisibility(View.VISIBLE);
                                if(result.getResult()!=null&&
                                    result.getResult().getGroupmembercount()!=null
                                    &&result.getResult().getGroupmembercount().length()>0){
                                    //群组聊天显示群组人数
                                    titleTx.setText(titleStr+"("+result.getResult().getGroupmembercount()+")");
                                }
                            }else {
                                groupInfo.setVisibility(View.INVISIBLE);
                            }
                        }
                    } else {
                        Toast.makeText(ConversationActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                            getString(R.string.get_group_count_fail)),Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                Toast.makeText(ConversationActivity.this, getString(R.string.get_group_count_fail)
                    , Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onFail(String errorMsf) {
            ToastUtil.showShort(getResources().getString(R.string.get_group_count_fail),
                getApplicationContext());
        }
    };

    private void getGroupCount(){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsGroupId = new RequestParams("groupid", targetId);
        list.add(paramsToken);
        list.add(paramsGroupId);
        RequestNet requestNet = new RequestNet((MyApplication) getApplication(),
            ConversationActivity.this, list, Urls.GET_GROUP_COUNT,getGroupCountListener, RequestNet.POST);
    }

    private RequestListener getGroupInfoListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }

        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<GroupInfoBean> result = GsonUtil.processJson(jsonObject, GroupInfoBean.class);
                if (ResultUtil.isSuccess(result)) {
                    if(result!=null){
                        RongIM.getInstance().refreshGroupInfoCache(new Group(result.getResult().getGroupid(),
                            result.getResult().getGroupname(), Uri.parse(result.getResult().getGroupimg())));
                        titleStr = result.getResult().getGroupname();
                        getGroupCount();
                    }
                } else {
                    getGroupCount();
                    Toast.makeText(ConversationActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                        getString(R.string.get_group_info_fail)), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                ToastUtil.showShort(ConversationActivity.this.getResources().getString(R.string.get_group_info_fail),
                    ConversationActivity.this.getApplicationContext());
            }
        }

        @Override
        public void onFail(String errorMsf) {
            ToastUtil.showShort(ConversationActivity.this.getResources().getString(R.string.get_user_info_fail),
                ConversationActivity.this.getApplicationContext());
        }
    };

    private void getGroupInfo(){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(this.getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsGroupId = new RequestParams("groupid",targetId);
        list.add(paramsToken);
        list.add(paramsGroupId);
        RequestNet requestNet = new RequestNet((MyApplication)this.getApplication(), this, list,
            Urls.GET_GROUP_INFO,getGroupInfoListener, RequestNet.POST);

    }


}

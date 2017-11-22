package com.example.administrator.lubanone.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.bean.message.MemberInfoBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.manager.JyActivityManager;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.GlideRoundTransform;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import io.rong.imkit.RongIM;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\7\3 0003.
 */

public class MemberInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView back;
    private TextView datumSet;
    private TextView title;
    private ImageView memberImg;
    private TextView memberRemark;
    private TextView memberName;
    private TextView memberId;
    private TextView memberGrade;
    private TextView personalitySignature;
    private TextView remarkSet;
    private TextView addFriend;
    //private TextView sendSysMessage;
    private MemberInfoBean memberInfoBean;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_member_info);
        if(getIntent()!=null&&getIntent().hasExtra("userId")){
            userId = getIntent().getStringExtra("userId");
        }
        initView();
    }


    public void initView() {

        back = (TextView) this.findViewById(R.id.back);
        datumSet = (TextView) this.findViewById(R.id.data_set);
        title = (TextView) this.findViewById(R.id.title);
        memberImg = (ImageView) this.findViewById(R.id.member_img);
        memberRemark = (TextView) this.findViewById(R.id.member_remark);
        memberName = (TextView) this.findViewById(R.id.member_name);
        memberId = (TextView) this.findViewById(R.id.member_id);
        memberGrade = (TextView) this.findViewById(R.id.member_grade);
        personalitySignature = (TextView) this.findViewById(R.id.member_personality_signature);
        remarkSet = (TextView) this.findViewById(R.id.remark_set);
        addFriend = (TextView) this.findViewById(R.id.add_friend);
        //sendSysMessage = (TextView) this.findViewById(R.id.send_system_message);

        initData();
        back.setOnClickListener(this);
        remarkSet.setOnClickListener(this);
        addFriend.setOnClickListener(this);
        datumSet.setOnClickListener(this);
        //sendSysMessage.setOnClickListener(this);

    }

    private void initData(){
        getMemberInfo(userId);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                MemberInfoActivity.this.finish();
                break;
            case R.id.remark_set:
                break;
            case R.id.add_friend:
                if(memberInfoBean.getZt()!=null&&memberInfoBean.getZt().length()>0){
                    if(memberInfoBean.getZt().equals("0")){
                        Intent intent = new Intent();
                        intent.putExtra("applyId",memberInfoBean.getUserid());
                        intent.setClass(MemberInfoActivity.this,FriendTestActivity.class);
                        MemberInfoActivity.this.startActivity(intent);
                        MemberInfoActivity.this.finish();
                    }else if(memberInfoBean.getZt().equals("1")){
                        //好友关系
                        if(memberInfoBean.getUserid()!=null&&memberInfoBean.getUserid().length()>0){
                            JyActivityManager.getInstance().finishNameActivity(ConversationActivity.class);
                            RongIM.getInstance().startPrivateChat(this,memberInfoBean.getUserid(),
                                memberInfoBean.getUsername());
                        }
                        MemberInfoActivity.this.finish();
                    }
                }

                break;
            case  R.id.data_set:
                Intent intent = new Intent();
                intent.putExtra("userId",userId);
                intent.setClass(MemberInfoActivity.this,DatumSetActivity.class);
                MemberInfoActivity.this.startActivity(intent);
                this.finish();
                break;
            /*case R.id.send_system_message:
                Intent intent1 = new Intent();
                intent1.setClass(MemberInfoActivity.this,SystemMessageActivity.class);
                MemberInfoActivity.this.startActivity(intent1);
                break;*/
            default:
                break;
        }
    }

    private RequestListener getMemberInfoListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }

        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<MemberInfoBean> result = GsonUtil.processJson(jsonObject, MemberInfoBean.class);
                if(result!=null) {
                    if (result.getResult() != null) {
                        memberInfoBean = new MemberInfoBean(result.getResult().getUserid(),
                            result.getResult().getUserimg(),
                            result.getResult().getUsername(),
                            result.getResult().getAccounts(),
                            result.getResult().getLevelname(),
                            result.getResult().getAutograph(),
                            result.getResult().getZt());
                        Glide.with(MemberInfoActivity.this)
                            .load(memberInfoBean.getUserimg())
                            .transform(new GlideRoundTransform(MemberInfoActivity.this, 4))
                            .into(memberImg);
                        memberRemark.setText(memberInfoBean.getAccounts());
                        memberName.setText(memberInfoBean.getAccounts());
                        memberId.setText(memberInfoBean.getUserid());
                        memberGrade.setText(memberInfoBean.getLevelname());
                        personalitySignature.setText(memberInfoBean.getAutograph());
                        if(memberInfoBean.getZt()!=null&&memberInfoBean.getZt().length()>0){
                            if(memberInfoBean.getZt().equals("0")){
                                //非好友关系
                                addFriend.setText(getString(R.string.add_friend_btn));
                                datumSet.setVisibility(View.INVISIBLE);
                            }else if(memberInfoBean.getZt().equals("1")){
                                //好友关系
                                title.setText(memberInfoBean.getUsername());
                                addFriend.setText(getString(R.string.send_conversation_message));
                                datumSet.setVisibility(View.VISIBLE);
                            }
                        }
                    }else {
                        ToastUtil.showShort(getResources().getString(R.string.get_member_info_null),
                            getApplicationContext());
                    }
                }
            } catch (Exception e) {
                ToastUtil.showShort(getResources().getString(R.string.get_member_info_fail),
                    getApplicationContext());
            }

        }

        @Override
        public void onFail(String errorMsf) {
            ToastUtil.showShort(getResources().getString(R.string.get_member_info_fail),
                getApplicationContext());
        }
    };

    private void getMemberInfo(String userid){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsUserId = new RequestParams("userid", userid);
        list.add(paramsToken);
        list.add(paramsUserId);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), this, list,
            Urls.GET_MEMBER_INFO, getMemberInfoListener, RequestNet.POST);

    }


}

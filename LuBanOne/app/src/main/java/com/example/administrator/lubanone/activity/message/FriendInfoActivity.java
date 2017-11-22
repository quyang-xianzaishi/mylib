package com.example.administrator.lubanone.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;

import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.bean.message.FriendInfoBean;
import com.example.administrator.lubanone.bean.message.MemberInfoBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
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

public class FriendInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back;
    private TextView title;
    private TextView dataSet;
    private ImageView friendImg;
    private TextView friendRemark;
    private TextView friendName;
    private TextView friendGrade;
    private TextView friendPersonalitySignature;
    private LinearLayout friendRemarkSet;
    private LinearLayout personalAlbumLinear;
    private ImageView personalAlbum1;
    private ImageView personalAlbum2;
    private ImageView personalAlbum3;
    private ImageView personalAlbum4;
    private TextView sendMessage;
    private List<ImageView> imageViews;
    private MemberInfoBean memberInfoBean;

    private String targetId;//用户id
    private String userName;//用户昵称


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_friend_info);
        initView();
    }

    public void initView() {
        back = (TextView) this.findViewById(R.id.back);
        title = (TextView) this.findViewById(R.id.title);
        dataSet = (TextView) this.findViewById(R.id.data_set);
        friendImg = (ImageView) this.findViewById(R.id.member_img);
        friendRemark = (TextView) this.findViewById(R.id.member_remark);
        friendName = (TextView) this.findViewById(R.id.member_name);
        friendGrade = (TextView) this.findViewById(R.id.member_grade);
        friendPersonalitySignature = (TextView) this.findViewById(R.id.member_personality_signature);
        friendRemarkSet = (LinearLayout) this.findViewById(R.id.remark_set);
        personalAlbumLinear = (LinearLayout) this.findViewById(R.id.personal_album_linear);
        sendMessage = (TextView) this.findViewById(R.id.send_message);

        personalAlbum1 = (ImageView) this.findViewById(R.id.personal_album1);
        personalAlbum2 = (ImageView) this.findViewById(R.id.personal_album2);
        personalAlbum3 = (ImageView) this.findViewById(R.id.personal_album3);
        personalAlbum4 = (ImageView) this.findViewById(R.id.personal_album4);

        imageViews = new ArrayList<>();
        imageViews.add(personalAlbum1);
        imageViews.add(personalAlbum2);
        imageViews.add(personalAlbum3);
        imageViews.add(personalAlbum4);

        back.setOnClickListener(this);
        dataSet.setOnClickListener(this);
        friendRemarkSet.setOnClickListener(this);
        personalAlbumLinear.setOnClickListener(this);
        sendMessage.setOnClickListener(this);

        if(getIntent()!=null){
            if(getIntent().hasExtra("targetId")){
                targetId = getIntent().getStringExtra("targetId");
            }
            if(getIntent().hasExtra("userName")){
                userName = getIntent().getStringExtra("userName");
            }
        }

        //initData();
        getFriendInfo();
    }

    private void initData(){
        List<String> list2 = new ArrayList<>();
        list2.add("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg");
        list2.add("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg");
        list2.add("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg");
        FriendInfoBean friendInfoBean = new FriendInfoBean
            ("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg",
                "郑连","mumu1021","M2","别让人生，输给了心情。别让人生，输给了心情。别让人生，输给了心情。",list2);
        userName = friendInfoBean.getUserName();
        refreshUI(friendInfoBean);
    }

    private void refreshUI(FriendInfoBean friendInfoBean){
        Glide.with(this)
            .load(friendInfoBean.getUserImg())
            .into(friendImg);
        title.setText(friendInfoBean.getNickName());
        friendRemark.setText(friendInfoBean.getNickName());
        friendName.setText(friendInfoBean.getUserName());
        friendGrade.setText(friendInfoBean.getUserGrade());
        friendPersonalitySignature.setText(friendInfoBean.getSignature());
        if(friendInfoBean.getImgList()!=null&&friendInfoBean.getImgList().size()>0){
            for(int i = 0;i<friendInfoBean.getImgList().size();i++){
                Glide.with(this)
                    .load(friendInfoBean.getImgList().get(i))
                    .into(imageViews.get(i));
            }
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                FriendInfoActivity.this.finish();
                break;
            case R.id.data_set:
                //资料设置
                Intent intent = new Intent();
                intent.putExtra("userId",targetId);
                intent.setClass(FriendInfoActivity.this,DatumSetActivity.class);
                FriendInfoActivity.this.startActivity(intent);
                this.finish();
                break;
            case R.id.remark_set:
                //设置备注
                break;
            case R.id.personal_album_linear:
                //个人相册
                Intent intent1 = new Intent();
                intent1.setClass(FriendInfoActivity.this,AlbumActivity.class);
                FriendInfoActivity.this.startActivity(intent1);
                break;
            case R.id.send_message:
                //发送信息
                RongIM.getInstance().startPrivateChat(FriendInfoActivity.this,targetId,userName);
                FriendInfoActivity.this.finish();
                /*Intent intent = new Intent();
                intent.setClass(FriendInfoActivity.this,ConversationActivity.class);
                FriendInfoActivity.this.startActivity(intent);*/
                break;
            default:
                break;

        }
    }

    private RequestListener getFriendInfoListener = new RequestListener() {
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
                        Glide.with(FriendInfoActivity.this)
                            .load(memberInfoBean.getUserimg())
                            .transform(new GlideRoundTransform(FriendInfoActivity.this, 4))
                            .into(friendImg);
                        if(userName!=null&&userName.length()>0){
                            title.setText(userName);
                        }else {
                            title.setText(memberInfoBean.getUsername());
                        }
                        friendRemark.setText(memberInfoBean.getAccounts());
                        friendName.setText(memberInfoBean.getAccounts());
                        friendGrade.setText(memberInfoBean.getLevelname());
                        friendPersonalitySignature.setText(memberInfoBean.getAutograph());
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

    private void getFriendInfo(){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsUserId = new RequestParams("userid", targetId);
        list.add(paramsToken);
        list.add(paramsUserId);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), this, list,
            Urls.GET_MEMBER_INFO, getFriendInfoListener, RequestNet.POST);
    }


}

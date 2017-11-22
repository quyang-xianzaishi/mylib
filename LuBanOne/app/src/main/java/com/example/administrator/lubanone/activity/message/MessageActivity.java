package com.example.administrator.lubanone.activity.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.FragmentVpAdapter;
import com.example.administrator.lubanone.event.HomeEvent;
import com.example.administrator.lubanone.fragment.message.AddressFragment;
import com.example.administrator.lubanone.fragment.message.ConversationFragment;
import com.example.administrator.lubanone.fragment.message.MessageFragment;
import com.example.administrator.lubanone.fragment.message.MomentsFragment;
import com.example.administrator.lubanone.utils.DensityUtil;
import com.example.administrator.lubanone.widgets.AddFriendPopupWindow;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by zheng on 2017\6\28 0028.
 */

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    public static MessageActivity instance;
    private TextView getMessageActivityBackTitle;
    private TextView messageActivityBack;
    private TextView messageActivityAdd;
    private TextView messageActivityAddFriend;
    private ViewPager messageActivityPager;
    private ImageButton message;
    private ImageButton address;
    private ImageButton moments;
    private ImageButton selfInfo;
    private TextView messageTx;
    private TextView addressTx;
    private TextView momentsTx;
    public TextView systemUnread;
    public TextView friendUnread;

    private LinearLayout messageLinear;
    private LinearLayout conversationLinear;
    private LinearLayout addressLinear;


    private TextView messageBtn;
    private TextView conversationBtn;
    private LinearLayout messageConversationBtn;
    private TextView shoot;

    private AddFriendPopupWindow mAddFriendPopupWindow;

    private Boolean isMessage = true;
    private List fragments;
    private ConversationFragment conversationFragment;
    private MessageFragment messageFragment;
    private ConversationFragment mConversationFragment;
    private AddressFragment addressFragment;
    private MomentsFragment momentsFragment;
    public FragmentVpAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_message);
        initView();
    }

    public void initView() {

        messageLinear = (LinearLayout) findViewById(R.id.message_linear);
        conversationLinear = (LinearLayout) findViewById(R.id.conversation_linear);
        addressLinear = (LinearLayout) findViewById(R.id.address_linear);
        getMessageActivityBackTitle = (TextView) findViewById(R.id.activity_message_title);
        messageActivityBack = (TextView) findViewById(R.id.activity_message_back);
        messageActivityAdd = (TextView) findViewById(R.id.activity_message_add);
        messageActivityAddFriend = (TextView) findViewById(R.id.activity_message_add_friend);
        messageActivityPager = (ViewPager) findViewById(R.id.activity_message_viewpager);
        message = (ImageButton) findViewById(R.id.activity_message_msg);
        address = (ImageButton) findViewById(R.id.activity_message_address);
        moments = (ImageButton) findViewById(R.id.activity_message_moments);
        selfInfo = (ImageButton) findViewById(R.id.activity_message_selfinfo);
        messageTx = (TextView) findViewById(R.id.activity_message_text);
        addressTx = (TextView) findViewById(R.id.activity_address_text);
        momentsTx = (TextView) findViewById(R.id.activity_moments_text);
        systemUnread = (TextView) findViewById(R.id.system_unread);
        friendUnread = (TextView) findViewById(R.id.friend_unread);
        messageBtn = (TextView) findViewById(R.id.message_btn);
        conversationBtn = (TextView) findViewById(R.id.conversation_btn);
        messageConversationBtn = (LinearLayout) findViewById(R.id.message_conversation_btn);
        shoot = (TextView) findViewById(R.id.shoot_img);

        messageActivityBack.setOnClickListener(this);
        messageActivityAdd.setOnClickListener(this);
        messageActivityAddFriend.setOnClickListener(this);
        message.setOnClickListener(this);
        address.setOnClickListener(this);
        moments.setOnClickListener(this);
        selfInfo.setOnClickListener(this);
        messageBtn.setOnClickListener(this);
        conversationBtn.setOnClickListener(this);
        messageLinear.setOnClickListener(this);
        conversationLinear.setOnClickListener(this);
        addressLinear.setOnClickListener(this);

        messageFragment = new MessageFragment();
        mConversationFragment = new ConversationFragment();
        addressFragment = new AddressFragment();
        momentsFragment = new MomentsFragment();
        fragments = new ArrayList<>();
        fragments.add(messageFragment);
        fragments.add(mConversationFragment);
        fragments.add(addressFragment);
        //fragments.add(momentsFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new FragmentVpAdapter(fragmentManager, fragments);
        messageActivityPager.setAdapter(adapter);
        messageActivityPager.setCurrentItem(0);
        messageActivityPager.setOffscreenPageLimit(3);
        message.setSelected(true);

        messageActivityPager.setOnPageChangeListener(new MyPageChangeListener());

        if (null != getIntent() ){
            if(getIntent().getData()!=null){
                Uri uri = getIntent().getData();
                if(uri.getEncodedPath().contains("conversationlist")){
                    messageActivityPager.setCurrentItem(1);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_message_back:
                //返回
                MessageActivity.this.finish();
                break;
            case R.id.activity_message_add:
                //添加
                WindowManager wm = this.getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                if (mAddFriendPopupWindow == null) {
                    //自定义的单击事件
                    mAddFriendPopupWindow = new AddFriendPopupWindow(MessageActivity.this, this,width*2/5,height/6);
                    //监听窗口的焦点事件，点击窗口外面则取消显示
                    mAddFriendPopupWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                mAddFriendPopupWindow.dismiss();
                            }
                        }
                    });
                }
                WindowManager.LayoutParams params=this.getWindow().getAttributes();
                params.alpha=0.7f;
                this.getWindow().setAttributes(params);
                mAddFriendPopupWindow.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = MessageActivity.this.getWindow().getAttributes();
                        lp.alpha = 1f;
                        MessageActivity.this.getWindow().setAttributes(lp);
                    }
                });
                //设置默认获取焦点
                mAddFriendPopupWindow.setFocusable(true);
                //以某个控件的x和y的偏移量位置开始显示窗口
                mAddFriendPopupWindow.showAsDropDown(messageActivityAdd, 0, DensityUtil.dip2px(this,13));
                //如果窗口存在，则更新
                mAddFriendPopupWindow.update();
                /*Intent intent = new Intent();
                intent.setClass(MessageActivity.this,AddFriendActivity.class);
                MessageActivity.this.startActivity(intent);*/
                break;
            case R.id.message_linear:
            case R.id.activity_message_msg:
                //消息
                messageActivityPager.setCurrentItem(0, false);
                message.setSelected(true);
                messageTx.setTextColor(MessageActivity.this.
                    getResources().getColor(R.color.title_background_blue));
                addressTx.setTextColor(MessageActivity.this.
                    getResources().getColor(R.color._888888));
                momentsTx.setTextColor(MessageActivity.this.
                    getResources().getColor(R.color._888888));
                address.setSelected(false);
                moments.setSelected(false);
                selfInfo.setSelected(false);
                break;
            case R.id.conversation_linear:
            case R.id.activity_message_address:
                //通讯录
                messageActivityPager.setCurrentItem(1, false);
                address.setSelected(true);
                addressTx.setTextColor(MessageActivity.this.
                    getResources().getColor(R.color.title_background_blue));
                messageTx.setTextColor(MessageActivity.this.
                    getResources().getColor(R.color._888888));
                momentsTx.setTextColor(MessageActivity.this.
                    getResources().getColor(R.color._888888));
                message.setSelected(false);
                moments.setSelected(false);
                selfInfo.setSelected(false);
                break;
            case R.id.address_linear:
            case R.id.activity_message_moments:
                //朋友圈
                messageActivityPager.setCurrentItem(2, false);
                moments.setSelected(true);
                momentsTx.setTextColor(MessageActivity.this.
                    getResources().getColor(R.color.title_background_blue));
                addressTx.setTextColor(MessageActivity.this.
                    getResources().getColor(R.color._888888));
                messageTx.setTextColor(MessageActivity.this.
                    getResources().getColor(R.color._888888));
                address.setSelected(false);
                message.setSelected(false);
                selfInfo.setSelected(false);
                /*Intent intent3 = new Intent();
                intent3.setClass(MessageActivity.this,MomentsActivity.class);
                MessageActivity.this.startActivity(intent3);*/
                break;
            case R.id.activity_message_selfinfo:
                //我的
                break;
            case R.id.group_chat:
                if (mAddFriendPopupWindow != null) {
                    mAddFriendPopupWindow.dismiss();
                }
                Intent intent1 = new Intent();
                intent1.putExtra("type","create");
                intent1.setClass(MessageActivity.this,ChooseFriendActivity.class);
                MessageActivity.this.startActivity(intent1);
                break;
            case R.id.add_friend:
                if (mAddFriendPopupWindow != null) {
                    mAddFriendPopupWindow.dismiss();
                }
                Intent intent = new Intent();
                intent.setClass(MessageActivity.this,AddFriendActivity.class);
                MessageActivity.this.startActivity(intent);
                break;
            case R.id.activity_message_add_friend:
                Intent intent2 = new Intent();
                intent2.setClass(MessageActivity.this,AddFriendActivity.class);
                MessageActivity.this.startActivity(intent2);
                break;
            case R.id.message_btn:
                //消息列表
                messageFragment.conversationList.setVisibility(View.GONE);
                messageFragment.messageList.setVisibility(View.VISIBLE);
                messageBtn.setBackgroundDrawable(
                    this.getResources().getDrawable(R.drawable.gentou_title_text_border3));
                messageBtn.setTextColor(this.getResources().getColor(R.color.blue));
                conversationBtn.setBackgroundDrawable(
                    this.getResources().getDrawable(R.drawable.gentou_title_text_border4));
                conversationBtn.setTextColor(this.getResources().getColor(R.color.white));

                break;
            case R.id.conversation_btn:
                //会话列表
                messageFragment.conversationList.setVisibility(View.VISIBLE);
                messageFragment.messageList.setVisibility(View.GONE);
                conversationBtn.setBackgroundDrawable(
                    this.getResources().getDrawable(R.drawable.gentou_title_text_border2));
                conversationBtn.setTextColor(this.getResources().getColor(R.color.blue));
                messageBtn.setBackgroundDrawable(
                    this.getResources().getDrawable(R.drawable.gentou_title_text_border));
                messageBtn.setTextColor(this.getResources().getColor(R.color.white));
                break;
            default:
                break;
        }
    }

    public void changeTitleText(String title){
        getMessageActivityBackTitle.setText(title);
    }

    public void changeTitleBtn(int position){
        switch (position){
            case 0:
                messageConversationBtn.setVisibility(View.GONE);
                getMessageActivityBackTitle.setVisibility(View.VISIBLE);
                messageActivityBack.setVisibility(View.VISIBLE);
                messageActivityAdd.setVisibility(View.GONE);
                messageActivityAddFriend.setVisibility(View.INVISIBLE);
                shoot.setVisibility(View.GONE);
                break;
            case 1:
                messageConversationBtn.setVisibility(View.GONE);
                getMessageActivityBackTitle.setVisibility(View.VISIBLE);
                messageActivityBack.setVisibility(View.VISIBLE);
                messageActivityAdd.setVisibility(View.VISIBLE);
                messageActivityAddFriend.setVisibility(View.GONE);
                shoot.setVisibility(View.GONE);
                break;
            case 2:
                messageConversationBtn.setVisibility(View.GONE);
                getMessageActivityBackTitle.setVisibility(View.VISIBLE);
                messageActivityBack.setVisibility(View.VISIBLE);
                messageActivityAdd.setVisibility(View.VISIBLE);
                messageActivityAddFriend.setVisibility(View.GONE);
                shoot.setVisibility(View.GONE);
                break;
            case 3:
                break;
            default:
                break;
        }


    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    changeTitleText(getResources().getString(R.string.message_list_title));
                    changeTitleBtn(position);
                    message.setSelected(true);
                    messageTx.setTextColor(MessageActivity.this.
                        getResources().getColor(R.color.title_background_blue));
                    addressTx.setTextColor(MessageActivity.this.
                        getResources().getColor(R.color._888888));
                    momentsTx.setTextColor(MessageActivity.this.
                        getResources().getColor(R.color._888888));
                    address.setSelected(false);
                    moments.setSelected(false);
                    selfInfo.setSelected(false);
                    break;
                case 1:
                    changeTitleText(getResources().getString(R.string.conversation_list_title));
                    changeTitleBtn(position);
                    address.setSelected(true);
                    addressTx.setTextColor(MessageActivity.this.
                        getResources().getColor(R.color.title_background_blue));
                    messageTx.setTextColor(MessageActivity.this.
                        getResources().getColor(R.color._888888));
                    momentsTx.setTextColor(MessageActivity.this.
                        getResources().getColor(R.color._888888));
                    message.setSelected(false);
                    moments.setSelected(false);
                    selfInfo.setSelected(false);
                    break;
                case 2:
                    changeTitleText(getResources().getString(R.string.address_list_title));
                    changeTitleBtn(position);
                    moments.setSelected(true);
                    momentsTx.setTextColor(MessageActivity.this.
                        getResources().getColor(R.color.title_background_blue));
                    addressTx.setTextColor(MessageActivity.this.
                        getResources().getColor(R.color._888888));
                    messageTx.setTextColor(MessageActivity.this.
                        getResources().getColor(R.color._888888));
                    address.setSelected(false);
                    message.setSelected(false);
                    selfInfo.setSelected(false);
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(messageActivityPager.getCurrentItem()==2){
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                if (momentsFragment.edittextbody != null && momentsFragment.edittextbody.getVisibility() == View.VISIBLE) {
                    //edittextbody.setVisibility(View.GONE);
                    momentsFragment.updateEditTextBodyVisible(View.GONE, null);
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        HomeEvent homeEvent = new HomeEvent();
        EventBus.getDefault().post(homeEvent);
        super.finish();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /*if (null != intent) {
            if(intent.getData()!=null){
                Uri uri = intent.getData();
                if(uri.getEncodedPath().contains("conversationlist")){
                    messageActivityPager.setCurrentItem(1);
                }
            }
        }*/
    }
}

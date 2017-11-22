package com.example.administrator.lubanone.activity.message;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.adapter.message.MyGridViewAdapter;
import com.example.administrator.lubanone.bean.message.GroupInfoSetResultBean;
import com.example.administrator.lubanone.bean.message.GroupMemberInfoBean;
import com.example.administrator.lubanone.customview.AutoLineLayout;
import com.example.administrator.lubanone.customview.ToggleButtonView.ToggleButton;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationNotificationStatus;
import io.rong.imlib.model.Conversation.ConversationType;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\6\30 0030.
 */

public class ConversationInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back;
    private TextView title;
    private AutoLineLayout imgAutoLine;
    private LinearLayout setGroupName;
    private TextView groupName;
    private LinearLayout setGroupNotice;
    private TextView groupNotice;
    private ToggleButton messageDisturb;
    private ToggleButton conversationTop;
    private ToggleButton saveAddress;
    private LinearLayout setUserName;
    private ToggleButton showGroupName;
    private LinearLayout searchChatContent;
    private LinearLayout complain;
    private LinearLayout clearChatContent;
    private TextView detele;
    private GridView imgGrid;

    private ImageView groupNameClickable;
    private ImageView groupNoticeClickable;
    private TextView groupNoticeText;
    private TextView checkAllMember;

    private String targetId;//融云会话id

    private Boolean addAddress = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_conversation_info);

        if(this.getIntent().hasExtra("targetId")){
            targetId = this.getIntent().getStringExtra("targetId");
        }
        initViews();
    }

    private void initViews(){

        back = (TextView)this.findViewById(R.id.back);
        title = (TextView) this.findViewById(R.id.title);
        imgAutoLine = (AutoLineLayout) this.findViewById(R.id.img_auto_line);
        setGroupName = (LinearLayout) this.findViewById(R.id.group_name_set);
        groupName = (TextView) this.findViewById(R.id.group_name);
        setGroupNotice = (LinearLayout) this.findViewById(R.id.group_notice_set);
        groupNotice = (TextView) this.findViewById(R.id.group_notice);
        messageDisturb = (ToggleButton) this.findViewById(R.id.message_disturb);
        conversationTop = (ToggleButton) this.findViewById(R.id.conversation_top);
        saveAddress = (ToggleButton) this.findViewById(R.id.save_address);
        setUserName = (LinearLayout) this.findViewById(R.id.user_name_set);
        showGroupName = (ToggleButton) this.findViewById(R.id.group_name_show);
        searchChatContent = (LinearLayout) this.findViewById(R.id.search_chat_content);
        complain = (LinearLayout) this.findViewById(R.id.complain);
        clearChatContent = (LinearLayout) this.findViewById(R.id.clear_chat_content);
        detele = (TextView) this.findViewById(R.id.delete_and_exit);
        imgGrid = (GridView) this.findViewById(R.id.my_grid_view);
        groupNameClickable = (ImageView) this.findViewById(R.id.group_name_clickable);
        groupNoticeClickable = (ImageView) this.findViewById(R.id.group_notice_clickable);
        groupNoticeText = (TextView) this.findViewById(R.id.group_notice_text);
        checkAllMember = (TextView) this.findViewById(R.id.check_all_member);

        back.setOnClickListener(this);
        groupNameClickable.setOnClickListener(this);
        groupNoticeClickable.setOnClickListener(this);
        setUserName.setOnClickListener(this);
        searchChatContent.setOnClickListener(this);
        complain.setOnClickListener(this);
        clearChatContent.setOnClickListener(this);
        detele.setOnClickListener(this);
        checkAllMember.setOnClickListener(this);
        setToggleChanged();
        //setData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                ConversationInfoActivity.this.finish();
                break;
            case R.id.group_name_clickable:
                //设置群聊名称
                Intent intent = new Intent();
                if(mGroupInfoSetResultBean.getGroupname()!=null
                    &&mGroupInfoSetResultBean.getGroupname().length()>0){
                    intent.putExtra("groupName",mGroupInfoSetResultBean.getGroupname());
                }
                if(mGroupInfoSetResultBean.getGroupmaster()!=null
                    &&mGroupInfoSetResultBean.getGroupmaster().length()>0){
                    intent.putExtra("groupmaster",mGroupInfoSetResultBean.getGroupmaster());
                }
                if(mGroupInfoSetResultBean.getGroupimg()!=null
                    &&mGroupInfoSetResultBean.getGroupimg().length()>0){
                    intent.putExtra("groupimg",mGroupInfoSetResultBean.getGroupimg());
                }
                if(targetId!=null&&targetId.length()>0){
                    intent.putExtra("groupId",targetId);
                }
                intent.setClass(ConversationInfoActivity.this,GroupNameActivity.class);
                ConversationInfoActivity.this.startActivity(intent);
                break;
            case R.id.group_notice_clickable:
                //设置群公告
                Intent intent2 = new Intent();
                if(mGroupInfoSetResultBean.getGroupnotice()!=null
                    &&mGroupInfoSetResultBean.getGroupnotice().length()>0){
                    intent2.putExtra("groupNotice",mGroupInfoSetResultBean.getGroupnotice());
                }
                if(mGroupInfoSetResultBean.getGroupmaster()!=null
                    &&mGroupInfoSetResultBean.getGroupmaster().length()>0){
                    intent2.putExtra("groupmaster",mGroupInfoSetResultBean.getGroupmaster());
                }
                if(targetId!=null&&targetId.length()>0){
                    intent2.putExtra("groupId",targetId);
                }
                intent2.setClass(ConversationInfoActivity.this,GroupNoticeActivity.class);
                ConversationInfoActivity.this.startActivity(intent2);
                break;
            case R.id.user_name_set:
                //设置用户名称
                break;
            case R.id.search_chat_content:
                //搜索聊天内容
                break;
            case R.id.complain:
                //投诉
                Intent intent1 = new Intent();
                if(targetId!=null&&targetId.length()>0){
                    intent1.putExtra("targetId",targetId);
                }
                intent1.putExtra("type","group");
                intent1.putExtra("groupId",targetId);
                intent1.setClass(ConversationInfoActivity.this,GroupMemberActivity.class);
                //intent1.setClass(ConversationInfoActivity.this,ComplainActivity.class);
                ConversationInfoActivity.this.startActivity(intent1);
                break;
            case R.id.clear_chat_content:
                //清除聊天记录
                List<String> list = new ArrayList<>();
                list.add(this.getResources().getString(R.string.clear_chat_content));
                StytledDialog.showBottomItemDialog(this, list,
                    getString(R.string.clear_conversation_record_cancle), true, true,
                        new MyItemDialogListener() {
                    @Override
                    public void onItemClick(String text, int position) {
                        switch (position){
                            case 0:
                                if(text!=null&&text.length()>0&&text.equals(
                                    getString(R.string.clear_chat_content))){
                                    //后台接口清空记录
                                    RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP,
                                            targetId, new RongIMClient.ResultCallback<Boolean>() {
                                                @Override
                                                public void onSuccess(Boolean aBoolean) {
                                                    //聊天记录清空成功
                                                    Toast.makeText(ConversationInfoActivity.this,
                                                        getString(R.string.clear_conversation_success),
                                                        Toast.LENGTH_LONG).show();
                                                }
                                                @Override
                                                public void onError(RongIMClient.ErrorCode errorCode) {
                                                    //聊天记录清空失败
                                                    Toast.makeText(ConversationInfoActivity.this,
                                                        getString(R.string.clear_conversation_fail),
                                                        Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
                break;
            case R.id.delete_and_exit:
                //区分群主非群主
                //删除并退出
                if(mGroupInfoSetResultBean.getGroupmaster()!=null&&
                    mGroupInfoSetResultBean.getGroupmaster().length()>0){
                    if(mGroupInfoSetResultBean.getGroupmaster().equals("1")){
                        //群主  解散群组
                        showDissolveDialog();
                    }else if(mGroupInfoSetResultBean.getGroupmaster().equals("0")){
                        //非群主  退出群组
                        showExitDialog();
                    }
                }
                break;
          case R.id.check_all_member:
            Intent intent3 = new Intent();
            if(mGroupInfoSetResultBean.getGroupmaster()!=null
                &&mGroupInfoSetResultBean.getGroupmaster().length()>0){
              intent3.putExtra("groupmaster",mGroupInfoSetResultBean.getGroupmaster());
            }
            if(targetId!=null&&targetId.length()>0){
              intent3.putExtra("groupId",targetId);
            }
            intent3.setClass(ConversationInfoActivity.this,AddGroupMemberActivity.class);
            ConversationInfoActivity.this.startActivity(intent3);

            break;
            default:
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setData();
    }

    private void setData(){
        getGroupInfo();
        RongIM.getInstance().getConversationNotificationStatus(ConversationType.GROUP, targetId,
            new ResultCallback<ConversationNotificationStatus>() {
                @Override
                public void onSuccess(ConversationNotificationStatus conversationNotificationStatus) {
                    //获取消息是否免打扰状态成功
                    if(conversationNotificationStatus.equals(ConversationNotificationStatus.NOTIFY)){
                        //消息提醒
                        messageDisturb.setToggleOff();
                    }else {
                        //消息免打扰
                        messageDisturb.setToggleOn();
                    }
                }
                @Override
                public void onError(ErrorCode errorCode) {
                    //获取消息是否免打扰状态失败
                    Toast.makeText(ConversationInfoActivity.this,
                        getString(R.string.get_conversation_notification_status_fail),
                        Toast.LENGTH_LONG).show();
                }
            });
        RongIM.getInstance().getRongIMClient().getConversation(ConversationType.GROUP, targetId,
            new ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                  if(conversation!=null){
                      if (conversation.isTop()){
                          //置顶
                          conversationTop.setToggleOn();
                      }else {
                          //未置顶
                          conversationTop.setToggleOff();
                      }
                  }
                }
                @Override
                public void onError(ErrorCode errorCode) {
                    //获取会话是否置顶状态失败
                    Toast.makeText(ConversationInfoActivity.this,
                        getString(R.string.get_conversation_top_status_fail),
                        Toast.LENGTH_LONG).show();
                }
            });

    }


    private void setToggleChanged(){

        messageDisturb.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                //消息免打扰
                if(on){
                    RongIM.getInstance().setConversationNotificationStatus(
                        Conversation.ConversationType.GROUP, targetId,
                        Conversation.ConversationNotificationStatus.DO_NOT_DISTURB,
                        new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                            @Override
                            public void onSuccess(Conversation.ConversationNotificationStatus
                                                          conversationNotificationStatus) {
                                //设置免打扰成功
                                Toast.makeText(ConversationInfoActivity.this,
                                    getString(R.string.set_conversation_notification_true_success),
                                    Toast.LENGTH_LONG).show();

                            }
                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                //设置免打扰失败
                                Toast.makeText(ConversationInfoActivity.this,
                                    getString(R.string.set_conversation_notification_true_fail),
                                    Toast.LENGTH_LONG).show();
                            }
                        });
                }else {
                    RongIM.getInstance().setConversationNotificationStatus(
                            Conversation.ConversationType.GROUP, targetId,
                            Conversation.ConversationNotificationStatus.NOTIFY,
                            new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                                @Override
                                public void onSuccess(Conversation.ConversationNotificationStatus
                                                              conversationNotificationStatus) {
                                    //设置消息提醒成功
                                    Toast.makeText(ConversationInfoActivity.this,
                                        getString(R.string.set_conversation_notification_false_success),
                                        Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    //设置消息提醒失败
                                    Toast.makeText(ConversationInfoActivity.this,
                                        getString(R.string.set_conversation_notification_false_fail),
                                        Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        conversationTop.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    //会话置顶
                    RongIM.getInstance().setConversationToTop(Conversation.ConversationType.GROUP,
                            targetId, true, new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    //置顶成功
                                    Toast.makeText(ConversationInfoActivity.this,
                                        getString(R.string.set_conversation_top_true_success),
                                        Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    //置顶失败
                                    Toast.makeText(ConversationInfoActivity.this,
                                        getString(R.string.set_conversation_top_true_fail),
                                        Toast.LENGTH_LONG).show();
                                }
                            });
                }else {
                    //取消会话置顶
                    RongIM.getInstance().setConversationToTop(Conversation.ConversationType.GROUP,
                            targetId, false, new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    //取消置顶成功
                                    Toast.makeText(ConversationInfoActivity.this,
                                        getString(R.string.set_conversation_top_false_success),
                                        Toast.LENGTH_LONG).show();
                                }
                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    //取消置顶失败
                                    Toast.makeText(ConversationInfoActivity.this,
                                        getString(R.string.set_conversation_top_false_fail),
                                        Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        saveAddress.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    //保存到通讯录
                    addAddress = true;
                    addAddress("1");
                }else {
                    //从通讯录移除
                    addAddress = false;
                    addAddress("0");
                }
            }
        });
    }


    private GroupInfoSetResultBean mGroupInfoSetResultBean;
    private RequestListener getGroupSetInfoListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<GroupInfoSetResultBean> result = GsonUtil.processJson(jsonObject,
                    GroupInfoSetResultBean.class);
                mGroupInfoSetResultBean = result.getResult();
                if (ResultUtil.isSuccess(result)&&result!=null) {
                    if(mGroupInfoSetResultBean!=null){
                        List<GroupMemberInfoBean> memberInfoBeanList = mGroupInfoSetResultBean.getData();
                        if(memberInfoBeanList!=null){
                            MyGridViewAdapter myGridViewAdapter = new MyGridViewAdapter(
                                ConversationInfoActivity.this,memberInfoBeanList,
                                mGroupInfoSetResultBean.getGroupmaster(),targetId);
                            imgGrid.setAdapter(myGridViewAdapter);
                            title.setText(getString(R.string.conversation_info_title)
                                +"("+memberInfoBeanList.size()+")");
                        }else {
                            title.setText(getString(R.string.conversation_info_title));
                        }
                        if(mGroupInfoSetResultBean.getGroupname()!=null&&
                            mGroupInfoSetResultBean.getGroupname().length()>0){
                            groupName.setText(mGroupInfoSetResultBean.getGroupname());
                        }
                        if(mGroupInfoSetResultBean.getGroupnotice()!=null&&
                            mGroupInfoSetResultBean.getGroupnotice().length()>0){
                            groupNoticeText.setVisibility(View.VISIBLE);
                            groupNoticeText.setText(mGroupInfoSetResultBean.getGroupnotice());
                        }else {
                            groupNoticeText.setVisibility(View.GONE);
                        }
                        if(mGroupInfoSetResultBean.getGroupmaster()!=null
                            &&mGroupInfoSetResultBean.getGroupmaster().length()>0){
                            if(mGroupInfoSetResultBean.getGroupmaster().equals("1")){
                                //群主
                                if(memberInfoBeanList.size()>48){
                                    checkAllMember.setVisibility(View.VISIBLE);
                                }else {
                                    checkAllMember.setVisibility(View.GONE);
                                }
                                //detele.setText(getString(R.string.dissolve_group));
                                detele.setText(getString(R.string.quit_group));
                                groupNameClickable.setVisibility(View.VISIBLE);
                                groupNoticeClickable.setVisibility(View.VISIBLE);
                            }else if(mGroupInfoSetResultBean.getGroupmaster().equals("0")){
                                //非群主
                                if(memberInfoBeanList.size()>49){
                                    checkAllMember.setVisibility(View.VISIBLE);
                                }else {
                                    checkAllMember.setVisibility(View.GONE);
                                }
                                detele.setText(getString(R.string.quit_group));
                                groupNameClickable.setVisibility(View.GONE);
                                if(mGroupInfoSetResultBean.getGroupnotice()!=null&&
                                    mGroupInfoSetResultBean.getGroupnotice().length()>0){
                                    groupNoticeClickable.setVisibility(View.VISIBLE);
                                }else {
                                    groupNoticeClickable.setVisibility(View.GONE);
                                }
                            }
                        }
                        if(mGroupInfoSetResultBean.getGroupnotice()!=null&&
                            mGroupInfoSetResultBean.getGroupnotice().length()>0){
                            groupNoticeText.setVisibility(View.VISIBLE);
                            groupNoticeText.setText(mGroupInfoSetResultBean.getGroupnotice());
                            groupNotice.setText("");
                        }else {
                            groupNoticeText.setVisibility(View.GONE);
                        }
                        if(mGroupInfoSetResultBean.getMaillist()!=null&&mGroupInfoSetResultBean.getMaillist().equals("1")){
                            //已保存
                            saveAddress.setToggleOn();
                        }else {
                            //未保存
                            saveAddress.setToggleOff();
                        }
                        //groupName.setText(mGroupInfoSetResultBean.getGroupname());
                        //groupNotice.setText(mGroupInfoSetResultBean.getGroupnotice());
                    }else {
                        ToastUtil.showShort(getResources().getString(R.string.get_group_set_info_null),
                            ConversationInfoActivity.this);
                    }
                } else {
                    Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                        getString(R.string.get_group_set_info_fail)),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(ConversationInfoActivity.this, getString(R.string.get_group_set_info_fail)
                    ,Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onFail(String errorMsf) {
            Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(errorMsf,
                getString(R.string.get_group_info_fail)),Toast.LENGTH_LONG).show();
        }
    };
    private void getGroupInfo(){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsGroupId = new RequestParams("groupid", targetId);
        list.add(paramsToken);
        list.add(paramsGroupId);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), ConversationInfoActivity.this, list,
            Urls.GET_GROUP_SET_INFO, getGroupSetInfoListener, RequestNet.POST);

    }

    private void showDissolveDialog(){
       /* List<String> list1 = new ArrayList<>();
        //list1.add(this.getResources().getString(R.string.dissolve_hint));
        list1.add(this.getResources().getString(R.string.exit_hint));
        list1.add(getString(R.string.choose_friend_commit));
        StytledDialog.showBottomItemDialog(this, list1, getString(R.string.clear_conversation_record_cancle), true, true,
            new MyItemDialogListener() {
                @Override
                public void onItemClick(String text, int position) {
                    switch (position){
                        case 0:
                            break;
                        case 1:
                            //解散群组
                            dissolveGroup();
                            break;
                        default:
                            break;
                    }
                }
            });*/

        StytledDialog.showIosAlert(this, getString(R.string.detele_and_exit),
            getString(R.string.exit_hint), getString(R.string.cancel),
            getString(R.string.confim_no_space), "", true, true, new MyDialogListener() {
                @Override
                public void onFirst(DialogInterface dialog) {
                    dialog.dismiss();
                }

                @Override
                public void onSecond(DialogInterface dialog) {
                    dialog.dismiss();
                    //解散群组
                    dissolveGroup();
                }
            });
    }

    private void showExitDialog(){
        /*List<String> list1 = new ArrayList<>();
        list1.add(this.getResources().getString(R.string.exit_hint));
        list1.add(getString(R.string.choose_friend_commit));
        StytledDialog.showBottomItemDialog(this, list1,
            getString(R.string.clear_conversation_record_cancle), true, true,
            new MyItemDialogListener() {
                @Override
                public void onItemClick(String text, int position) {
                    switch (position){
                        case 0:
                            break;
                        case 1:
                            //退出群组
                            exitGroup();
                            break;
                        default:
                            break;
                    }
                }
            });*/

        StytledDialog.showIosAlert(this, getString(R.string.detele_and_exit),
            getString(R.string.exit_hint), getString(R.string.cancel),
            getString(R.string.confim_no_space), "", true, true, new MyDialogListener() {
                @Override
                public void onFirst(DialogInterface dialog) {
                    dialog.dismiss();
                }

                @Override
                public void onSecond(DialogInterface dialog) {
                    dialog.dismiss();
                    //退出群组
                    exitGroup();
                }
            });
    }


    private RequestListener dissolveGroupListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
                if (ResultUtil.isSuccess(result)) {
                    /*ToastUtil.showShort(getResources().getString(R.string.dissolve_group_success),
                        getApplicationContext());*/
                    ToastUtil.showShort(getResources().getString(R.string.quit_group_success),
                        getApplicationContext());
                    if(RongIM.getInstance()!=null){
                        RongIM.getInstance().removeConversation(ConversationType.GROUP, targetId,
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
                    ConversationActivity.instance.finish();
                    ConversationInfoActivity.this.finish();
                } else {
                    /*Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                        getString(R.string.dissolve_group_fail)),Toast.LENGTH_LONG).show();*/
                    Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                        getString(R.string.quit_group_fail)),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                /*Toast.makeText(ConversationInfoActivity.this, getString(R.string.dissolve_group_fail)
                    ,Toast.LENGTH_LONG).show();*/
                Toast.makeText(ConversationInfoActivity.this,
                    ConversationInfoActivity.this.getResources().
                        getString(R.string.quit_group_fail),Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onFail(String errorMsf) {
            /*Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(errorMsf,
                getString(R.string.dissolve_group_fail)),Toast.LENGTH_LONG).show();*/
            Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(errorMsf,
                getString(R.string.quit_group_fail)),Toast.LENGTH_LONG).show();
        }
    };
    private void dissolveGroup(){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsGroupId = new RequestParams("groupid", targetId);
        list.add(paramsToken);
        list.add(paramsGroupId);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), ConversationInfoActivity.this, list,
            Urls.DISSOLVE_GROUP, dissolveGroupListener, RequestNet.POST);
    }

    private RequestListener exitGroupListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
                if (ResultUtil.isSuccess(result)) {
                    ToastUtil.showShort(getResources().getString(R.string.quit_group_success),
                        getApplicationContext());
                    if(RongIM.getInstance()!=null){
                        RongIM.getInstance().removeConversation(ConversationType.GROUP, targetId,
                            new ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {
                                    //移除会话列表成功
                                }
                                @Override
                                public void onError(ErrorCode errorCode) {
                                    //移除会话列表失败
                                }
                            });
                    }
                    ConversationActivity.instance.finish();
                    ConversationInfoActivity.this.finish();
                } else {
                    Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                        getString(R.string.quit_group_fail)),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(ConversationInfoActivity.this, getString(R.string.quit_group_fail)
                    ,Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onFail(String errorMsf) {
            Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(errorMsf,
                getString(R.string.quit_group_fail)),Toast.LENGTH_LONG).show();
        }
    };

    private void exitGroup(){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsGroupId = new RequestParams("groupid", targetId);
        list.add(paramsToken);
        list.add(paramsGroupId);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), ConversationInfoActivity.this, list,
            Urls.EXIT_GROUP, exitGroupListener, RequestNet.POST);

    }

    private RequestListener addAddressListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
                if (result!=null&&ResultUtil.isSuccess(result)) {
                    if(addAddress){
                        ToastUtil.showShort(getResources().getString(R.string.add_address_success),
                            getApplicationContext());
                    }else {
                        ToastUtil.showShort(getResources().getString(R.string.remove_address_success),
                            getApplicationContext());
                    }
                } else {
                    if(addAddress){
                        Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                            getString(R.string.add_address_fail)),Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                            getString(R.string.remove_address_fail)),Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                if(addAddress){
                    Toast.makeText(ConversationInfoActivity.this,
                        getString(R.string.add_address_fail),Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ConversationInfoActivity.this,
                        getString(R.string.remove_address_fail),Toast.LENGTH_LONG).show();
                }
            }
        }
        @Override
        public void onFail(String errorMsf) {
            if(addAddress){
                Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(errorMsf,
                    getString(R.string.add_address_fail)),Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(ConversationInfoActivity.this, DebugUtils.convert(errorMsf,
                    getString(R.string.remove_address_fail)),Toast.LENGTH_LONG).show();
            }
        }
    };

    private void addAddress(String maillist){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsGroupId = new RequestParams("groupid", targetId);
        RequestParams paramsMailList = new RequestParams("maillist", maillist);
        list.add(paramsToken);
        list.add(paramsGroupId);
        list.add(paramsMailList);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), ConversationInfoActivity.this, list,
            Urls.GROUP_SAVE_ADDRESS, addAddressListener, RequestNet.POST);

    }




}

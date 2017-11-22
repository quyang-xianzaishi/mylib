package com.example.administrator.lubanone.fragment.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.message.CustomMessageActivity;
import com.example.administrator.lubanone.activity.message.MessageActivity;
import com.example.administrator.lubanone.activity.message.OrderMessageActivity;
import com.example.administrator.lubanone.activity.message.RecommendFriendListActivity;
import com.example.administrator.lubanone.activity.message.SystemMessageListActivity;
import com.example.administrator.lubanone.activity.message.TeamMessageActivity;
import com.example.administrator.lubanone.activity.message.TrainMessageActivity;
import com.example.administrator.lubanone.activity.message.TrainingNoticeActivity;
import com.example.administrator.lubanone.bean.message.GetMessageUreadResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.DateUtils;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\6\28 0028.
 */

public class MessageFragment extends Fragment implements View.OnClickListener{

    private EditText searchEdt;
    private LinearLayout orderMessage;
    private LinearLayout systemMessage;
    private LinearLayout trainingMessage;
    private LinearLayout teamMessage;
    private LinearLayout customMessage;
    private LinearLayout trainMessage;
    public LinearLayout conversationList;
    public LinearLayout messageList;
    private LinearLayout recommendFriend;
    private ConversationListFragment conversationListFragment;
    private TextView orderUnread;
    private TextView systemUnread;
    private TextView trainUnread;
    private TextView recommendUnread;
    public TextView customerUnread;

    private TextView orderLatestMess;
    private TextView systemLatestMess;
    private TextView trainLatestMess;
    private TextView recommendLatestMess;
    public TextView customerLatestMess;
    private TextView orderLatestMessTime;
    private TextView systemLatestMessTime;
    private TextView trainLatestMessTime;
    private TextView recommendLatestMessTime;
    public TextView customerLatestMessTime;
    private GetMessageUreadResultBean getMessageUreadResultBean;
    private int listUnread = 0 ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchEdt = (EditText) getActivity().findViewById(R.id.fragment_message_search);
        orderMessage = (LinearLayout) getActivity().findViewById(R.id.order_message);
        systemMessage = (LinearLayout) getActivity().findViewById(R.id.system_message);
        trainingMessage = (LinearLayout) getActivity().findViewById(R.id.training_message);
        teamMessage = (LinearLayout) getActivity().findViewById(R.id.team_message);
        customMessage = (LinearLayout) getActivity().findViewById(R.id.custom_message);
        recommendFriend = (LinearLayout) getActivity().findViewById(R.id.recommend_friend);
        trainMessage = (LinearLayout) getActivity().findViewById(R.id.train_message);

        orderUnread = (TextView) getActivity().findViewById(R.id.order_message_unread);
        systemUnread = (TextView) getActivity().findViewById(R.id.system_message_unread);
        trainUnread = (TextView) getActivity().findViewById(R.id.train_message_unread);
        recommendUnread = (TextView) getActivity().findViewById(R.id.recommend_message_unread);
        customerUnread = (TextView) getActivity().findViewById(R.id.customer_message_unread);

        orderLatestMess = (TextView) getActivity().findViewById(R.id.order_latest_content);
        systemLatestMess = (TextView) getActivity().findViewById(R.id.system_latest_content);
        trainLatestMess = (TextView) getActivity().findViewById(R.id.train_latest_content);
        recommendLatestMess = (TextView) getActivity().findViewById(R.id.recommend_latest_content);
        customerLatestMess = (TextView) getActivity().findViewById(R.id.custom_latest_content);

        orderLatestMessTime = (TextView) getActivity().findViewById(R.id.order_latest_time);
        systemLatestMessTime = (TextView) getActivity().findViewById(R.id.system_latest_time);
        trainLatestMessTime = (TextView) getActivity().findViewById(R.id.train_latest_time);
        recommendLatestMessTime = (TextView) getActivity().findViewById(R.id.recommend_latest_time);
        customerLatestMessTime = (TextView) getActivity().findViewById(R.id.custom_latest_time);

        conversationList =  (LinearLayout) getActivity().findViewById(R.id.fragment_conversation_list);
        messageList = (LinearLayout) getActivity().findViewById(R.id.message_list);

        addRongConversationList();
        setListener();
    }

    @Override
    public void onResume(){
        super.onResume();
        requestData();
    }


    private void setListener(){
        orderMessage.setOnClickListener(this);
        systemMessage.setOnClickListener(this);
        trainingMessage.setOnClickListener(this);
        teamMessage.setOnClickListener(this);
        customMessage.setOnClickListener(this);
        recommendFriend.setOnClickListener(this);
        trainMessage.setOnClickListener(this);
    }

    private void requestData(){
        getMessageUreadResultBean = new GetMessageUreadResultBean();
      int unread = RongIM.getInstance().getUnreadCount(
          new ConversationType[]{ConversationType.CUSTOMER_SERVICE});
      MyApplication.getInstance().customerUnReadCount = unread;
        if(MyApplication.getInstance().customerUnReadCount>0) {
          customerUnread.setVisibility(View.VISIBLE);
          if(MyApplication.getInstance().customerUnReadCount<100){
            customerUnread.setText(Integer.toString(MyApplication.getInstance().customerUnReadCount));
          }else {
            customerUnread.setText("99+");
          }
            /*customerUnread.setVisibility(View.VISIBLE);
            customerUnread.setText(Integer.toString(MyApplication.getInstance().customerUnReadCount));*/
        } else {
            customerUnread.setVisibility(View.GONE);
        }
        RongIM.getInstance().getLatestMessages(ConversationType.CUSTOMER_SERVICE,
            "KEFU150106338381419", 1, new ResultCallback<List<Message>>() {
                @Override
                public void onSuccess(List<Message> messages) {
                  if(messages!=null&&messages.size()>0){
                    if (messages.get(0).getContent() instanceof TextMessage) {//文本消息
                      TextMessage textMessage = (TextMessage) messages.get(0).getContent();
                      customerLatestMess.setText(textMessage.getContent().toString());
                    }
                    if (messages.get(0).getContent() instanceof ImageMessage) {//图片消息
                      customerLatestMess.setText(getActivity().getString(R.string.picture_text));
                    }
                    if (messages.get(0).getContent() instanceof FileMessage) {//文件消息
                      customerLatestMess.setText(getActivity().getString(R.string.picture_text));
                    }
                    if (messages.get(0).getContent() instanceof VoiceMessage) {//语音消息
                      customerLatestMess.setText(getActivity().getString(R.string.voice_text));
                    }
                    customerLatestMessTime.setText(DateUtils.timedate(
                        Long.toString(messages.get(0).getReceivedTime())));
                  }

                }

                @Override
                public void onError(ErrorCode errorCode) {

                }
            });
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
        list.add(paramsToken);

        RequestNet requestNet = new RequestNet((MyApplication) getActivity().getApplication(), getActivity(), list,
            Urls.GET_MESSAGE_UNREAD_COUNT, getUnreadListener, RequestNet.POST);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_message:
                //订单消息
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), OrderMessageActivity.class);
                getActivity().startActivity(intent2);
                orderUnread.setVisibility(View.GONE);
                break;
            case R.id.system_message:
                //系统消息
                Intent intent = new Intent();
                intent.setClass(getActivity(), SystemMessageListActivity.class);
                getActivity().startActivity(intent);
                systemUnread.setVisibility(View.GONE);
                break;
            case R.id.training_message:
                //培训通告
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), TrainingNoticeActivity.class);
                getActivity().startActivity(intent1);
                break;
            case R.id.team_message:
                //团队消息
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), TeamMessageActivity.class);
                getActivity().startActivity(intent3);
                break;
            case R.id.custom_message:
                //客服
                //首先需要构造使用客服者的用户信息
                CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
                CSCustomServiceInfo csInfo = csBuilder.nickName(getString(R.string.rongyun_nickname)).build();
                /*Intent intent4 = new Intent();
                intent4.putExtra("customServiceInfo",csInfo);
                intent4.setClass(getActivity(), CustomMessageActivity.class);
                getActivity().startActivity(intent4);*/
                customerUnread.setVisibility(View.GONE);

                RongIM.getInstance().startCustomerServiceChat(getActivity(), "KEFU150106338381419", "在线客服",csInfo);
                break;
            case R.id.recommend_friend:
                //推荐好友
                Intent intent5 = new Intent();
                intent5.setClass(getActivity(), RecommendFriendListActivity.class);
                getActivity().startActivity(intent5);
                recommendUnread.setVisibility(View.GONE);
                break;
            case R.id.train_message:
                //培训消息
                Intent intent6 = new Intent();
                intent6.setClass(getActivity(), TrainMessageActivity.class);
                getActivity().startActivity(intent6);
                trainUnread.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }

    private void addRongConversationList(){
        if (conversationListFragment == null) {
            conversationListFragment = new ConversationListFragment();
        }
        //添加融云会话列表
        Uri uri = Uri.parse("rong://" + "com.example.administrator.lubanone").buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")
                .build();
        conversationListFragment.setUri(uri);
        if(!conversationListFragment.isAdded()){
            getChildFragmentManager().beginTransaction().add(R.id.fragment_conversation_list,
                    conversationListFragment).commit();
        }
        RongIM.getInstance().getConversationList();

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userId) {
                UserInfo userInfo = null;
               /* String myImageUrl = "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123914329.jpg";
                Uri uri = Uri.parse(myImageUrl);
                userInfo = new UserInfo("88888", "房东", uri);*/
                return userInfo;
            }
        }, true);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ConversationListAdapter listAdapter = (ConversationListAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight =0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //请求数据
    private RequestListener getUnreadListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }

        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<GetMessageUreadResultBean> result = GsonUtil
                    .processJson(jsonObject, GetMessageUreadResultBean.class);
                if (ResultUtil.isSuccess(result) && null != result && null != result.getResult()) {
                    getMessageUreadResultBean = result.getResult();
                    setUnread();
                } else {
                     ToastUtil.showShort(ResultUtil.getErrorMsg(result),
                        getActivity());
                }
            } catch (Exception e) {
                if (isAdded()) {
                    ToastUtil.showShort(getActivity().getString(R.string.ger_unread_count_fail),
                        getActivity());
                }
            }
        }

        @Override
        public void onFail(String errorMsf) {
            ToastUtil.showShort(errorMsf,
                getActivity());
        }
    };

    private void setUnread(){
        if(getMessageUreadResultBean!=null){
            int orderunread = Integer.parseInt(getMessageUreadResultBean.getOrderunread());
            int systemunread = Integer.parseInt(getMessageUreadResultBean.getSystemunresd());
            int trainunread = Integer.parseInt(getMessageUreadResultBean.getTrainunread());
            int recommendunread = Integer.parseInt(getMessageUreadResultBean.getRecommendunread());
            int unread = MyApplication.getInstance().customerUnReadCount
                + orderunread + systemunread + trainunread + recommendunread;
            MyApplication.listUnReadCount = orderunread + systemunread + trainunread + recommendunread;
            if(orderunread>0){
                orderUnread.setVisibility(View.VISIBLE);
                if(orderunread<100){
                    orderUnread.setText(Integer.toString(orderunread));
                }else {
                    orderUnread.setText("99+");
                }
            }else {
                orderUnread.setVisibility(View.GONE);
            }
            if(systemunread>0){
                systemUnread.setVisibility(View.VISIBLE);
                if(systemunread<100){
                    systemUnread.setText(Integer.toString(systemunread));
                }else {
                    systemUnread.setText("99+");
                }
            }else {
                systemUnread.setVisibility(View.GONE);
            }
            if(trainunread>0){
                trainUnread.setVisibility(View.VISIBLE);
                if(trainunread<100){
                    trainUnread.setText(Integer.toString(trainunread));
                }else {
                    trainUnread.setText("99+");
                }
            }else {
                trainUnread.setVisibility(View.GONE);
            }
            if(recommendunread>0){
                recommendUnread.setVisibility(View.VISIBLE);
                if(recommendunread<100){
                    recommendUnread.setText(Integer.toString(recommendunread));
                }else {
                    recommendUnread.setText("99+");
                }
            }else {
                recommendUnread.setVisibility(View.GONE);
            }
            if(unread>0){
                ((MessageActivity)getActivity()).systemUnread.setVisibility(View.VISIBLE);
                if(unread<100){
                    ((MessageActivity)getActivity()).systemUnread.setText(Integer.toString(unread));
                }else {
                    ((MessageActivity)getActivity()).systemUnread.setText("99+");
                }
            }else {
                ((MessageActivity)getActivity()).systemUnread.setVisibility(View.GONE);
            }
            setLatestMessage();
        }else {
            orderUnread.setVisibility(View.GONE);
            systemUnread.setVisibility(View.GONE);
            trainUnread.setVisibility(View.GONE);
            recommendUnread.setVisibility(View.GONE);
            ((MessageActivity)getActivity()).systemUnread.setVisibility(View.GONE);
        }

    }

    private void setLatestMessage(){
        orderLatestMess.setText(getMessageUreadResultBean.getOrderlatestmess());
        orderLatestMessTime.setText(getMessageUreadResultBean.getOrderlatestmesstime());
        systemLatestMess.setText(getMessageUreadResultBean.getSystemlatestmess());
        systemLatestMessTime.setText(getMessageUreadResultBean.getSystemlatestmesstime());
        trainLatestMess.setText(getMessageUreadResultBean.getTrainlatestmess());
        trainLatestMessTime.setText(getMessageUreadResultBean.getTrainlatestmesstime());
        recommendLatestMess.setText(getMessageUreadResultBean.getRecommendlatestmess());
        recommendLatestMessTime.setText(getMessageUreadResultBean.getRecommendlatestmesstime());
    }


}

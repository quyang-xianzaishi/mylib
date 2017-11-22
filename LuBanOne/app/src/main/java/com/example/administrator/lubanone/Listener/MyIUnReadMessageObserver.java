package com.example.administrator.lubanone.Listener;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.message.MessageActivity;
import com.example.administrator.lubanone.fragment.message.MessageFragment;
import com.example.administrator.lubanone.utils.DateUtils;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import java.util.List;

/**
 * Created by Administrator on 2017\8\17 0017.
 */

public class MyIUnReadMessageObserver implements IUnReadMessageObserver {

  //private int unReadCount;
  //private int customerUnReadCount;


  @Override
  public void onCountChanged(int i) {

    //获取客服未读消息
    RongIM.getInstance().getUnreadCount(ConversationType.CUSTOMER_SERVICE, "KEFU150106338381419",
        new ResultCallback<Integer>() {
          @Override
          public void onSuccess(Integer integer) {
            //获取客服未读消息成功
            MyApplication.getInstance().customerUnReadCount = integer;
            if(MessageActivity.instance!=null){
              int unreadCount = integer + MyApplication.listUnReadCount;
              if(MessageActivity.instance.adapter!=null&&
                  MessageActivity.instance.adapter.getCurrentFragment() instanceof MessageFragment){
                final MessageFragment fragment = (MessageFragment) MessageActivity.instance.adapter.getCurrentFragment();
                if(integer>0){
                  fragment.customerUnread.setVisibility(View.VISIBLE);
                  if(integer<100){
                    fragment.customerUnread.setText(Integer.toString(integer));
                  }else {
                    fragment.customerUnread.setText("99+");
                  }
                }else {
                  fragment.customerUnread.setVisibility(View.GONE);
                }
                RongIM.getInstance().getLatestMessages(ConversationType.CUSTOMER_SERVICE,
                    "KEFU150106338381419", 1, new ResultCallback<List<Message>>() {
                      @Override
                      public void onSuccess(List<Message> messages) {
                        if(messages!=null&&messages.size()>0){
                          if (messages.get(0).getContent() instanceof TextMessage) {//文本消息
                            TextMessage textMessage = (TextMessage) messages.get(0).getContent();
                            fragment.customerLatestMess.setText(textMessage.getContent().toString());
                          }
                          if (messages.get(0).getContent() instanceof ImageMessage) {//图片消息
                            fragment.customerLatestMess.setText(
                                MessageActivity.instance.getString(R.string.picture_text));
                          }
                          if (messages.get(0).getContent() instanceof FileMessage) {//文件消息
                            fragment.customerLatestMess.setText(
                                MessageActivity.instance.getString(R.string.picture_text));
                          }
                          if (messages.get(0).getContent() instanceof VoiceMessage) {//语音消息
                            fragment.customerLatestMess.setText(
                                MessageActivity.instance.getString(R.string.voice_text));
                          }
                          fragment.customerLatestMessTime.setText(DateUtils.timedate(
                              Long.toString(messages.get(0).getReceivedTime())));
                        }

                      }
                      @Override
                      public void onError(ErrorCode errorCode) {

                      }
                    });
              }

              if(unreadCount>0){
                MessageActivity.instance.systemUnread.setVisibility(View.VISIBLE);
                if(unreadCount<100){
                  MessageActivity.instance.systemUnread.setText(Integer.toString(unreadCount));
                }else {
                  MessageActivity.instance.systemUnread.setText("99+");
                }
              }else {
                MessageActivity.instance.systemUnread.setVisibility(View.GONE);
              }
            }

          }

          @Override
          public void onError(ErrorCode errorCode) {
            //获取客服未读消息失败
            Log.e("MyIUnReadMessageObserver","获取客服未读消息失败");
          }
        });

    MyApplication.getInstance().unReadCount =  RongIM.getInstance().getUnreadCount(
        new ConversationType[]{ConversationType.GROUP,ConversationType.PRIVATE,
            ConversationType.CUSTOMER_SERVICE});

    Log.e("MyIUnReadMessageObserver","unReadCount: " + MyApplication.getInstance().unReadCount);
    Log.e("MyIUnReadMessageObserver","customerUnReadCount: " +
        MyApplication.getInstance().customerUnReadCount);
    if(MessageActivity.instance!=null){
      int friendUnread = RongIM.getInstance().getUnreadCount(
          new ConversationType[]{ConversationType.GROUP, ConversationType.PRIVATE});
      if(friendUnread>0){
        MessageActivity.instance.friendUnread.setVisibility(View.VISIBLE);
        if(friendUnread<100){
          MessageActivity.instance.friendUnread.setText(Integer.toString(friendUnread));
        }else {
          MessageActivity.instance.friendUnread.setText("99+");
        }
      }else {
        MessageActivity.instance.friendUnread.setVisibility(View.GONE);
      }

    }

  }


}

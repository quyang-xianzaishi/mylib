package com.example.administrator.lubanone.Listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.example.administrator.lubanone.activity.message.FriendInfoActivity;
import com.example.administrator.lubanone.activity.message.MemberInfoActivity;
import com.example.administrator.lubanone.manager.JyActivityManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2017\9\13 0013.
 */

public class MyConversationBehaviorListener implements RongIM.ConversationBehaviorListener{

  /**
   * 当点击用户头像后执行。
   *
   * @param context           上下文。
   * @param conversationType  会话类型。
   * @param userInfo          被点击的用户的信息。
   * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
   */
  @Override
  public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
    if(conversationType!=null&&
        (conversationType.equals(ConversationType.GROUP)
            ||conversationType.equals(ConversationType.PRIVATE))){
      Intent intent = new Intent(JyActivityManager.getInstance().getCurrentActivity(),
          MemberInfoActivity.class);
      intent.putExtra("userId",userInfo.getUserId());
      JyActivityManager.getInstance().getCurrentActivity().startActivity(intent);

    }
    return false;
  }

  /**
   * 当长按用户头像后执行。
   *
   * @param context          上下文。
   * @param conversationType 会话类型。
   * @param userInfo         被点击的用户的信息。
   * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
   */
  @Override
  public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
    return false;
  }

  /**
   * 当点击消息时执行。
   *
   * @param context 上下文。
   * @param view    触发点击的 View。
   * @param message 被点击的消息的实体信息。
   * @return 如果用户自己处理了点击后的逻辑，则返回 true， 否则返回 false, false 走融云默认处理方式。
   */
  @Override
  public boolean onMessageClick(Context context, View view, Message message) {
    return false;
  }

  /**
   * 当长按消息时执行。
   *
   * @param context 上下文。
   * @param view    触发点击的 View。
   * @param message 被长按的消息的实体信息。
   * @return 如果用户自己处理了长按后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
   */
  @Override
  public boolean onMessageLongClick(Context context, View view, Message message) {
    return false;
  }
  /**
   * 当点击链接消息时执行。
   *
   * @param context 上下文。
   * @param link    被点击的链接。
   * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
   */
  @Override
  public boolean onMessageLinkClick(Context context, String link) {
    return false;
  }
}

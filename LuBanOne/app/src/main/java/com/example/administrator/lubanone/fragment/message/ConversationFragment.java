package com.example.administrator.lubanone.fragment.message;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.message.MessageActivity;
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
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\7\25 0025.
 */

public class ConversationFragment extends Fragment implements View.OnClickListener{

  private LinearLayout conversationList;
  private ConversationListFragment conversationListFragment;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_conversation, container,false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    conversationList =  (LinearLayout) getActivity().findViewById(R.id.fragment_conversation_list);

    addRongConversationList();
    //setListener();
    setRongProvider();
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

  }

  @Override
  public void onResume(){
    super.onResume();
    setUnread();
  }

  private void setUnread(){
    int friendUnread = RongIM.getInstance().getUnreadCount(
        new ConversationType[]{ConversationType.GROUP, ConversationType.PRIVATE});
    if(friendUnread>0){
      ((MessageActivity)getActivity()).friendUnread.setVisibility(View.VISIBLE);
      if(friendUnread<100){
        ((MessageActivity)getActivity()).friendUnread.setText(Integer.toString(friendUnread));
      }else {
        ((MessageActivity)getActivity()).friendUnread.setText("99+");
      }
    }else {
      ((MessageActivity)getActivity()).friendUnread.setVisibility(View.GONE);
    }
  }

  @Override
  public void onClick(View v) {

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
        Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.get_user_info_fail),
            Toast.LENGTH_LONG).show();
      }

    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(getActivity().getResources().getString(R.string.get_user_info_fail),
          getActivity());
    }
  };

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
          }
        } else {
          Toast.makeText(getActivity(), DebugUtils.convert(ResultUtil.getErrorMsg(result),
              getString(R.string.get_group_info_fail)), Toast.LENGTH_LONG).show();
        }
      } catch (Exception e) {
        ToastUtil.showShort(getActivity().getResources().getString(R.string.get_group_info_fail),
            getActivity().getApplicationContext());
      }
    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(getActivity().getResources().getString(R.string.get_user_info_fail),
          getActivity().getApplicationContext());
    }
  };


  private void setRongProvider(){
    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
      @Override
      public UserInfo getUserInfo(String userId) {
        UserInfo userInfo = null;
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getActivity().getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsUserId = new RequestParams("userid", userId);
        list.add(paramsToken);
        list.add(paramsUserId);
        RequestNet requestNet = new RequestNet((MyApplication) getActivity().getApplication(), getActivity(), list,
            Urls.GET_USER_INFO,getUserInfoListener, RequestNet.POST);
        return null;
      }
    }, true);

    RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider(){
      @Override
      public Group getGroupInfo(String s) {
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getActivity().getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsGroupId = new RequestParams("groupid",s);
        list.add(paramsToken);
        list.add(paramsGroupId);
        RequestNet requestNet = new RequestNet((MyApplication) getActivity().getApplication(), getActivity(), list,
            Urls.GET_GROUP_INFO,getGroupInfoListener, RequestNet.POST);
        return null;
      }
    },true);

  }

}

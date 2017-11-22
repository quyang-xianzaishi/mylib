package com.example.administrator.lubanone.fragment.message;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.administrator.lubanone.R;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.*;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation.ConversationType;

/**
 * Created by Administrator on 2017\8\5 0005.
 */

public class CustomFragment extends Fragment {

  private LinearLayout conversationLinear;
  private io.rong.imkit.fragment.ConversationFragment mConversationFragment;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_custom, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    conversationLinear = (LinearLayout) getActivity().findViewById(R.id.fragment_conversation);

    addRongConversation();

  }

  private void addRongConversation(){

    if (mConversationFragment == null) {
      mConversationFragment = new io.rong.imkit.fragment.ConversationFragment();
    }
    //添加融云会话
    CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
    CSCustomServiceInfo csInfo = csBuilder.nickName(getActivity().getString(R.string.rongyun_nickname)).build();
    Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
        .appendPath("conversation")
        .appendPath(ConversationType.CUSTOMER_SERVICE.getName().toLowerCase())
        .appendQueryParameter("targetId", "KEFU150106338381419")
        .appendQueryParameter("title", "")
        .build();
    mConversationFragment.setUri(uri);
    if(!mConversationFragment.isAdded()){
      getChildFragmentManager().beginTransaction().add(R.id.fragment_conversation,
          mConversationFragment).commit();
    }
    //RongIM.getInstance().getConversationList();

  }

}

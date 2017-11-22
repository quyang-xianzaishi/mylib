package com.example.administrator.lubanone.activity.message;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.message.SearchFriendBean;
import com.example.administrator.lubanone.bean.message.SearchResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\8\31 0031.
 */

public class SearchFriendActivity extends BaseActivity {

  private EditText searchEdit;
  private TextView cancle;
  private ImageView clear;
  private TextView searchNoUser;
  private LinearLayout search;
  private TextView searchText;

  private List<SearchFriendBean> mSearchFriendBeanList;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_search_friend;
  }

  @Override
  public void initView() {
    searchEdit = (EditText) this.findViewById(R.id.search_edit);
    cancle = (TextView) this.findViewById(R.id.cancle);
    clear = (ImageView) this.findViewById(R.id.edit_clear);
    searchNoUser = (TextView) this.findViewById(R.id.search_no_user);
    search = (LinearLayout) this.findViewById(R.id.search);
    searchText = (TextView) this.findViewById(R.id.search_text);

    cancle.setOnClickListener(this);
    clear.setOnClickListener(this);
    search.setOnClickListener(this);
    searchEdit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
       if(s!=null&&s.length()>0){
         clear.setVisibility(View.VISIBLE);
         searchText.setText(s);
         search.setVisibility(View.VISIBLE);
         searchNoUser.setVisibility(View.GONE);
       }else {
         clear.setVisibility(View.GONE);
         search.setVisibility(View.GONE);
         searchNoUser.setVisibility(View.GONE);
       }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });


  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.cancle:
        this.finish();
        break;
      case R.id.edit_clear:
        searchEdit.setText("");
        break;
      case R.id.search:
        String str = "";
        if(searchEdit.getText()!=null){
          str = searchEdit.getText().toString().trim();
        }
        if(str!=null&&str.length()>0){
          searchFriend(str);
        }
        break;
      default:
        break;
    }
  }

  private RequestListener searchFriendListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<SearchResultBean> result = GsonUtil.processJson(jsonObject, SearchResultBean.class);
        if(result!=null){
          if(result.getResult()!=null){
            search.setVisibility(View.GONE);
            mSearchFriendBeanList = new ArrayList<>();
            for(int i = 0;i<result.getResult().getList().size();i++){
              mSearchFriendBeanList.add(new SearchFriendBean(
                  result.getResult().getList().get(i).getUserid(),
                  result.getResult().getList().get(i).getUsername(),
                  result.getResult().getList().get(i).getUserimg()));
            }
            Intent intent = new Intent();
            intent.putExtra("userId",mSearchFriendBeanList.get(0).getUserid());
            intent.setClass(SearchFriendActivity.this,MemberInfoActivity.class);
            SearchFriendActivity.this.startActivity(intent);
          }else {
            search.setVisibility(View.GONE);
            searchNoUser.setVisibility(View.VISIBLE);
          }
        }else {
          search.setVisibility(View.GONE);
          searchNoUser.setVisibility(View.VISIBLE);
        }
      } catch (Exception e) {
        ToastUtil.showShort(getResources().getString(R.string.search_user_fail),
            getApplicationContext());
      }

    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(getResources().getString(R.string.search_user_fail),
          getApplicationContext());
    }
  };


  private void searchFriend(String str){
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsUserId = new RequestParams("searchname", str);
    list.add(paramsToken);
    list.add(paramsUserId);
    RequestNet requestNet = new RequestNet((MyApplication)getApplication(), this, list,
        Urls.SEARCH_FRIEND, searchFriendListener, RequestNet.POST);
  }

}

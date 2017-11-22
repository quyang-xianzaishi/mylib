package com.example.administrator.lubanone.activity.message;

import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.message.RecommendFriendAdapter;
import com.example.administrator.lubanone.bean.RecommendFriendListResultBean;
import com.example.administrator.lubanone.bean.message.RecommendFriendBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableImageView;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\8\8 0008.
 */

public class RecommendFriendListActivity extends BaseActivity {

  private TextView back;
  private TextView title;
  private PullToRefreshLayout recommendFriendListRefresh;
  private PullableListView recommendFriendList;
  private PullToRefreshLayout noData;
  private PullableImageView loadingFail;
  private List<RecommendFriendBean> mList;
  private RecommendFriendAdapter mRecommendFriendAdapter;
  private int page = 1;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_recommend_friend_list;
  }

  @Override
  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    recommendFriendListRefresh = (PullToRefreshLayout) this.findViewById(R.id.recommend_friend_refresh);
    recommendFriendList = (PullableListView) this.findViewById(R.id.recommend_friend_list);
    noData = (PullToRefreshLayout) this.findViewById(R.id.no_data);
    loadingFail = (PullableImageView) this.findViewById(R.id.no_data_image);
    title.setText(getString(R.string.recommend_friend_list));

    back.setOnClickListener(this);
    recommendFriendListRefresh.setOnPullListener(new MyRefreshListener());
    noData.setOnPullListener(new MyRefreshListener());
    recommendFriendList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    recommendFriendListRefresh.setPullUpEnable(true);//设置是否让上拉加载

    mList = new ArrayList<>();
    //initData();
  }

  private void initData(){
    mList.add(new RecommendFriendBean("好友推荐","2017/8/8 13:00:45","    尊敬的会员，您的好友XXX向您推荐了好友XXX。","蘅暮","60"));
    mList.add(new RecommendFriendBean("好友推荐","2017/4/8 13:45:45","    尊敬的会员，您的好友夏天向您推荐了好友XXX。","mumu","61"));
    mList.add(new RecommendFriendBean("好友推荐","2017/5/8 13:30:45","    尊敬的会员，您的好友木木向您推荐了好友XXX。","墨明棋妙","62"));
    mList.add(new RecommendFriendBean("好友推荐","2017/6/8 13:10:45","    尊敬的会员，您的好友穆穆向您推荐了好友XXX。","河图","63"));
    mRecommendFriendAdapter = new RecommendFriendAdapter(this,mList);
    recommendFriendList.setAdapter(mRecommendFriendAdapter);

  }

  @Override
  public void loadData() {
    mRecommendFriendAdapter = new RecommendFriendAdapter(this,mList);
    recommendFriendList.setAdapter(mRecommendFriendAdapter);
    getList();

  }

  @Override
  protected void onResume() {
    page = 1;
    mList.clear();
    super.onResume();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.activity_back:
        RecommendFriendListActivity.this.finish();
        break;
      default:
        break;
    }

  }

  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      mList = new ArrayList<>();
      //initData();
      page = 1;
      loadData();
      noData.refreshFinish(PullToRefreshLayout.SUCCEED);
      recommendFriendListRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mRecommendFriendAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      //initData();
      loadData();
      noData.refreshFinish(PullToRefreshLayout.SUCCEED);
      recommendFriendListRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mRecommendFriendAdapter.notifyDataSetChanged();

    }

  }

  private RequestListener getRecommendFriendListListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<RecommendFriendListResultBean> result = GsonUtil
            .processJson(jsonObject, RecommendFriendListResultBean.class);
        if(result!=null&&ResultUtil.isSuccess(result)) {
          if (result.getResult() != null) {
            if(result.getResult().getData()!=null&&result.getResult().getData().size()>0){
              for(int i = 0;i<result.getResult().getData().size();i++) {
                mList.add(new RecommendFriendBean(
                    result.getResult().getData().get(i).getTitle(),
                    result.getResult().getData().get(i).getTime(),
                    result.getResult().getData().get(i).getContent(),
                    result.getResult().getData().get(i).getBerecommendname(),
                    result.getResult().getData().get(i).getBerecommendid()));
              }
              mRecommendFriendAdapter.notifyDataSetChanged();
              page++;
              recommendFriendListRefresh.setVisibility(View.VISIBLE);
              noData.setVisibility(View.GONE);
              if (result.getResult().getData().size() < 10) {
                recommendFriendListRefresh.setPullUpEnable(false);
              } else {
                recommendFriendListRefresh.setPullUpEnable(true);
              }
            }else {
              if(page>1){
                ToastUtil.showShort(getResources().getString(R.string.no_more_message),
                    RecommendFriendListActivity.this);
              }else {
                recommendFriendListRefresh.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                loadingFail.setImageDrawable(RecommendFriendListActivity.this.
                    getResources().getDrawable(R.drawable.no_data));
              }
            }
          }else {
            if(page>1){
              ToastUtil.showShort(getResources().getString(R.string.no_more_message),
                  RecommendFriendListActivity.this);
            }else {
              recommendFriendListRefresh.setVisibility(View.GONE);
              noData.setVisibility(View.VISIBLE);
              loadingFail.setImageDrawable(RecommendFriendListActivity.this.
                  getResources().getDrawable(R.drawable.no_data));
            }
          }
        }else {
          //接口异常
          Toast.makeText(RecommendFriendListActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
              getString(R.string.get_recommend_friend_list_fail)),Toast.LENGTH_LONG).show();
          recommendFriendListRefresh.setVisibility(View.GONE);
          noData.setVisibility(View.VISIBLE);
          loadingFail.setImageDrawable(RecommendFriendListActivity.this.
              getResources().getDrawable(R.drawable.loading_fail));
        }
      } catch (Exception e) {
        ToastUtil.showShort(getResources().getString(R.string.get_recommend_friend_list_fail),
            RecommendFriendListActivity.this);
        recommendFriendListRefresh.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
        loadingFail.setImageDrawable(RecommendFriendListActivity.this.
            getResources().getDrawable(R.drawable.loading_fail));
      }

    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(getResources().getString(R.string.get_recommend_friend_list_fail),
          RecommendFriendListActivity.this);
      recommendFriendListRefresh.setVisibility(View.GONE);
      noData.setVisibility(View.VISIBLE);
      loadingFail.setImageDrawable(RecommendFriendListActivity.this.
          getResources().getDrawable(R.drawable.loading_fail));
    }
  };

  private void getList(){
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsPage = new RequestParams("page",Integer.toString(page));
    list.add(paramsToken);
    list.add(paramsPage);
    RequestNet requestNet = new RequestNet((MyApplication)getApplication(), RecommendFriendListActivity.this, list,
        Urls.RECOMMEND_FRIEND_LIST, getRecommendFriendListListener, RequestNet.POST);
  }
}

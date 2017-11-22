package com.example.administrator.lubanone.activity.message;

import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.message.NewFriendAdapter;
import com.example.administrator.lubanone.bean.message.NewFriendBean;
import com.example.administrator.lubanone.bean.message.NewFriendResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.qlibrary.entity.Result;
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
 * Created by Administrator on 2017\7\31 0031.
 */

public class NewFriendActivity extends BaseActivity{

  private TextView back;
  private PullToRefreshLayout newFriendRefresh;
  private PullableListView newFriendList;
  private PullToRefreshLayout noData;
  private PullableImageView loadingFail;
  private List<NewFriendBean> mList;
  private NewFriendAdapter mNewFriendAdapter;
  private int page = 1;
  private Boolean isLoad = false;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_new_friend;
  }

  @Override
  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    back.setOnClickListener(this);

    newFriendRefresh = (PullToRefreshLayout) this.findViewById(R.id.new_friend_refresh);
    newFriendList = (PullableListView) this.findViewById(R.id.new_friend_list);
    noData = (PullToRefreshLayout) this.findViewById(R.id.no_data);
    loadingFail = (PullableImageView) this.findViewById(R.id.no_data_image);

    back.setOnClickListener(this);

    newFriendRefresh.setOnPullListener(new MyRefreshListener());
    noData.setOnPullListener(new MyRefreshListener());
    newFriendList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    newFriendRefresh.setPullUpEnable(false);//设置是否让上拉加载

    mList = new ArrayList<>();
    mNewFriendAdapter = new NewFriendAdapter(this, mList);

  }

  @Override
  public void loadData() {
   /* mList.clear();
    mList.add(new NewFriendBean("11","http://file.juzimi.com/weibopic/jazami.jpg",
        "浅笑嫣然","我是浅笑嫣然","1"));
    mList.add(new NewFriendBean("12","http://file.juzimi.com/weibopic/jazami.jpg",
        "墨明棋妙","我是墨明棋妙","2"));
    mList.add(new NewFriendBean("13","http://file.juzimi.com/weibopic/jazami.jpg",
        "花开不记年","我是花开不记年","2"));
    mList.add(new NewFriendBean("14","http://file.juzimi.com/weibopic/jazami.jpg",
        "蘅暮","我是蘅暮","2"));
    mList.add(new NewFriendBean("15","http://file.juzimi.com/weibopic/jazami.jpg",
        "白浅","我是白浅","1"));*/
   getNewFriendList();


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
        NewFriendActivity.this.finish();
        break;
      default:
        break;
    }
  }

  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      mList.clear();
      page = 1;
      loadData();
      newFriendRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      noData.refreshFinish(PullToRefreshLayout.SUCCEED);
      mNewFriendAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      isLoad = true;
      loadData();
      newFriendRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      noData.refreshFinish(PullToRefreshLayout.SUCCEED);
      mNewFriendAdapter.notifyDataSetChanged();

    }
  }

  private RequestListener getNewFriendListListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<NewFriendResultBean> result = GsonUtil.processJson(jsonObject, NewFriendResultBean.class);
        if(result!=null) {
          if (result.getResult() != null) {
            if(result.getResult().getData()!=null&&result.getResult().getData().size()>0){
              for(int i = 0;i<result.getResult().getData().size();i++){
                mList.add(new NewFriendBean(
                    result.getResult().getData().get(i).getUserid(),
                    result.getResult().getData().get(i).getUserimg(),
                    result.getResult().getData().get(i).getUsername(),
                    result.getResult().getData().get(i).getApplymess(),
                    result.getResult().getData().get(i).getZt()));
                newFriendList.setAdapter(mNewFriendAdapter);
                mNewFriendAdapter.notifyDataSetChanged();
                newFriendRefresh.setVisibility(View.VISIBLE);
                if(result.getResult().getData().size()<10){
                  newFriendRefresh.setPullUpEnable(false);
                }else {
                  newFriendRefresh.setPullUpEnable(true);
                }
                noData.setVisibility(View.GONE);
              }
              page++;
            }else {
              newFriendRefresh.setVisibility(View.GONE);
              noData.setVisibility(View.VISIBLE);
              loadingFail.setImageDrawable(NewFriendActivity.this.
                  getResources().getDrawable(R.drawable.no_data));
            }
          }else {
            if(page>1){
              ToastUtil.showShort(getResources().getString(R.string.no_more_message),
                getApplicationContext());
              isLoad = false;
            }else {
              newFriendRefresh.setVisibility(View.GONE);
              noData.setVisibility(View.VISIBLE);
              loadingFail.setImageDrawable(NewFriendActivity.this.
                  getResources().getDrawable(R.drawable.no_data));
            }
          }
        }
      } catch (Exception e) {
        ToastUtil.showShort(getResources().getString(R.string.get_friend_apply_fail),
            getApplicationContext());
        newFriendRefresh.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
        loadingFail.setImageDrawable(NewFriendActivity.this.
            getResources().getDrawable(R.drawable.loading_fail));
      }

    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(getResources().getString(R.string.get_friend_apply_fail),
          getApplicationContext());
      newFriendRefresh.setVisibility(View.GONE);
      noData.setVisibility(View.VISIBLE);
      loadingFail.setImageDrawable(NewFriendActivity.this.
          getResources().getDrawable(R.drawable.loading_fail));
    }
  };

  private void getNewFriendList(){
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsPage = new RequestParams("page", Integer.toString(page));
    list.add(paramsToken);
    list.add(paramsPage);
    RequestNet requestNet = new RequestNet((MyApplication)getApplication(), this, list,
        Urls.GET_FRIEND_APPLY_LIST, getNewFriendListListener, RequestNet.POST);

  }
}

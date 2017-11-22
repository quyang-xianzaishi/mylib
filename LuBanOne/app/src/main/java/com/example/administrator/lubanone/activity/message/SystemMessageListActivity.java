package com.example.administrator.lubanone.activity.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.adapter.message.SystemMessageListAdapter;
import com.example.administrator.lubanone.bean.message.SystemMessageBean;
import com.example.administrator.lubanone.bean.message.SystemMessageListBean;
import com.example.administrator.lubanone.bean.message.SystemMessageListResultBean;
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
 * Created by Administrator on 2017\7\5 0005.
 */

public class SystemMessageListActivity extends AppCompatActivity implements View.OnClickListener {

  private TextView back;
  private TextView title;
  private PullToRefreshLayout systemMessageListRefresh;
  private PullToRefreshLayout noData;
  private PullableImageView loadingFail;
  private PullableListView systemMessageList;
  private List<SystemMessageListBean> mList;
  private SystemMessageListAdapter mSystemMessageListAdapter;
  private int page = 1;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Window window = this.getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    setContentView(R.layout.activity_system_message_list);
    initView();
  }

  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    noData = (PullToRefreshLayout) this.findViewById(R.id.no_data);
    loadingFail = (PullableImageView) this.findViewById(R.id.no_data_image);
    systemMessageListRefresh = (PullToRefreshLayout) this.findViewById(R.id.system_message_refresh);
    systemMessageList = (PullableListView) this.findViewById(R.id.system_message_list);
    title.setText(getString(R.string.system_message));

    back.setOnClickListener(this);
    systemMessageListRefresh.setOnPullListener(new MyRefreshListener());
    noData.setOnPullListener(new MyRefreshListener());
    systemMessageList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    systemMessageListRefresh.setPullUpEnable(true);//设置是否让上拉加载

    mList = new ArrayList<>();
    mSystemMessageListAdapter = new SystemMessageListAdapter(this, mList);
    systemMessageList.setAdapter(mSystemMessageListAdapter);
    initData();

  }

  private void initData(){
    getSystemMessageList();
  }


  @Override
  public void onClick(View v) {
      switch (v.getId()){
        case R.id.activity_back:
          SystemMessageListActivity.this.finish();
          break;
        default:
          break;
      }
  }

  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      mList = new ArrayList<>();
      page = 1;
      initData();
      systemMessageListRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      noData.refreshFinish(PullToRefreshLayout.SUCCEED);
      mSystemMessageListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      systemMessageListRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      noData.refreshFinish(PullToRefreshLayout.SUCCEED);
      mSystemMessageListAdapter.notifyDataSetChanged();

    }

  }

  private RequestListener getSystemMessageListListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<SystemMessageListResultBean> result = GsonUtil
            .processJson(jsonObject, SystemMessageListResultBean.class);
        if(result!=null&& ResultUtil.isSuccess(result)) {
          if (result.getResult() != null) {
            if(result.getResult().getData()!=null&&result.getResult().getData().size()>0){
              for(int i = 0;i<result.getResult().getData().size();i++) {
                mList.add(new SystemMessageListBean(
                    result.getResult().getData().get(i).getUserid(),
                    result.getResult().getData().get(i).getUsername(),
                    result.getResult().getData().get(i).getTitle(),
                    result.getResult().getData().get(i).getContent(),
                    result.getResult().getData().get(i).getTime()));
              }
              mSystemMessageListAdapter.notifyDataSetChanged();
              page++;
              systemMessageListRefresh.setVisibility(View.VISIBLE);
              noData.setVisibility(View.GONE);
              if (result.getResult().getData().size() < 10) {
                systemMessageListRefresh.setPullUpEnable(false);
              } else {
                systemMessageListRefresh.setPullUpEnable(true);
              }
            }else {
              if(page>1){
                ToastUtil.showShort(getResources().getString(R.string.no_more_message),
                    SystemMessageListActivity.this);
              }else {
                systemMessageListRefresh.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                loadingFail.setImageDrawable(SystemMessageListActivity.this.
                    getResources().getDrawable(R.drawable.no_data));
              }
            }
          }else {
            if(page>1){
              ToastUtil.showShort(getResources().getString(R.string.no_more_message),
                  SystemMessageListActivity.this);
            }else {
              systemMessageListRefresh.setVisibility(View.GONE);
              noData.setVisibility(View.VISIBLE);
              loadingFail.setImageDrawable(SystemMessageListActivity.this.
                  getResources().getDrawable(R.drawable.no_data));
            }
          }
        }else {
          //接口异常
          Toast.makeText(SystemMessageListActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
              getString(R.string.get_system_message_list_fail)), Toast.LENGTH_LONG).show();
          systemMessageListRefresh.setVisibility(View.GONE);
          noData.setVisibility(View.VISIBLE);
          loadingFail.setImageDrawable(SystemMessageListActivity.this.
              getResources().getDrawable(R.drawable.loading_fail));
        }
      } catch (Exception e) {
        ToastUtil.showShort(getResources().getString(R.string.get_system_message_list_fail),
            SystemMessageListActivity.this);
        systemMessageListRefresh.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
        loadingFail.setImageDrawable(SystemMessageListActivity.this.
            getResources().getDrawable(R.drawable.loading_fail));
      }

    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(getResources().getString(R.string.get_system_message_list_fail),
          SystemMessageListActivity.this);
      systemMessageListRefresh.setVisibility(View.GONE);
      noData.setVisibility(View.VISIBLE);
      loadingFail.setImageDrawable(SystemMessageListActivity.this.
          getResources().getDrawable(R.drawable.loading_fail));
    }
  };

  private void getSystemMessageList(){
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsPage = new RequestParams("page",Integer.toString(page));
    list.add(paramsToken);
    list.add(paramsPage);
    RequestNet requestNet = new RequestNet((MyApplication)getApplication(), SystemMessageListActivity.this, list,
        Urls.SYSTEM_GET, getSystemMessageListListener, RequestNet.POST);

  }

}

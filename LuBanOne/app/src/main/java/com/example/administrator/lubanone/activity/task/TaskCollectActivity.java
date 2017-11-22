package com.example.administrator.lubanone.activity.task;

import android.content.Intent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.task.TaskCollectLvAdapter;
import com.example.administrator.lubanone.bean.TaskCollectBean;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.qlibrary.utils.SPUtils;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableImageView;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2017/6/24.
 */
public class TaskCollectActivity extends BaseActivity {

  private LinearLayout backBtn;
  private PullToRefreshLayout noDataLayout;
  private PullToRefreshLayout mPullToRefreshLayout;
  private PullableListView mPullableListView;
  private PullableImageView noDataImage;
  private TaskCollectLvAdapter mAdapter;
  private List<TaskCollectBean.TaskCollectList> myTasks;
  private int pageNo = 1;             //请求页数

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_task_collect;
  }

  @Override
  public void initView() {
    noDataImage = (PullableImageView) findViewById(R.id.task_collect_no_data_image);
    noDataLayout = (PullToRefreshLayout) findViewById(R.id.task_collect_no_data);
    backBtn = (LinearLayout) findViewById(R.id.task_collect_back_icon);
    mPullableListView = (PullableListView) findViewById(R.id.task_collect_refresh_listview);
    mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.task_collect_refresh_layout);

    backBtn.setOnClickListener(this);

    MyRefreshListener myRefreshListener = new MyRefreshListener();//拖动监听
    mPullToRefreshLayout.setOnPullListener(myRefreshListener);
    noDataLayout.setOnPullListener(myRefreshListener);
    noDataLayout.setPullUpEnable(false);
    mPullToRefreshLayout.setPullUpEnable(true);//设置是否让上拉加载
    //设置下拉动画
    //pullableListView = (PullableListView) RefreshSetting.setListProcessRefresh(context,pullToRefreshLayout);
    //设置摩擦力，减慢滑动速度
    mPullableListView.setFriction(ViewConfiguration.getScrollFriction() * 20);
    //pullableListView.setBackgroundColor(Color.WHITE);

    myTasks = new ArrayList<>();
    mAdapter = new TaskCollectLvAdapter(mContext, myTasks);
    mPullableListView.setAdapter(mAdapter);

    //listview item 事件监听
    mPullableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, TaskDetailsActivity.class);
        intent.putExtra("if_id", myTasks.get(position).getIf_id());
        startActivity(intent);
      }
    });

  }

  @Override
  protected void onResume() {
    super.onResume();
    getListData(mPullToRefreshLayout);
  }

  private void getListData(final PullToRefreshLayout refreshLayout) {
    Subscriber subscriber = new MySubscriber<TaskCollectBean>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        noDataLayout.setVisibility(View.VISIBLE);
        refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        HouLog.d(TAG + "任务列表onError", e.toString());
      }

      @Override
      public void onNext(TaskCollectBean tasksBeanList) {
        noDataLayout.setVisibility(View.GONE);
        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        if (pageNo == 1) {
          myTasks.clear();
        }

        if (tasksBeanList != null && tasksBeanList.getTasklist() != null
            && tasksBeanList.getTasklist().size() > 0) {
          myTasks.addAll(tasksBeanList.getTasklist());
          HouLog.d(TAG, "返回数据个数: " + tasksBeanList.getTasklist().size());
          HouLog.d(TAG, "列表数据个数: " + myTasks.size());
        } else {
          if (pageNo > 1) {
            HouToast.showLongToast(TaskCollectActivity.this,getInfo(R.string.no_more_message));
            pageNo--;
          } else {
            noDataImage.setImageResource(R.drawable.no_data);
            noDataLayout.setVisibility(View.VISIBLE);
          }
        }
        mAdapter.notifyDataSetChanged();
        HouLog.d(TAG + "页数：", String.valueOf(pageNo));
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token",
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("page", String.valueOf(pageNo));
    HouLog.d(TAG, "任务列表请求参数:" + params.toString());
    MyApplication.rxNetUtils.getTaskService().getTaskCollectList(params)
        .map(new BaseModelFunc<TaskCollectBean>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  /**
   * 下拉刷新监听类
   */
  public class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      pageNo = 1;
      getListData(pullToRefreshLayout);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      pageNo++;
      getListData(pullToRefreshLayout);
    }
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.task_collect_back_icon:
        finish();
//        myApp.removeAllActivity();
        break;
      default:
        break;
    }
  }
}

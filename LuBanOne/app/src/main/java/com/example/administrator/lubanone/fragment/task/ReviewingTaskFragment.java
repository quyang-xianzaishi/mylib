package com.example.administrator.lubanone.fragment.task;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.task.TaskDetailsActivity;
import com.example.administrator.lubanone.adapter.task.TaskCommonListAdapter;
import com.example.administrator.lubanone.bean.model.TaskModel;
import com.example.administrator.lubanone.fragment.BaseFragment;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.qlibrary.utils.SPUtils;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullToRefreshLayout.OnPullListener;
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
 * Created by hou on 2017/8/24.
 */

public class ReviewingTaskFragment extends BaseFragment {

  private static final String TAG = "ReviewingTaskFragment";

  private PullToRefreshLayout noDataLayout, listLayout;
  private PullableImageView noDataImage;
  private PullableListView mListView;
  private TaskCommonListAdapter mAdapter;
  private List<TaskModel.TaskList> datas;
  private int pageNo = 1;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_task_common, null);
    noDataImage = (PullableImageView) view.findViewById(R.id.task_common_no_data_image);
    noDataLayout = (PullToRefreshLayout) view.findViewById(R.id.task_common_no_data_layout);
    listLayout = (PullToRefreshLayout) view.findViewById(R.id.task_common_list_layout);
    mListView = (PullableListView) view.findViewById(R.id.task_common_list_view);

    MyRefreshListener myRefreshListener = new MyRefreshListener();
    noDataLayout.setOnPullListener(myRefreshListener);
    noDataLayout.setPullUpEnable(false);
    listLayout.setOnPullListener(myRefreshListener);
    listLayout.setPullDownEnable(true);
    listLayout.setPullUpEnable(true);

    datas = new ArrayList<>();
    mAdapter = new TaskCommonListAdapter(mActivity, datas);
    mListView.setAdapter(mAdapter);

    mListView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), TaskDetailsActivity.class);
        intent.putExtra("if_id", datas.get(position).getTaskid());
        startActivity(intent);
      }
    });

    return view;
  }

  class MyRefreshListener implements OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      pageNo = 1;
      getDataFromServer(pullToRefreshLayout);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      pageNo++;
      getDataFromServer(pullToRefreshLayout);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    pageNo = 1;
    getDataFromServer(listLayout);
  }

  private void getDataFromServer(final PullToRefreshLayout refreshLayout) {
    Subscriber subscriber = new MySubscriber<TaskModel>(getActivity()) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        noDataLayout.setVisibility(View.VISIBLE);
        noDataImage.setImageResource(R.drawable.loading_fail);
        refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        HouLog.d(TAG + "审核中任务列表onError", e.toString());
      }

      @Override
      public void onNext(TaskModel taskModel) {
        noDataLayout.setVisibility(View.GONE);
        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        if (pageNo == 1) {
          datas.clear();
        }
        if (taskModel != null && taskModel.getTasklist() != null
            && taskModel.getTasklist().size() > 0) {
          datas.addAll(taskModel.getTasklist());
          HouLog.d(TAG, "返回数据个数: " + taskModel.getTasklist().size());
          HouLog.d(TAG, "列表数据个数: " + datas.size());
        } else {
          if (pageNo > 1) {
            HouToast.showLongToast(getActivity(), getInfo(R.string.no_more_message));
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
    params.put("token", SPUtils.getStringValue(mActivity, Config.USER_INFO, Config.TOKEN, ""));
    params.put("page", String.valueOf(pageNo));
    params.put("type", "1");
    HouLog.d(TAG, "任务列表.审核中任务参数：" + params.toString());
    MyApplication.rxNetUtils.getTaskService().getTaskListTwo(params)
        .map(new BaseModelFunc<TaskModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  @Override
  public void initData() {

  }
}

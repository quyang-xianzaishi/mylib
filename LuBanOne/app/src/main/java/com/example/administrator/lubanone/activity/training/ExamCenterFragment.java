package com.example.administrator.lubanone.activity.training;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.training.activity.ExaminationActivity;
import com.example.administrator.lubanone.adapter.training.ExamCenterAdapter;
import com.example.administrator.lubanone.bean.model.TrainTestCenterModel;
import com.example.administrator.lubanone.bean.model.TrainTestLIstModel;
import com.example.administrator.lubanone.fragment.BaseFragment;
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
 * Created by Administrator on 2017\6\22 0022.
 */

public class ExamCenterFragment extends BaseFragment {

  private final static String TAG = "ExamCenterFragment";

  private LinearLayout backBtn;
  private List<TrainTestCenterModel> mList;
  private PullToRefreshLayout examCenterRefresh;
  private PullableListView examCenterList;
  private ExamCenterAdapter mExamCenterAdapter;

  private PullToRefreshLayout noDataLayout;
  private PullableImageView noDataImage;
  private int pageNo = 1;

  private View listViewHeader;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_exam_center, null);

    listViewHeader = mInflater.inflate(R.layout.train_exam_listview_header, null);
    backBtn = (LinearLayout) view.findViewById(R.id.train_test_back_btn);
    examCenterRefresh = (PullToRefreshLayout) view.findViewById(R.id.exam_center_refresh);
    examCenterList = (PullableListView) view.findViewById(R.id.exam_center_list);
    noDataLayout = (PullToRefreshLayout) view.findViewById(R.id.train_exam_center_no_data_layout);
    noDataImage = (PullableImageView) view.findViewById(R.id.train_exam_center_no_data_image);

    backBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        getActivity().finish();
      }
    });
    MyRefreshListener listener = new MyRefreshListener();
    noDataLayout.setOnPullListener(listener);
    examCenterRefresh.setOnPullListener(listener);
    noDataLayout.setPullUpEnable(false);
    examCenterList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    examCenterRefresh.setPullUpEnable(true);//设置是否让上拉加载
    mList = new ArrayList<>();
    mExamCenterAdapter = new ExamCenterAdapter(getActivity(), mList);
    examCenterList.setAdapter(mExamCenterAdapter);
    examCenterList.addHeaderView(listViewHeader);
    examCenterList.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
          Intent intent = new Intent();
          intent.putExtra("testId", mList.get(position - 1).getTestid());
          intent.setClass(getActivity(), ExaminationActivity.class);
          startActivity(intent);
        }
      }
    });
    return view;
  }

  @Override
  public void initData() {

  }

  @Override
  public void onResume() {
    super.onResume();
    pageNo = 1;
    getData(examCenterRefresh);
  }

  private void getData(final PullToRefreshLayout refreshLayout) {
    Subscriber subscriber = new MySubscriber<TrainTestLIstModel>(getActivity()) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        noDataLayout.setVisibility(View.VISIBLE);
        noDataImage.setImageResource(R.drawable.loading_fail);
        HouLog.d(TAG, "考试列表onError" + e.toString());
      }

      @Override
      public void onNext(TrainTestLIstModel trainListDataBaseModel) {
        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        noDataLayout.setVisibility(View.GONE);
        if (trainListDataBaseModel != null) {
          HouLog.d(TAG, trainListDataBaseModel.getList().toString());
          if (pageNo == 1) {
            mList.clear();
          }
          mList.addAll(trainListDataBaseModel.getList());
          mExamCenterAdapter.notifyDataSetChanged();
        } else {
          if (pageNo > 1) {
            HouToast.showLongToast(getActivity(), getInfo(R.string.no_more_message));
            pageNo--;
          } else {
            noDataImage.setImageResource(R.drawable.no_data);
            noDataLayout.setVisibility(View.VISIBLE);
          }
        }
        HouLog.d(TAG + " 页数：", String.valueOf(pageNo));
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("page", String.valueOf(pageNo));
    HouLog.d("试题参数:" + params.toString());
    MyApplication.rxNetUtils.getTrainService().getTestList(params)
        .map(new BaseModelFunc<TrainTestLIstModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }


  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      pageNo = 1;
      getData(examCenterRefresh);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      pageNo++;
      getData(examCenterRefresh);
    }
  }

}

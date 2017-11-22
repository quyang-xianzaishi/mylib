package com.example.administrator.lubanone.activity.training;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.training.activity.TrainDetailsActivity;
import com.example.administrator.lubanone.adapter.training.TrainCenterCommonAdapter;
import com.example.administrator.lubanone.bean.model.TrainCenterCommonModel;
import com.example.administrator.lubanone.bean.model.TrainDataModel;
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
 * Created by Administrator on 2017\6\24 0024.
 */

public class MyJoinTrainingFragment extends BaseFragment {

  private final static String TAG = "MyJoinTrainingFragment";
  private PullToRefreshLayout noDataLayout;
  private PullableImageView noDataImage;
  private PullToRefreshLayout mPullToRefreshLayout;
  private PullableListView mPullableListView;
  private List<TrainCenterCommonModel> trainDatas;
  private TrainCenterCommonAdapter adapter;
  MyRefreshListener listener = new MyRefreshListener();
  private int pageNo = 1;

  @Override
  public View initView() {

    View view = mInflater.inflate(R.layout.fragment_train_center_common, null);
    mPullToRefreshLayout = (PullToRefreshLayout) view
        .findViewById(R.id.train_center_common_pull_layout);
    mPullableListView = (PullableListView) view.findViewById(R.id.train_center_common_list_view);
    noDataLayout = (PullToRefreshLayout) view.findViewById(R.id.train_center_common_no_data_layout);
    noDataImage = (PullableImageView) view.findViewById(R.id.train_center_common_no_data_image);
    noDataLayout.setPullUpEnable(false);
    noDataLayout.setOnPullListener(listener);
    mPullToRefreshLayout.setOnPullListener(listener);
    trainDatas = new ArrayList<>();
    adapter = new TrainCenterCommonAdapter(trainDatas, getActivity(), false);
    mPullableListView.setAdapter(adapter);
    mPullableListView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), TrainDetailsActivity.class);
        intent.putExtra("trainId", trainDatas.get(position).getTrainid());
        intent.putExtra("theme", trainDatas.get(position).getTheme());
        intent.putExtra("imageUrl", trainDatas.get(position).getImage());
        startActivity(intent);
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
    getData(mPullToRefreshLayout);
  }

  private class MyRefreshListener implements OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      pageNo = 1;
      getData(pullToRefreshLayout);

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      pageNo++;
      getData(pullToRefreshLayout);
    }
  }

  private void getData(final PullToRefreshLayout refreshLayout) {
    Subscriber subscriber = new MySubscriber<TrainDataModel>(getActivity()) {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        noDataLayout.setVisibility(View.VISIBLE);
        noDataImage.setImageResource(R.drawable.loading_fail);
        HouLog.d(TAG, "我参加的培训列表onError" + e.toString());
      }

      @Override
      public void onNext(TrainDataModel trainDataModel) {
        HouLog.d("onNext");
        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        noDataLayout.setVisibility(View.GONE);
        if (trainDataModel != null) {
          HouLog.d(TAG, trainDataModel.getList().toString());
          if (pageNo == 1) {
            trainDatas.clear();
          }
          trainDatas.addAll(trainDataModel.getList());
          adapter.notifyDataSetChanged();
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
    HouLog.d(TAG + "我参加的培训参数:", params.toString());
    MyApplication.rxNetUtils.getTrainService()
        .getJoinTrain(params)
        .map(new BaseModelFunc<TrainDataModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }
}

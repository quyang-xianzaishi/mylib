package com.example.administrator.lubanone.activity.message;

import android.content.Intent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.train.UploadTrainActivity;
import com.example.administrator.lubanone.activity.training.activity.TrainUploadActivity;
import com.example.administrator.lubanone.adapter.message.TrainMessageAdapter;
import com.example.administrator.lubanone.bean.message.TrainMessageBean;
import com.example.administrator.lubanone.bean.message.TrainMessageResultBean;
import com.example.administrator.lubanone.bean.model.TrainVipGrade;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017\8\9 0009.
 */

public class TrainMessageActivity extends BaseActivity {

  private TextView back;
  private TextView title;
  private PullToRefreshLayout noData;
  private PullableImageView loadingFail;
  private PullToRefreshLayout trainMessageRefresh;
  private PullableListView trainMessageList;
  private List<TrainMessageBean> mList;
  private TrainMessageAdapter mTrainMessageAdapter;

  private int page = 1;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_train_message;
  }

  @Override
  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    noData = (PullToRefreshLayout) this.findViewById(R.id.no_data);
    loadingFail = (PullableImageView) this.findViewById(R.id.no_data_image);
    trainMessageRefresh = (PullToRefreshLayout) this.findViewById(R.id.train_message_refresh);
    trainMessageList = (PullableListView) this.findViewById(R.id.train_message_list);
    title.setText(getString(R.string.train_message));

    back.setOnClickListener(this);

    trainMessageRefresh.setOnPullListener(new MyRefreshListener());
    noData.setOnPullListener(new MyRefreshListener());
    trainMessageList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    trainMessageRefresh.setPullUpEnable(true);//设置是否让上拉加载

    mList = new ArrayList<>();
    mTrainMessageAdapter = new TrainMessageAdapter(this, mList);
    trainMessageList.setAdapter(mTrainMessageAdapter);

  }

  @Override
  public void loadData() {
    getTrainMessageList();

  }

  @Override
  protected void onResume() {
    page = 1;
    mList.clear();
    super.onResume();
  }

  @Override
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.activity_back:
        TrainMessageActivity.this.finish();
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
      loadData();
      noData.refreshFinish(PullToRefreshLayout.SUCCEED);
      trainMessageRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainMessageAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      loadData();
      noData.refreshFinish(PullToRefreshLayout.SUCCEED);
      trainMessageRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainMessageAdapter.notifyDataSetChanged();

    }

  }

  private RequestListener getTrainMessageListListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<TrainMessageResultBean> result = GsonUtil
            .processJson(jsonObject, TrainMessageResultBean.class);
        if (result != null && ResultUtil.isSuccess(result)) {
          if (result.getResult() != null) {
            if (result.getResult().getData() != null && result.getResult().getData().size() > 0) {
              for (int i = 0; i < result.getResult().getData().size(); i++) {
                mList.add(new TrainMessageBean(
                    result.getResult().getData().get(i).getUserid(),
                    result.getResult().getData().get(i).getUsername(),
                    result.getResult().getData().get(i).getTitle(),
                    result.getResult().getData().get(i).getTime(),
                    result.getResult().getData().get(i).getContent(),
                    result.getResult().getData().get(i).getTraintheme(),
                    result.getResult().getData().get(i).getUploadtime(),
                    result.getResult().getData().get(i).getObtainintegration(),
                    result.getResult().getData().get(i).getType(),
                    result.getResult().getData().get(i).getTrainid(),
                    result.getResult().getData().get(i).getTrainimg()));
              }
              mTrainMessageAdapter.notifyDataSetChanged();
              page++;
              trainMessageRefresh.setVisibility(View.VISIBLE);
              noData.setVisibility(View.GONE);
              if (result.getResult().getData().size() < 10) {
                trainMessageRefresh.setPullUpEnable(false);
              } else {
                trainMessageRefresh.setPullUpEnable(true);
              }
            } else {
              if (page > 1) {
                ToastUtil.showShort(getResources().getString(R.string.no_more_message),
                    TrainMessageActivity.this);
              } else {
                trainMessageRefresh.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                loadingFail.setImageDrawable(TrainMessageActivity.this.
                    getResources().getDrawable(R.drawable.no_data));
              }
            }
          } else {
            if (page > 1) {
              ToastUtil.showShort(getResources().getString(R.string.no_more_message),
                  TrainMessageActivity.this);
            } else {
              trainMessageRefresh.setVisibility(View.GONE);
              noData.setVisibility(View.VISIBLE);
              loadingFail.setImageDrawable(TrainMessageActivity.this.
                  getResources().getDrawable(R.drawable.no_data));
            }
          }
        } else {
          //接口异常
          Toast.makeText(TrainMessageActivity.this,
              DebugUtils.convert(ResultUtil.getErrorMsg(result),
                  getString(R.string.get_train_message_list_fail)), Toast.LENGTH_LONG).show();
          trainMessageRefresh.setVisibility(View.GONE);
          noData.setVisibility(View.VISIBLE);
          loadingFail.setImageDrawable(TrainMessageActivity.this.
              getResources().getDrawable(R.drawable.loading_fail));
        }
      } catch (Exception e) {
        ToastUtil.showShort(getResources().getString(R.string.get_train_message_list_fail),
            TrainMessageActivity.this);
        trainMessageRefresh.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
        loadingFail.setImageDrawable(TrainMessageActivity.this.
            getResources().getDrawable(R.drawable.loading_fail));
      }

    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(getResources().getString(R.string.get_train_message_list_fail),
          TrainMessageActivity.this);
      trainMessageRefresh.setVisibility(View.GONE);
      noData.setVisibility(View.VISIBLE);
      loadingFail.setImageDrawable(TrainMessageActivity.this.
          getResources().getDrawable(R.drawable.loading_fail));
    }
  };

  private void getTrainMessageList() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsPage = new RequestParams("page", Integer.toString(page));
    list.add(paramsToken);
    list.add(paramsPage);
    RequestNet requestNet = new RequestNet((MyApplication) getApplication(),
        TrainMessageActivity.this, list,
        Urls.GET_TRAIN_MESSAGE_LIST, getTrainMessageListListener, RequestNet.POST);

  }

  /**
   * 获取会员等级
   */
  private void getVipGrade() {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<TrainVipGrade>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
        HouLog.d(TAG, "获取会员等级onError: " + e.toString());
      }

      @Override
      public void onNext(TrainVipGrade trainVipGrade) {
        dismissLoadingDialog();
        if (trainVipGrade != null) {
          if (Integer.parseInt(trainVipGrade.getLevel()) < 4) {
            HouToast.showLongToast(TrainMessageActivity.this, getInfo(R.string.vip_grade_tips));
          } else {
            Intent intent = new Intent(TrainMessageActivity.this, UploadTrainActivity.class);
            startActivity(intent);
          }
        }
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(TrainMessageActivity.this, Config.USER_INFO, Config.TOKEN, ""));
    MyApplication.rxNetUtils.getTrainService()
        .getVipGrade(params)
        .map(new BaseModelFunc<TrainVipGrade>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }
}

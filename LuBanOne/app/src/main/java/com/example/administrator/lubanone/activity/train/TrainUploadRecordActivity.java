package com.example.administrator.lubanone.activity.train;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.training.activity.TrainDetailsActivity;
import com.example.administrator.lubanone.adapter.training.MyUploadTrainAdapter;
import com.example.administrator.lubanone.adapter.training.MyUploadTrainAdapter.onItemNotifyMember;
import com.example.administrator.lubanone.bean.model.CommonModel;
import com.example.administrator.lubanone.bean.model.TrainMyUploadModel;
import com.example.administrator.lubanone.bean.model.TrainMyUploadModel.MyUploadInfo;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.example.qlibrary.utils.WindoswUtil;
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
 * Created by hou on 2017/9/1.
 */

public class TrainUploadRecordActivity extends BaseActivity {

  private LinearLayout backBtn;
  private PullToRefreshLayout noDataLayout;
  private PullableImageView noDataImage;
  private PullToRefreshLayout mPullToRefreshLayout;
  private PullableListView mPullableListView;
  private List<MyUploadInfo> datas = new ArrayList<>();
  private MyUploadTrainAdapter adapter;
  private int pageNo = 1;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_train_upload_record;
  }

  @Override
  public void initView() {
    mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.train_my_upload_pull_layout);
    mPullableListView = (PullableListView) findViewById(R.id.train_my_upload_list_view);
    noDataLayout = (PullToRefreshLayout) findViewById(R.id.train_my_upload_no_data_layout);
    noDataImage = (PullableImageView) findViewById(R.id.train_my_upload_no_data_image);
    backBtn = (LinearLayout) findViewById(R.id.train_upload_record_back_icon);
    backBtn.setOnClickListener(this);
    noDataLayout.setPullUpEnable(false);
    MyRefreshListener listener = new MyRefreshListener();
    noDataLayout.setOnPullListener(listener);
    mPullToRefreshLayout.setOnPullListener(listener);
    adapter = new MyUploadTrainAdapter(mContext, datas, true);
    mPullableListView.setAdapter(adapter);
    mPullableListView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (datas.get(position).getStatus()) {
          case "0"://审核中
            break;
          case "1"://审核通过
            Intent intent = new Intent(TrainUploadRecordActivity.this, TrainDetailsActivity.class);
            intent.putExtra("trainId", datas.get(position).getTrainid());
            intent.putExtra("theme", datas.get(position).getTheme());
            startActivity(intent);
            break;
          case "2"://审核未通过
//            Intent intent = new Intent(TrainUploadRecordActivity.this, TrainRecordDetailsActivity.class);
//            intent.putExtra("theme", datas.get(position).getTheme());
//            intent.putExtra("time", datas.get(position).getTraintime());
//            intent.putExtra("number", datas.get(position).getTestnumber());
//            intent.putExtra("status", datas.get(position).getStatus());
//            intent.putExtra("score", datas.get(position).getCode());
//            intent.putExtra("reason", datas.get(position).getReason());
//            startActivity(intent);
            choosePic(position);
            break;
          default:
            break;
        }
      }
    });

    adapter.setOnItemNotifyMemberListener(new onItemNotifyMember() {
      @Override
      public void onNotifyMember(int position) {
        showNotifyMemberAlert(datas.get(position).getTrainid());
      }
    });
  }

  //是否通知伞下会员弹框
  private void showNotifyMemberAlert(final String trainId) {

    final Dialog dialog = new Dialog(this, R.style.MyDialog);
    View view = LayoutInflater.from(this)
        .inflate(R.layout.notify_member_alert_layout, null);
    TextView cancelBtn = (TextView) view.findViewById(R.id.notify_alert_cancel);
    TextView confirmBtn = (TextView) view.findViewById(R.id.notify_alert_confirm);
    dialog.setContentView(view);
    dialog.show();
    cancelBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
    confirmBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
        notifyMemberFromServer(trainId);
      }
    });

  }

  //通知伞下会员网络请求
  private void notifyMemberFromServer(String trainId) {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<CommonModel>(this) {
      @Override
      public void onCompleted() {
        dismissLoadingDialog();
      }

      @Override
      public void onError(Throwable e) {
        dismissLoadingDialog();
        super.onError(e);
        HouToast.showLongToast(TrainUploadRecordActivity.this, getInfo(R.string.notify_fail_toast));
        HouLog.d("通知伞下会员onError:" + e.toString());
      }

      @Override
      public void onNext(CommonModel commonModel) {
        dismissLoadingDialog();
        HouLog.d("通知伞下会员onNext");
        dialogNew();
        getData(mPullToRefreshLayout);
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
    params.put("train_id", trainId);
    HouLog.d("通知伞下会员参数", params.toString());
    MyApplication.rxNetUtils.getTrainService().notifyMember(params)
        .map(new BaseModelFunc<CommonModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  //通知成功alert
  private void dialogNew() {
    final Dialog dialog = new Dialog(this, R.style.MyDialog);
    View view = LayoutInflater.from(this)
        .inflate(R.layout.notify_member_success_layout, null);

    dialog.setContentView(view);
    dialog.getWindow()
        .setLayout(WindoswUtil.getWindowWidth(this) - 200,
            WindowManager.LayoutParams.WRAP_CONTENT);
    dialog.show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (dialog.isShowing()) {
          dialog.dismiss();
        }
      }
    }, 3500);
  }

  private void choosePic(final int listPosition) {
    ArrayList<String> list = new ArrayList<>();
    list.add(getInfo(R.string.train_delete_upload_record));
    list.add(getInfo(R.string.upload_again));

    StytledDialog
        .showBottomItemDialog(mContext, list, getInfo(R.string.upload_select_cancel), true,
            true,
            new MyItemDialogListener() {
              @Override
              public void onItemClick(String text, int position) {
                openPic(position, listPosition);
              }
            });
  }

  private void openPic(int position, int listPosition) {

    if (0 == position) {
      deleteData(datas.get(listPosition).getTrainid(), listPosition);
    }
    if (1 == position) {
      startActivity(new Intent(mContext, UploadTrainActivity.class));
    }
  }

  //下拉刷新
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

  /**
   * 请求数据
   */
  private void getData(final PullToRefreshLayout refreshLayout) {
    Subscriber subscriber = new MySubscriber<TrainMyUploadModel>(this) {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        noDataLayout.setVisibility(View.VISIBLE);
        HouLog.d(TAG, "培训上传记录onError" + e.toString());
      }

      @Override
      public void onNext(TrainMyUploadModel trainDataModel) {
        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        noDataLayout.setVisibility(View.GONE);
        if (trainDataModel != null) {
          HouLog.d(TAG, trainDataModel.getList().toString());
          if (pageNo == 1) {
            datas.clear();
          }
          datas.addAll(trainDataModel.getList());
          adapter.notifyDataSetChanged();
        } else {
          if (pageNo > 1) {
            ToastUtil.showShort(getInfo(R.string.no_more_message), mContext);
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
    params.put("token", SPUtils.getStringValue(mContext, Config.USER_INFO, Config.TOKEN, ""));
    params.put("page", String.valueOf(pageNo));
    HouLog.d(TAG + "我上传的培训列表参数:", params.toString());
    MyApplication.rxNetUtils.getTrainService()
        .getUploadList(params)
        .map(new BaseModelFunc<TrainMyUploadModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  /**
   * 删除未通过审核的条目
   */
  private void deleteData(String trainId, final int position) {
    showLoadingDialog();
    Subscriber subscriber = new Subscriber<CommonModel>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        dismissLoadingDialog();
        HouLog.d("onError: " + e.toString());
        ToastUtil.showShort(getInfo(R.string.upload_record_fail), mContext);
      }

      @Override
      public void onNext(CommonModel commonModel) {
        dismissLoadingDialog();
        datas.remove(position);
        adapter.notifyDataSetChanged();
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(mContext, Config.USER_INFO, Config.TOKEN, ""));
    params.put("trainid", trainId);
    HouLog.d("删除数据参数：" + params.toString());
    MyApplication.rxNetUtils.getTrainService()
        .deleteTrain(params)
        .map(new BaseModelFunc<CommonModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onResume() {
    super.onResume();
    HouLog.d("onResume");
    getData(mPullToRefreshLayout);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.train_upload_record_back_icon:
        finish();
        break;
    }
  }
}

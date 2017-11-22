package com.example.administrator.lubanone.activity.us;

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
import com.example.administrator.lubanone.adapter.UsSpreadUploadRecordAdapter;
import com.example.administrator.lubanone.adapter.UsSpreadUploadRecordAdapter.onItemNotifyMember;
import com.example.administrator.lubanone.bean.model.CommonModel;
import com.example.administrator.lubanone.bean.model.UploadRecord;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.WindoswUtil;
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
 * Created by hou on 2017/8/29.
 */

public class MoreUploadRecordActivity extends BaseActivity {

  private LinearLayout backBtn;
  private PullableListView mListView;
  private PullToRefreshLayout mRefreshLayout;
  private PullToRefreshLayout noDataLayout;
  private PullableImageView noDataImage;
  private UsSpreadUploadRecordAdapter mAdapter;
  private List<UploadRecord> datas;
  private int pageNo = 1;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_more_upload_record;
  }

  @Override
  public void initView() {

    backBtn = (LinearLayout) findViewById(R.id.us_upload_record_back_icon);
    mRefreshLayout = (PullToRefreshLayout) findViewById(
        R.id.us_spread_upload_more_record_pull_layout);
    mListView = (PullableListView) findViewById(R.id.us_spread_upload_more_record_list);
    noDataLayout = (PullToRefreshLayout) findViewById(R.id.us_spread_more_record_no_data_layout);
    noDataImage = (PullableImageView) findViewById(R.id.us_spread_more_record_no_data_image);
    MyRefreshListener myRefreshListener = new MyRefreshListener();
    mRefreshLayout.setOnPullListener(myRefreshListener);
    noDataLayout.setOnPullListener(myRefreshListener);
    noDataLayout.setPullUpEnable(false);

    backBtn.setOnClickListener(this);

    datas = new ArrayList<>();
    mAdapter = new UsSpreadUploadRecordAdapter(datas, mContext, true);
    mListView.setAdapter(mAdapter);

    mListView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (datas.get(position).getState()) {
          case "0":
            //审核中
            break;
          case "1":
            //审核通过
            Intent intent = new Intent(MoreUploadRecordActivity.this,
                CommonListDetailsActivity.class);
            intent.putExtra("if_id", datas.get(position).getIf_id());
            intent.putExtra("theme", datas.get(position).getIf_title());
            intent.putExtra("content", datas.get(position).getIf_time());
            startActivity(intent);
            break;
          case "2":
            //审核未通过
            choosePic(position);
            break;
          default:

            break;
        }
      }
    });

    mAdapter.setOnItemNotifyMemberListener(new onItemNotifyMember() {
      @Override
      public void onNotifyMember(int position) {
        showNotifyMemberAlert(datas.get(position).getIf_id());
      }
    });

  }

  //是否通知伞下会员弹框
  private void showNotifyMemberAlert(final String ifId) {
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
        notifyMemberFromServer(ifId);
      }
    });
  }

  //通知伞下会员网络请求
  private void notifyMemberFromServer(String ifId) {
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
        HouToast.showLongToast(MoreUploadRecordActivity.this, getInfo(R.string.notify_fail_toast));
        HouLog.d("通知伞下会员onError:" + e.toString());
      }

      @Override
      public void onNext(CommonModel commonModel) {
        dismissLoadingDialog();
        HouLog.d("通知伞下会员onNext");
        dialogNew();
        getDataRetrofit(mRefreshLayout);
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(MoreUploadRecordActivity.this, Config.USER_INFO, Config.TOKEN, ""));
    params.put("if_id", ifId);
    HouLog.d("通知伞下会员参数", params.toString());
    MyApplication.rxNetUtils.getUploadService().notifyMember(params)
        .map(new BaseModelFunc<CommonModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }
  //通知成功alert
  private void dialogNew() {
    final Dialog dialog = new Dialog(MoreUploadRecordActivity.this, R.style.MyDialog);
    View view = LayoutInflater.from(MoreUploadRecordActivity.this)
        .inflate(R.layout.notify_member_success_layout, null);

    dialog.setContentView(view);
    dialog.getWindow()
        .setLayout(WindoswUtil.getWindowWidth(MoreUploadRecordActivity.this) - 200,
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

  @Override
  protected void onResume() {
    super.onResume();
    getDataRetrofit(mRefreshLayout);
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
      deleteNoPassRecord(datas.get(listPosition).getIf_id(), listPosition);
    }
    if (1 == position) {
      startActivity(new Intent(mContext, UsSpreadUploadActivity.class));
    }
  }

  //删除未通过记录
  private void deleteNoPassRecord(String ifId, final int position) {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<List<CommonModel>>(this) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
        HouLog.d("onError: " + e.toString());
        HouToast.showLongToast(MoreUploadRecordActivity.this, getInfo(R.string.upload_record_fail));
      }

      @Override
      public void onNext(List<CommonModel> commonModels) {
        dismissLoadingDialog();
        HouToast.showLongToast(MoreUploadRecordActivity.this, getInfo(R.string.delete_success));
        datas.remove(position);
        mAdapter.notifyDataSetChanged();
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(mContext, Config.USER_INFO, Config.TOKEN, ""));
    params.put("if_id", ifId);
    MyApplication.rxNetUtils.getUsListViewService()
        .getDeleteInfo(params)
        .map(new BaseModelFunc<List<CommonModel>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  //刷新监听
  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      pageNo = 1;
      getDataRetrofit(pullToRefreshLayout);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      pageNo++;
      getDataRetrofit(pullToRefreshLayout);
    }
  }

  private void getDataRetrofit(final PullToRefreshLayout refreshLayout) {
    Subscriber subscriber = new MySubscriber<List<UploadRecord>>(this) {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        noDataLayout.setVisibility(View.VISIBLE);
        HouLog.d("上传记录error", e.toString());
      }

      @Override
      public void onNext(List<UploadRecord> usListViewModel) {
        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        noDataLayout.setVisibility(View.GONE);
        if (pageNo == 1) {
          datas.clear();
        }
        if (usListViewModel != null && usListViewModel.size() > 0) {
          HouLog.d("上传记录result结果", usListViewModel.size() + "  " + usListViewModel.toString());
          datas.addAll(usListViewModel);
          mAdapter.notifyDataSetChanged();
        } else {
          if (pageNo > 1) {
            HouToast
                .showLongToast(MoreUploadRecordActivity.this, getInfo(R.string.no_more_message));
            pageNo--;
          } else {
            noDataImage.setImageResource(R.drawable.no_data);
            noDataLayout.setVisibility(View.VISIBLE);
          }
        }
        HouLog.d("页数：", String.valueOf(pageNo));
      }
    };
    Map<String, String> map = new HashMap<>();
    map.put("token", SPUtils.getStringValue(mContext, Config.USER_INFO, Config.TOKEN, ""));
    map.put("page", String.valueOf(pageNo));
    HouLog.d("上传记录请求参数", map.toString());
    MyApplication.rxNetUtils.getUsListViewService().getUploadListView(map)
        .map(new BaseModelFunc<List<UploadRecord>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);

  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.us_upload_record_back_icon:
        finish();
        break;
    }
  }
}

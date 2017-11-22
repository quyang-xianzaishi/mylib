package com.example.administrator.lubanone.fragment.us;

import static android.app.Activity.RESULT_OK;
import static com.example.administrator.lubanone.R.id.us_spread_upload_more_btn;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.us.CommonListDetailsActivity;
import com.example.administrator.lubanone.activity.us.MoreUploadRecordActivity;
import com.example.administrator.lubanone.activity.us.UsSpreadUploadActivity;
import com.example.administrator.lubanone.adapter.UsSpreadUploadRecordAdapter;
import com.example.administrator.lubanone.adapter.UsSpreadUploadRecordAdapter.onItemNotifyMember;
import com.example.administrator.lubanone.bean.model.CommonModel;
import com.example.administrator.lubanone.bean.model.UploadRecord;
import com.example.administrator.lubanone.customview.ScollListView;
import com.example.administrator.lubanone.fragment.BaseFragment;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.WindoswUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2017/7/3.
 */

public class ListSpreadUploadDataFragment extends BaseFragment implements View.OnClickListener {

  private static final int REQUEST_UPLOAD_CODE = 111;
  private TextView lookMore;
  private TextView uploadBtn;
  private RelativeLayout moreRecordBtn;
  private ScollListView mListView;
  private UsSpreadUploadRecordAdapter mAdapter;
  private List<UploadRecord> datas;
  private int pageNo = 1;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_spread_upload, null);

    uploadBtn = (TextView) view.findViewById(R.id.us_spread_upload_rl);
    lookMore = (TextView) view.findViewById(R.id.us_spread_upload_look_more);
    moreRecordBtn = (RelativeLayout) view.findViewById(us_spread_upload_more_btn);
    mListView = (ScollListView) view.findViewById(R.id.us_spread_upload_record_list_view);

    uploadBtn.setOnClickListener(this);
    lookMore.setOnClickListener(this);
    moreRecordBtn.setOnClickListener(this);

    datas = new ArrayList<>();
    mAdapter = new UsSpreadUploadRecordAdapter(datas, mActivity, false);
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
            Intent intent = new Intent(getActivity(), CommonListDetailsActivity.class);
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

    return view;
  }

  //是否通知伞下会员弹框
  private void showNotifyMemberAlert(final String ifId) {
    final Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);
    View view = LayoutInflater.from(getActivity())
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
    Subscriber subscriber = new MySubscriber<CommonModel>(getActivity()) {
      @Override
      public void onCompleted() {
        dismissLoadingDialog();
      }

      @Override
      public void onError(Throwable e) {
        dismissLoadingDialog();
        super.onError(e);
        HouToast.showLongToast(getActivity(), getInfo(R.string.notify_fail_toast));
        HouLog.d("通知伞下会员onError:" + e.toString());
      }

      @Override
      public void onNext(CommonModel commonModel) {
        dismissLoadingDialog();
        HouLog.d("通知伞下会员onNext");
        dialogNew();
        getDataRetrofit();
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
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
    final Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);
    View view = LayoutInflater.from(getActivity())
        .inflate(R.layout.notify_member_success_layout, null);

    dialog.setContentView(view);
    dialog.getWindow()
        .setLayout(WindoswUtil.getWindowWidth(getActivity()) - 200,
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
        .showBottomItemDialog(getActivity(), list, getInfo(R.string.upload_select_cancel), true,
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
      startActivity(new Intent(getActivity(), UsSpreadUploadActivity.class));
    }
  }

  //删除未通过记录
  private void deleteNoPassRecord(String ifId, final int position) {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<List<CommonModel>>(getActivity()) {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        dismissLoadingDialog();
        HouLog.d("onError: " + e.toString());
        HouToast.showLongToast(getActivity(), getInfo(R.string.upload_record_fail));
      }

      @Override
      public void onNext(List<CommonModel> commonModels) {
        dismissLoadingDialog();
        HouToast.showLongToast(getActivity(), getInfo(R.string.delete_success));
        datas.remove(position);
        mAdapter.notifyDataSetChanged();
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("if_id", ifId);
    MyApplication.rxNetUtils.getUsListViewService()
        .getDeleteInfo(params)
        .map(new BaseModelFunc<List<CommonModel>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  @Override
  public void onResume() {
    super.onResume();
    HouLog.d("onResume");
    getDataRetrofit();
  }

  @Override
  public void initData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.us_spread_upload_rl:
        Intent intent = new Intent(getActivity(), UsSpreadUploadActivity.class);
        startActivity(intent);
        break;
      case R.id.us_spread_upload_more_btn:
        startActivity(new Intent(getActivity(), MoreUploadRecordActivity.class));
        break;
      case R.id.us_spread_upload_look_more:
        startActivity(new Intent(getActivity(), MoreUploadRecordActivity.class));
        break;
      default:
        break;
    }
  }

  //获取数据
  private void getDataRetrofit() {
    Subscriber subscriber = new MySubscriber<List<UploadRecord>>(getActivity()) {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        HouLog.d("推广上传error", e.toString());
      }

      @Override
      public void onNext(List<UploadRecord> usListViewModel) {
        datas.clear();
        if (usListViewModel != null && usListViewModel.size() > 0) {
          HouLog.d("推广上传result结果", usListViewModel.size() + "  " + usListViewModel.toString());
          datas.addAll(usListViewModel);
          mAdapter.notifyDataSetChanged();
          if (datas.size() > 5) {
            lookMore.setVisibility(View.VISIBLE);
          }
        }
        HouLog.d("页数：", String.valueOf(pageNo));
      }
    };
    Map<String, String> map = new HashMap<>();
    map.put("token", SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""));
    map.put("page", String.valueOf(pageNo));
    HouLog.d("推广上传请求参数", map.toString());
    MyApplication.rxNetUtils.getUsListViewService().getUploadListView(map)
        .map(new BaseModelFunc<List<UploadRecord>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case REQUEST_UPLOAD_CODE:
          HouToast.showLongToast(getActivity(), getInfo(R.string.upload_success));
          break;
        default:
          break;
      }
    }
  }
}

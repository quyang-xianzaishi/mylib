package com.example.administrator.lubanone.activity.training;

import static android.app.Activity.RESULT_OK;

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
import com.example.administrator.lubanone.activity.train.TrainUploadRecordActivity;
import com.example.administrator.lubanone.activity.train.UploadTrainActivity;
import com.example.administrator.lubanone.activity.training.activity.TrainDetailsActivity;
import com.example.administrator.lubanone.adapter.training.MyUploadTrainAdapter;
import com.example.administrator.lubanone.adapter.training.MyUploadTrainAdapter.onItemNotifyMember;
import com.example.administrator.lubanone.bean.model.CommonModel;
import com.example.administrator.lubanone.bean.model.TrainMyUploadModel;
import com.example.administrator.lubanone.bean.model.TrainMyUploadModel.MyUploadInfo;
import com.example.administrator.lubanone.bean.model.TrainVipGrade;
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
 * Created by hou on 2017/8/5.
 */

public class MyUploadTrainFragment extends BaseFragment implements OnClickListener {

  private final static String TAG = "MyUploadTrainFragment";
  private static final int REQUEST_UPLOAD_CODE = 111;

  private TextView applyTrainBtn;
  private List<MyUploadInfo> datas = new ArrayList<>();
  private MyUploadTrainAdapter adapter;
  private ScollListView mListView;
  private RelativeLayout moreRecordBtn;
  private int pageNo = 1;

  private TextView lookMore;

  @Override
  public View initView() {

    View view = mInflater.inflate(R.layout.fragment_my_upload_train, null);
    lookMore = (TextView) view.findViewById(R.id.train_upload_look_more);
    applyTrainBtn = (TextView) view.findViewById(R.id.my_upload_train_apply);
    mListView = (ScollListView) view.findViewById(R.id.train_upload_record_list_view);
    moreRecordBtn = (RelativeLayout) view.findViewById(R.id.train_upload_more_btn);

    moreRecordBtn.setOnClickListener(this);
    lookMore.setOnClickListener(this);
    applyTrainBtn.setOnClickListener(this);

    adapter = new MyUploadTrainAdapter(getActivity(), datas, false);
    mListView.setAdapter(adapter);

    mListView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HouLog.d("点击位置 " + position);
        if (5 == position) {
          startActivity(new Intent(getActivity(), TrainUploadRecordActivity.class));
        } else {
          switch (datas.get(position).getStatus()) {
            case "0"://审核中
              break;
            case "1"://审核通过
              Intent intent = new Intent(getActivity(), TrainDetailsActivity.class);
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
      }
    });

    adapter.setOnItemNotifyMemberListener(new onItemNotifyMember() {
      @Override
      public void onNotifyMember(int position) {
//        HouToast.showLongToast(getActivity(), "通知伞下会员");
        showNotifyMemberAlert(datas.get(position).getTrainid());
      }
    });

    return view;
  }

  //是否通知伞下会员弹框
  private void showNotifyMemberAlert(final String trainId) {
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
        notifyMemberFromServer(trainId);
      }
    });

  }

  //通知伞下会员网络请求
  private void notifyMemberFromServer(String trainId) {
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
        getData();
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
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


  //删除底部选择栏
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
      deleteData(datas.get(listPosition).getTrainid(), listPosition);
    }
    if (1 == position) {
      getVipGrade();
    }
  }

  /**
   * 请求数据
   */
  private void getData() {
    Subscriber subscriber = new MySubscriber<TrainMyUploadModel>(getActivity()) {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        HouLog.d(TAG, "培训上传记录列表onError" + e.toString());
      }

      @Override
      public void onNext(TrainMyUploadModel trainDataModel) {
        if (trainDataModel != null) {
          HouLog.d(TAG, trainDataModel.getList().toString());
          if (pageNo == 1) {
            datas.clear();
          }
          datas.addAll(trainDataModel.getList());
          if (datas.size() > 5) {
            lookMore.setVisibility(View.VISIBLE);
          }
          adapter.notifyDataSetChanged();
        } else {
          //无上传记录
        }
        HouLog.d(TAG + " 页数：", String.valueOf(pageNo));
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("page", String.valueOf(pageNo));
    HouLog.d(TAG + "我上传的培训列表参数:", params.toString());
    MyApplication.rxNetUtils.getTrainService()
        .getUploadList(params)
        .map(new BaseModelFunc<TrainMyUploadModel>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  @Override
  public void onResume() {
    super.onResume();
    getData();
  }

  @Override
  public void initData() {

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.my_upload_train_apply:
        getVipGrade();
//        startActivity(new Intent(getActivity(), UploadTrainActivity.class));
        break;
      case R.id.train_upload_more_btn:
        startActivity(new Intent(getActivity(), TrainUploadRecordActivity.class));
        break;
      case R.id.train_upload_look_more:
        startActivity(new Intent(getActivity(), TrainUploadRecordActivity.class));
        break;
      default:
        break;
    }
  }


  /**
   * 获取会员等级
   */
  private void getVipGrade() {
    showLoadingDialog();
    Subscriber subscriber = new MySubscriber<TrainVipGrade>(getActivity()) {
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
            HouToast.showLongToast(getActivity(), getInfo(R.string.vip_grade_tips));
          } else {
            Intent intent = new Intent(getActivity(), UploadTrainActivity.class);
            startActivity(intent);
          }
        }
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""));
    MyApplication.rxNetUtils.getTrainService()
        .getVipGrade(params)
        .map(new BaseModelFunc<TrainVipGrade>())
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
        HouToast.showLongToast(getActivity(), getInfo(R.string.upload_record_fail));
      }

      @Override
      public void onNext(CommonModel commonModel) {
        dismissLoadingDialog();
        datas.remove(position);
        adapter.notifyDataSetChanged();
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""));
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

package com.example.administrator.lubanone.activity.training.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.chunkupload.upload.UploadManager;
import com.example.administrator.lubanone.chunkupload.upload.UploadTask;
import com.example.administrator.lubanone.chunkupload.upload.UploadTaskListener;
import com.example.administrator.lubanone.chunkupload.utils.OkHttpRequestUtils;
import com.example.administrator.lubanone.widgets.CustomDatePicker;
import com.example.qlibrary.utils.ToastUtil;
import com.zhy.http.okhttp.callback.StringCallback;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import okhttp3.Call;

/**
 * Created by Administrator on 2017\6\23 0023.
 */

public class ApplyTrainingActivity extends BaseActivity implements UploadTaskListener {

  private static final String TAG = "ApplyTrainingActivity";
  private UploadManager uploadManager;
  private ProgressBar mProgressBar;
  private TextView mTvStatus;

  private TextView back;
  private TextView title;
  private LinearLayout main;
  private EditText trainingTheme;
  private EditText trainingSummary;
  private ImageView timeChoose;
  private EditText timeEntry;
  private TextView commit;
  private CustomDatePicker customDatePicker;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_apply_training;
  }

  @Override
  public void initView() {
    uploadManager = UploadManager.getInstance();
    mProgressBar = (ProgressBar) findViewById(R.id.training_progressBar);
    mTvStatus = (TextView) findViewById(R.id.training_tv_status);

    initViews();
    initDatePicker();
  }

  @Override
  public void loadData() {

  }

  private void initViews() {
    back = (TextView) this.findViewById(R.id.activity_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    main = (LinearLayout) this.findViewById(R.id.apply_training_linear);
    trainingTheme = (EditText) this.findViewById(R.id.training_theme);
    trainingSummary = (EditText) this.findViewById(R.id.training_summary);
    timeChoose = (ImageView) this.findViewById(R.id.training_apply_time_choose);
    timeEntry = (EditText) this.findViewById(R.id.training_time);
    commit = (TextView) this.findViewById(R.id.training_apply_commit);
    title.setText(this.getResources().getString(R.string.apply_training));

    timeChoose.setOnClickListener(new TrainingApplyOnClickListener());

    commit.setOnClickListener(new TrainingApplyOnClickListener());
    back.setOnClickListener(new TrainingApplyOnClickListener());
  }

  private void initDatePicker() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    String now = sdf.format(new Date());
    timeEntry.setText(now.split(" ")[0]);
    timeEntry.setText(now);
    customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
      @Override
      public void handle(String time) { // 回调接口，获得选中的时间
        timeEntry.setText(time);
      }
    }, now, "2030-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
    customDatePicker.showSpecificTime(true); // 显示时和分
    customDatePicker.setIsLoop(true); // 允许循环滚动
  }

  @Override
  public void onClick(View v) {

  }

  private class TrainingApplyOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.training_apply_time_choose:
          customDatePicker.show(timeEntry.getText().toString());
          break;
        case R.id.training_apply_commit:
          //培训申请提交验证
//          applyTest();
          applyCommit();
          break;
        case R.id.activity_back:
          ApplyTrainingActivity.this.finish();
          break;
        default:
          break;
      }
    }
  }

  static int[] DAYS = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  /**
   * @param date yyyy-MM-dd HH:mm:ss
   */

  public static boolean isValidDate(String date) {
    try {
      int year = Integer.parseInt(date.substring(0, 4));
      if (year <= 0) {
        return false;
      }
      int month = Integer.parseInt(date.substring(5, 7));
      if (month <= 0 || month > 12) {
        return false;
      }
      int day = Integer.parseInt(date.substring(8, 10));
      if (day <= 0 || day > DAYS[month]) {
        return false;
      }
      if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {
        return false;
      }
      int hour = Integer.parseInt(date.substring(11, 13));
      if (hour < 0 || hour > 23) {
        return false;
      }
      int minute = Integer.parseInt(date.substring(14, 16));
      if (minute < 0 || minute > 59) {
        return false;
      }
            /*int second = Integer.parseInt(date.substring(17, 19));
            if (second < 0 || second > 59)
                return false;*/
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static final boolean isGregorianLeapYear(int year) {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
  }

  private void applyTest() {
    if (trainingTheme.getText() == null || trainingTheme.getText().toString().equals("")) {
      ToastUtil.showShort(this.getResources().getString(R.string.training_commit_theme_null),
          getApplicationContext());
      return;
    }
    if (timeEntry.getText() == null || timeEntry.getText().toString().equals("")) {
      ToastUtil.showShort(this.getResources().getString(R.string.training_commit_time_null),
          getApplicationContext());
      return;
    }
    if (!isValidDate(timeEntry.getText().toString())) {
      ToastUtil.showShort(this.getResources().getString(R.string.training_commit_time_mistake),
          getApplicationContext());
      return;
    }
    if (trainingSummary.getText() == null || trainingSummary.getText().toString().equals("")) {
      ToastUtil.showShort(this.getResources().getString(R.string.training_commit_summary_null),
          getApplicationContext());
      return;
    }
    if (trainingSummary.getText().length() > 200) {
      ToastUtil.showShort(this.getResources().getString(R.string.training_commit_summary_over),
          getApplicationContext());
      return;
    }
    //输入内容正确，提交申请
    applyCommit();
  }

  private void applyCommit() {
    Map<String, String> map = new HashMap<>();
    map.put("fileName", "a.mp4");
    OkHttpRequestUtils.getInstance(mContext).getUploadInfo("", map, new StringCallback() {
      @Override
      public void onError(Call call, Exception e, int id) {

      }

      @Override
      public void onResponse(String response, int id) {

        UploadTask task = new UploadTask.Builder()
            .setId("")
            .setUrl("")
            .setChunck(1)
            .setFileName("a.mp4")
            .setListener(ApplyTrainingActivity.this)
            .build();
        uploadManager.addUploadTask(task);

      }
    });

//    final UserPopupWindow userPopupWindow = new UserPopupWindow(ApplyTrainingActivity.this,
//        this.getResources().getLayout(R.layout.apply_training_success_dialog));
//    userPopupWindow.showUserPopupWindow(main);
//    userPopupWindow.setOnClickListener(userPopupWindow.getView(), new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        userPopupWindow.dismiss();
//      }
//    });
  }
  @Override
  public void onUploading(UploadTask uploadTask, String percent, int position) {
    mProgressBar.setProgress(Integer.parseInt(percent));
    mTvStatus.setText("正在上传..." + percent + "%");
  }

  @Override
  public void onUploadSuccess(UploadTask uploadTask, File file) {
    mTvStatus.setText("上传完成 path：" + file.getAbsolutePath());
  }

  @Override
  public void onError(UploadTask uploadTask, int errorCode, int position) {
    mTvStatus.setText("上传失败errorCode=" + errorCode);
  }

  @Override
  public void onPause(UploadTask uploadTask) {
    mTvStatus.setText("上传暂停！");
  }

}

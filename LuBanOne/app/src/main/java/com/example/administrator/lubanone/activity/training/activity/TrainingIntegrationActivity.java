package com.example.administrator.lubanone.activity.training.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.home.AlwaysProblemsActivity;
import com.example.administrator.lubanone.activity.home.TrainSystemActivity;
import com.example.administrator.lubanone.widgets.ObtainIntegrationPopupWindow;
import com.example.qlibrary.utils.ToastUtil;

/**
 * Created by Administrator on 2017\7\12 0012.
 */

public class TrainingIntegrationActivity extends AppCompatActivity implements OnClickListener{
  private TextView back;
  private TextView obtainIntegration;
  private TextView trainingIntegration;
  private LinearLayout trainingIntegrationSystem;
  private LinearLayout commonProblem;
  private LinearLayout trainingIntegrationRecord;
  private ObtainIntegrationPopupWindow mObtainIntegrationPopupWindow;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_training_integration);
    initView();
  }

  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    obtainIntegration = (TextView) this.findViewById(R.id.activity_obtain_integration);
    trainingIntegration = (TextView) this.findViewById(R.id.training_integration);
    trainingIntegrationSystem = (LinearLayout) this.findViewById(R.id.training_integration_system);
    commonProblem = (LinearLayout) this.findViewById(R.id.common_problem);
    trainingIntegrationRecord = (LinearLayout) this.findViewById(R.id.integration_record);

    back.setOnClickListener(this);
    obtainIntegration.setOnClickListener(this);
    trainingIntegrationSystem.setOnClickListener(this);
    commonProblem.setOnClickListener(this);
    trainingIntegrationRecord.setOnClickListener(this);

  }


  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.activity_back:
        TrainingIntegrationActivity.this.finish();
        break;
      case R.id.activity_obtain_integration:
        //点击获取积分
        showObtainIntegrationPopupWindow();
        break;
      case R.id.training_integration_system:
        //培训积分制度
        Intent intent = new Intent();
        intent.setClass(this,TrainSystemActivity.class);
        this.startActivity(intent);
        break;
      case R.id.common_problem:
        //常见问题
        Intent intent2 = new Intent();
        intent2.setClass(this,AlwaysProblemsActivity.class);
        this.startActivity(intent2);
        break;
      case R.id.integration_record:
        //积分记录
        Intent intent1 = new Intent();
        intent1.setClass(this,TrainingIntegrationRecordActivity.class);
        this.startActivity(intent1);
        break;
      case R.id.complete_exam:
        //完成考试
        ToastUtil.showShort("进入考试中心",this);
        break;
      case R.id.apply_train:
        //发起培训
        ToastUtil.showShort("进入我发起的培训",this);
        break;
      case R.id.upload_material:
        //上传推广材料
        ToastUtil.showShort("进入上传推广材料",this);
        break;
      default:
        break;
    }

  }

  private void showObtainIntegrationPopupWindow(){
    WindowManager wm = this.getWindowManager();
    int width = wm.getDefaultDisplay().getWidth();
    int height = wm.getDefaultDisplay().getHeight();
    if (mObtainIntegrationPopupWindow == null) {
      //自定义的单击事件
      mObtainIntegrationPopupWindow = new ObtainIntegrationPopupWindow(TrainingIntegrationActivity.this, this,width/2,height/4);
      //监听窗口的焦点事件，点击窗口外面则取消显示
      mObtainIntegrationPopupWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
          if (!hasFocus) {
            mObtainIntegrationPopupWindow.dismiss();
          }
        }
      });
    }
    //设置默认获取焦点
    mObtainIntegrationPopupWindow.setFocusable(true);
    //以某个控件的x和y的偏移量位置开始显示窗口
    mObtainIntegrationPopupWindow.showAsDropDown(obtainIntegration, 0, 0);
    //如果窗口存在，则更新
    mObtainIntegrationPopupWindow.update();

  }


}

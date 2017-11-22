package com.example.administrator.lubanone.activity.message;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.utils.AndroidBug54971Workaround;
import com.example.administrator.lubanone.utils.AndroidBug5497Workaround;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class CustomMessageActivity extends AppCompatActivity implements View.OnClickListener {

  private TextView back;
  private TextView title;

  private TextView contactCustom;
  private LinearLayout customLinear;
  private LinearLayout contactCustomLinear;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Window window = this.getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    setContentView(R.layout.activity_custom_message);
    //AndroidBug5497Workaround.assistActivity(this);
    //hideBottomUIMenu();
    initView();
  }

  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    contactCustom = (TextView) this.findViewById(R.id.contact_custom_text);
    customLinear = (LinearLayout) this.findViewById(R.id.custom_linear);
    contactCustomLinear = (LinearLayout) this.findViewById(R.id.contact_custom);


    title.setText(getString(R.string.servicer));
    back.setOnClickListener(this);
    contactCustom.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.activity_back:
        CustomMessageActivity.this.finish();
        break;
      case R.id.contact_custom_text:
        contactCustomLinear.setVisibility(View.GONE);
        customLinear.setVisibility(View.VISIBLE);
      default:
        break;
    }
  }

  /**
   * 隐藏虚拟按键，并且设置成全屏
   */
  protected void hideBottomUIMenu(){
    if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
      View v = this.getWindow().getDecorView();
      v.setSystemUiVisibility(View.GONE);
    } else if (Build.VERSION.SDK_INT >= 19) {
      //for new api versions.
      View decorView = getWindow().getDecorView();
      int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
 //         | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
          | View.SYSTEM_UI_FLAG_IMMERSIVE;
      decorView.setSystemUiVisibility(uiOptions);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
  }


}

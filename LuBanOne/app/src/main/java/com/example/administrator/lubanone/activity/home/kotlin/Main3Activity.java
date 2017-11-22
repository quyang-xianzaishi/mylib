package com.example.administrator.lubanone.activity.home.kotlin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.administrator.lubanone.R;
import com.example.qlibrary.utils.ColorUtil;
import org.zackratos.ultimatebar.UltimateBar;

public class Main3Activity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main3);
    UltimateBar ultimateBar = new UltimateBar(this);
    ultimateBar.setColorBar(ColorUtil.getColor(R.color.red, this));
  }
}

package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.SellMatchListResultBean.JsbzlistBean;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.StringUtil;

/**
 * 买入待匹配详情
 */
public class SellMatchFramentDEtailsActivity extends BaseActivity {

  @BindView(R.id.title)
  TextView mTitle;
  @BindView(R.id.back)
  ImageView mBack;
  @BindView(R.id.tv_order_id)
  TextView mTvOrderId;
  @BindView(R.id.tv_seed_count)
  TextView mTvSeedCount;
  @BindView(R.id.tv_order_time)
  TextView mTvOrderTime;
  @BindView(R.id.tv_pay)
  TextView mTvPay;
  @BindView(R.id.tv_time_left)
  TextView tv_time_left;
  private String mPrice;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_match_frament_details;
  }

  @Override
  public void initView() {
    tv_time_left.setText(getString(R.string.apply_time));

    Intent intent = getIntent();
    if (null != intent) {
      Bundle item = intent.getBundleExtra("item");
      if (item != null) {
        JsbzlistBean itemSerializable = (JsbzlistBean) item.getSerializable("item");
        mPrice = item.getString("price");
        updatePage(itemSerializable);
      }

    }

  }

  private void updatePage(JsbzlistBean itemSerializable) {
    if (null == itemSerializable) {
      return;
    }
    DebugUtils.setStringValue(itemSerializable.getOrderid(), "", mTvOrderId);
    DebugUtils.setStringValue(itemSerializable.getSeedcount(), "", mTvSeedCount);
    DebugUtils.setStringValue(itemSerializable.getApplytime(), "", mTvOrderTime);

    String multile = DemicalUtil.multile(DebugUtils.convert(itemSerializable.getSeedcount(), "0"),
        DebugUtils.convert(mPrice, "0"));
    DebugUtils.setStringValue("¥" + StringUtil.getThreeString(multile), "", mTvPay);

  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {

  }


  @OnClick(R.id.back)
  public void onViewClicked() {
    finish();
  }
}

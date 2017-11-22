package com.example.administrator.lubanone.activity.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.UserCardInfoAdapter;
import com.example.administrator.lubanone.bean.homepage.UserCardInfoBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 银行卡/账户
 */
public class CardAccountActivity extends BaseActivity {


  //请求用户已添加的银行卡信息
  RequestListener mBankInfoListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
      hideLandingDialog();
      //TODO 曲洋
      udpateList();
    }

    @Override
    public void testSuccess(String jsonObject) {
      //TODO 曲洋
      show(jsonObject);
      hideLandingDialog();
      udpateList();
    }

    @Override
    public void onFail(String errorMsf) {
      hideLandingDialog();
      show(errorMsf);
    }
  };


  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.lv)
  ListView mLv;
  @BindView(R.id.activity_card_account)
  LinearLayout mActivityCardAccount;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_card_account;
  }

  @Override
  public void initView() {

  }

  @Override
  public void loadData() {

    if (Config.isOnline) {
      judgeNet();
    }

    showLandingDialog();

    RequestNet requestNet = new RequestNet(myApp,this,null, Urls.TEST_URL, null,
        Config.isOnline ? RequestNet.POST : RequestNet.GET);

  }

  @Override
  public void onClick(View v) {

  }

  @OnClick(R.id.iv_back)
  public void onViewClicked() {
    finish();
  }

  //填充list
  private void udpateList() {
    List<UserCardInfoBean> list = new ArrayList<>();
    for (int a = 0; a < 10; a++) {
      UserCardInfoBean userCardInfoBean = new UserCardInfoBean();
      userCardInfoBean.setCardBand("中国银行" + a);
      list.add(userCardInfoBean);
    }

    mLv.setAdapter(new UserCardInfoAdapter(getApplicationContext(), list));

  }

}

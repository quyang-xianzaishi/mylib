package com.example.administrator.lubanone.activity.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.GsonUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 卖方会员信息
 */
public class VIPInfoActivity extends BaseActivity {


  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.frameLayout)
  RelativeLayout mFrameLayout;
  @BindView(R.id.lv)
  ListView mLv;
  @BindView(R.id.tv_back)
  TextView tvBack;


  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        getListInfo(result);
      } catch (Exception e) {
        hideLandingDialog();
        showMsg(getInfo(R.string.PARSE_RESULT_ERROR));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideLandingDialog();
      showMsg(errorMsf);
    }
  };

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_vipinfo;
  }

  @Override
  public void initView() {
    tvBack.setOnClickListener(this);
  }

  @Override
  public void loadData() {

    showLandingDialog();
    List<RequestParams> list = new ArrayList<>();

//    RequestNet requestNet = new RequestNet(list, Urls.VIP_INFO, mListener, RequestNet.POST);

  }


  private void getListInfo(Result<Object> result) {
    hideLandingDialog();
    if (null == result) {
      showMsg(getInfo(R.string.PARSE_RESULT_ERROR));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      updatepage(result);
    } else {
      showMsg(ResultUtil.getErrorMsg(result));
    }
  }

  private void updatepage(Result<Object> result) {

  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_back:
        finish();
        break;
      default:
        break;
    }

  }

  @OnClick(R.id.iv_back)
  public void onViewClicked() {
    finish();
  }
}

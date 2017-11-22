package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.NewAlwaysProblemsAdapter;
import com.example.administrator.lubanone.bean.homepage.AlwaysProblesResulBean;
import com.example.administrator.lubanone.bean.homepage.AlwaysProblesResulBean.ResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.SPUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 常见问题
 */
public class AlwaysProblemsActivity extends BaseActivity implements OnItemClickListener {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.frameLayout)
  RelativeLayout mFrameLayout;
  @BindView(R.id.lv)
  ListView mLv;
  @BindView(R.id.activity_always_problems)
  LinearLayout mActivityAlwaysProblems;

  private RequestListener mGetProblesListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Gson gson = new Gson();
        AlwaysProblesResulBean bean = gson
            .fromJson(jsonObject, AlwaysProblesResulBean.class);
        getProblesList(bean);
      } catch (Exception e) {
        showMsg(getString(R.string.get_always_probles_info_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.get_always_probles_info_fail));
    }
  };
  private String mKey;
  private List<ResultBean> mBeanList;


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_always_problems;
  }


  @Override
  public void initView() {

    Intent intent = getIntent();
    if (null != intent) {
      mKey = intent.getStringExtra("key");
    }

    mLv.setOnItemClickListener(this);
    mTvBack.setOnClickListener(this);

  }

  @Override
  public void loadData() {
    try {
      judgeNet();
      checkParams();
      List<RequestParams> paramsList = getParamsList();
      RequestNet requestNet = new RequestNet(myApp, this, paramsList, Urls.ALWAYS_PROBLES,
          mGetProblesListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.get_always_probles_info_fail));
    }
  }

  private void getProblesList(AlwaysProblesResulBean result) {
    if (null == result) {
      showMsg(getString(R.string.get_always_probles_info_fail));
      return;
    }
    String type = result.getType();
    String msg = result.getMsg();
    if ("1".equals(type)) {
      mBeanList = result.getResult();
      updatePage(mBeanList);
    } else {
      showMsg(DebugUtils.convert(msg,
          getString(R.string.get_always_probles_info_fail)));
    }
  }

  private void updatePage(List<ResultBean> beanList) {
    List<String> headList = new ArrayList<>();
    List<String> childsList = new ArrayList<>();
    for (ResultBean bean : beanList) {
      if (null == bean) {
        continue;
      }
      headList.add(bean.getTitle());
//      childsList.add(bean.getContent());
    }
    if (CollectionUtils.isEmpty(headList)) {
      return;
    }
    mLv.setAdapter(new NewAlwaysProblemsAdapter(this, beanList));
  }


  private List<RequestParams> getParamsList() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams requestParams = new RequestParams("type", getType(mKey));
    list.add(paramsToken);
    list.add(requestParams);
    return list;
  }

  private String getType(String key) {
    //0 会员等级,1 培训积分 2 信用评分;
    if ("level".equals(key)) {
      return 0 + "";

    }
    if ("train".equals(key)) {
      return 1 + "";

    }
    if ("credit".equals(key)) {
      return 2 + "";

    }
    return "";
  }

  private void checkParams() {

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

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (CollectionUtils.isNotEmpty(mBeanList)) {
      ResultBean itemAtPosition = mBeanList.get(position);
      Intent intent = new Intent(this, AlwaysProblemDetailsActivity.class);
      Bundle bundle = new Bundle();
      bundle.putSerializable("item", itemAtPosition);
      intent.putExtra("item", bundle);
      startActivity(intent);
    }
  }
}

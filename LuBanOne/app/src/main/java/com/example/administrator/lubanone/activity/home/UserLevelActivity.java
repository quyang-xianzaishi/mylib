package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.VIPLevelResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 用户级别
 */
public class UserLevelActivity extends BaseActivity {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.one)
  FrameLayout mOne;
  @BindView(R.id.tv_vip_level)
  TextView mTvVipLevel;
  @BindView(R.id.tv_team_count)
  TextView mTvTeamCount;
  @BindView(R.id.tv_zhitui_count)
  TextView mTvZhituiCount;
  @BindView(R.id.tv_my_team)
  TextView mTvMyTeam;
  @BindView(R.id.tv_vip_system)
  TextView mTvVipSystem;
  @BindView(R.id.tv_always_problems)
  TextView mTvAlwaysProblems;
  @BindView(R.id.activity_user_level)
  LinearLayout mActivityUserLevel;
  @BindView(R.id.tv_back)
  TextView tvBack;
  private String mCodeSize;
  @BindView(R.id.iv_icon)
  ImageView ivIcon;
  @BindView(R.id.tv_grade)
  TextView tv_grade;


  //获取会员级别信息
  private RequestListener mGetVIPListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<VIPLevelResultBean> result = GsonUtil
            .processJson(jsonObject, VIPLevelResultBean.class);
        getVIPInfo(result);
      } catch (Exception e) {
        showMsg(getString(R.string.GET_DATE_FAIL));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.GET_DATE_FAIL));
    }
  };

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_user_level;
  }

  @Override
  public void initView() {

    tv_grade.setText(getString(R.string.current_member_level));

    Intent intent = getIntent();
    if (null != intent) {
      mCodeSize = intent.getStringExtra("code_size");//激活码个数
    }

  }

  @Override
  public void loadData() {
    try {
      judgeNet();
      List<RequestParams> paramList = getParamList();
      RequestNet requestNet = new RequestNet(myApp, this, paramList, Urls.VIP_LEVEL,
          mGetVIPListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.GET_DATE_FAIL)));
    }
  }

  private void getVIPInfo(Result<VIPLevelResultBean> result) {
    if (null == result.getResult()) {
      showMsg(getString(R.string.get_vip_level_info_error));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      updatePage(result.getResult());
    } else {
      showMsg(DebugUtils
          .convert(ResultUtil.getErrorMsg(result), getString(R.string.get_vip_level_info_error)));
    }
  }

  private void updatePage(VIPLevelResultBean result) {
    if (null == result) {
      return;
    }
//    Glide.with(this).load(getUserLevelImageId(result.getLevel())).diskCacheStrategy(
//        DiskCacheStrategy.ALL).into(ivIcon);
    ivIcon.setBackgroundResource(getUserLevelImageId(result.getLevel()));
    tv_grade.setText(StringUtil.getBufferString(getString(R.string.current_member_level), DebugUtils
        .convert(result.getLevel(), Config.default_value)));
    mTvTeamCount.setText(DebugUtils.convert(result.getTeamnum(), Config.default_value));
    mTvZhituiCount.setText(DebugUtils.convert(result.getDirectnum(), Config.default_value));
  }

  public int getUserLevelImageId(String a) {
    if (TextUitl.isEmpty(a)) {
      return R.mipmap.grade_m0;
    }
    if ("M0".equals(a)) {
      return R.mipmap.grade_m0;
    }
    if ("M1".equals(a)) {
      return R.mipmap.grade_m1;
    }
    if ("M2".equals(a)) {
      return R.mipmap.grade_m2;
    }
    if ("M3".equals(a)) {
      return R.mipmap.grade_m3;
    }
    if ("M4".equals(a)) {
      return R.mipmap.grade_m4;
    }
    if ("M5".equals(a)) {
      return R.mipmap.grade_m5;
    }
    if ("M6".equals(a)) {
      return R.mipmap.grade_m6;
    } else {
      return R.mipmap.grade_m7;
    }
  }


  private List<RequestParams> getParamList() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    list.add(paramsToken);
    return list;
  }

  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.iv_back, R.id.tv_my_team, R.id.tv_vip_system, R.id.tv_always_problems,
      R.id.tv_back})
  public void onViewClicked(View view) {
    Intent intent = null;
    switch (view.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
        finish();
        break;
      case R.id.tv_my_team:
        intent = new Intent(this, MyTeamActivityNew.class);
        intent.putExtra("code_size", DebugUtils.convert(mCodeSize, "0"));
        break;
      case R.id.tv_vip_system:
        intent = new Intent(this, UserLevelSystemActivity.class);
        break;
      case R.id.tv_always_problems:
        intent = new Intent(this, AlwaysProblemsActivity.class);
        intent.putExtra("key", "level");
        break;
      default:
        break;
    }
    if (null != intent) {
      startActivity(intent);
    }
  }
}

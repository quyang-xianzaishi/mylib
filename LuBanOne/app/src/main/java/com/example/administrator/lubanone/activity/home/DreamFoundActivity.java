package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.DreamFoundNewsBean;
import com.example.administrator.lubanone.bean.homepage.DreamFoundNewsBean.AboutlistBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableScrollView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 梦想基金池
 */
public class DreamFoundActivity extends BaseActivity implements OnListViewItemListener,
    OnScrollListener {


  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.one)
  RelativeLayout mOne;
  @BindView(R.id.btn_commit)
  Button mBtnCommit;
  @BindView(R.id.lv)
  PullableScrollView mLv;
  @BindView(R.id.task_fragment_pullToRefreshLayout)
  PullToRefreshLayout pull;
  @BindView(R.id.activity_dream_found)
  LinearLayout mActivityDreamFound;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.ll_container)
  LinearLayout ll_container;
  private List<AboutlistBean> newList = new ArrayList<>();
  private Boolean getMore;
  private int size = 5;
  private int itemCount;
  private TextView mTvDreamFound;
  private PullToRefreshLayout mEmptyLayout;
  private int mLastVisiblePosition;
  private Boolean getDownMore;


  private RequestListener mNewsListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<DreamFoundNewsBean> result = GsonUtil
            .processJson(jsonObject, DreamFoundNewsBean.class);
        getNewList(result);
      } catch (Exception e) {
        showEmptyLayout(true);
        fail();
        resetGetMore(true);
        resetEmptyRefresh(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      fail();
      showEmptyLayout(true);
      resetGetMore(true);
      resetEmptyRefresh(false);
    }
  };


  private RequestListener mCommitListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
      hideCommitDialog();
      thankActivity();
    }

    @Override
    public void testSuccess(String jsonObject) {
      hideCommitDialog();
      thankActivity();
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDialog();
      showMsg(errorMsf);
    }
  };

  private void thankActivity() {
    startNewActivity(this, ThankActivity.class);
  }


  public void resetGetMore(boolean toTargetPosition) {
    if (null != getMore && getMore) {
      getMore = false;
      if (toTargetPosition) {
      }
    }
  }

  public void resetGetDownMore() {
    if (null != getDownMore && getDownMore) {
      getDownMore = false;
    }
  }

  @Override
  protected void beforeSetContentView() {

  }


  public void showEmptyMsg() {
    if (null != emptyRefresh && emptyRefresh) {
      showMsg(getString(R.string.no_more_message));
    }
  }


  private Boolean emptyRefresh;

  public void resetEmptyRefresh(boolean showMsg) {
    if (showMsg) {
      showEmptyMsg();
    }
    if (null != emptyRefresh && emptyRefresh) {
      emptyRefresh = false;
    }
  }

  //滑动监听
  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {

  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
      int totalItemCount) {
    mLastVisiblePosition = view.getLastVisiblePosition();
  }

  public class EmptyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      emptyRefresh = true;
      sendRequest();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
    }
  }


  @Override
  protected int getContentViewId() {
    return R.layout.activity_dream_found;
  }

  @Override
  public void initView() {
    mTvDreamFound = (TextView) findViewById(R.id.tv_dream_found);
    mEmptyLayout = (PullToRefreshLayout) findViewById(R.id.empty_layout);

    EmptyRefreshListener listener1 = new EmptyRefreshListener();
    mEmptyLayout.setOnPullListener(listener1);
    mEmptyLayout.setPullUpEnable(false);

    Intent intent = getIntent();
    if (null != intent) {
      String stringExtra = intent.getStringExtra(Config.USER_DREAM_PACKAGE_KEY);
      String threeString = StringUtil.getThreeString(StringUtil
          .getBufferString(stringExtra));

      if (TextUitl.isNotEmpty(threeString)) {
        mTvDreamFound.setText(StringUtil.getThreeString(threeString));
      } else {
        mTvDreamFound.setText("0");
      }

    }

    //滑动监听
    RefreshListener listener = new RefreshListener();
    pull.setOnPullListener(listener);
    pull.setPullUpEnable(true);

//    mLv.setOnScrollListener(this);

  }


  public void setEmptyFail() {
    if (mEmptyLayout != null && mEmptyLayout.isShown()) {
      mEmptyLayout.refreshFinish(PullToRefreshLayout.FAIL);
    }
  }

  public void setEmptySuccess() {
    if (mEmptyLayout != null && mEmptyLayout.isShown()) {
      mEmptyLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
  }


  public void showEmptyLayout(boolean yes) {
    if (yes) {
      mEmptyLayout.setVisibility(View.VISIBLE);
      pull.setVisibility(View.GONE);
    } else {
      mEmptyLayout.setVisibility(View.GONE);
      pull.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void loadData() {
    sendRequest();
  }

  @Override
  public void onClick(View v) {

  }


  @OnClick({R.id.iv_back, R.id.btn_commit, R.id.tv_back})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
        finish();
        break;
      case R.id.btn_commit:
        commit();
        break;
      default:
        break;
    }
  }

  @Override
  public void onItem(Object object, int position) {
    AboutlistBean resultBean = (AboutlistBean) object;
    Intent intent = new Intent(this
        , DreamFoundNewsDetailsActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("item", resultBean);
    intent.putExtra("bundle", bundle);
    startActivity(intent);
  }


  //下拉监听
  public class RefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      sendRequest();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      getMore = true;
      sendRequest();
    }
  }

  private void updatePage(List<AboutlistBean> result) {
    success();
    newList.addAll(result);
    showEmptyLayout(false);
    resetEmptyRefresh(false);
    if (null != getMore && getMore) {
      getMore = false;
      if (itemCount == newList.size()) {
        showMsg(getString(R.string.no_more_message));
      }
    }
    itemCount = newList.size();
//    mLv.setAdapter(new DreamFoundAdapter(this, newList, this));
    setAdapter(newList);
  }

  private void setAdapter(List<AboutlistBean> newList) {

    ll_container.removeAllViews();
    for (AboutlistBean bean : newList) {
      if (null == bean) {
        continue;
      }
      View inflate = getLayoutInflater().inflate(R.layout.dream_found_item, null);

      View root = inflate.findViewById(R.id.root);

      SimpleDraweeView ivIcon = (SimpleDraweeView) inflate.findViewById(R.id.iv_icon);
      TextView tvTitle = (TextView) inflate.findViewById(R.id.tv_title);
      TextView tvContent = (TextView) inflate.findViewById(R.id.tv_content);

      if (TextUitl.isEmpty(bean.getThumimg())) {
      } else {
        ivIcon.setImageURI(Uri.parse(bean.getThumimg()));
      }

      tvTitle.setText(bean.getTitle());
      tvContent.setText(bean.getDatetime());

      root.setOnClickListener(new MyClick(bean));

      ll_container.addView(inflate);
    }

  }

  private class MyClick implements View.OnClickListener {

    private AboutlistBean mAboutlistBean;

    public MyClick(AboutlistBean bean) {
      this.mAboutlistBean = bean;
    }

    @Override
    public void onClick(View v) {
      Intent intent = new Intent(DreamFoundActivity.this
          , DreamFoundNewsDetailsActivity.class);
      Bundle bundle = new Bundle();
      bundle.putSerializable("item", mAboutlistBean);
      intent.putExtra("bundle", bundle);
      startActivity(intent);

    }
  }

  //获取新闻列表
  private void sendRequest() {
    try {
      judgeNet();
      List<RequestParams> paramList = getParamList();
      RequestNet requestNet = new RequestNet(myApp, this, paramList, Urls.DREAM_FOUND_NEWS,
          mNewsListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.get_news_fail));
      resetEmptyRefresh(false);
      setEmptyFail();
    }
  }


  private void getNewList(Result<DreamFoundNewsBean> dreamFoundNewsBean) {
    if (null == dreamFoundNewsBean || dreamFoundNewsBean.getResult() == null) {
      fail();
      showEmptyLayout(true);
      resetGetMore(true);
      resetEmptyRefresh(false);
      return;
    }
    if ("0".equals(dreamFoundNewsBean.getType())) {
      fail();
      showEmptyLayout(true);
      resetGetMore(true);
      resetEmptyRefresh(false);
    } else if ("1".equals(dreamFoundNewsBean.getType())) {
      List<AboutlistBean> result = dreamFoundNewsBean.getResult().getAboutlist();
      newList.clear();
      if (CollectionUtils.isEmpty(result)) {
        showEmptyLayout(true);
        fail();
        resetGetMore(true);
        resetEmptyRefresh(false);
      } else {
        updatePage(result);
      }
    }
  }

  private List<RequestParams> getParamList() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsType = new RequestParams("if_type", "news");

    if (null != getMore && getMore) {
      size = newList.size() + 5;
    } else {
      size = newList.size() == 0 ? size : newList.size() + 5;
    }
    RequestParams paramsPage = new RequestParams("number", size + "");

    list.add(paramsToken);
    list.add(paramsType);
    list.add(paramsPage);
    return list;
  }

  private void commit() {
    thankActivity();
  }

  public void success() {
    if (null != pull && pull.isShown()) {
      pull.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
  }


  public void fail() {
    if (null != pull && pull.isShown()) {
      pull.refreshFinish(PullToRefreshLayout.FAIL);
    }
  }
}

package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.pull;
import static com.example.administrator.lubanone.R.id.tv_member_level;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.message.FriendTestActivity;
import com.example.administrator.lubanone.bean.message.MemberInfoBeanNew;
import com.example.administrator.lubanone.bean.message.MemberInfoBeanNew.FriendType;
import com.example.administrator.lubanone.bean.message.MemberInfoBeanNew.PayinformationBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableScrollView;
import io.rong.imkit.RongIM;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 买种子会员信息
 * Created by Administrator on 2017\7\3 0003.
 */

public class SeedsMemberInfoActivity extends BaseActivity implements OnClickListener {


  @BindView(R.id.back)
  TextView mBack;
  @BindView(R.id.title)
  TextView mTitle;
  @BindView(R.id.iv_icon)
  SimpleDraweeView mIvIcon;
  @BindView(R.id.tv_name)
  TextView mTvName;
  @BindView(tv_member_level)
  TextView mTvMemberLevel;
  @BindView(R.id.tv_user_assign)
  TextView mTvUserAssign;
  @BindView(R.id.btn)
  Button mBtn;
  @BindView(R.id.lv)
  PullableScrollView mLv;
  @BindView(pull)
  PullToRefreshLayout mPull;
  @BindView(R.id.ll_container)
  LinearLayout ll_container;

  private Boolean getMore;
  private String mUserid;
  private int size = 5;

  private int count;


  private RequestListener getMemberInfoListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        success();
        Result<MemberInfoBeanNew> result = GsonUtil
            .processJson(jsonObject, MemberInfoBeanNew.class);
        judgetResult(result);
      } catch (Exception e) {
        ToastUtil.showShort(getResources().getString(R.string.get_member_info_fail),
            getApplicationContext());
        resetGetMore();
      }

    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(getResources().getString(R.string.get_member_info_fail),
          getApplicationContext());
      resetGetMore();
      fail();
    }
  };

  private void judgetResult(Result<MemberInfoBeanNew> result) {

    if (ResultUtil.isSuccess(result)) {
      if (result.getResult() == null) {
        showMsg(getString(R.string.get_member_info_fail));
        resetGetMore();
        hideCommitDataDialog();
        return;
      }
      adapterDate(result.getResult());
    } else {
      resetGetMore();
      hideCommitDataDialog();
      showMsg(DebugUtils.convert(result.getMsg(), getString(R.string.get_member_info_fail)));
    }
  }

  private MemberInfoBeanNew mResult;

  private void adapterDate(MemberInfoBeanNew result) {
    mResult = result;

    //头像url
    String userimg = mResult.getHeaderimage();

    //用户名
    String username = mResult.getUsername();

    //个人签名
    String autograph = mResult.getAutograph();

    //用户级别
    String levelname = mResult.getLevel();

    //设置头像
    if (TextUitl.isNotEmpty(userimg)) {
//      Glide.with(this).load(userimg).diskCacheStrategy(DiskCacheStrategy.ALL).into(mIvIcon);
      mIvIcon.setImageURI(userimg);
    } else {
      mIvIcon.setBackgroundResource(R.mipmap.pho_tx);
    }

    //设置用户名
    mTvName.setText(getString(R.string.user_namess) + "：" + DebugUtils.convert(username, ""));

    //设置用户级别
    mTvMemberLevel
        .setText(getString(R.string.vip_level) + ": " + DebugUtils.convert(levelname, ""));

    //设置签名
    if (!TextUitl.isEmpty(autograph)) {

      /*String s = getString(R.string.user_assign) + "   " + (autograph.length() > 10 ? autograph
          .substring(0, 10) + "..." : autograph);*/

      String s = (autograph.length() > 10 ? autograph
          .substring(0, 10) + "..." : autograph);
      char charAt = autograph.charAt(0);

      StringUtil.setTextSizeNewOne(s, mTvUserAssign, 13, 13, charAt, charAt,
          ColorUtil.getColor(this, R.color.c333));
    } else {
      //mTvUserAssign.setText(getString(R.string.user_assign));
    }

    //添加好友
    if (TextUitl.isNotEmpty(mResult.getZt())) {
      if (FriendType.friend.equals(result.getZt())) {
        //非好友关系
        mBtn.setText(getString(R.string.add_friend_btn));
      } else if (FriendType.send.equals(mResult.getZt())) {
        //好友关系
        mBtn.setText(getString(R.string.send_conversation_message));
      } else {
        mBtn.setEnabled(false);
      }
    }

    //适配列表
    setList(result.getPayinformation(), result.getQrcode());


  }


  public void fail() {
    if (mPull != null && mPull.isShown()) {
      mPull.refreshFinish(PullToRefreshLayout.FAIL);
    }
  }

  public void success() {
    if (null != mPull && mPull.isShown()) {
      mPull.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
  }

  private void setList(List<PayinformationBean> list, String alipay) {
    int size = list.size();
    ll_container.removeAllViews();
    if (CollectionUtils.isNotEmpty(list)) {

      if (null != getMore && getMore) {
        if (count == list.size()) {
          getMore = false;
          showMsg(getString(R.string.no_more_message));
        }
      }

      for (PayinformationBean bean : list) {
        if (null == bean) {
          continue;
        }
        int i = list.indexOf(bean) + 1;

        View inflate = LayoutInflater.from(this).inflate(R.layout.buy_sell_member_info, null);
        TextView tv = (TextView) inflate.findViewById(R.id.tv);
        TextView tv_open_bank = (TextView) inflate.findViewById(R.id.tv_open_bank);
        TextView tv_bank_account = (TextView) inflate.findViewById(R.id.tv_bank_account);
        TextView tv_receiver = (TextView) inflate.findViewById(R.id.tv_receiver);
        TextView tv_phone = (TextView) inflate.findViewById(R.id.tv_phone);
        TextView tv_bank_code = (TextView) inflate.findViewById(R.id.tv_bank_code);

        tv.setText(getString(R.string.receive_money_info) + (i < 9 ? "0" + i : i + ""));
        tv_open_bank.setText(DebugUtils.convert(bean.getBankname(), ""));
        tv_bank_account.setText(DebugUtils.convert(bean.getBankaccount(), ""));
        tv_receiver.setText(DebugUtils.convert(bean.getTruename(), ""));
        tv_phone.setText(DebugUtils.convert(bean.getPhone(), ""));
        tv_bank_code.setText(DebugUtils.convert(bean.getBankcode(), ""));

        ll_container.addView(inflate);
      }

      count = ll_container.getChildCount();
    }

    if (TextUitl.isNotEmpty(alipay)) {
      View inflate = LayoutInflater.from(this).inflate(R.layout.member_infos, null);
      TextView tv = (TextView) inflate.findViewById(R.id.tv);
      ImageView iv = (ImageView) inflate.findViewById(R.id.iv);
      int a = size + 1;
      tv.setText(getString(R.string.receive_money_info) + (a < 9 ? "0" + a : a + ""));
      Glide.with(this).load(alipay).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);

      ll_container.addView(inflate);
    }

    if (CollectionUtils.isEmpty(list) && TextUitl.isEmpty(alipay)) {
      ll_container.removeAllViews();
      View inflate = LayoutInflater.from(this).inflate(R.layout.member_empty_info, null);
      ll_container.addView(inflate);
    }

    hideCommitDataDialog();
  }

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_member_info_new;
  }

  @Override
  public void initView() {

    //设置用户名
    mTvName.setText(getString(R.string.user_namess) + "：");

    Intent intent = getIntent();
    if (null != intent) {
      mUserid = intent.getStringExtra("userId");
    }

    //下拉熟悉
    mLv = (PullableScrollView) findViewById(R.id.lv);
    mPull = (PullToRefreshLayout) findViewById(R.id.pull);
    //滑动监听
    RefreshListener listener = new RefreshListener();
    mPull.setOnPullListener(listener);
    mPull.setPullUpEnable(true);
  }


  @Override
  public String setTip() {
    return getString(R.string.landing_date);
  }

  @Override
  public void loadData() {
    try {
      judgeNet();
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsUserId = new RequestParams("sellmember", DebugUtils.convert(mUserid, ""));

      if (null != getMore && getMore) {
        size = ll_container.getChildCount() + 5;
      } else {
        size = ll_container.getChildCount() == 0 ? size : ll_container.getChildCount();
      }

      RequestParams paramsPage = new RequestParams("number", size + "");

      Log.e("SeedsMemberInfoActivity", "loadData=size=" + size + ",token=" + SPUtils
          .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, "")
          + "=buymember=" + mUserid);

      list.add(paramsToken);
      list.add(paramsUserId);
      list.add(paramsPage);
      RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this, list,
          Urls.SELL_MEMBER_INFO, getMemberInfoListener, RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.GET_DATE_FAIL));
      resetGetMore();
    }
  }

  private void resetGetMore() {
    if (null != getMore && getMore) {
      getMore = false;
    }
  }


  public class RefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      loadData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      getMore = true;
      loadData();
    }

  }


  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.back, R.id.btn})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back:
        finish();
        break;
      case R.id.btn:
        if (null != mResult && TextUitl.isNotEmpty(mResult.getZt())) {
          if (FriendType.friend.equals(mResult.getZt())) {
            Intent intent = new Intent();
            intent.putExtra("applyId", mResult.getUserid());
            intent.setClass(this, FriendTestActivity.class);
            startActivity(intent);
          } else if (FriendType.send.equals(mResult.getZt())) {
            //好友关系
            if (TextUitl.isNotEmpty(mResult.getUserid())) {
              RongIM.getInstance().startPrivateChat(this, mResult.getUserid(),
                  mResult.getUsername());
            }
          }
        }
        break;
    }
  }
}

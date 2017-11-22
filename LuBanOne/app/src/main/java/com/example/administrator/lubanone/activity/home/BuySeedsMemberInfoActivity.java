package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.tv_member_level;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.message.FriendTestActivity;
import com.example.administrator.lubanone.bean.message.MemberInfoBeanNewBuy;
import com.example.administrator.lubanone.bean.message.MemberInfoBeanNewBuy.FriendType;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import io.rong.imkit.RongIM;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 卖种子会员信息
 * Created by Administrator on 2017\7\3 0003.
 */

public class BuySeedsMemberInfoActivity extends BaseActivity implements OnClickListener {


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
        Result<MemberInfoBeanNewBuy> result = GsonUtil
            .processJson(jsonObject, MemberInfoBeanNewBuy.class);
        judgetResult(result);
      } catch (Exception e) {
        ToastUtil.showShort(getResources().getString(R.string.get_member_info_fail),
            getApplicationContext());
      }

    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(getResources().getString(R.string.get_member_info_fail),
          getApplicationContext());
    }
  };

  private void judgetResult(Result<MemberInfoBeanNewBuy> result) {

    if (ResultUtil.isSuccess(result)) {
      if (result.getResult() == null) {
        showMsg(getString(R.string.get_member_info_fail));
        resetGetMore();
        return;
      }
      adapterDate(result.getResult());
    } else {
      resetGetMore();
      showMsg(DebugUtils.convert(result.getMsg(), getString(R.string.get_member_info_fail)));
    }


  }

  private MemberInfoBeanNewBuy mResult;

  private void adapterDate(MemberInfoBeanNewBuy result) {

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
      if (FriendType.is_friend.equals(result.getZt())) {
        //非好友关系
        mBtn.setText(getString(R.string.add_friend_btn));
      } else if (FriendType.no_friend.equals(mResult.getZt())) {
        //好友关系
        mBtn.setText(getString(R.string.send_conversation_message));
      } else {
        mBtn.setEnabled(false);
      }
    }
  }


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_member_info_new_buy;
  }

  @Override
  public void initView() {

    //设置用户名
    mTvName.setText(getString(R.string.user_namess) + "：");

    Intent intent = getIntent();
    if (null != intent) {
      mUserid = intent.getStringExtra("userId");
    }
  }

  @Override
  public void loadData() {
    try {
      judgeNet();
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsUserId = new RequestParams("buymember", DebugUtils.convert(mUserid, ""));

      list.add(paramsToken);
      list.add(paramsUserId);
      RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this, list,
          Urls.BUY_MEMBER_INFO, getMemberInfoListener, RequestNet.POST);
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
          if (FriendType.is_friend.equals(mResult.getZt())) {
            Intent intent = new Intent();
            intent.putExtra("applyId", mResult.getUserid());
            intent.setClass(this, FriendTestActivity.class);
            startActivity(intent);
          } else if (FriendType.no_friend.equals(mResult.getZt())) {
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

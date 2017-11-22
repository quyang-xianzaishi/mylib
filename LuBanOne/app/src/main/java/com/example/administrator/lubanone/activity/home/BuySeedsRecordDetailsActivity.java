package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.tv_reap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.BuyRecordResultBean.ResultBean;
import com.example.administrator.lubanone.bean.homepage.NewHomeFragmentResultBean.GrowseedslistBean;
import com.example.administrator.lubanone.bean.homepage.NewHomeFragmentResultBean.GrowseedslistBean.ShouGeType;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.administrator.lubanone.utils.ViewsUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import java.util.ArrayList;
import org.json.JSONObject;

public class BuySeedsRecordDetailsActivity extends BaseActivity {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_title)
  TextView mTvTitle;
  @BindView(R.id.frameLayout)
  LinearLayout mFrameLayout;
  @BindView(R.id.tv_get_seed_count)
  TextView mTvGetSeedCount;
  @BindView(R.id.textView14)
  TextView mTextView14;
  @BindView(R.id.tv_order_id)
  TextView mTvOrderId;
  @BindView(R.id.tv_seed_count)
  TextView mTvSeedCount;
  @BindView(R.id.tv_match_time)
  TextView mTvMatchTime;
  @BindView(R.id.tv_money_time)
  TextView mTvMoneyTime;
  @BindView(R.id.tv_complete_time)
  TextView mTvCompleteTime;
  @BindView(R.id.tv_money_receiver)
  TextView mTvMoneyReceiver;
  @BindView(R.id.textView15)
  TextView mTextView15;
  @BindView(R.id.tv_grow_status)
  TextView mTvGrowStatus;
  @BindView(R.id.tv_grow_day)
  TextView mTvGrowDay;
  @BindView(R.id.tv_reap)
  TextView mTvReap;
  @BindView(R.id.tv_get_pingfeng)
  TextView tv_get_pingfeng;
  private ResultBean mItem;

  private String mOrderId;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_buy_seeds_record_details;
  }

  @Override
  public void initView() {

    mTvTitle.setText(getString(R.string.seeds_details));

    Intent intent = getIntent();
    if (null != intent) {
      Bundle bundleExtra = intent.getBundleExtra("item");
      if (null != bundleExtra) {
        mItem = (ResultBean) bundleExtra.getSerializable("item");
        if (null != mItem) {
          updatePage(mItem);
        }
      }

      Bundle items = intent.getBundleExtra("items");
      if (null != items) {
        GrowseedslistBean serializable = (GrowseedslistBean) items.getSerializable("items");
        if (null != serializable) {
          updatePageOne(serializable);
        }
      }
    }
  }

  private void updatePageOne(GrowseedslistBean item) {

    mOrderId = item.getOrderid();

    //种子数量
    String s = item.getUser_jj_lx() + " PCS";
    StringUtil.setTextSize(s, mTvGetSeedCount, 25, 11);

    String s1 = DebugUtils.convert(item.getJb(), "0") + " PCS";
    StringUtil.setTextSize(s1, mTvSeedCount, 11, 9);

    DebugUtils.setStringValue(item.getOrderid(), "0", mTvOrderId);//订单编号
    DebugUtils.setStringValue(item.getDate(), "", mTvMatchTime);//匹配时间tv_match_time
    DebugUtils.setStringValue(item.getDate_hk(), "", mTvMoneyTime);//打款时间 tv_money_time
    DebugUtils.setStringValue(item.getDate_su(), "", mTvCompleteTime);//完成时间 tv_complete_time
    DebugUtils.setStringValue(item.getG_user(), "", mTvMoneyReceiver);//收款者 tv_money_receiver
    DebugUtils.setStringValue(item.getGpingjia(), "", tv_get_pingfeng);//获得评分
    DebugUtils.setStringValue(item.getZt(), "0", mTvGrowStatus);//成长状态 tv_grow_status
    DebugUtils.setStringValue(item.getUser_jj_ts(),
        "0" + getString(R.string.days), mTvGrowDay);//成长天数

    if (ShouGeType.HIDE.equals(item.getIsshouge())) {
      mTvReap.setVisibility(View.VISIBLE);
      mTvReap.setEnabled(false);
      mTvReap.setText(getString(R.string.wait_reap));
      mTvReap.setTextColor(ColorUtil.getColor(R.color.c888, this));
    } else {
      mTvReap.setVisibility(View.VISIBLE);
      mTvReap.setEnabled(true);
      mTvReap.setText(getString(R.string.reap_seeds));
      mTvReap.setTextColor(ColorUtil.getColor(R.color.red, this));
    }
  }

  private void alertConfimDialog() {
    StytledDialog
        .showTwoBtnDialog(this, getString(R.string.confim_reap), getString(R.string.reap_tips),
            getString(R.string.cancels), getString(R.string.confim), false, true,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
                dialog.dismiss();
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();
                reapSeeds();
              }
            });
  }

  private void updatePage(ResultBean item) {

    mOrderId = item.getOrderid();

    //种子数量
    String s = item.getUser_jj_lx() + " PCS";
    StringUtil.setTextSize(s, mTvGetSeedCount, 25, 11);

    String s1 = DebugUtils.convert(item.getJb(), "0") + " PCS";
    StringUtil.setTextSize(s1, mTvSeedCount, 11, 9);

//    DebugUtils.setStringValue(item.getUser_jj_lx(), "0", mTvGetSeedCount);//获得的种子数
//    DebugUtils.setStringValue(item.getJb(), "0", mTvSeedCount);//种子数量

    DebugUtils.setStringValue(item.getOrderid(), "0", mTvOrderId);//订单编号
    DebugUtils.setStringValue(item.getDate(), "", mTvMatchTime);//匹配时间tv_match_time
    DebugUtils.setStringValue(item.getDate_hk(), "", mTvMoneyTime);//打款时间 tv_money_time
    DebugUtils.setStringValue(item.getDate_su(), "", mTvCompleteTime);//完成时间 tv_complete_time
    DebugUtils.setStringValue(item.getG_user(), "", mTvMoneyReceiver);//收款者 tv_money_receiver
    DebugUtils.setStringValue(item.getGpingjia(), "", tv_get_pingfeng);//获得评分
    DebugUtils.setStringValue(item.getZt(), "0", mTvGrowStatus);//成长状态 tv_grow_status
    DebugUtils.setStringValue(item.getUser_jj_ts(), "0", mTvGrowDay);//成长天数

//    if (ReapType.HIDE.equals(item.getIsshouge())) {
//      mTvReap.setVisibility(View.VISIBLE);
//
//    } else {
//      mTvReap.setVisibility(View.VISIBLE);
//    }

    if (ShouGeType.HIDE.equals(item.getIsshouge())) {
      mTvReap.setVisibility(View.VISIBLE);
      mTvReap.setEnabled(false);
      mTvReap.setText(getString(R.string.wait_reap));
      mTvReap.setTextColor(ColorUtil.getColor(R.color.c888, this));
    } else {
      mTvReap.setVisibility(View.VISIBLE);
      mTvReap.setEnabled(true);
      mTvReap.setText(getString(R.string.reap_seeds));
      mTvReap.setTextColor(ColorUtil.getColor(R.color.red, this));
    }
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.iv_back, tv_reap})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
        finish();
        break;
      case tv_reap:
        alertConfimDialog();
        break;
    }
  }

  private void reapSeeds() {
    try {
      if (!NetUtil.isConnected(getApplicationContext())) {
        showMsg(getString(R.string.NET_ERROR));
        return;
      }

      if (TextUitl.isEmpty(mOrderId)) {
        showMsg(getString(R.string.reap_fail));
        return;
      }

      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderId = new RequestParams("orderid", mOrderId);
      list.add(paramsToken);
      list.add(paramsOrderId);
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.REAP_SEEDS, mReapListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.reap_fail));
    }
  }


  private RequestListener mReapListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        reapResult(result);
      } catch (Exception e) {
        show(getString(R.string.reap_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(errorMsf);
    }
  };


  private void reapResult(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.reap_success));
      ViewsUtils.setTextView(mTvReap, getString(R.string.has_reap_seeds), getApplicationContext());
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.reap_fail)));
    }
  }


}

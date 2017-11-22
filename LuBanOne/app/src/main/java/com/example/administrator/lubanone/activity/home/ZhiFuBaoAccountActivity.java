package com.example.administrator.lubanone.activity.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.event.ZhiFuBaoAccountActivityEvent;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.TextUitl;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class ZhiFuBaoAccountActivity extends BaseActivity {

  @BindView(R.id.title)
  TextView mTitle;
  @BindView(R.id.back)
  ImageView mBack;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.tv_save)
  ImageView mTvSave;
  @BindView(R.id.add_card)
  ImageView mAddCard;
  @BindView(R.id.rl_top)
  RelativeLayout mRlTop;
  @BindView(R.id.iv_erweima)
  ImageView mIvErweima;
  @BindView(R.id.tv_change_account)
  TextView mTvChangeAccount;
  @BindView(R.id.tv_delete_account)
  TextView mTvDeleteAccount;
  @BindView(R.id.rl_empty_layout)
  RelativeLayout mRlEmptyLayout;
  @BindView(R.id.real_layout)
  RelativeLayout real_layout;


  private boolean no = false;
  private String mAccount;


  private void showEmptyLayout(boolean show) {
    if (show) {
      mRlEmptyLayout.setVisibility(View.VISIBLE);
      real_layout.setVisibility(View.GONE);
    } else {
      mRlEmptyLayout.setVisibility(View.GONE);
      real_layout.setVisibility(View.VISIBLE);
    }
  }

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_zhi_fu_bao_account;
  }

  @Override
  public void initView() {
    EventBus.getDefault().register(this);

    Intent intent = getIntent();
    if (null != intent) {
      String url = intent.getStringExtra("url");

      mAccount = intent.getStringExtra("user_name");
      if (TextUitl.isNotEmpty(url)) {
        try {
          no = true;
          Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(mIvErweima);
        } catch (Exception e) {
          no = false;
        }
      } else {
        //显示空布局
        showEmptyLayout(true);
      }
    }
  }

  @Override
  public void loadData() {

  }

  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.tv_back, R.id.add_card, R.id.tv_change_account, R.id.tv_delete_account, R.id.back})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.tv_back:
      case R.id.back:
        finish();
        break;
      case R.id.add_card:
        if (!no) {
          Intent intent = new Intent(this, AliActivity.class);
          intent.putExtra("account", DebugUtils.convert(mAccount, ""));
          startActivity(intent);
        } else {
          alertWarmDialog();
        }

        break;
      case R.id.tv_change_account://更换
        Intent intent = new Intent(this, AliActivity.class);
        intent.putExtra("account", DebugUtils.convert(mAccount, ""));
        startActivity(intent);
        break;
      case R.id.tv_delete_account:
        alertDeleteDialog();
        break;
    }
  }

  private void alertWarmDialog() { //add_ali_tips
    StytledDialog
        .showOneBtn(true, ColorUtil.getColor(this, R.color.c000), this, null,
            getString(R.string.add_ali_tips), getString(R.string.close), null,
            null, true, true, new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
                dialog.dismiss();
              }

              @Override
              public void onSecond(DialogInterface dialog) {

              }
            });
  }

  private void alertDeleteDialog() {
    StytledDialog
        .showIosAlert(false, ColorUtil.getColor(this, R.color.c333), this,
            getString(R.string.confim_delete_account), null, getInfo(R.string.cancels),
            getString(R.string.confirm), null,
            false, true,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
                dialog.dismiss();
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();
                deleteRequest();
              }
            });
  }

  private void deleteRequest() {
    ArrayList<RequestParams> objects = new ArrayList<>();
    RequestParams requestParams = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
    objects.add(requestParams);
    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, objects, Urls.DELETE_ALI_ACCOUNT,
        mDeleteListener, RequestNet.POST);
  }

  @Override
  public String setTip() {
    return getString(R.string.delete_accounting);
  }

  private RequestListener mDeleteListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        hideCommitDataDialog();
        Result<Object> result = GsonUtil
            .processJson(jsonObject, Object.class);
        if (ResultUtil.isSuccess(result)) {
          showMsg(getString(R.string.delete_card_success));
          showEmptyLayout(true);
        } else {
          showMsg(
              DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.delete_fail)));
        }
      } catch (Exception e) {
        hideCommitDataDialog();
        show(getInfo(R.string.delete_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      show(getInfo(R.string.delete_fail));
    }
  };

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void updteone(ZhiFuBaoAccountActivityEvent event) {
    try {
      System.out.println("ZhiFuBaoAccountActivity.updteone=" + event.getAlipay());
      if (TextUitl.isNotEmpty(event.getAlipay())) {
        Glide.with(this).load(event.getAlipay()).diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mIvErweima);
        showEmptyLayout(false);
      }
    } catch (Exception e) {

    }
  }
}

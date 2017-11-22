package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.tv_dream_package_seeds_count;
import static com.example.administrator.lubanone.R.id.tv_receive_money;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bigkoo.pickerview.OptionsPickerView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.SellSeedsResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.example.qlibrary.utils.WindoswUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 卖出种子
 */
public class SellSeedsActivity extends BaseActivity implements OnFocusChangeListener {


  @BindView(R.id.iv_back_click)
  ImageView mIvBackClick;
  @BindView(tv_dream_package_seeds_count)
  TextView mTvDreamPackageSeedsCount;
  @BindView(R.id.tv_money_package_seeds_count)
  TextView mTvMoneyPackageSeedsCount;
  @BindView(R.id.tv_select_to_sell)
  TextView mTvSelectToSell;
  @BindView(tv_receive_money)
  TextView mTvReceiveMoney;
  @BindView(R.id.et_safe_code)
  EditText mEtSafeCode;
  @BindView(R.id.btn_commit)
  Button mBtnCommit;
  @BindView(R.id.activity_buy_seeds)
  LinearLayout mActivityBuySeeds;
  @BindView(R.id.et_to_sell)
  EditText mEtToSell;
  private String mPackageType;
  private String mPrice;
  @BindView(R.id.tv_back)
  TextView mTvBack;


  private RequestListener mSellSeedsListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        dismissLoadingDialog();
        Result<SellSeedsResultBean> result = GsonUtil
            .processJson(jsonObject, SellSeedsResultBean.class);
        getSellSeedInfo(result);
      } catch (Exception e) {
        showMsg(getInfo(R.string.GET_DATE_FAIL));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      dismissLoadingDialog();
      showMsg(getString(R.string.GET_DATE_FAIL));
    }
  };

  private RequestListener mCommitListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        commitInfo(result);
      } catch (Exception e) {
        hideCommitDataDialog();
        showMsg(getInfo(R.string.commits_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getString(R.string.commits_fail));
    }
  };
  private String mLeastseeds;


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_sell_seeds;
  }

  @Override
  public void initView() {

    mEtToSell.addTextChangedListener(new MyTextWatcher());

  }


  @Override
  public void loadData() {
    try {
      judgeNet();
      List<RequestParams> list = gettParamList();
      showLoadingDialog();
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SELL_SEEDS_COMMIT,
          mSellSeedsListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.GET_DATE_FAIL)));
      dismissLoadingDialog();
    }
  }

  private List<RequestParams> gettParamList() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    list.add(paramsToken);
    return list;
  }

  private void getSellSeedInfo(Result<SellSeedsResultBean> result) {
    isResultNullWithException(result, getInfo(R.string.GET_DATE_FAIL));
    if (ResultUtil.isSuccess(result)) {
      mTvDreamPackageSeedsCount.setText(result.getResult().getDreambag());
      mTvMoneyPackageSeedsCount.setText(result.getResult().getRewardbag());
      mPrice = result.getResult().getPrice();
      mLeastseeds = result.getResult().getLeastseeds();
      mTvReceiveMoney.setText(DebugUtils.convert(mPrice, ""));



    } else {
      showMsg(
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.GET_DATE_FAIL)));
    }
  }

  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.iv_back_click, R.id.tv_select_to_sell, R.id.btn_commit, R.id.tv_back})
  public void onViewClicked(View view) {
    try {
      switch (view.getId()) {
        case R.id.iv_back_click:
        case R.id.tv_back:
          finish();
          break;
        case R.id.btn_commit:
          commit();
          break;
        case R.id.tv_select_to_sell:
          select();
          break;
        default:
          break;
      }
    } catch (Exception e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }


  private void commit() {
    judgeNet();

    final String count = mEtToSell.getText().toString().trim();
    final String code = mEtSafeCode.getText().toString().trim();

    isEmptyWithException(mPackageType, getString(R.string.please_package_type));
    isEmptyWithException(count, getString(R.string.seeds_count_empty));
    if ("rewardbag".equals(mPackageType)) {
      isFalseWithException(DemicalUtil.less(count, DebugUtils.convert(mLeastseeds, "0")),
          getString(R.string.seed_count_less_least_sell));
    }

    isEmptyWithException(code, getString(R.string.safe_pwd_empty));
    isEmptyWithException(mTvReceiveMoney.getText().toString().trim(),
        getString(R.string.shoud_receive_money_empty));

    StytledDialog
        .showIosAlert(false, ColorUtil.getColor(this, R.color.c333), this,
            getInfo(R.string.is_commit_one),
            getMsg(count), getInfo(R.string.cancel), getInfo(R.string.commits), "",
            false,
            true,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();
                realCommit(count, code, mTvReceiveMoney.getText().toString().trim());
              }
            });
  }

  private String getMsg(String count) {
    StringBuilder sb = new StringBuilder();
    sb.append(getString(R.string.sell_seeds_count_is)).append(count)
        .append(getString(R.string.sell_seeds_count_one))
        .append(mTvReceiveMoney.getText().toString().trim())
        .append(getString(R.string.sell_seeds_count_two));
    return sb.toString();
  }

  private void realCommit(String count, String code, String shouldPay) {
    try {
      judgeNet();
      List<RequestParams> list = getRealCommitParams(count, code);
      showCommitDataDialog();
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SELL_SEEDS_COMMIT_INFO,
          mCommitListener,
          RequestNet.POST);
    } catch (DefineException e) {
      showMsg(e.getMessage());
    }
  }

  @Override
  public String setTip() {
    return getString(R.string.commiting);
  }

  private List<RequestParams> getRealCommitParams(String count, String code) {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN, SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsPackageType = new RequestParams("bagtype", mPackageType);
    RequestParams paramsCode = new RequestParams("paypwd", code);
    RequestParams paramsCount = new RequestParams("sellcount", count);
    list.add(paramsToken);
    list.add(paramsPackageType);
    list.add(paramsCode);
    list.add(paramsCount);
    return list;
  }

  private void commitInfo(Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
//      dialog();
      dialogNew();
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.commits_fail)));
    }
  }


  private void dialogNew() {
    final Dialog dialog = new Dialog(this, R.style.MyDialog);
    View view = LayoutInflater.from(this).inflate(R.layout.tousu_success_layout, null);
    TextView title = (TextView) view.findViewById(R.id.tv);
    TextView content = (TextView) view.findViewById(R.id.content);
    title.setText(getString(R.string.commits_success));
    content.setText(getString(R.string.sell_tips));

    dialog.setContentView(view);
    dialog.getWindow()
        .setLayout(WindoswUtil.getWindowWidth(this) - 200, WindowManager.LayoutParams.WRAP_CONTENT);
    dialog.show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (dialog.isShowing()) {
          dialog.dismiss();
        }
        finish();
      }
    }, 3500);
  }

  private void dialog() {
    final Dialog dialog = new Dialog(this, R.style.MyDialog);
    View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
    TextView tv = (TextView) view.findViewById(R.id.tv);
    tv.setText(getInfo(R.string.commit_success_tips_one));
    dialog.setContentView(view);
    dialog.getWindow()
        .setLayout(WindoswUtil.getWindowWidth(this) - 200, WindowManager.LayoutParams.WRAP_CONTENT);
    dialog.show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
        finish();
      }
    }, 3500);

  }

  public void select() {
    List<String> list = new ArrayList<>();
    list.add(getString(R.string.dream_packge));
    list.add(getString(R.string.money_packge));
    selectBankList(list);
//
//    StytledDialog.showBottomItemDialog(this, list, getString(R.string.cancel), true, true,
//        new MyItemDialogListener() {
//          @Override
//          public void onItemClick(String text, int position) {
//            mTvSelectToSell.setText(text);
//            mPackageType = getPackageType(position);
//          }
//        });
  }


  //选择银行卡
  public void selectBankList(final List<String> objects) {
    OptionsPickerView a = new OptionsPickerView.Builder(this,
        new OptionsPickerView.OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3, View v) {
            mTvSelectToSell.setText(objects.get(options1));
            mPackageType = getPackageType(options1);
          }
        })
        .setSubmitText(getString(R.string.complete_normal))//确定按钮文字
        .setCancelText(getString(R.string.cancels))//取消按钮文字
        .setTitleText("")//标题
        .setSubCalSize(17)//确定和取消文字大小
        .setTitleSize(20)//标题文字大小
        .setTitleColor(ColorUtil.getColor(this, R.color.c333))//标题文字颜色
        .setSubmitColor(ColorUtil.getColor(this, R.color.c333))//确定按钮文字颜色
        .setCancelColor(ColorUtil.getColor(this, R.color.c333))//取消按钮文字颜色
        .setTitleBgColor(ColorUtil.getColor(this, R.color.white))//标题背景颜色 Night mode
        .setBgColor(ColorUtil.getColor(this, R.color.white))//滚轮背景颜色 Night mode
        .setContentTextSize(18)//滚轮文字大小
        .setLinkage(false)//设置是否联动，默认true
        .setLabels("", null, null)//设置选择的三级单位
        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        .setCyclic(false, false, false)//循环与否
        .setSelectOptions(0, 1, 1)  //设置默认选中项
        .setOutSideCancelable(true)//点击外部dismiss default true
        .isDialog(false)//是否显示为对话框样式
        .build();
    a.setPicker(objects);
    a.show(true);

  }


  public String getPackageType(int position) {
    String result = "";
    switch (position) {
      case 0:
        result = "dreambag";
        mEtToSell.setHint(getString(R.string.please_input_seeds_count));
        break;
      case 1:
        result = "rewardbag";
        String s =
            getString(R.string.can_sell_seeds) + DebugUtils.convert(mLeastseeds, "0") + getString(
                R.string.can_buy_seeds_later);
        mEtToSell.setHint(s);
        break;
      default:
        break;
    }
    return result;
  }

  //种子数量监听
  @Override
  public void onFocusChange(View v, boolean hasFocus) {
    if (!hasFocus && !TextUtils.isEmpty(mEtToSell.getText().toString().trim())) {
      if (TextUtils.isEmpty(mPrice)) {
        showMsg(getString(R.string.seed_price_empty));
        return;
      }
      BigDecimal decimal = new BigDecimal(mEtToSell.getText().toString().trim());
      BigDecimal price = new BigDecimal(mPrice);
      String allPrice = decimal.multiply(price).toString();
      mTvReceiveMoney.setText(allPrice);
    }
  }

  //监听et内容变化
  private class MyTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

      if (!TextUtils.isDigitsOnly(s)) {
        showMsg(getString(R.string.just_nuber));
        return;
      }

      if (TextUtils.isEmpty(mPrice)) {
        showMsg(getString(R.string.seed_price_empty));
        return;
      }

      if (TextUtils.isEmpty(s)) {
        return;
      }

      BigDecimal decimal = new BigDecimal(s.toString());
      BigDecimal price = new BigDecimal(mPrice);
      String allPrice = decimal.multiply(price).toString();

      if (count != before) {
        String sss = "";
        String string = allPrice.toString().replace(",", "");
        int b = string.length() / 3;
        if (string.length() >= 3) {
          int yushu = string.length() % 3;
          if (yushu == 0) {
            b = string.length() / 3 - 1;
            yushu = 3;
          }
          for (int i = 0; i < b; i++) {
            sss = sss + string.substring(0, yushu) + "," + string.substring(yushu, 3);
            string = string.substring(3, string.length());
          }
          sss = sss + string;
          mTvReceiveMoney.setText(sss);
        }
      }


    }

    @Override
    public void afterTextChanged(Editable s) {

    }
  }
}

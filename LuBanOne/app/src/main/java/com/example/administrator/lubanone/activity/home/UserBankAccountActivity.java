package com.example.administrator.lubanone.activity.home;

import static com.example.administrator.lubanone.R.id.et_card_number;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.landregister.BankListResultBean;
import com.example.administrator.lubanone.bean.landregister.BankListResultBean.ResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * 添加银行账户
 */
public class UserBankAccountActivity extends BaseActivity {


  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.user_name_input)
  TextView mUserNameInput;
  @BindView(R.id.ll_user_name)
  LinearLayout mLlUserName;
  @BindView(R.id.line)
  TextView mLine;
  @BindView(R.id.tv_choosen_bank)
  TextView mTvChoosenBank;
  @BindView(R.id.ll_pwd)
  LinearLayout mLlPwd;
  @BindView(R.id.line_two)
  TextView mLineTwo;
  @BindView(et_card_number)
  EditText mEtCardNumber;
  @BindView(R.id.ll_phone)
  LinearLayout mLlPhone;
  @BindView(R.id.line_three)
  TextView mLineThree;
  @BindView(R.id.user_check_code_input)
  EditText mUserCheckCodeInput;
  @BindView(R.id.ll_check_code)
  LinearLayout mLlCheckCode;
  @BindView(R.id.line_four)
  TextView mLineFour;
  @BindView(R.id.btn_commit)
  Button mBtnCommit;
  @BindView(R.id.activity_register)
  LinearLayout mActivityRegister;

  private Dialog mDialogs;
  private String mBankType ;

  private List<ResultBean> mBanklist;


  private RequestListener mCommitListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        commitData(result);
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


  RequestListener mRequestListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Gson gson = new Gson();
        BankListResultBean bankListResultBean = gson.fromJson(jsonObject, BankListResultBean.class);
        getBankList(bankListResultBean);
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
    return R.layout.activity_bank_card_set;
  }

  @Override
  public void initView() {
    mUserNameInput.setText(
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.REAL_NAME, ""));


  }

  @Override
  public void loadData() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    list.add(paramsToken);
    showLandingDialog();
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.BANK_LIST, mRequestListener,
        RequestNet.POST);
  }

  @Override
  public void onClick(View v) {

  }


  private void commit() {
    String openBank = mEtCardNumber.getText().toString().trim();
    String bankAccount = mUserCheckCodeInput.getText().toString().trim();
    if (isEmpty(openBank) || isEmpty(bankAccount)) {
      showMsg(getString(R.string.input_right_info));
      return;
    }
    if (mBankType == null) {
      showMsg(getString(R.string.select_bank));
      return;
    }
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsBankType = new RequestParams("bank", mBankType + "");
    RequestParams paramsOpenBank = new RequestParams("banksite", openBank);
    RequestParams paramsAccount = new RequestParams("bankaccount", bankAccount);
    list.add(paramsBankType);
    list.add(paramsAccount);
    list.add(paramsOpenBank);
    list.add(paramsToken);

    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.ADD_CARD, mCommitListener,
        RequestNet.POST);

  }

  @Override
  public String setTip() {
    return getString(R.string.commiting);
  }

  private void showChoiceDialog() {
    List<String> list = new ArrayList<>();
    if (CollectionUtils.isEmpty(mBanklist)) {
      showMsg(getString(R.string.get_bank_list_fail));
      return;
    }
    for (ResultBean bean : mBanklist) {
      if (null == bean) {
        continue;
      }
      list.add(bean.getBankname());
    }

    mDialogs = StytledDialog
        .showBottomItemDialog(this, list, getString(R.string.cancels), false, true,
            new MyItemDialogListener() {
              @Override
              public void onItemClick(String text, int position) {
                mDialogs.dismiss();
                mTvChoosenBank.setText(text);
                setBankType(position);
              }
            });
  }


  @OnClick({R.id.iv_back, R.id.tv_choosen_bank, R.id.btn_commit})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
        finish();
        break;
      case R.id.tv_choosen_bank:
        showChoiceDialog();
        break;
      case R.id.btn_commit:
        commit();
        break;
      default:
        break;
    }
  }

  private void getBankList(BankListResultBean result) {
    hideLandingDialog();
    if ("1".equals(result.getType())) {
      mBanklist = result.getResult();
    } else {
      showMsg(DebugUtils.convert(result.getMsg(), ""));
    }
  }

  public void setBankType(int position) {
    mBankType = mBanklist.get(position).getBanktype();
  }

  private void commitData(Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
      showMsg(getInfo(R.string.commits_success));
    } else {
      showMsg(
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.commits_success)));
    }
  }

}



package com.example.administrator.lubanone.activity.register;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.bigkoo.pickerview.OptionsPickerView;
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
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class RegisterFourActivity extends BaseActivity {

  private TextView mInputName;
  private TextView mBankName;
  private EditText mOpenBank;
  private EditText mCarCode;
  private String mRealName;


  private List<ResultBean> mBanklist;

  private String bankType;


  private RequestListener mCompleteListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        bindComplete(result);
      } catch (Exception e) {
        hideCommitDataDialog();
        showMsg(getInfo(R.string.bind_card_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getInfo(R.string.bind_card_fail));
    }
  };

  private void bindComplete(Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.bind_card_success));
      startNewActivity(this, AccountActiveActivity.class);
    } else {
      showMsg(
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.bind_card_fail)));
    }
  }

  //获取银行列表
  private RequestListener mBankListListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Gson gson = new Gson();
        BankListResultBean resultBean = gson.fromJson(jsonObject, BankListResultBean.class);
        getBankList(resultBean);
      } catch (Exception e) {
        showMsg(getInfo(R.string.get_bank_list_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.get_bank_list_fail));
    }
  };

  private void getBankList(BankListResultBean result) {
    if (null == result) {
      showMsg(getInfo(R.string.get_bank_list_fail));
      return;
    }
    if ("1".equals(result.getType())) {
      mBanklist = result.getResult();
    } else {
      showMsg(DebugUtils
          .convert(result.getMsg(), getString(R.string.get_bank_list_fail)));
    }
  }

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_register_four;
  }

  @Override
  public void finish() {
    super.finish();
  }


  @Override
  public void initView() {

    mInputName = (TextView) findViewById(R.id.user_name_input);
    mBankName = (TextView) findViewById(R.id.user_pwd_input);
    mOpenBank = (EditText) findViewById(R.id.user_phone_input);
    mCarCode = (EditText) findViewById(R.id.user_check_code_input);
    Button complete = (Button) findViewById(R.id.btn_next);
    TextView back = (TextView) this.findViewById(R.id.tv_back);

    back.setOnClickListener(this);
    complete.setOnClickListener(this);
    mBankName.setOnClickListener(this);


  }

  @Override
  public String setTip() {
    return getString(R.string.bind_carding);
  }

  @Override
  public void loadData() {
    mRealName = SPUtils
        .getStringValue(getApplicationContext(), Config.USER_INFO, Config.REAL_NAME,
            "");
    mInputName.setText(mRealName);

    try {
      List<RequestParams> list = new ArrayList<>();
      RequestParams params = new RequestParams(Config.TOKEN,
          SPUtils
              .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      list.add(params);

      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.BANK_LIST,
          mBankListListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.get_bank_list_fail));
    }
  }

  //选择银行卡
  public void selectBankList(final List<String> objects) {
    OptionsPickerView a = new OptionsPickerView.Builder(this,
        new OptionsPickerView.OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3, View v) {
            //返回的分别是三个级别的选中位置
//            ResultBean banklistBean = mBanklist.get(options1);//tv_choosen_bank_account
//            mTvChooseBankAccount.setText(banklistBean.getBankname());
//            mBankType = Integer.parseInt(banklistBean.getBanktype());

            mBankName.setText(objects.get(options1));
            bankType = mBanklist.get(options1).getBanktype();
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

  @Override
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.btn_next:
          complete();
          break;
        case R.id.user_pwd_input:
          selectBank();
          break;
        case R.id.tv_back:
          RegisterFourActivity.this.finish();
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private void selectBank() {

    if (CollectionUtils.isEmpty(mBanklist)) {
      showMsg(getString(R.string.account_list_empty));
      return;
    }

    List<String> list = new ArrayList<>();
    for (ResultBean bean : mBanklist) {
      if (null == bean) {
        continue;
      }
      String bankname = bean.getBankname();
      list.add(bankname);
    }

    selectBankList(list);

//    StytledDialog.showBottomItemDialog(this, list, getString(R.string.cancels), true, true,
//        new MyItemDialogListener() {
//          @Override
//          public void onItemClick(String text, int position) {
//            mBankName.setText(text);
//            if (!CollectionUtils.isEmpty(mBanklist)) {
//              bankType = mBanklist.get(position).getBanktype();
//            }
//          }
//        });
  }

  private void complete() {
    check();
    judgeNet();
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils
            .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsBankName = new RequestParams("bank", DebugUtils.convert(bankType, ""));
    RequestParams paramsOpenBank = new RequestParams("banksite",
        mOpenBank.getText().toString().trim());
    RequestParams paramsAccount = new RequestParams("bankaccount",
        mCarCode.getText().toString().trim());

    list.add(paramsToken);
    list.add(paramsBankName);
    list.add(paramsOpenBank);
    list.add(paramsAccount);

    showCommitDataDialog();
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SET_BANK_ACCOUNT,
        mCompleteListener,
        RequestNet.POST);

  }

  private void check() {
    String name = mInputName.getText().toString().trim();
    String openBank = mOpenBank.getText().toString().trim();
    String carCode = mCarCode.getText().toString().trim();

    if (isEmpty(name) || isEmpty(openBank) || isEmpty(carCode)) {
      throw new DefineException("填写信息有误");
    }
    if (null == bankType) {
      throw new DefineException("您还未选择银行类型");
    }
    if (!TextUtils.isDigitsOnly(carCode)) {
      throw new DefineException("银行卡号只能是数字");
    }
  }
}

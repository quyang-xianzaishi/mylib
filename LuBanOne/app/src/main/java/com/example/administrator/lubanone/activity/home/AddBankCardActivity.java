package com.example.administrator.lubanone.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.BankListBean;
import com.example.administrator.lubanone.bean.homepage.BankListBean.ResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DateUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;

/**
 * add bank card
 */
public class AddBankCardActivity extends BaseActivity {

  private List<String> bankList = new ArrayList<>();
  private LinearLayout mRlAddCount;
  private LinearLayout mRlAddCard;
  private int cardBankType = -1;
  private TextView mTitle;
  private TextView mTvUserNameAccount;
  private EditText mEtCardNumberAccount;
  private EditText mEtBankAccount;
  private TextView mTvChooseBankAccount;
  private String[] mCardList;
  private List<ResultBean> mBanklist;


  private Integer bankType;//用户选择的银行类型
  private long date;//用户选择的时间

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.user_name_input)
  TextView mUserNameInput;
  @BindView(R.id.ll_user_name)
  LinearLayout mLlUserName;
  @BindView(R.id.line)
  TextView mLine;
  TextView mLineTwo;
  @BindView(R.id.et_card_number)
  EditText mEtCardNumber;
  @BindView(R.id.ll_phone)
  LinearLayout mLlPhone;
  @BindView(R.id.line_three)
  TextView mLineThree;
  @BindView(R.id.user_check_code_input)
  TextView mUserCheckCodeInput;
  @BindView(R.id.iv_calendar)
  ImageView mIvCalendar;
  @BindView(R.id.ll_check_code)
  LinearLayout mLlCheckCode;
  @BindView(R.id.line_four)
  TextView mLineFour;
  @BindView(R.id.btn_commit)
  Button mBtnCommit;
  @BindView(R.id.activity_register)
  LinearLayout mActivityRegister;
  private int mBankType = -1;
  private Dialog mDialog;
  private Dialog mMDialog;
  private Dialog mDialogs;
  private Boolean isBankCard;

  //  @BindView(R.id.et_open_bank)
//  EditText mEtOpenBank;
  @BindView(R.id.tv_bank_account_add_card)
  TextView tv_bank_account_add_card;


  //提交账户信息
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


  //提交信息
  RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        hideCommitDataDialog();
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        addCartInfo(result);
      } catch (Exception e) {
        hideCommitDataDialog();
        showMsg(getInfo(R.string.add_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDataDialog();
      showMsg(getString(R.string.add_fail));
    }
  };

  private void addCartInfo(Result<Object> result) {
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.add_success));
      finish();
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.add_fail)));
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
        BankListBean bankListBean = gson.fromJson(jsonObject, BankListBean.class);
        getBankList(bankListBean);
      } catch (Exception e) {
        showMsg(getInfo(R.string.get_bank_list_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.get_bank_list_fail));
    }
  };


  private void getBankList(BankListBean result) {
//    if (null == result || CollectionUtils.isEmpty(result.getResult())) {
//      showMsg(getInfo(R.string.get_bank_list_fail));
//      return;
//    }

    if (null != result && "1".equals(result.getType()) && CollectionUtils
        .isNotEmpty(result.getResult())) {
      mBanklist = result.getResult();
    } else {
      showMsg(DebugUtils
          .convert(null, getString(R.string.get_bank_list_fail)));
    }
  }


  @Override
  protected void beforeSetContentView() {

  }


  @Override
  protected int getContentViewId() {
    return R.layout.activity_add_bank_card_new;
  }


  @Override
  public void initView() {

    TextView tvBack = (TextView) findViewById(R.id.tv_back);
    tvBack.setOnClickListener(this);

    //添加账户
    //选择银行类型
    mTvChooseBankAccount = (TextView) findViewById(R.id.tv_choosen_bank_account);
    mTvChooseBankAccount.setOnClickListener(this);
    //用户名称
    mTvUserNameAccount = (TextView) findViewById(R.id.user_name_input_account);
    mTvUserNameAccount.setText(
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.REAL_NAME, ""
        ));

    //开户行
    mEtCardNumberAccount = (EditText) findViewById(R.id.et_card_number_account);
    //银行账号
    mEtBankAccount = (EditText) findViewById(R.id.user_check_code_input_account);
    //提交
    Button etCommitAccount = (Button) findViewById(R.id.btn_commit_account);
    etCommitAccount.setOnClickListener(this);

//    user_name_input

    mUserNameInput.setText(
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.REAL_NAME, ""));

    mTitle = (TextView) findViewById(R.id.title);

    mRlAddCount = (LinearLayout) findViewById(R.id.rl_add_count);
    mRlAddCard = (LinearLayout) findViewById(R.id.rl_add_card);

    mRlAddCount.setVisibility(View.GONE);
    mRlAddCard.setVisibility(View.GONE);

    mUserNameInput.setText(
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.REAL_NAME, ""));

    Intent intent = getIntent();
    if (null != intent) {
      int type = intent.getIntExtra("type", -1);
      showRightView(type);
    }
  }

  private void showRightView(int type) {
    if (type == 0) {//银行账户
      type = 0;
      showLayoutByPosition(0);
    }
    if (type == 1) {//银行卡
      type = 1;
      showLayoutByPosition(1);
    }
  }

  private int resource = -1;


  private void showLayoutByPosition(int position) {
    if (0 == position) {//银行账户
      resource = 0;
      mRlAddCount.setVisibility(View.VISIBLE);
      mRlAddCard.setVisibility(View.GONE);
      mTitle.setText(getString(R.string.add_bank_account));
      isBankCard = false;
    }
    if (1 == position) {//银行卡
      resource = 1;
      mRlAddCount.setVisibility(View.GONE);
      mRlAddCard.setVisibility(View.VISIBLE);
      mTitle.setText(getString(R.string.add_bank_card));
      isBankCard = true;
    }
  }

  @Override
  public void loadData() {
    mCardList = getResources().getStringArray(R.array.card_list);

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


  @Override
  public String setTip() {
    return getString(R.string.commiting);
  }

  @Override
  public void onClick(View v) {

    switch (v.getId()) {
      /*case R.id.tv_choosen_bank://选择银行卡
        showDialog2ChoosenBankType();
        break;*/
      case R.id.btn_commit_account://账户 提交
        accountCommit();
        break;
      case R.id.tv_choosen_bank_account://选择银账户
        selectBankList(getStringList(mBanklist));
        break;
      case R.id.tv_back:
        finish();
        break;
      default:
        break;
    }
  }

  private List<String> getStringList(List<ResultBean> banklist) {
    if (CollectionUtils.isEmpty(banklist)) {
      return null;
    }

    ArrayList<String> list = new ArrayList<>();

    for (ResultBean bean : banklist) {
      if (null == bean) {
        continue;
      }
      list.add(bean.getBankname());
    }

    return list;
  }


  //添加账户  英文的是写死的
  private void showDialog2ChoosenBankAccount() {
    if (CollectionUtils.isNotEmpty(mBanklist)) {
      String[] strings = new String[mBanklist.size()];
      for (int a = 0; a < mBanklist.size(); a++) {
        strings[a] = mBanklist.get(a).getBankname();
      }
      showChoiceDialog(strings);
    }
  }

  /*@OnClick({R.id.iv_back, R.id.tv_choosen_bank, R.id.iv_calendar, R.id.btn_commit,
      R.id.tv_bank_account_add_card})*/
  @OnClick({R.id.iv_back, R.id.iv_calendar, R.id.btn_commit,
      R.id.tv_bank_account_add_card})
  public void onViewClicked(View view) {
    try {
      switch (view.getId()) {
        case R.id.iv_back:
          finish();
          break;
        case R.id.iv_calendar:
          openCalendar();
          break;
        case R.id.btn_commit://添加银行卡
          commit();
          break;
//        case R.id.tv_choosen_bank://添加银行账户
//          showDialog2ChoosenBankType();
//          break;
        case R.id.tv_bank_account_add_card://添加 银行卡
//          showChoiceDialogCard();
//          selectBankListOne(getStringList(mBanklist));
          break;
        default:
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  //添加银行账户
  private void accountCommit() {
    try {
      String openBank = mEtCardNumberAccount.getText().toString().trim();//银行账号
      String bankAccount = mEtBankAccount.getText().toString().trim();//银行代码
      if (isEmpty(openBank) || isEmpty(bankAccount)) {
        showMsg(getString(R.string.input_right_info));
        return;
      }
      if (mBankType == -1) {
        showMsg(getString(R.string.select_bank));
        return;
      }

      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsBankType = new RequestParams("bank", mBankType + "");//银行名称
      RequestParams paramsOpenBank = new RequestParams("banksite", bankAccount);//银行代码
      RequestParams paramsAccount = new RequestParams("bankaccount", openBank);//银行账户


//      token,bank银行,banksite银行代号,bankaccount银行账号

      list.add(paramsBankType);
      list.add(paramsAccount);
      list.add(paramsOpenBank);
      list.add(paramsToken);

      showCommitDataDialog();
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.SET_BANK_ACCOUNT,
          mCommitListener,
          RequestNet.POST);

    } catch (Exception e) {
      showMsg(getString(R.string.add_fail));
      hideCommitDataDialog();
    }
  }


  private void commitData(Result<Object> result) {
    hideCommitDataDialog();
    if (ResultUtil.isSuccess(result)) {
      //BankCardListActivity
      showMsg(getString(R.string.commits_success));
      finish();
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.commits_fail)));
    }
  }

  //添加银行卡
  private void commit() {

    try {//bankaccount银行账号,到期时间expiretime,bankunion银行卡类型;

      judgeNet();
      //String openBank = mEtOpenBank.getText().toString().trim();
      String cardCode = mEtCardNumber.getText().toString().trim();
      if (date == 0 || isEmpty(cardCode)) {
        showMsg(getString(R.string.input_info_error));
        return;
      }
      if (mBankType == -1) {
        showMsg(getString(R.string.select_bank_type));
        return;
      }

//      if (cardBankType == -1) {
//        showMsg(getString(R.string.select_bank_name));
//        return;
//      }

      List<RequestParams> paramsesList = new ArrayList<>();
      RequestParams requestParamsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams requestParamsBank = new RequestParams("bankunion", mBankType + "");
      RequestParams requestParamsAccount = new RequestParams("bankaccount", cardCode);
      RequestParams requestParamsBankName = new RequestParams("bank", cardBankType + "");
      RequestParams requestParamsArrivedTime = new RequestParams("expiretime",
          DateUtil.getDateString(AddBankCardActivity.this.date));

      paramsesList.add(requestParamsToken);
      paramsesList.add(requestParamsBank);
      paramsesList.add(requestParamsAccount);
      paramsesList.add(requestParamsArrivedTime);
      paramsesList.add(requestParamsBankName);

      showCommitDataDialog();
      RequestNet requestNet = new RequestNet(myApp, this, paramsesList, Urls.ADD_CARD,
          mListener,
          RequestNet.POST);
    } catch (Exception e) {
      showMsg(getString(R.string.add_fail));
      hideCommitDataDialog();
    }
  }

  private String getUrl() {
    if (resource == 0) {
      return Urls.SET_BANK_ACCOUNT;
    }
    if (resource == 1) {
      return Urls.ADD_CARD;
    }
    throw new DefineException(getString(R.string.url_error));
  }


  private void openCalendar() {
    timePick();
  }

  private void timePick() {

    //时间选择器
    TimePickerView pvTime = new TimePickerView.Builder(this,
        new TimePickerView.OnTimeSelectListener() {
          @Override
          public void onTimeSelect(Date date, View v) {//选中事件回调
            AddBankCardActivity.this.date = date.getTime();
            String stringData = DateUtil.getStringData(date.getTime());
            mUserCheckCodeInput.setText(stringData);
          }
        })
        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
        .setContentSize(15)
        .gravity(Gravity.CENTER)
        .setLabel(getString(R.string.year), getString(R.string.month), getString(R.string.day),
            getString(R.string.hour), getString(R.string.minte),
            getString(R.string.second))//默认设置为年月日时分秒
        .build();
    pvTime.setDate(Calendar
        .getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
    pvTime.show();
  }

  //添加银行卡
  private void showDialog2ChoosenBankType() {
    showChoiceDialog(mCardList);
  }

  //选择银行卡
  private void showChoiceDialog(String[] infoList) {
    List<String> list = new ArrayList<>();
    if (CollectionUtils.isEmpty(infoList)) {
      return;
    }
    for (String bean : infoList) {
      if (null == bean) {
        continue;
      }
      list.add(bean);
    }
    if (!CollectionUtils.isEmpty(list)) {
      mDialogs = StytledDialog
          .showBottomItemDialog(this, list, getString(R.string.cancels), true, true,
              new MyItemDialogListener() {
                @Override
                public void onItemClick(String text, int position) {
                  mDialogs.dismiss();
                  if (isBankCard) {
                    //mTvChoosenBank.setText(text);
                  } else {
                    mTvChooseBankAccount.setText(text);
                  }
                  setBankType(position);
                }
              });
    } else {
      showMsg(getString(R.string.bank_list_empty));
    }
  }


  //选择银行卡
  public void selectBankListOne(final List<String> objects) {
    OptionsPickerView a = new OptionsPickerView.Builder(this,
        new OptionsPickerView.OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3, View v) {
            //返回的分别是三个级别的选中位置
            ResultBean banklistBean = mBanklist.get(options1);//tv_bank_account_add_card
            tv_bank_account_add_card.setText(banklistBean.getBankname());
//            mBankType = options1;
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


  //选择银行卡
  public void selectBankList(final List<String> objects) {
    OptionsPickerView a = new OptionsPickerView.Builder(this,
        new OptionsPickerView.OnOptionsSelectListener() {
          @Override
          public void onOptionsSelect(int options1, int option2, int options3, View v) {
            //返回的分别是三个级别的选中位置
            ResultBean banklistBean = mBanklist.get(options1);//tv_choosen_bank_account
            mTvChooseBankAccount.setText(banklistBean.getBankname());
            mBankType = Integer.parseInt(banklistBean.getBanktype());
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


  //选择银行卡 银行名称
  private void showChoiceDialogCard() {
    if (CollectionUtils.isEmpty(mBanklist)) {
      return;
    }
    ArrayList<String> list = new ArrayList<>();
    for (ResultBean bean : mBanklist) {
      if (null == bean) {
        continue;
      }
      list.add(bean.getBankname());
    }
    if (!CollectionUtils.isEmpty(mBanklist)) {
      mDialogs = StytledDialog
          .showBottomItemDialog(this, list, getString(R.string.cancels), true, true,
              new MyItemDialogListener() {
                @Override
                public void onItemClick(String text, int position) {
                  mDialogs.dismiss();
                  tv_bank_account_add_card.setText(text);
                  cardBankType = position;
                }
              });
    } else {
      showMsg(getString(R.string.bank_list_empty));
    }
  }

  /**
   * 根据position获取对应的银行类型
   */
  public void setBankType(int position) {
    mBankType = position + 1;
  }


}

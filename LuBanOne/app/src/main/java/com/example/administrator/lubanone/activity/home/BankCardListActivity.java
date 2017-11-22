package com.example.administrator.lubanone.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.CardListAdapter;
import com.example.administrator.lubanone.bean.homepage.BindCardResultBean;
import com.example.administrator.lubanone.bean.homepage.BindCardResultBean.BankaccountlistBean;
import com.example.administrator.lubanone.customview.AddCardListView;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

/**
 * 银行卡列表
 */
public class BankCardListActivity extends BaseActivity implements
    OnMoneyPayListener<BankaccountlistBean>, OnScrollListener {


  @BindView(R.id.title)
  TextView mTitle;
  @BindView(R.id.back)
  ImageView mBack;
  @BindView(R.id.tv_save)
  ImageView mTvSave;
  @BindView(R.id.lv)
  AddCardListView mLv;
  @BindView(R.id.activity_register)
  LinearLayout mActivityRegister;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.add_card)
  ImageView addCard;
  private Dialog mDialog;

  private RequestListener mCardListListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<BindCardResultBean> result = GsonUtil
            .processJson(jsonObject, BindCardResultBean.class);
        getCardListResult(result);
      } catch (Exception e) {
        showMsg(getInfo(R.string.get_card_list_fail));
      }

    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(errorMsf);
    }
  };

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_bank_card_list;
  }

  @Override
  public void initView() {
    EventBus.getDefault().register(this);
    mLv.setOnScrollListener(this);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void get2(Class<BankCardListActivity> clazz){
    getInitData();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  public void loadDate() {
    getInitData();
  }

  @Override
  public void loadData() {
  }

  private void getInitData() {
    if (!NetUtil.isConnected(this)) {
      showMsg(getInfo(R.string.NET_ERROR));
      return;
    }
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    list.add(paramsToken);
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.CARD_LIST, mCardListListener,
        RequestNet.POST);
  }

  private void getCardListResult(Result<BindCardResultBean> result) {
    isResultNullWithException(result, getInfo(R.string.GET_DATE_FAIL));
    if (ResultUtil.isSuccess(result)) {
      updatePage(result.getResult());
    } else {
      showMsg(ResultUtil.getErrorMsg(result));
    }
  }

  private void updatePage(BindCardResultBean result) {
    List<BankaccountlistBean> bankaccountlist = result.getBankaccountlist();
    if (!CollectionUtils.isEmpty(bankaccountlist)) {
      mLv.setAdapter(new CardListAdapter(getApplicationContext(), bankaccountlist, this));
    }
  }


  @Override
  public void onClick(View v) {

  }


  @OnClick({R.id.back, R.id.tv_save, R.id.tv_back, R.id.add_card})
  public void onViewClicked(View view) {
    try {
      switch (view.getId()) {
        case R.id.back:
        case R.id.tv_back:
          finish();
          break;
        case R.id.tv_save:
//        case R.id.tv_add_info:
//          addNewCard();
//          break;
        case R.id.add_card:
          addNewCard();
          break;
        default:
          break;
      }
    } catch (Exception e) {
      showMsg(e.getMessage());
    }

  }

  private void addNewCard() {
    setCard();
  }

  private void setCard() {
    Intent intent = new Intent(this, AddBankCardActivity.class);
    intent.putExtra("type", 0);
    startActivityForResult(intent,0);
//
//    ArrayList<String> li = new ArrayList<>();
//    li.add(getString(R.string.bank_account_title));
////    li.add(getString(R.string.bank_card));
//    //li.add(getString(R.string.cancels));
//    mDialog = StytledDialog.showBottomItemDialog(this, li,getString(R.string.cancels),
//        true, true, new MyItemDialogListener() {
//      @Override
//      public void onItemClick(String text, int position) {
//        if (3 == position) {
//          mDialog.dismiss();
//          return;
//        }
//        showLayoutByPosition(position);
//      }
//    });
    /*mDialog = StytledDialog.showBankChoose(this, li, true, true, new MyItemDialogListener() {
      @Override
      public void onItemClick(String text, int position) {
        if (3 == position) {
          mDialog.dismiss();
          return;
        }
        showLayoutByPosition(position);
      }
    });*/
  }


  private void showLayoutByPosition(int position) {
    mDialog.dismiss();
    if (0 == position) {//银行账户
      Intent intent = new Intent(this, AddBankCardActivity.class);
      intent.putExtra("type", 0);
      startActivity(intent);
    }
    if (1 == position) {//银行卡
      Intent intent = new Intent(this, AddBankCardActivity.class);
      intent.putExtra("type", 1);
      startActivity(intent);
    }
  }

  //点击item的回调
  @Override
  public void onCuiPayClick(BankaccountlistBean item, int position) {
    Intent intent = new Intent(this, CardDetailsActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("item", item);
    bundle.putInt("position", position);
    intent.putExtra("bundle", bundle);
    startActivityForResult(intent,0);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    getInitData();
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {

  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
      int totalItemCount) {
  }
}

package com.example.administrator.lubanone.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.customview.progressdialog.BaseProgressDialog;
import com.example.administrator.lubanone.customview.progressdialog.MyProgressDialog;
import com.example.qlibrary.dialog.DialogManager;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.ToastUtil;

/**
 * Created by quyang on 2017/6/24.
 */

public abstract class BaseFragment extends Fragment {

  private static final String TAG = "TAG";


  public Activity mActivity;
  public LayoutInflater mInflater;
  private DialogManager mDialogManager;
  private DialogManager mCommitDialog;
  private BaseProgressDialog LoadingDialog;
  private BaseProgressDialog uploadingDialog;
  private BaseProgressDialog commitRequest;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (Config.test_memery) {
      MyApplication.getWatcher().watch(this);
    }
    mActivity = getActivity();
    mInflater = LayoutInflater.from(mActivity);
  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    LoadingDialog = new MyProgressDialog(getActivity()).setLabel(getString(R.string.loadings));
    commitRequest = new MyProgressDialog(getActivity()).setLabel(setTip());
    uploadingDialog = new MyProgressDialog(getActivity()).setLabel(getString(R.string.uploading));
    return initView();
  }

  public String setTip() {
    return getString(R.string.commit_request);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    initLandIng();
    commitLandIng();
    initData();
  }

  @Override
  public void onResume() {
    super.onResume();
//    initData();
  }

  public void showUploadingDialog() {
    uploadingDialog.show();
  }

  public void dismissUploadingDialog() {
    uploadingDialog.dismiss();
  }


  public void showLoadingDialog() {
    if (!LoadingDialog.isShowing()) {
      LoadingDialog.show();
    }
  }

  public void dismissLoadingDialog() {
    if (LoadingDialog.isShowing()) {
      LoadingDialog.dismiss();
    }
  }

  public void showRequestDialog() {
    if (!commitRequest.isShowing()) {
      commitRequest.show();
    }
  }

  public void dismissRequestDialog() {
    if (commitRequest.isShowing()) {
      commitRequest.dismiss();
    }
  }

  public abstract View initView();


  public abstract void initData();


  private void commitLandIng() {
    mCommitDialog = new DialogManager(getActivity(), getInfo(R.string.committing));
  }

  public void showCommitDialog() {
    mCommitDialog.show();
  }


  public void hideCommitDialog() {
    mCommitDialog.dismiss();
  }


  public void show(String msg) {
    if (Config.isOffLine) {
      ToastUtil.showShort(msg, MyApplication.getContext());
    }
  }

  public void showMsg(String msg) {
    ToastUtil.showShort(msg, MyApplication.getContext());
  }

  private void initLandIng() {
    mDialogManager = new DialogManager(getActivity(),
        getInfo(com.example.qlibrary.R.string.loading));
  }

  public void showLandingDialog() {
    mDialogManager.show();
  }

  public void hideLandingDialog() {
    mDialogManager.dismiss();
  }

  public String getInfo(int resId) {
    return getResources().getString(resId);
  }

  public void judgeNet() {
    if (!NetUtil.isConnected(MyApplication.getContext())) {
      throw new DefineException(getInfo(com.example.qlibrary.R.string.NET_ERROR));
    }
  }

  /**
   * Fragment当前状态是否可见
   */
  protected boolean isVisible;

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (getUserVisibleHint()) {
      isVisible = true;
      onVisible();
    } else {
      isVisible = false;
      onInvisible();
    }
  }

  public  void onInvisible(){};

  public  void onVisible(){};
}

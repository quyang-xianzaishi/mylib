package com.example.qlibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.qlibrary.dialog.DialogManager;
import com.example.qlibrary.utils.ToastUtil;

/**
 * Created by Administrator on 2017/6/18.
 */

public abstract class BaseFragment extends Fragment {

  public Activity mActivity;
  private DialogManager mManager;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = getActivity();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return initView();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    initLandIng();
    initData();
  }


  public void startActivityNoFinish(Activity activity, Class target) {
    Intent intent = new Intent(activity, target);
    startActivity(intent);
  }

  private void initLandIng() {
    mManager = new DialogManager(getActivity(), "数据加载中...");
  }

  public void showLandingDialog() {
    mManager.show();
  }

  public void hideLandingDialog() {
    mManager.dismiss();
  }


  public abstract View initView();


  public abstract void initData();


  public void show(String msg) {
    if (null != getActivity()) {
      ToastUtil.showShort(msg, getActivity());
    }
  }

}

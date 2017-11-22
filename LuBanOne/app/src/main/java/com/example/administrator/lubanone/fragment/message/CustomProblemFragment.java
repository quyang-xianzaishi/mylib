package com.example.administrator.lubanone.fragment.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.adapter.homepage.ExpandListViewAdapter;
import com.example.administrator.lubanone.bean.homepage.AlwaysProblesResulBean;
import com.example.administrator.lubanone.bean.homepage.AlwaysProblesResulBean.ResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.SPUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\8\5 0005.
 */

public class CustomProblemFragment extends Fragment {

  ExpandableListView mLv;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_custom_problem, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    initView();
    loadData();

  }

  private RequestListener mGetProblesListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Gson gson = new Gson();
        AlwaysProblesResulBean bean = gson
            .fromJson(jsonObject, AlwaysProblesResulBean.class);
        getProblesList(bean);
      } catch (Exception e) {
        Toast.makeText(getActivity(), getString(R.string.get_always_probles_info_fail),
            Toast.LENGTH_LONG).show();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      Toast.makeText(getActivity(), getString(R.string.get_always_probles_info_fail),
          Toast.LENGTH_LONG).show();
    }
  };

  public void initView() {

    mLv = (ExpandableListView) getActivity().findViewById(R.id.lv);

    int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
    mLv.setIndicatorBounds(width - 120, width - 20);
  }

  public void loadData() {
    try {
      checkParams();
      List<RequestParams> paramsList = getParamsList();
      RequestNet requestNet = new RequestNet(((MyApplication) (getActivity().getApplication())),
          getActivity(), paramsList, Urls.ALWAYS_PROBLES,
          mGetProblesListener,
          RequestNet.POST);
    } catch (Exception e) {
      Toast.makeText(getActivity(), getString(R.string.get_always_probles_info_fail),
          Toast.LENGTH_LONG).show();
    }
  }

  private void getProblesList(AlwaysProblesResulBean result) {
    if (null == result) {
      Toast.makeText(getActivity(), getString(R.string.get_always_probles_info_fail),
          Toast.LENGTH_LONG).show();
      return;
    }
    String type = result.getType();
    String msg = result.getMsg();
    if ("1".equals(type)) {
      List<ResultBean> beanList = result.getResult();
      updatePage(beanList);
    } else {
      Toast.makeText(getActivity(), DebugUtils.convert(msg,
          getString(R.string.get_always_probles_info_fail)), Toast.LENGTH_LONG).show();
    }
  }

  private void updatePage(List<ResultBean> beanList) {
    List<String> headList = new ArrayList<>();
    List<String> childsList = new ArrayList<>();
    for (ResultBean bean : beanList) {
      if (null == bean) {
        continue;
      }
      headList.add(bean.getTitle());
//      childsList.add(bean.getContent());
    }
    if (CollectionUtils.isEmpty(headList) || CollectionUtils.isEmpty(childsList)) {
      return;
    }
    mLv.setAdapter(new ExpandListViewAdapter(getActivity(), headList, childsList));
    mLv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
      @Override
      public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        Toast.makeText(getActivity(), "groupPosition=" + groupPosition,
            Toast.LENGTH_LONG).show();
        return false;
      }
    });
  }


  private List<RequestParams> getParamsList() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils
            .getStringValue(getActivity().getApplicationContext(), Config.USER_INFO, Config.TOKEN,
                ""));
    list.add(paramsToken);
    return list;
  }

  private void checkParams() {

  }


}

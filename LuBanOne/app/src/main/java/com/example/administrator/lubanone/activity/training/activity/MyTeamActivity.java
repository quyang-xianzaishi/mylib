package com.example.administrator.lubanone.activity.training.activity;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.training.MyTeamTreeAdapter;
import com.example.administrator.lubanone.bean.Member;
import com.example.administrator.lubanone.bean.training.MyTeamResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.Node;
import com.example.administrator.lubanone.utils.NodeHelper;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\6\29 0029.
 */

public class MyTeamActivity extends BaseActivity implements OnClickListener {

  private TextView back;
  private TextView myTeamNumber;
  private Spinner chooseGrade;
  private ListView myTeamList;
  private MyTeamTreeAdapter mAdapter;
  private LinkedList<Node> mLinkedList = new LinkedList<>();
  private Dialog mDialog;
  private List gradeList;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_my_team;
  }

  @Override
  public void initView() {
    back = (TextView) this.findViewById(R.id.my_team_back);
    myTeamNumber = (TextView) this.findViewById(R.id.my_team_number);
    chooseGrade = (Spinner) this.findViewById(R.id.choose_team_spinner);
    myTeamList = (ListView) this.findViewById(R.id.my_team_tree);

    back.setOnClickListener(this);
    mAdapter = new MyTeamTreeAdapter(this, myTeamList, mLinkedList);
    myTeamList.setAdapter(mAdapter);
  }

  @Override
  public void loadData() {
    initData();
  }


  private void initData() {
    gradeList = new ArrayList();
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
    list.add(paramsToken);

    mDialog = StytledDialog.showProgressDialog(this, "加载数据中...", true, true);
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.MY_TEAM, MyTeamListener,
        RequestNet.POST);
    //mDialog.show();
  }

  //请求数据
  private RequestListener MyTeamListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<MyTeamResultBean> result = GsonUtil
            .processJson(jsonObject, MyTeamResultBean.class);
        List<Node> data = new ArrayList<>();
        if (ResultUtil.isSuccess(result) && null != result && null != result.getResult()) {
          MyTeamResultBean myTeamResultBean = result.getResult();
          if (myTeamResultBean != null) {
            myTeamNumber.setText(myTeamResultBean.getTeamsize());
            addData(myTeamResultBean.getList(), data);
            mLinkedList.addAll(NodeHelper.sortNodes(data));
            mAdapter.notifyDataSetChanged();
          }
        } else {
          show(ResultUtil.getErrorMsg(result));
        }

      } catch (Exception e) {
        //mDialog.dismiss();
        show(getString(R.string.PARSE_RESULT_ERROR));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      //mDialog.dismiss();
      show(errorMsf);
    }
  };

  private void addData(List<Member> list, List<Node> data) {
        /*data.add(new Member("1", "0", "M7级", "M7"));//可以直接注释掉此项，即可构造一个森林
        data.add(new Member("2", "1", "M6级  1", "M6"));
        data.add(new Member("3", "1", "M6级  2", "M6"));
        data.add(new Member("4", "1", "M6级  3", "M6"));
        data.add(new Member("5", "1", "M6级  4", "M6"));
        data.add(new Member("6", "1", "M6级  5", "M6"));
        data.add(new Member("7", "1", "M6级  6", "M6"));
        data.add(new Member("8", "1", "M6级  7", "M6"));
        data.add(new Member("9", "1", "M6级  8", "M6"));
        data.add(new Member("10", "1", "M6级  9", "M6"));
        data.add(new Member("11", "7", "M5级", "M5"));
        data.add(new Member("13", "11", "M4级", "M4"));
        data.add(new Member("14", "13", "M3级", "M3"));
        data.add(new Member("15", "14", "M2级", "M2"));
        data.add(new Member("16", "15", "M1级", "M1"));*/

    gradeList.add("全部等级");
    if (!CollectionUtils.isEmpty(list)) {
      for (Member bean : list) {
        if (null == bean) {
          continue;
        }
        Member member = new Member();
        member.setUser_id(bean.getUser_id());
        member.setP_id(bean.getP_id());
        member.setUsername(bean.getUsername());
        member.setUsertruename(bean.getUsertruename());
        member.setGrade(bean.getGrade());
        if (gradeList != null && gradeList.size() > 0) {
          for (int i = 0; i < gradeList.size(); i++) {
            if (bean.getGrade().equals(gradeList.get(i))) {
              break;
            }
            if (i == gradeList.size() - 1) {
              gradeList.add(bean.getGrade());
            }
          }
        } else {
          gradeList.add(bean.getGrade());
        }
        member.setState(bean.getState());
        member.setReason(bean.getReason());
        data.add(member);
      }
      ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_my_team_spinner_item,
          R.id.spinner_item_text, gradeList);
      chooseGrade.setPadding(0, 0, 0, 0);
      chooseGrade.setAdapter(adapter);
    }
  }

  public void show(String msg) {
    if (null != this) {
      ToastUtil.showShort(msg, this);
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.my_team_back:
        MyTeamActivity.this.finish();
        break;
      default:
        break;
    }

  }
}

package com.example.administrator.lubanone.activity.home;

import android.app.Dialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.HomeMyTeamTreeAdapterNew;
import com.example.administrator.lubanone.bean.Member;
import com.example.administrator.lubanone.bean.training.MyTeamResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.Node;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONObject;

public class MyNewTeamActivity extends BaseActivity {


  @BindView(R.id.my_team_back)
  TextView mMyTeamBack;
  @BindView(R.id.my_team_tree)
  ListView mMyTeamTree;
  @BindView(R.id.activity_my_team)
  LinearLayout mActivityMyTeam;

  private LinkedList<Node> mLinkedList = new LinkedList<>();

  private Dialog mDialog;
  private MyTeamResultBean mMyTeamResultBean;


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
          mMyTeamResultBean = result.getResult();
          if (mMyTeamResultBean != null) {
            addData(mMyTeamResultBean.getList(), data);
            HomeMyTeamTreeAdapterNew adapter = new HomeMyTeamTreeAdapterNew(MyNewTeamActivity.this, mMyTeamTree,
                mLinkedList, mActivityMyTeam);
            mMyTeamTree.setAdapter(adapter);

          }
        } else {
          show(ResultUtil.getErrorMsg(result));
        }
      } catch (Exception e) {
        //mDialog.dismiss();
        show(getString(R.string.team_info_empty));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      //mDialog.dismiss();
      show(getString(R.string.team_info_empty));
    }
  };


  private void addData(List<Member> list, List<Node> data) {
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
        member.setState(bean.getState());
        member.setReason(bean.getReason());
        member.setTeamsize(bean.getTeamsize());
        data.add(member);
      }
    }
  }


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_my_new_team;
  }

  @Override
  public void initView() {
    mMyTeamTree.setVisibility(View.VISIBLE);

  }

  @Override
  public void loadData() {

    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
    list.add(paramsToken);

    mDialog = StytledDialog.showProgressDialog(this, getString(R.string.landing_date), true, true);
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.MY_TEAM, MyTeamListener,
        RequestNet.POST);
  }

  @Override
  public void onClick(View v) {

  }


  @OnClick(R.id.my_team_back)
  public void onViewClicked() {
  }
}

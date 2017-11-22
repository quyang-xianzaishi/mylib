package com.example.administrator.lubanone.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.homepage.HomeMyTeamTreeAdapter;
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
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONObject;

/**
 * 我的团队
 */
public class MyTeamActivity extends BaseActivity {
  private LinearLayout activityMyTeam;
  private TextView back;
//  private ImageView sensitizeMember;
  private TextView myLeader;
  private ListView myTeam;
  private Dialog mDialog;

  private HomeMyTeamTreeAdapter mAdapter;
  private LinkedList<Node> mLinkedList = new LinkedList<>();
  private String mCodeSize;//激活码数量
  private TextView mTvMyLevel;
  private MyTeamResultBean mMyTeamResultBean;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_my_team_from_home;
  }

  @Override
  public void initView() {

    mTvMyLevel = (TextView) findViewById(R.id.tv_my_level);
    activityMyTeam = (LinearLayout) this.findViewById(R.id.activity_my_team);
    back = (TextView) this.findViewById(R.id.my_team_back);
//    sensitizeMember = (ImageView) this.findViewById(R.id.sensitize_member);
    myLeader = (TextView) this.findViewById(R.id.my_leader);
    myTeam = (ListView) this.findViewById(R.id.my_team_tree);
    myTeam.setVisibility(View.GONE);

    back.setOnClickListener(this);
//    sensitizeMember.setOnClickListener(this);
    myLeader.setOnClickListener(this);
    mAdapter = new HomeMyTeamTreeAdapter(this, myTeam, mLinkedList, activityMyTeam);
    myTeam.setAdapter(mAdapter);

    Intent intent = getIntent();
    if (null != intent) {
      mCodeSize = intent.getStringExtra("code_size");
    }
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
    //mDialog.show();

  }

  private void requestData() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
    list.add(paramsToken);

    mDialog = StytledDialog.showProgressDialog(this, getString(R.string.landing_date), true, true);
    RequestNet requestNet = new RequestNet(myApp, this, list, Urls.MY_TEAM, MyTeamListener,
        RequestNet.POST);
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
          mMyTeamResultBean = result.getResult();
          if (mMyTeamResultBean != null) {
            myLeader.setText(mMyTeamResultBean.getLeader());
            mTvMyLevel.setText(mMyTeamResultBean.getLevel());
//            mTvPerson.setText(mMyTeamResultBean.getTeamsize() + "");
            addData(mMyTeamResultBean.getList(), data);
            mLinkedList.clear();
            mLinkedList.addAll(NodeHelper.sortNodes(data));
            mAdapter.notifyDataSetChanged();
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
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.my_team_back:
        this.finish();
        break;
      case R.id.sensitize_member:
        //激活会员
        //Toast.makeText(this, "激活会员待开发。。。。。", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(this, ActivateActivity.class);
        intent.putExtra("code_size", DebugUtils.convert(mCodeSize, "0"));
        this.startActivity(intent);
        break;
      case R.id.my_leader:
        //查看我的领导
        Intent intent2 = new Intent();
        intent2.setClass(this, BuySeedsMemberInfoActivity.class);
        intent2.putExtra("userId", DebugUtils.convert(mMyTeamResultBean.getLeader(),""));
        this.startActivity(intent2);
        break;
      case R.id.icon:

        break;
      case R.id.tv_look:
        if (!myTeam.isShown()) {
          myTeam.setVisibility(View.VISIBLE);
        } else {
          myTeam.setVisibility(View.GONE);
        }
        break;
      default:
        break;
    }

  }

  @Override
  public void onResume() {
    super.onResume();
    requestData();
  }

}
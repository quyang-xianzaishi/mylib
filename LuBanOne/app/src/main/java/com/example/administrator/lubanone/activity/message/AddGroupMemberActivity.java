package com.example.administrator.lubanone.activity.message;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.message.AddGroupMemberAdapter;
import com.example.administrator.lubanone.bean.message.GroupMemberBean;
import com.example.administrator.lubanone.bean.message.GroupMemberInfoBean;
import com.example.administrator.lubanone.bean.message.GroupMemberResultBean;
import com.example.administrator.lubanone.customview.MySearchView;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\8\7 0007.
 */

public class AddGroupMemberActivity extends BaseActivity {

  private TextView back;
  private TextView title;
  private MySearchView memberSearch;
  private GridView imgGrid;
  private AddGroupMemberAdapter mAddGroupMemberAdapter;
  private String groupmaster;
  private String groupId;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_add_group_member;
  }

  @Override
  public void initView() {

    if(getIntent()!=null&&getIntent().hasExtra("groupmaster")){
      groupmaster = getIntent().getStringExtra("groupmaster");
    }
    if(getIntent()!=null&&getIntent().hasExtra("groupId")){
      groupId = getIntent().getStringExtra("groupId");
    }

    back = (TextView)this.findViewById(R.id.back);
    title = (TextView) this.findViewById(R.id.title);
    imgGrid = (GridView) this.findViewById(R.id.my_grid_view);
    memberSearch = (MySearchView) this.findViewById(R.id.group_member_search);

    back.setOnClickListener(this);

  }


  //请求数据
  private RequestListener getGroupMemberListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        List<GroupMemberInfoBean> memberBeanList;
        Result<GroupMemberResultBean> result = GsonUtil
            .processJson(jsonObject, GroupMemberResultBean.class);
        if (ResultUtil.isSuccess(result) && null != result ) {
          if(null != result.getResult()){
            GroupMemberResultBean groupMemberResultBean = result.getResult();
            if (groupMemberResultBean != null) {
              memberBeanList = new ArrayList<>();
              for (int i = 0; i < groupMemberResultBean.getData().size(); i++) {
                GroupMemberInfoBean groupMemberBean = new GroupMemberInfoBean();
                groupMemberBean.setUserimg(groupMemberResultBean.getData().get(i).getUserimg());
                groupMemberBean.setUserid(groupMemberResultBean.getData().get(i).getUserid());
                groupMemberBean.setUsername(groupMemberResultBean.getData().get(i).getUsername());
                memberBeanList.add(groupMemberBean);
              }
              title.setText(getString(R.string.conversation_info_title)+"("+memberBeanList.size()+")");
              if(groupmaster!=null&&groupmaster.length()>0){
                if(groupmaster.equals("0")){
                  //非群主
                  memberBeanList.add(new GroupMemberInfoBean("","","add"));
                }else if(groupmaster.equals("1")){
                  //群主
                  memberBeanList.add(new GroupMemberInfoBean("","","add"));
                  memberBeanList.add(new GroupMemberInfoBean("","","delete"));
                }
              }
              //设置数据
              mAddGroupMemberAdapter = new AddGroupMemberAdapter(AddGroupMemberActivity.this,memberBeanList,groupmaster,groupId);
              imgGrid.setAdapter(mAddGroupMemberAdapter);
              //设置回调
              memberSearch.setDatas(memberBeanList);
              memberSearch.setAdapter(mAddGroupMemberAdapter);
              memberSearch
                  .setSearchDataListener(new MySearchView.SearchDatas<GroupMemberInfoBean>() {
                    //参数一：全部数据，参数二：筛选后的数据，参数三：输入的内容
                    @Override
                    public List filterDatas(List<GroupMemberInfoBean> datas,
                        List<GroupMemberInfoBean> filterdatas, String inputstr) {
                      for (int i = 0; i < datas.size(); i++) {
                        //筛选条件
                        if ((datas.get(i).getUsername()).contains(inputstr)) {
                          filterdatas.add(datas.get(i));
                        }
                      }
                      return filterdatas;
                    }
                  });
            }else {
            }
          }
        } else {
          Toast.makeText(AddGroupMemberActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
              getString(R.string.get_group_member_fail)),Toast.LENGTH_LONG).show();
        }
      } catch (Exception e) {
        ToastUtil.showShort(getResources().getString(R.string.get_group_member_fail),
            getApplicationContext());
      }
    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(errorMsf,
          getApplicationContext());
    }
  };

  @Override
  public void loadData() {
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsGroupId = new RequestParams("groupid", groupId);
    list.add(paramsToken);
    list.add(paramsGroupId);
    RequestNet requestNet = new RequestNet((MyApplication)getApplication(), AddGroupMemberActivity.this, list,
        Urls.GET_GROUP_MEMBER, getGroupMemberListener, RequestNet.POST);

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.back:
        AddGroupMemberActivity.this.finish();
        break;
      default:
        break;
    }
  }


  private void getMemberList(){


  }


}

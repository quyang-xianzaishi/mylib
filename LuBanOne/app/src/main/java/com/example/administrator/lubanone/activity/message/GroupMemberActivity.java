package com.example.administrator.lubanone.activity.message;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.message.GroupMemberBean;
import com.example.administrator.lubanone.bean.message.GroupMemberResultBean;
import com.example.administrator.lubanone.customview.MySearchView;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.GlideRoundTransform;
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
 * Created by Administrator on 2017\8\2 0002.
 */

public class GroupMemberActivity extends BaseActivity {

  private MySearchView memberSearch;
  private ListView memberList;
  private TextView noData;
  private TextView back;
  private TextView title;
  private TextView commit;
  private String groupId;
  private List chooseList;
  private GroupMemberAdapter mGroupMemberAdapter;
  private String type;
  private String reasonId;//投诉原因id

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_group_member;
  }

  @Override
  public void initView() {

    if(getIntent()!=null&&getIntent().hasExtra("groupId")){
      groupId = getIntent().getStringExtra("groupId");
    }
    if(getIntent()!=null&&getIntent().hasExtra("type")){
      type = getIntent().getStringExtra("type");
    }
    if(getIntent()!=null&&getIntent().hasExtra("reasonId")){
      reasonId = getIntent().getStringExtra("reasonId");
    }
    back = (TextView) this.findViewById(R.id.choose_member_cancle);
    title = (TextView) this.findViewById(R.id.title);
    commit = (TextView) this.findViewById(R.id.choose_member_commit);
    commit.setClickable(false);
    memberSearch = (MySearchView) this.findViewById(R.id.group_member_search);
    memberList = (ListView) this.findViewById(R.id.group_member_list);
    noData = (TextView) this.findViewById(R.id.no_data);

    back.setOnClickListener(this);
    commit.setOnClickListener(this);

    if(type!=null&&type.equals("delete")){
      //删除群成员
      title.setText(getString(R.string.detele_member_title));
      commit.setText(getString(R.string.detele_member_commit));

    }else if(type!=null&&type.equals("group")){
      //投诉
      title.setText(getString(R.string.choose_member_title));
      commit.setText(getString(R.string.choose_member_commit));

    }

  }

  @Override
  public void loadData() {
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsGroupId = new RequestParams("groupid", groupId);
      list.add(paramsToken);
      list.add(paramsGroupId);
      RequestNet requestNet = new RequestNet((MyApplication)getApplication(), GroupMemberActivity.this, list,
          Urls.GET_GROUP_MEMBER, getGroupMemberListener, RequestNet.POST);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.choose_member_commit:
        if(chooseList!=null&&chooseList.size()>0){
          /*Toast.makeText(GroupMemberActivity.this,Integer.toString(chooseList.size()),
              Toast.LENGTH_SHORT).show();*/
          StringBuffer sb = new StringBuffer("");
          for(int i=0;i<chooseList.size();i++){
            if(i==chooseList.size()-1){
              sb.append(chooseList.get(i));
            }else {
              sb.append(chooseList.get(i)+",");
            }
          }
          if(type!=null&&type.length()>0){
            if(type!=null&&type.equals("delete")){
              if (SPUtils.getStringValue(getApplicationContext(),"user_info", "userid","")!=null&&
                  SPUtils.getStringValue(getApplicationContext(),"user_info", "userid","").length()>0){
                for(int i=0;i<chooseList.size();i++){
                  if(SPUtils.getStringValue(getApplicationContext(),"user_info", "userid","").equals(chooseList.get(i))){
                    //删除成员中包含自己
                    Toast.makeText(GroupMemberActivity.this,getString(R.string.delete_member_me),
                        Toast.LENGTH_LONG).show();
                    break;
                  }
                  if(i==chooseList.size()-1){
                    deleteMember(sb.toString());
                  }
                }
              }else {
                deleteMember(sb.toString());
              }
            }else if(type!=null&&type.equals("group")){
              //群投诉
              if(chooseList.size()>1){
                Toast.makeText(GroupMemberActivity.this,getString(R.string.complain_member_over),
                    Toast.LENGTH_LONG).show();
              }else {
                if (SPUtils.getStringValue(getApplicationContext(),"user_info", "userid","")!=null&&
                    SPUtils.getStringValue(getApplicationContext(),"user_info", "userid","").length()>0){
                  if(SPUtils.getStringValue(getApplicationContext(),"user_info", "userid","").equals(chooseList.get(0))){
                    Toast.makeText(GroupMemberActivity.this,getString(R.string.complain_member_me),
                        Toast.LENGTH_LONG).show();
                  }else {
                    Intent intent = new Intent();
                    intent.putExtra("complainId",sb.toString());
                    intent.putExtra("reasonId",reasonId);
                    intent.putExtra("type",type);
                    intent.setClass(GroupMemberActivity.this,ComplainActivity.class);
                    //intent.setClass(GroupMemberActivity.this,UploadComplainEvidenceActivity.class);
                    GroupMemberActivity.this.startActivity(intent);
                    GroupMemberActivity.this.finish();
                  }
                }else {
                  Intent intent = new Intent();
                  intent.putExtra("complainId",sb.toString());
                  intent.putExtra("reasonId",reasonId);
                  intent.putExtra("type",type);
                  intent.setClass(GroupMemberActivity.this,ComplainActivity.class);
                  GroupMemberActivity.this.startActivity(intent);
                  GroupMemberActivity.this.finish();
                }
              }
            }
          }
        }else {
          Toast.makeText(GroupMemberActivity.this,getString(R.string.create_group_member_null),
              Toast.LENGTH_LONG).show();
        }
        break;
      case R.id.choose_member_cancle:
        GroupMemberActivity.this.finish();
        break;
      default:
        break;

    }

  }

  //请求数据
  private RequestListener getGroupMemberListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    @Override
    public void testSuccess(String jsonObject) {
      try {
        //commit.setText(getString(R.string.choose_friend_commit));
        chooseList = new ArrayList();
        List<GroupMemberBean> memberBeanList;
        Result<GroupMemberResultBean> result = GsonUtil
            .processJson(jsonObject, GroupMemberResultBean.class);
        if (ResultUtil.isSuccess(result) && null != result ) {
          if(null != result.getResult()){
            memberList.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
            GroupMemberResultBean groupMemberResultBean = result.getResult();
            if (groupMemberResultBean != null) {
              memberBeanList = new ArrayList<>();
              for (int i = 0; i < groupMemberResultBean.getData().size(); i++) {
                if (SPUtils.getStringValue(getApplicationContext(),"user_info", "userid","")!=null&&
                    SPUtils.getStringValue(getApplicationContext(),"user_info", "userid","").length()>0){
                  if(groupMemberResultBean.getData().get(i).getUserid().equals(
                      SPUtils.getStringValue(getApplicationContext(),"user_info", "userid",""))){

                  }else {
                    GroupMemberBean groupMemberBean = new GroupMemberBean();
                    groupMemberBean.setUserimg(groupMemberResultBean.getData().get(i).getUserimg());
                    groupMemberBean.setUserid(groupMemberResultBean.getData().get(i).getUserid());
                    groupMemberBean.setUsername(groupMemberResultBean.getData().get(i).getUsername());
                    memberBeanList.add(groupMemberBean);
                  }
                }else {
                  GroupMemberBean groupMemberBean = new GroupMemberBean();
                  groupMemberBean.setUserimg(groupMemberResultBean.getData().get(i).getUserimg());
                  groupMemberBean.setUserid(groupMemberResultBean.getData().get(i).getUserid());
                  groupMemberBean.setUsername(groupMemberResultBean.getData().get(i).getUsername());
                  memberBeanList.add(groupMemberBean);
                }
              }
              //设置数据
              mGroupMemberAdapter = new GroupMemberAdapter(GroupMemberActivity.this,chooseList,memberBeanList);
              memberList.setAdapter(mGroupMemberAdapter);
              //设置回调
              memberSearch.setDatas(memberBeanList);
              memberSearch.setAdapter(mGroupMemberAdapter);
              memberSearch
                  .setSearchDataListener(new MySearchView.SearchDatas<GroupMemberBean>() {
                    //参数一：全部数据，参数二：筛选后的数据，参数三：输入的内容
                    @Override
                    public List filterDatas(List<GroupMemberBean> datas,
                        List<GroupMemberBean> filterdatas, String inputstr) {
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
              memberList.setVisibility(View.GONE);
              noData.setVisibility(View.VISIBLE);
              noData.setBackground(GroupMemberActivity.this.
                  getResources().getDrawable(R.drawable.no_data));
            }
          }
        } else {
          Toast.makeText(GroupMemberActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
              getString(R.string.get_group_member_fail)),Toast.LENGTH_LONG).show();
          memberList.setVisibility(View.GONE);
          noData.setVisibility(View.VISIBLE);
          noData.setBackground(GroupMemberActivity.this.
              getResources().getDrawable(R.drawable.loading_fail));
        }
      } catch (Exception e) {
        ToastUtil.showShort(getResources().getString(R.string.get_group_member_fail),
            getApplicationContext());
        memberList.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
        noData.setBackground(GroupMemberActivity.this.
            getResources().getDrawable(R.drawable.loading_fail));
      }
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(errorMsf,
          getApplicationContext());
      memberList.setVisibility(View.GONE);
      noData.setVisibility(View.VISIBLE);
      noData.setBackground(GroupMemberActivity.this.
          getResources().getDrawable(R.drawable.loading_fail));
    }
  };

  class GroupMemberAdapter extends BaseAdapter {
    private Context mContext;
    private List<GroupMemberBean> users;
    private List chooseFriends = new ArrayList();
    public GroupMemberAdapter(Context mContext,List list,List<GroupMemberBean> friendList) {
      this.mContext = mContext;
      this.users = friendList;
      this.chooseFriends = list;
    }

    public void setData(List<GroupMemberBean> data){
      this.users.clear();
      this.users.addAll(data);
    }

    @Override
    public int getCount() {
      return users.size();
    }

    @Override
    public Object getItem(int position) {
      return users.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder = null;
      if (convertView == null) {
        viewHolder = new ViewHolder();
        convertView = LayoutInflater.from(mContext).inflate(R.layout.group_member_item, null);
        viewHolder.tvState = (TextView) convertView.findViewById(R.id.state);
        viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
        viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
        viewHolder.tvItem = (LinearLayout) convertView.findViewById(R.id.item);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }

      Glide.with(mContext)
          .load(users.get(position).getUserimg())
          .transform(new GlideRoundTransform(mContext, 4))
          .diskCacheStrategy(DiskCacheStrategy.ALL).
          into(viewHolder.img);
      viewHolder.tvName.setText(users.get(position).getUsername());
      viewHolder.tvName.setTag(position);
      viewHolder.tvState.setTag(position);
      viewHolder.tvItem.setTag(position);
      viewHolder.tvItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          /*String str = users.get(position).getUsername();
          if(str!=null&&!str.equals("")){
            Toast.makeText(mContext,users.get(position).getUsername(), Toast.LENGTH_SHORT).show();
          }*/
          if(chooseFriends!=null&&chooseFriends.size()>0){
            for(int i = 0;i<chooseFriends.size();i++){
              if(users.get(position).getUserid().equals(chooseFriends.get(i))){
                ((TextView)v.findViewById(R.id.state)).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_nosel));
                chooseFriends.remove(users.get(position).getUserid());
                if(chooseFriends!=null&&chooseFriends.size()>0){
                  if(type!=null&&type.equals("group")){
                    commit.setText(getString(R.string.choose_friend_commit)
                        +"("+chooseFriends.size()+")");
                  }else if (type!=null&&type.equals("delete")){
                    commit.setText(getString(R.string.detele_member_commit)
                        +"("+chooseFriends.size()+")");
                  }
                  commit.setTextColor(getResources().getColor(R.color.white));
                  commit.setClickable(true);
                }else {
                  if(type!=null&&type.equals("group")){
                    commit.setText(getString(R.string.choose_friend_commit));
                  }else if (type!=null&&type.equals("delete")){
                    commit.setText(getString(R.string.detele_member_commit));
                  }
                  //commit.setText(getString(R.string.choose_friend_commit));
                  commit.setTextColor(getResources().getColor(R.color._888888));
                  commit.setClickable(false);
                }
                break;
              }
              if(i==chooseFriends.size()-1){
                if(type!=null&&type.equals("group")){
                  Toast.makeText(GroupMemberActivity.this,getString(R.string.complain_member_over),
                      Toast.LENGTH_LONG).show();
                  return;
                }
                ((TextView)v.findViewById(R.id.state)).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
                chooseFriends.add(users.get(position).getUserid());
                if(chooseFriends!=null&&chooseFriends.size()>0){
                  if(type!=null&&type.equals("group")){
                    commit.setText(getString(R.string.choose_friend_commit)
                        +"("+chooseFriends.size()+")");
                  }else if (type!=null&&type.equals("delete")){
                    commit.setText(getString(R.string.detele_member_commit)
                        +"("+chooseFriends.size()+")");
                  }
                  commit.setTextColor(getResources().getColor(R.color.white));
                  commit.setClickable(true);
                }else {
                  if(type!=null&&type.equals("group")){
                    commit.setText(getString(R.string.choose_friend_commit));
                  }else if (type!=null&&type.equals("delete")){
                    commit.setText(getString(R.string.detele_member_commit));
                  }
                  //commit.setText(getString(R.string.choose_friend_commit));
                  commit.setTextColor(getResources().getColor(R.color._888888));
                  commit.setClickable(false);
                }
                break;
              }
            }
          }else {
            ((TextView)v.findViewById(R.id.state)).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
            chooseFriends.add(users.get(position).getUserid());
            if(chooseFriends!=null&&chooseFriends.size()>0){
              if(type!=null&&type.equals("group")){
                /*commit.setText(getString(R.string.choose_friend_commit)
                    +"("+chooseFriends.size()+")");*/
                commit.setText(getString(R.string.choose_friend_commit));
              }else if (type!=null&&type.equals("delete")){
                commit.setText(getString(R.string.detele_member_commit)
                    +"("+chooseFriends.size()+")");
              }
              commit.setTextColor(getResources().getColor(R.color.white));
              commit.setClickable(true);
            }else {
              if(type!=null&&type.equals("group")){
                commit.setText(getString(R.string.choose_friend_commit));
              }else if (type!=null&&type.equals("delete")){
                commit.setText(getString(R.string.detele_member_commit));
              }
              //commit.setText(getString(R.string.choose_friend_commit));
              commit.setTextColor(getResources().getColor(R.color._888888));
              commit.setClickable(false);
            }
          }
        }
      });
      if(chooseFriends!=null&&chooseFriends.size()>0){
        for(int i = 0;i<chooseFriends.size();i++){
          if(users.get(position).getUserid().equals(chooseFriends.get(i))){
            viewHolder.tvState.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
            break;
          }
          if(i==chooseFriends.size()-1){
            viewHolder.tvState.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_nosel));
            break;
          }
        }
      }else {
        viewHolder.tvState.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_nosel));
      }
      viewHolder.tvState.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
          if(chooseFriends!=null&&chooseFriends.size()>0){
            if(type!=null&&type.equals("group")){
              Toast.makeText(GroupMemberActivity.this,getString(R.string.complain_member_over),
                  Toast.LENGTH_LONG).show();
              return;
            }
            for(int i = 0;i<chooseFriends.size();i++){
              if(users.get(position).getUserid().equals(chooseFriends.get(i))){
                ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_nosel));
                chooseFriends.remove(users.get(position).getUserid());
                if(chooseFriends!=null&&chooseFriends.size()>0){
                  if(type!=null&&type.equals("group")){
                    commit.setText(getString(R.string.choose_friend_commit)
                        +"("+chooseFriends.size()+")");
                  }else if (type!=null&&type.equals("delete")){
                    commit.setText(getString(R.string.detele_member_commit)
                        +"("+chooseFriends.size()+")");
                  }
                  commit.setTextColor(getResources().getColor(R.color.white));
                  commit.setClickable(true);
                }else {
                  if(type!=null&&type.equals("group")){
                    commit.setText(getString(R.string.choose_friend_commit));
                  }else if (type!=null&&type.equals("delete")){
                    commit.setText(getString(R.string.detele_member_commit));
                  }
                  //commit.setText(getString(R.string.choose_friend_commit));
                  commit.setTextColor(getResources().getColor(R.color._888888));
                  commit.setClickable(false);
                }
                break;
              }
              if(i==chooseFriends.size()-1){
                if(type!=null&&type.equals("group")){
                  Toast.makeText(GroupMemberActivity.this,getString(R.string.complain_member_over),
                      Toast.LENGTH_LONG).show();
                  return;
                }
                ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
                chooseFriends.add(users.get(position).getUserid());
                if(chooseFriends!=null&&chooseFriends.size()>0){
                  if(type!=null&&type.equals("group")){
                    commit.setText(getString(R.string.choose_friend_commit)
                        +"("+chooseFriends.size()+")");
                  }else if (type!=null&&type.equals("delete")){
                    commit.setText(getString(R.string.detele_member_commit)
                        +"("+chooseFriends.size()+")");
                  }
                  commit.setTextColor(getResources().getColor(R.color.white));
                  commit.setClickable(true);
                }else {
                  if(type!=null&&type.equals("group")){
                    commit.setText(getString(R.string.choose_friend_commit));
                  }else if (type!=null&&type.equals("delete")){
                    commit.setText(getString(R.string.detele_member_commit));
                  }
                  //commit.setText(getString(R.string.choose_friend_commit));
                  commit.setTextColor(getResources().getColor(R.color._888888));
                  commit.setClickable(false);
                }
                break;
              }
            }
          }else {
            ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
            chooseFriends.add(users.get(position).getUserid());
            if(chooseFriends!=null&&chooseFriends.size()>0){
              if(type!=null&&type.equals("group")){
                commit.setText(getString(R.string.choose_friend_commit)
                    +"("+chooseFriends.size()+")");
              }else if (type!=null&&type.equals("delete")){
                commit.setText(getString(R.string.detele_member_commit)
                    +"("+chooseFriends.size()+")");
              }
              commit.setTextColor(getResources().getColor(R.color.white));
              commit.setClickable(true);
            }else {
              if(type!=null&&type.equals("group")){
                commit.setText(getString(R.string.choose_friend_commit));
              }else if (type!=null&&type.equals("delete")){
                commit.setText(getString(R.string.detele_member_commit));
              }
              //commit.setText(getString(R.string.choose_friend_commit));
              commit.setTextColor(getResources().getColor(R.color._888888));
              commit.setClickable(false);
            }
          }
        }
      });

      return convertView;
    }

    class ViewHolder {
      TextView tvState;
      TextView tvName;
      ImageView img;
      LinearLayout tvItem;
    }

  }


  private RequestListener deleteMemberListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }
    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        if (ResultUtil.isSuccess(result)) {
          ToastUtil.showShort(getResources().getString(R.string.delete_group_member_success),
              getApplicationContext());
          GroupMemberActivity.this.finish();
        } else {
          Toast.makeText(GroupMemberActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
              getString(R.string.delete_group_member_fail)),Toast.LENGTH_LONG).show();
        }
      } catch (Exception e) {
        Toast.makeText(GroupMemberActivity.this, getString(R.string.delete_group_member_fail)
            ,Toast.LENGTH_LONG).show();
      }
    }
    @Override
    public void onFail(String errorMsf) {
      Toast.makeText(GroupMemberActivity.this, DebugUtils.convert(errorMsf,
          getString(R.string.delete_group_member_fail)),Toast.LENGTH_LONG).show();
    }
  };
  private void deleteMember(String useridlist){
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsGroupId = new RequestParams("groupid", groupId);
    RequestParams paramsUserIdList = new RequestParams("useridlist", useridlist);
    list.add(paramsToken);
    list.add(paramsGroupId);
    list.add(paramsUserIdList);
    RequestNet requestNet = new RequestNet((MyApplication)getApplication(), GroupMemberActivity.this, list,
        Urls.DELETE_GROUP_MEMBER, deleteMemberListener, RequestNet.POST);

  }

}

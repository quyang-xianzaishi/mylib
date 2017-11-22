package com.example.administrator.lubanone.activity.message;

import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.adapter.message.GroupAdapter;
import com.example.administrator.lubanone.bean.message.Group;
import com.example.administrator.lubanone.bean.message.GroupListResultBean;
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
 * Created by Administrator on 2017\6\29 0029.
 */

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener  {

    private TextView back;
    private MySearchView<Group> addressSearch;
    private ListView groupListView;
    private TextView noData;
    private List<Group> groupList;
    private GroupAdapter mGroupAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_group_chat);
        initView();
    }

    private void initView(){

        back = (TextView) this.findViewById(R.id.activity_group_chat_back);
        addressSearch = (MySearchView<Group>) this.findViewById(R.id.group_search);
        groupListView = (ListView) this.findViewById(R.id.group_list);
        noData = (TextView) this.findViewById(R.id.no_data);
        back.setOnClickListener(this);

        //getGroupList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_group_chat_back:
                GroupChatActivity.this.finish();
                break;
            default:
                break;
        }

    }

  @Override
  public void onResume(){
    super.onResume();
    getGroupList();
  }


    private RequestListener getGroupListListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }

        @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<GroupListResultBean> result = GsonUtil.processJson(jsonObject, GroupListResultBean.class);
                if (result != null && ResultUtil.isSuccess(result)) {
                    GroupListResultBean groupListResultBean = result.getResult();
                    if(groupListResultBean!=null){
                        if(groupListResultBean.getData()!=null){
                            noData.setVisibility(View.GONE);
                            groupListView.setVisibility(View.VISIBLE);
                            groupList = groupListResultBean.getData();
                            mGroupAdapter = new GroupAdapter(GroupChatActivity.this,groupList);
                            groupListView.setAdapter(mGroupAdapter);
                            addressSearch.setDatas(groupList);
                            addressSearch.setAdapter(mGroupAdapter);
                            addressSearch.setSearchDataListener(new MySearchView.SearchDatas<Group>() {
                                //参数一：全部数据，参数二：筛选后的数据，参数三：输入的内容
                                @Override
                                public List filterDatas(List<Group> datas, List<Group> filterdatas, String inputstr) {
                                    for (int i = 0; i < datas.size(); i++) {
                                        //筛选条件
                                        if ((datas.get(i).getGroupname()).contains(inputstr)) {
                                            filterdatas.add(datas.get(i));
                                        }
                                    }
                                    return filterdatas;
                                }
                            });
                        }else {
                            noData.setVisibility(View.VISIBLE);
                            groupListView.setVisibility(View.GONE);
                            noData.setBackground(GroupChatActivity.this.
                                getResources().getDrawable(R.drawable.no_data));
                        }
                    }else {
                        /*ToastUtil.showShort(getResources().getString(R.string.get_group_list_fail),
                            getApplicationContext());*/
                        noData.setVisibility(View.VISIBLE);
                        groupListView.setVisibility(View.GONE);
                        noData.setBackground(GroupChatActivity.this.
                            getResources().getDrawable(R.drawable.no_data));
                    }

                } else {
                    Toast.makeText(GroupChatActivity.this,
                        DebugUtils.convert(ResultUtil.getErrorMsg(result),
                            getString(R.string.get_group_list_fail)), Toast.LENGTH_LONG).show();
                    noData.setVisibility(View.VISIBLE);
                    groupListView.setVisibility(View.GONE);
                    noData.setBackground(GroupChatActivity.this.
                        getResources().getDrawable(R.drawable.loading_fail));
                }
            } catch (Exception e) {
                Toast.makeText(GroupChatActivity.this,
                    getString(R.string.get_group_list_fail), Toast.LENGTH_LONG).show();
                noData.setVisibility(View.VISIBLE);
                groupListView.setVisibility(View.GONE);
                noData.setBackground(GroupChatActivity.this.
                    getResources().getDrawable(R.drawable.loading_fail));
            }
        }

        @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
        @Override
        public void onFail(String errorMsf) {
            Toast.makeText(GroupChatActivity.this, DebugUtils.convert(errorMsf,
                getString(R.string.get_group_list_fail)), Toast.LENGTH_LONG).show();
            noData.setVisibility(View.VISIBLE);
            groupListView.setVisibility(View.GONE);
            noData.setBackground(GroupChatActivity.this.
                getResources().getDrawable(R.drawable.loading_fail));
        }
    };

    private void getGroupList(){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        list.add(paramsToken);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), GroupChatActivity.this, list,
            Urls.GET_GROUP_LIST, getGroupListListener, RequestNet.POST);
    }


}

package com.example.administrator.lubanone.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.bean.message.AddFriendBean;
import com.example.administrator.lubanone.bean.message.SearchFriendBean;
import com.example.administrator.lubanone.bean.message.SearchResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 * Created by zheng on 2017\6\28 0028.
 */

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back;
    private EditText searchEdit;
    private EditText search;
    private TextView searchTv;
    private RecyclerView searchRecylerView;
    private TextView noData;
    private List<Map<String,String>> searchList;
    private List<AddFriendBean> datas;
    private List<SearchFriendBean> mSearchFriendBeanList;
    private AddFriendAdapter mAddFriendAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_add_friend);
        initView();
    }

    public void initView() {
        back = (TextView) this.findViewById(R.id.activity_add_friend_back);
        searchEdit = (EditText) this.findViewById(R.id.activity_friend_search);
        searchTv = (TextView) this.findViewById(R.id.search_wancheng);
        searchRecylerView = (RecyclerView)this.findViewById(R.id.friend_search_recyclerview);
        noData = (TextView) this.findViewById(R.id.search_no_user);
        search = (EditText) this.findViewById(R.id.search_edit);

        back.setOnClickListener(this);
        searchTv.setOnClickListener(this);
        searchRecylerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecylerView.setAdapter(new AddFriendAdapter());

        searchList = new ArrayList<>();
        mSearchFriendBeanList = new ArrayList<>();
        mAddFriendAdapter = new AddFriendAdapter();
        search.setOnClickListener(this);
        /*search.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent();
                intent.setClass(AddFriendActivity.this,SearchFriendActivity.class);
                AddFriendActivity.this.startActivity(intent);
                AddFriendActivity.this.finish();
                return false;
            }
        });*/
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null&&(s.length()!=0)){
                    refreshData();
                }else {
                    //searchList.clear();
                    mSearchFriendBeanList.clear();
                    mAddFriendAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void refreshData(){
        datas = new ArrayList<>();
        datas.add(new AddFriendBean("1","https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4055210087,2472657194&fm=26&gp=0.jpg","郑连","1"));
        datas.add(new AddFriendBean("2","https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4055210087,2472657194&fm=26&gp=0.jpg","夏天","2"));
        //searchRecylerView.setAdapter(new AddFriendAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_add_friend_back:
                AddFriendActivity.this.finish();
                break;
            case R.id.search_wancheng:
                //搜索请求
                mSearchFriendBeanList.clear();

                if(searchEdit.getText().toString().trim()!=null&&
                    searchEdit.getText().toString().trim().length()>0){
                    searchFriend(searchEdit.getText().toString().trim());
                }else {
                    ToastUtil.showShort(getString(R.string.search_content_null),this);
                }
                break;
            case R.id.search_edit:
                Intent intent = new Intent();
                intent.setClass(AddFriendActivity.this,SearchFriendActivity.class);
                AddFriendActivity.this.startActivity(intent);
                AddFriendActivity.this.finish();
                break;
            default:
                break;
        }
    }


    private class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    AddFriendActivity.this).inflate(R.layout.add_friend_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            Glide.with(AddFriendActivity.this)
                    .load(mSearchFriendBeanList.get(position).getUserimg())
                    .placeholder(R.mipmap.b)
                    .error(R.mipmap.b)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).
                    into(holder.addFriendItemImg);
            holder.addFriendItemName.setText(mSearchFriendBeanList.get(position).getUsername());
           /* final String str = datas.get(position).getUserState();
            if(str!=null){
                if(str.equals("1")){
                    holder.addFriendItemState.setText("加为好友");
                }else  if(str.equals("2")){
                    holder.addFriendItemState.setText("发送消息");
                }else{
                    holder.addFriendItemState.setText("加为好友");
                }
            }else {
                holder.addFriendItemState.setText("加为好友");
            }
            //holder.addFriendItemState.setText(datas.get(position).getUserState());
            holder.addFriendItemState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(str!=null){
                        if(str.equals("1")){
                            //申请加好友
                            Intent intent = new Intent();
                            intent.setClass(AddFriendActivity.this,MemberInfoActivity.class);
                            //intent.setClass(AddFriendActivity.this,FriendTestActivity.class);
                            AddFriendActivity.this.startActivity(intent);
                        }else  if(str.equals("2")){
                            //发送消息
                            RongIM.getInstance().startPrivateChat(AddFriendActivity.this,
                                datas.get(position).getUserId(),datas.get(position).getUserName());
                            AddFriendActivity.this.finish();
                        }else{
                            //申请加好友
                            Intent intent = new Intent();
                            intent.setClass(AddFriendActivity.this,FriendTestActivity.class);
                            AddFriendActivity.this.startActivity(intent);
                        }
                    }else {
                        //申请加好友
                        Intent intent = new Intent();
                        intent.setClass(AddFriendActivity.this,FriendTestActivity.class);
                        AddFriendActivity.this.startActivity(intent);
                    }

                }
            });*/
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //查看个人信息
                    Intent intent = new Intent();
                    intent.putExtra("userId",mSearchFriendBeanList.get(position).getUserid());
                    intent.setClass(AddFriendActivity.this,MemberInfoActivity.class);
                    AddFriendActivity.this.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mSearchFriendBeanList!=null){
                return mSearchFriendBeanList.size();
            }
             return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView addFriendItemImg;
            TextView addFriendItemName;
            //TextView addFriendItemState;


            public MyViewHolder(View view) {
                super(view);

                addFriendItemImg = (ImageView) view.findViewById(R.id.add_friend_item_img);
                addFriendItemName = (TextView) view.findViewById(R.id.add_friend_item_name);
                //addFriendItemState = (TextView) view.findViewById(R.id.add_friend_item_btn);

            }

        }
    }

    private RequestListener searchFriendListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }

        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<SearchResultBean> result = GsonUtil.processJson(jsonObject, SearchResultBean.class);
                if(result!=null){
                    if(result.getResult()!=null){
                        //mSearchFriendBeanList = new ArrayList<>();
                        for(int i = 0;i<result.getResult().getList().size();i++){
                            mSearchFriendBeanList.add(new SearchFriendBean(
                                result.getResult().getList().get(i).getUserid(),
                                result.getResult().getList().get(i).getUsername(),
                                result.getResult().getList().get(i).getUserimg()));
                            searchRecylerView.setAdapter(mAddFriendAdapter);
                            searchRecylerView.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                        }
                    }else {
                        searchRecylerView.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                }else {
                    searchRecylerView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                ToastUtil.showShort(getResources().getString(R.string.search_user_fail),
                    getApplicationContext());
            }

        }

        @Override
        public void onFail(String errorMsf) {
            ToastUtil.showShort(getResources().getString(R.string.search_user_fail),
                getApplicationContext());
        }
    };


    private void searchFriend(String str){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsUserId = new RequestParams("searchname", str);
        list.add(paramsToken);
        list.add(paramsUserId);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), this, list,
            Urls.SEARCH_FRIEND, searchFriendListener, RequestNet.POST);
    }

}

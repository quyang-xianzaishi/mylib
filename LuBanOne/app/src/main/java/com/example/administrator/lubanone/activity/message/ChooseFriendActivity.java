package com.example.administrator.lubanone.activity.message;

import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.bean.message.Address;
import com.example.administrator.lubanone.bean.message.AddressResultBean;
import com.example.administrator.lubanone.bean.message.CreateGroupBean;
import com.example.administrator.lubanone.customview.MySearchView;
import com.example.administrator.lubanone.customview.SideBarView;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ChineseToEnglish;
import com.example.administrator.lubanone.utils.CompareSort;
import com.example.administrator.lubanone.utils.GlideRoundTransform;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import io.rong.imkit.RongIM;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONObject;


/**
 * Created by zheng on 2017\6\28 0028.
 */

public class ChooseFriendActivity extends AppCompatActivity implements View.OnClickListener {

    //private EditText  friendSearch;
    private LinearLayout chooseFriendImg;
    private HorizontalScrollView chooseHorizontal;
    private MySearchView<Address> friendSearch;
    private RelativeLayout listRelative;
    private TextView noData;
    private ListView friendList;
    private SideBarView friendSlide;
    private TextView cancleTx;
    private TextView commitTx;
    private TextView title;
    private FriendAdapter friendAdapter;
    private TextView mTip;
    private List chooseList;
    private String groupId;//加入群组
    private String type;//分辨是创建群组还是加入群组还是推荐好友
    private List<String> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_choose_friend);

        memberList = new ArrayList<>();
        if(getIntent()!=null&&getIntent().hasExtra("type")){
          type = getIntent().getStringExtra("type");
        }

        if(getIntent()!=null&&getIntent().hasExtra("groupId")){
        groupId = getIntent().getStringExtra("groupId");
        }
        if(getIntent()!=null&&getIntent().hasExtra("memberList")){
            memberList = getIntent().getStringArrayListExtra("memberList");
        }
        initView();
    }

    public void initView() {

        cancleTx = (TextView) this.findViewById(R.id.choose_friend_cancle);
        commitTx = (TextView) this.findViewById(R.id.choose_friend_commit);
        title = (TextView) this.findViewById(R.id.choose_friend_title);
        chooseHorizontal = (HorizontalScrollView) this.findViewById(R.id.horizonMenu);
        chooseFriendImg = (LinearLayout) this.findViewById(R.id.choose_friend_img);
        friendSearch = (MySearchView<Address>) this.findViewById(R.id.friend_search);
        listRelative = (RelativeLayout) this.findViewById(R.id.list_relative_layout);
        noData = (TextView) this.findViewById(R.id.no_data);
        friendList = (ListView) this.findViewById(R.id.choose_friend_list);
        friendSlide = (SideBarView) this.findViewById(R.id.choose_friend_slide);
        mTip = (TextView) this.findViewById(R.id.tip);

        cancleTx.setOnClickListener(this);
        commitTx.setOnClickListener(this);
        commitTx.setClickable(false);

        //title.setText("");
        requestData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.choose_friend_cancle:
                //取消
                chooseList = null;
                ChooseFriendActivity.this.finish();
                break;
            case R.id.choose_friend_commit:
                //确定
                if(chooseList!=null&&chooseList.size()>0){
                    /*Toast.makeText(ChooseFriendActivity.this,Integer.toString(chooseList.size()),
                        Toast.LENGTH_SHORT).show();*/
                  StringBuffer sb = new StringBuffer("");
                  for(int i=0;i<chooseList.size();i++){
                      if(i==chooseList.size()-1){
                          sb.append(chooseList.get(i));
                      }else {
                          sb.append(chooseList.get(i)+",");
                      }
                    //sb.append(","+chooseList.get(i));
                  }
                  if(type!=null&&type.length()>0){
                    if(type.equals("create")){
                        if(chooseList.size()<2){
                            Toast.makeText(ChooseFriendActivity.this,getString(R.string.create_group_member_less),
                                Toast.LENGTH_LONG).show();
                        }else {
                            createGroup(sb.toString());
                        }
                    }else if(type.equals("add")){
                      addGroup(sb.toString());
                    }else if(type.equals("recommend")){
                        //推荐好友
                        /*Toast.makeText(ChooseFriendActivity.this,"推荐好友",
                            Toast.LENGTH_SHORT).show();*/
                        recommendFriend(sb.toString());
                    }
                  }
                }else {
                    if(type.equals("create")){
                        Toast.makeText(ChooseFriendActivity.this,getString(R.string.create_group_member_null),
                            Toast.LENGTH_LONG).show();
                    }else if(type.equals("add")){
                        Toast.makeText(ChooseFriendActivity.this,getString(R.string.recommend_friend_null),
                            Toast.LENGTH_LONG).show();
                    }else if(type.equals("recommend")){
                        Toast.makeText(ChooseFriendActivity.this,getString(R.string.recommend_friend_null),
                            Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                break;
        }

    }

    private class MyLetterSelectListener implements SideBarView.LetterSelectListener{

        @Override
        public void onLetterSelected(String letter) {
            setListviewPosition(letter);
            mTip.setText(letter);
            mTip.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLetterChanged(String letter) {
            setListviewPosition(letter);
            mTip.setText(letter);
        }

        @Override
        public void onLetterReleased(String letter) {
            mTip.setVisibility(View.GONE);
        }

    }


    private void setListviewPosition(String letter){
        int firstLetterPosition = friendAdapter.getFirstLetterPosition(letter);
        if(firstLetterPosition != -1){
            friendList.setSelection(firstLetterPosition);
        }
    }

    class FriendAdapter extends BaseAdapter {
        private Context mContext;
        private List<Address> users;
        private List chooseFriends = new ArrayList();
        public FriendAdapter(Context mContext,List list,List<Address> friendList) {
            this.mContext = mContext;
            this.users = friendList;
            this.chooseFriends = list;
        }

        public void setData(List<Address> data){
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.choose_friend_item, null);
                viewHolder.tvState = (TextView) convertView.findViewById(R.id.state);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
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
            viewHolder.tvName.setText(users.get(position).getName());
            viewHolder.tvItem.setTag(position);
            viewHolder.tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*String str = users.get(position).getName();
                    if(str!=null&&!str.equals("")){
                        Toast.makeText(mContext,users.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }*/
                    if(memberList!=null&&memberList.size()>0){
                        for(int j = 0;j<memberList.size();j++){
                            if(users.get(position).getUserId().equals(memberList.get(j).toString())){
                                break;
                            }
                            if(j==memberList.size()-1){
                                if(chooseFriends!=null&&chooseFriends.size()>0){
                                    for(int i = 0;i<chooseFriends.size();i++){
                                        if(users.get(position).getUserId().equals(chooseFriends.get(i))){
                                            ((TextView)v.findViewById(R.id.state)).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_nosel));
                                            chooseFriends.remove(users.get(position).getUserId());
                                            if(chooseFriends!=null&&chooseFriends.size()>0){
                                                commitTx.setText(getString(R.string.wancheng_title)+"("+chooseFriends.size()+")");
                                                commitTx.setTextColor(getResources().getColor(R.color.white));
                                            }else {
                                                commitTx.setText(getString(R.string.wancheng_title));
                                                commitTx.setTextColor(getResources().getColor(R.color.c666));
                                            }
                                            deleteImage(users.get(position));
                                            break;
                                        }
                                        if(i==chooseFriends.size()-1){
                                            ((TextView)v.findViewById(R.id.state)).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
                                            chooseFriends.add(users.get(position).getUserId());
                                            if(chooseFriends!=null&&chooseFriends.size()>0){
                                                commitTx.setText(getString(R.string.wancheng_title)
                                                    +"("+chooseFriends.size()+")");
                                                commitTx.setTextColor(getResources().getColor(R.color.white));
                                            }else {
                                                commitTx.setText(getString(R.string.wancheng_title));
                                                commitTx.setTextColor(getResources().getColor(R.color.c666));
                                            }
                                            showCheckImage(users.get(position).getUserimg(),users.get(position));
                                            break;
                                        }
                                    }
                                }else {
                                    ((TextView)v.findViewById(R.id.state)).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
                                    chooseFriends.add(users.get(position).getUserId());
                                    if(chooseFriends!=null&&chooseFriends.size()>0){
                                        commitTx.setText(getString(R.string.wancheng_title)+"("+chooseFriends.size()+")");
                                        commitTx.setTextColor(getResources().getColor(R.color.white));
                                    }else {
                                        commitTx.setText(getString(R.string.wancheng_title));
                                        commitTx.setTextColor(getResources().getColor(R.color.c666));
                                    }
                                    showCheckImage(users.get(position).getUserimg(),users.get(position));
                                }
                                break;
                            }
                        }
                    }else {
                        if(chooseFriends!=null&&chooseFriends.size()>0){
                            for(int i = 0;i<chooseFriends.size();i++){
                                if(users.get(position).getUserId().equals(chooseFriends.get(i))){
                                    ((TextView)v.findViewById(R.id.state)).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_nosel));
                                    chooseFriends.remove(users.get(position).getUserId());
                                    if(chooseFriends!=null&&chooseFriends.size()>0){
                                        commitTx.setText(getString(R.string.wancheng_title)+"("+chooseFriends.size()+")");
                                        commitTx.setTextColor(getResources().getColor(R.color.white));
                                    }else {
                                        commitTx.setText(getString(R.string.wancheng_title));
                                        commitTx.setTextColor(getResources().getColor(R.color.c666));
                                    }
                                    deleteImage(users.get(position));
                                    break;
                                }
                                if(i==chooseFriends.size()-1){
                                    ((TextView)v.findViewById(R.id.state)).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
                                    chooseFriends.add(users.get(position).getUserId());
                                    if(chooseFriends!=null&&chooseFriends.size()>0){
                                        commitTx.setText(getString(R.string.wancheng_title)+"("+chooseFriends.size()+")");
                                        commitTx.setTextColor(getResources().getColor(R.color.white));
                                    }else {
                                        commitTx.setText(getString(R.string.wancheng_title));
                                        commitTx.setTextColor(getResources().getColor(R.color.c666));
                                    }
                                    showCheckImage(users.get(position).getUserimg(),users.get(position));
                                    break;
                                }
                            }
                        }else {
                            ((TextView)v.findViewById(R.id.state)).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
                            chooseFriends.add(users.get(position).getUserId());
                            if(chooseFriends!=null&&chooseFriends.size()>0){
                                commitTx.setText(getString(R.string.wancheng_title)+"("+chooseFriends.size()+")");
                                commitTx.setTextColor(getResources().getColor(R.color.white));
                            }else {
                                commitTx.setText(getString(R.string.wancheng_title));
                                commitTx.setTextColor(getResources().getColor(R.color.c666));
                            }
                            showCheckImage(users.get(position).getUserimg(),users.get(position));
                        }
                    }
                }
            });
            if(memberList!=null&&memberList.size()>0){
                for(int j = 0;j<memberList.size();j++){
                    if(users.get(position).getUserId().equals(memberList.get(j).toString())){
                        viewHolder.tvState.setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
                        break;
                    }
                    if(j==memberList.size()-1){
                        if(chooseFriends!=null&&chooseFriends.size()>0){
                            for(int i = 0;i<chooseFriends.size();i++){
                                if(users.get(position).getUserId().equals(chooseFriends.get(i))){
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
                        break;
                    }
                }
            }else {
                if(chooseFriends!=null&&chooseFriends.size()>0){
                    for(int i = 0;i<chooseFriends.size();i++){
                        if(users.get(position).getUserId().equals(chooseFriends.get(i))){
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
            }
            /*if(chooseFriends!=null&&chooseFriends.size()>0){
                for(int i = 0;i<chooseFriends.size();i++){
                    if(users.get(position).getUserId().equals(chooseFriends.get(i))){
                        viewHolder.tvState.setBackground(mContext.getResources().getDrawable(R.mipmap.i_agree_pre_x));
                        break;
                    }
                    if(i==chooseFriends.size()-1){
                        viewHolder.tvState.setBackground(mContext.getResources().getDrawable(R.mipmap.i_agree_back_2x));
                        break;
                    }
                }
            }else {
                viewHolder.tvState.setBackground(mContext.getResources().getDrawable(R.mipmap.i_agree_back_2x));
            }*/
            viewHolder.tvState.setTag(position);
            viewHolder.tvState.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    if(memberList!=null&&memberList.size()>0){
                        for(int j = 0;j<memberList.size();j++){
                            if(users.get(position).getUserId().equals(memberList.get(j).toString())){
                                break;
                            }
                            if(j==memberList.size()-1){
                                if(chooseFriends!=null&&chooseFriends.size()>0){
                                    for(int i = 0;i<chooseFriends.size();i++){
                                        if(users.get(position).getUserId().equals(chooseFriends.get(i))){
                                            ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_nosel));
                                            chooseFriends.remove(users.get(position).getUserId());
                                            if(chooseFriends!=null&&chooseFriends.size()>0){
                                                commitTx.setText(getString(R.string.wancheng_title)+"("+chooseFriends.size()+")");
                                                commitTx.setTextColor(getResources().getColor(R.color.white));
                                            }else {
                                                commitTx.setText(getString(R.string.wancheng_title));
                                                commitTx.setTextColor(getResources().getColor(R.color.c666));
                                            }
                                            deleteImage(users.get(position));
                                            break;
                                        }
                                        if(i==chooseFriends.size()-1){
                                            ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
                                            chooseFriends.add(users.get(position).getUserId());
                                            if(chooseFriends!=null&&chooseFriends.size()>0){
                                                commitTx.setText(getString(R.string.wancheng_title)+"("+chooseFriends.size()+")");
                                                commitTx.setTextColor(getResources().getColor(R.color.white));
                                            }else {
                                                commitTx.setText(getString(R.string.wancheng_title));
                                                commitTx.setTextColor(getResources().getColor(R.color.c666));
                                            }
                                            showCheckImage(users.get(position).getUserimg(),users.get(position));
                                            break;
                                        }
                                    }
                                }else {
                                    ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
                                    chooseFriends.add(users.get(position).getUserId());
                                    if(chooseFriends!=null&&chooseFriends.size()>0){
                                        commitTx.setText(getString(R.string.wancheng_title)+"("+chooseFriends.size()+")");
                                        commitTx.setTextColor(getResources().getColor(R.color.white));
                                    }else {
                                        commitTx.setText(getString(R.string.wancheng_title));
                                        commitTx.setTextColor(getResources().getColor(R.color.c666));
                                    }
                                    showCheckImage(users.get(position).getUserimg(),users.get(position));
                                }
                                break;
                            }
                        }
                    }else {
                        if(chooseFriends!=null&&chooseFriends.size()>0){
                            for(int i = 0;i<chooseFriends.size();i++){
                                if(users.get(position).getUserId().equals(chooseFriends.get(i))){
                                    ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_nosel));
                                    chooseFriends.remove(users.get(position).getUserId());
                                    if(chooseFriends!=null&&chooseFriends.size()>0){
                                        commitTx.setText(getString(R.string.wancheng_title)+"("+chooseFriends.size()+")");
                                        commitTx.setTextColor(getResources().getColor(R.color.white));
                                    }else {
                                        commitTx.setText(getString(R.string.wancheng_title));
                                        commitTx.setTextColor(getResources().getColor(R.color.c666));
                                    }
                                    deleteImage(users.get(position));
                                    break;
                                }
                                if(i==chooseFriends.size()-1){
                                    ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
                                    chooseFriends.add(users.get(position).getUserId());
                                    if(chooseFriends!=null&&chooseFriends.size()>0){
                                        commitTx.setText(getString(R.string.wancheng_title)+"("+chooseFriends.size()+")");
                                        commitTx.setTextColor(getResources().getColor(R.color.white));
                                    }else {
                                        commitTx.setText(getString(R.string.wancheng_title));
                                        commitTx.setTextColor(getResources().getColor(R.color.c666));
                                    }
                                    showCheckImage(users.get(position).getUserimg(),users.get(position));
                                    break;
                                }
                            }
                        }else {
                            ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.icon_sel));
                            chooseFriends.add(users.get(position).getUserId());
                            if(chooseFriends!=null&&chooseFriends.size()>0){
                                commitTx.setText(getString(R.string.wancheng_title)+"("+chooseFriends.size()+")");
                                commitTx.setTextColor(getResources().getColor(R.color.white));
                            }else {
                                commitTx.setText(getString(R.string.wancheng_title));
                                commitTx.setTextColor(getResources().getColor(R.color.c666));
                            }
                            showCheckImage(users.get(position).getUserimg(),users.get(position));
                        }
                    }
                    /*if(chooseFriends!=null&&chooseFriends.size()>0){
                        for(int i = 0;i<chooseFriends.size();i++){
                            if(users.get(position).getUserId().equals(chooseFriends.get(i))){
                                Toast.makeText(mContext,"取消选中:"+v.getTag(), Toast.LENGTH_SHORT).show();
                                ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.i_agree_back_2x));
                                chooseFriends.remove(users.get(position).getUserId());
                                if(chooseFriends!=null&&chooseFriends.size()>0){
                                    commitTx.setText("确定("+chooseFriends.size()+")");
                                }else {
                                    commitTx.setText("确定");
                                }
                                deleteImage(users.get(position));
                                break;
                            }
                            if(i==chooseFriends.size()-1){
                                Toast.makeText(mContext,"选中:"+v.getTag(), Toast.LENGTH_SHORT).show();
                                ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.i_agree_pre_x));
                                chooseFriends.add(users.get(position).getUserId());
                                if(chooseFriends!=null&&chooseFriends.size()>0){
                                    commitTx.setText("确定("+chooseFriends.size()+")");
                                }else {
                                    commitTx.setText("确定");
                                }
                                showCheckImage(users.get(position).getUserimg(),users.get(position));
                                break;
                            }
                        }
                    }else {
                        Toast.makeText(mContext,"选中:"+v.getTag(), Toast.LENGTH_SHORT).show();
                        ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.i_agree_pre_x));
                        chooseFriends.add(users.get(position).getUserId());
                        if(chooseFriends!=null&&chooseFriends.size()>0){
                            commitTx.setText("确定("+chooseFriends.size()+")");
                        }else {
                            commitTx.setText("确定");
                        }
                        showCheckImage(users.get(position).getUserimg(),users.get(position));
                    }*/
                }
            });
            //当前的item的title与上一个item的title不同的时候回显示title(A,B,C......)
            if(position == getFirstLetterPosition(position) && !users.get(position).getLetter().equals("@")
                || users.get(position).getLetter().equals("☆")){
                if(users.get(position).getLetter().equals("☆")) {
                    if (getFirstLetterPosition(users.get(position).getLetter().toString())
                        == position) {
                        viewHolder.tvTitle.setVisibility(View.VISIBLE);
                        viewHolder.tvTitle.setText(getString(R.string.star_sign_friend));
                    } else {
                        viewHolder.tvTitle.setVisibility(View.GONE);
                    }
                }else {
                    viewHolder.tvTitle.setVisibility(View.VISIBLE);
                    viewHolder.tvTitle.setText(users.get(position).getLetter().toUpperCase());
                }
            }else {
                viewHolder.tvTitle.setVisibility(View.GONE);
            }


            return convertView;
        }

        /**
         * 顺序遍历所有元素．找到position对应的title是什么（A,B,C?）然后找这个title下的第一个item对应的position
         *
         * @param position
         * @return
         */
        private int getFirstLetterPosition(int position) {

            String letter = users.get(position).getLetter();
            int cnAscii = ChineseToEnglish.getCnAscii(letter.toUpperCase().charAt(0));
            int size = users.size();
            for (int i = 0; i < size; i++) {
                if(cnAscii == users.get(i).getLetter().charAt(0)){
                    return i;
                }
            }
            return -1;
        }

        /**
         * 顺序遍历所有元素．找到letter下的第一个item对应的position
         * @param letter
         * @return
         */
        public int getFirstLetterPosition(String letter){
            int size = users.size();
            for (int i = 0; i < size; i++) {
                if(letter.charAt(0) == users.get(i).getLetter().charAt(0)){
                    return i;
                }
                if(letter.equals("☆")){
                    if(users.get(i).getLetter().equals("☆")){
                        return i;
                    }
                }
            }
            return -1;
        }

        class ViewHolder {
            TextView tvState;
            TextView tvName;
            TextView tvTitle;
            ImageView img;
            LinearLayout tvItem;
        }

    }


    //显示选择的头像
    private int maxWidth = 0;
    private Boolean getMaxWidth = false;
    private void showCheckImage(String img, Address glufineid) {
        // 包含TextView的LinearLayout
        // 参数设置
        android.widget.LinearLayout.LayoutParams menuLinerLayoutParames = new LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,  LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(this).inflate(
            R.layout.header_item, null);
        ImageView images = (ImageView) view.findViewById(R.id.iv_avatar);
        menuLinerLayoutParames.setMargins(0, 0, 5, 0);

        // 设置id，方便后面删除
        view.setTag(glufineid);
        if (img != null) {
            Glide.with(ChooseFriendActivity.this)
                .load(img)
                .transform(new GlideRoundTransform(ChooseFriendActivity.this, 4))
                .diskCacheStrategy(DiskCacheStrategy.ALL).
                into(images);
            ///images.setImageResource(R.mipmap.icon_head);
        }
        if(chooseList.size()>0){
            commitTx.setClickable(true);
        }
        if(chooseList.size()>6){
            if(!getMaxWidth){
                maxWidth = chooseFriendImg.getMeasuredWidth();
                getMaxWidth = true;
            }
            chooseHorizontal.setLayoutParams(new RelativeLayout.LayoutParams(
                maxWidth,LayoutParams.WRAP_CONTENT));
        }else {
            chooseHorizontal.setLayoutParams(new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        }
        chooseFriendImg.addView(view, menuLinerLayoutParames);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                chooseHorizontal.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        },100L);



    }

    //删除选择的头像
    private void deleteImage(Address glufineid) {
        View view = (View) chooseFriendImg.findViewWithTag(glufineid);
        chooseFriendImg.removeView(view);
        if(chooseList.size()>0){
            commitTx.setClickable(true);
        }else {
            commitTx.setClickable(false);
        }
        if(chooseList.size()>6){
            chooseHorizontal.setLayoutParams(new RelativeLayout.LayoutParams(
                maxWidth,LayoutParams.WRAP_CONTENT));
        }else {
            chooseHorizontal.setLayoutParams(new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        }
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                chooseHorizontal.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        },100L);
    }

    //请求数据
    private RequestListener AddressListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }

        @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
        @Override
        public void testSuccess(String jsonObject) {
            try {
                chooseList = new ArrayList();
                List<Address> addresses = new ArrayList<>();
                Result<AddressResultBean> result = GsonUtil
                    .processJson(jsonObject, AddressResultBean.class);
                if (ResultUtil.isSuccess(result) && null != result && null != result.getResult()) {
                    listRelative.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    AddressResultBean addressResultBean = result.getResult();
                    if (addressResultBean != null) {
                        addresses = new ArrayList<>();
                        if(addressResultBean.getList()!=null&&addressResultBean.getList().size()>0) {
                            for (int i = 0; i < addressResultBean.getList().size(); i++) {
                                Address address = new Address();
                                address.setUserimg(addressResultBean.getList().get(i).getUserimg());
                                address.setUserId(addressResultBean.getList().get(i).getUserid());
                                address.setName(addressResultBean.getList().get(i).getUsername());
                                String firstSpell = ChineseToEnglish.getFirstSpell(
                                    addressResultBean.getList().get(i).getUsername());
                                String substring = firstSpell.substring(0, 1).toUpperCase();
                                if (substring.matches("[A-Z]")) {
                                    address.setLetter(substring);
                                } else {
                                    address.setLetter("#");
                                }
                                addresses.add(address);
                            }
                        }else {
                            if(addressResultBean.getStardata() ==null){
                                listRelative.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                                noData.setBackground(ChooseFriendActivity.this.
                                    getResources().getDrawable(R.drawable.no_data));
                            }
                        }
                        //排序
                        Collections.sort(addresses, new CompareSort());
                        if (addressResultBean.getStardata() != null
                            && addressResultBean.getStardata().size() > 0) {
                            for (int j = 0; j < addressResultBean.getStardata().size(); j++) {
                                Address address2 = new Address();
                                address2.setUserimg(
                                    addressResultBean.getStardata().get(j).getUserimg());
                                address2
                                    .setUserId(addressResultBean.getStardata().get(j).getUserid());
                                address2
                                    .setName(addressResultBean.getStardata().get(j).getUsername());
                                address2.setLetter("☆");
                                addresses.add(j, address2);
                            }
                        }
                        //设置数据
                        friendAdapter = new FriendAdapter(ChooseFriendActivity.this, chooseList,
                            addresses);
                        friendList.setAdapter(friendAdapter);
                        //设置回调
                        friendSlide.setOnLetterSelectListen(new MyLetterSelectListener());
                        friendSearch.setDatas(addresses);
                        friendSearch.setAdapter(friendAdapter);
                        friendSearch
                            .setSearchDataListener(new MySearchView.SearchDatas<Address>() {
                                //参数一：全部数据，参数二：筛选后的数据，参数三：输入的内容
                                @Override
                                public List filterDatas(List<Address> datas,
                                    List<Address> filterdatas, String inputstr) {
                                    for (int i = 0; i < datas.size(); i++) {
                                        //筛选条件
                                        if ((datas.get(i).getName()).contains(inputstr)) {
                                            filterdatas.add(datas.get(i));
                                        }
                                    }
                                    return filterdatas;
                                }
                            });
                    }
                } else {
                    listRelative.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    noData.setBackground(ChooseFriendActivity.this.
                        getResources().getDrawable(R.drawable.no_data));
                }
            } catch (Exception e) {
                ToastUtil.showShort(getResources().getString(R.string.get_address_list_fail),
                    getApplicationContext());
                listRelative.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                noData.setBackground(ChooseFriendActivity.this.
                    getResources().getDrawable(R.drawable.loading_fail));
            }
        }

        @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
        @Override
        public void onFail(String errorMsf) {
            ToastUtil.showShort(errorMsf,
                getApplicationContext());
            listRelative.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            noData.setBackground(ChooseFriendActivity.this.
                getResources().getDrawable(R.drawable.loading_fail));
        }
    };

    private void requestData() {
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(this, Config.USER_INFO, Config.TOKEN, ""));
        list.add(paramsToken);

        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), this, list,
            Urls.GET_ADDRESS_LIST, AddressListener, RequestNet.POST);
    }


  private RequestListener createGroupListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }
    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<CreateGroupBean> result = GsonUtil.processJson(jsonObject, CreateGroupBean.class);
        if (ResultUtil.isSuccess(result)) {
            ToastUtil.showShort(getResources().getString(R.string.create_group_success),
                getApplicationContext());
            ChooseFriendActivity.this.finish();
            farwardConversation(result.getResult().getGroupid()
                ,result.getResult().getGroupname());
        } else {
            Toast.makeText(ChooseFriendActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                getString(R.string.create_group_fail)),Toast.LENGTH_LONG).show();
        }
      } catch (Exception e) {
          Toast.makeText(ChooseFriendActivity.this, getString(R.string.create_group_fail)
              ,Toast.LENGTH_LONG).show();
      }
    }
    @Override
    public void onFail(String errorMsf) {
      Toast.makeText(ChooseFriendActivity.this, DebugUtils.convert(errorMsf,
          getString(R.string.create_group_fail)),Toast.LENGTH_LONG).show();
    }
  };
    private void createGroup(String useridlist){
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsUserIdList = new RequestParams("useridlist", useridlist);
      list.add(paramsToken);
      list.add(paramsUserIdList);
      RequestNet requestNet = new RequestNet((MyApplication)getApplication(), ChooseFriendActivity.this, list,
          Urls.CREATE_GROUP, createGroupListener, RequestNet.POST);
    }

  private RequestListener addGroupListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }
    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        if (ResultUtil.isSuccess(result)) {
          ToastUtil.showShort(getResources().getString(R.string.add_group_success),
              getApplicationContext());
            ChooseFriendActivity.this.finish();
        } else {
          Toast.makeText(ChooseFriendActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
              getString(R.string.add_group_fail)),Toast.LENGTH_LONG).show();
        }
      } catch (Exception e) {
        Toast.makeText(ChooseFriendActivity.this, getString(R.string.add_group_fail)
            ,Toast.LENGTH_LONG).show();
      }
    }
    @Override
    public void onFail(String errorMsf) {
      Toast.makeText(ChooseFriendActivity.this, DebugUtils.convert(errorMsf,
          getString(R.string.add_group_fail)),Toast.LENGTH_LONG).show();
    }
  };

    private void addGroup(String useridlist){
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsGroupId = new RequestParams("groupid", groupId);
      RequestParams paramsUserIdList = new RequestParams("useridlist", useridlist);
      list.add(paramsToken);
      list.add(paramsGroupId);
      list.add(paramsUserIdList);
      RequestNet requestNet = new RequestNet((MyApplication)getApplication(), ChooseFriendActivity.this, list,
          Urls.ADD_GROUP, addGroupListener, RequestNet.POST);
    }

    private void farwardConversation(String groupId,String groupName){
        if(RongIM.getInstance()!=null){
            RongIM.getInstance().startGroupChat(this,groupId,groupName);
        }
    }

    private RequestListener recommendFriendListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
                if (ResultUtil.isSuccess(result)) {
                    ToastUtil.showShort(getResources().getString(R.string.recommend_friend_success),
                        getApplicationContext());
                    ChooseFriendActivity.this.finish();
                } else {
                    Toast.makeText(ChooseFriendActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                        getString(R.string.recommend_friend_fail)),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(ChooseFriendActivity.this, getString(R.string.recommend_friend_fail)
                    ,Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onFail(String errorMsf) {
            Toast.makeText(ChooseFriendActivity.this, DebugUtils.convert(errorMsf,
                getString(R.string.recommend_friend_fail)),Toast.LENGTH_LONG).show();
        }
    };

    private void recommendFriend(String memberStr){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsFriendId = new RequestParams("berecommenduserid", memberList.get(0));
        RequestParams paramsUserIdList = new RequestParams("recommenduserid", memberStr);
        list.add(paramsToken);
        list.add(paramsFriendId);
        list.add(paramsUserIdList);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), ChooseFriendActivity.this, list,
            Urls.RECOMMEND_FRIEND, recommendFriendListener, RequestNet.POST);

    }



}

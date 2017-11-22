package com.example.administrator.lubanone.adapter.message;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.message.AlbumActivity;
import com.example.administrator.lubanone.activity.message.ChooseFriendActivity;
import com.example.administrator.lubanone.activity.message.GroupMemberActivity;
import com.example.administrator.lubanone.activity.message.MemberInfoActivity;
import com.example.administrator.lubanone.bean.message.GroupMemberInfoBean;
import com.example.administrator.lubanone.bean.message.User;
import com.example.administrator.lubanone.utils.GlideRoundTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\7\27 0027.
 */

public class MyGridViewAdapter extends BaseAdapter {

  private Context mContext;
  private List<GroupMemberInfoBean> mList;
  private String groupMaster;
  private String groupId;
  private List<String> memberList;


  public MyGridViewAdapter(Context context,List<GroupMemberInfoBean> list,String groupMaster,String groupId) {
    this.mContext = context;
    this.mList =  list;
    this.groupMaster = groupMaster;
    this.groupId = groupId;
    setMemberList();
  }

  private void setMemberList(){
    if(mList!=null&&mList.size()>0){
      memberList = new ArrayList<>();
      for(int i=0;i<mList.size();i++){
        memberList.add(i,mList.get(i).getUserid());
      }
    }
  }

  @Override
  public int getCount() {
    if(groupMaster!=null&&groupMaster.length()>0){
      if(groupMaster.equals("0")){
        //非群主
        if(mList.size()>49){
          return 50;
        }else {
          return mList.size()+1;
        }
      }else if(groupMaster.equals("1")){
        //群主
        if(mList.size()>48){
          return 50;
        }else {
          return mList.size()+2;
        }
      }else {
        if(mList.size()>49){
          return 50;
        }else {
          return mList.size()+1;
        }
      }
    }else {
      if(mList.size()>49){
        return 50;
      }else {
        return mList.size()+1;
      }
    }
  }

  @Override
  public Object getItem(int position) {
    return mList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {

    ViewHolder viewHolder = null;
    if (convertView == null) {
      viewHolder = new ViewHolder();
      convertView = LayoutInflater.from(mContext).inflate(R.layout.member_grid_view_item, null);
      viewHolder.img = (ImageView) convertView.findViewById(R.id.member_grid_view_item_img);
      viewHolder.name = (TextView) convertView.findViewById(R.id.member_grid_view_item_name);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    if(groupMaster!=null&&groupMaster.length()>0){
      if(groupMaster.equals("0")){
        //非群主
        if(mList.size()>49){
          if (position < 50) {
            Glide.with(mContext)
                .load(mList.get(position).getUserimg())
                .transform(new GlideRoundTransform(mContext, 4))
                .placeholder(R.mipmap.b)
                .error(R.mipmap.b)
                .diskCacheStrategy(DiskCacheStrategy.ALL).
                into(viewHolder.img);
            viewHolder.name.setText(mList.get(position).getUsername());
          }else if(position==50){
            Glide.with(mContext).load(R.drawable.icon_add).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);
            viewHolder.name.setText("");
          }
        }else {
          if (position < mList.size()) {
            Glide.with(mContext)
                .load(mList.get(position).getUserimg())
                .transform(new GlideRoundTransform(mContext, 4))
                .placeholder(R.mipmap.b)
                .error(R.mipmap.b)
                .diskCacheStrategy(DiskCacheStrategy.ALL).
                into(viewHolder.img);
            viewHolder.name.setText(mList.get(position).getUsername());
          }else if(position == mList.size()){
            Glide.with(mContext).load(R.drawable.icon_add).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);
            viewHolder.name.setText("");
          }else if(position > mList.size()){
            Glide.with(mContext).load(R.drawable.icon_delect).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);
            viewHolder.name.setText("");
          }
        }
      }else if(groupMaster.equals("1")){
        //群主
        if(mList.size()>48){
          if (position < 49) {
            Glide.with(mContext)
                .load(mList.get(position).getUserimg())
                .transform(new GlideRoundTransform(mContext, 4))
                .placeholder(R.mipmap.b)
                .error(R.mipmap.b)
                .diskCacheStrategy(DiskCacheStrategy.ALL).
                into(viewHolder.img);
            viewHolder.name.setText(mList.get(position).getUsername());
          }else if(position==49){
            Glide.with(mContext).load(R.drawable.icon_add).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);
            viewHolder.name.setText("");
          }else {
            Glide.with(mContext).load(R.drawable.icon_delect).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);
            viewHolder.name.setText("");
          }
        }else {
          if (position < mList.size()) {
            Glide.with(mContext)
                .load(mList.get(position).getUserimg())
                .transform(new GlideRoundTransform(mContext, 4))
                .placeholder(R.mipmap.b)
                .error(R.mipmap.b)
                .diskCacheStrategy(DiskCacheStrategy.ALL).
                into(viewHolder.img);
            viewHolder.name.setText(mList.get(position).getUsername());
          }else if(position == mList.size()){
            Glide.with(mContext).load(R.drawable.icon_add).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);
            viewHolder.name.setText("");
          }else if(position > mList.size()){
            Glide.with(mContext).load(R.drawable.icon_delect).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);
            viewHolder.name.setText("");
          }
        }

      }
    }
   /* if (position < mList.size()) {
      Glide.with(mContext)
          .load(mList.get(position).getUserimg())
          .placeholder(R.mipmap.b)
          .error(R.mipmap.b)
          .diskCacheStrategy(DiskCacheStrategy.ALL).
          into(viewHolder.img);
      viewHolder.name.setText(mList.get(position).getUsername());
    }else if(position == mList.size()){
      Glide.with(mContext).load(R.drawable.item_add).asBitmap()
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .into(viewHolder.img);
      viewHolder.name.setText("");
    }else if(position > mList.size()){
      Glide.with(mContext).load(R.drawable.em_chat_file_pressed).asBitmap()
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .into(viewHolder.img);
      viewHolder.name.setText("");
    }*/

    viewHolder.img.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        farwordActivity(position);
      }
    });
    convertView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        farwordActivity(position);
      }
    });

    return convertView;
  }

  class ViewHolder {
    ImageView img;
    TextView name;
  }

  private void farwordActivity(int position){
    /*if(position == mList.size()){
      //添加好友到群
      Intent intent = new Intent();
      intent.putExtra("type","add");
      intent.putExtra("groupId",groupId);
      intent.putStringArrayListExtra("memberList", (ArrayList<String>) memberList);
      intent.setClass(mContext, ChooseFriendActivity.class);
      mContext.startActivity(intent);
    }
    if(position == mList.size()+1){
      //从群减少好友
      Intent intent = new Intent();
      intent.putExtra("type","delete");
      intent.putExtra("groupId",groupId);
      intent.setClass(mContext, GroupMemberActivity.class);
      mContext.startActivity(intent);
    }*/

    if(groupMaster!=null&&groupMaster.length()>0){
      if(groupMaster.equals("0")){
        //非群主
        if(mList.size()>49){
          if(position == 49){
            //添加好友到群
            Intent intent = new Intent();
            intent.putExtra("type","add");
            intent.putExtra("groupId",groupId);
            intent.putStringArrayListExtra("memberList", (ArrayList<String>) memberList);
            intent.setClass(mContext, ChooseFriendActivity.class);
            mContext.startActivity(intent);
          }else {
            Intent intent = new Intent();
            intent.putExtra("userId",mList.get(position).getUserid());
            intent.setClass(mContext,MemberInfoActivity.class);
            mContext.startActivity(intent);
          }
        }else {
          if(position == mList.size()) {
            //添加好友到群
            Intent intent = new Intent();
            intent.putExtra("type", "add");
            intent.putExtra("groupId", groupId);
            intent.putStringArrayListExtra("memberList", (ArrayList<String>) memberList);
            intent.setClass(mContext, ChooseFriendActivity.class);
            mContext.startActivity(intent);
          }else {
              Intent intent = new Intent();
              intent.putExtra("userId",mList.get(position).getUserid());
              intent.setClass(mContext,MemberInfoActivity.class);
              mContext.startActivity(intent);
            }
        }
      }else if(groupMaster.equals("1")){
        //群主
        if(mList.size()>48){
          if(position == 48){
            //添加好友到群
            Intent intent = new Intent();
            intent.putExtra("type","add");
            intent.putExtra("groupId",groupId);
            intent.putStringArrayListExtra("memberList", (ArrayList<String>) memberList);
            intent.setClass(mContext, ChooseFriendActivity.class);
            mContext.startActivity(intent);
          } else if(position == 49){
            //从群减少好友
            Intent intent = new Intent();
            intent.putExtra("type","delete");
            intent.putExtra("groupId",groupId);
            intent.setClass(mContext, GroupMemberActivity.class);
            mContext.startActivity(intent);
          }else {
            Intent intent = new Intent();
            intent.putExtra("userId",mList.get(position).getUserid());
            intent.setClass(mContext,MemberInfoActivity.class);
            mContext.startActivity(intent);
          }
        }else {
          if(position == mList.size()){
            //添加好友到群
            Intent intent = new Intent();
            intent.putExtra("type","add");
            intent.putExtra("groupId",groupId);
            intent.putStringArrayListExtra("memberList", (ArrayList<String>) memberList);
            intent.setClass(mContext, ChooseFriendActivity.class);
            mContext.startActivity(intent);
          } else if(position == mList.size()+1){
            //从群减少好友
            Intent intent = new Intent();
            intent.putExtra("type","delete");
            intent.putExtra("groupId",groupId);
            intent.setClass(mContext, GroupMemberActivity.class);
            mContext.startActivity(intent);
          }else {
            Intent intent = new Intent();
            intent.putExtra("userId",mList.get(position).getUserid());
            intent.setClass(mContext,MemberInfoActivity.class);
            mContext.startActivity(intent);
          }
        }
      }
    }
  }
}

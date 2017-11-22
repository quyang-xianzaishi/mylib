package com.example.administrator.lubanone.adapter.message;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.message.FriendInfoActivity;
import com.example.administrator.lubanone.activity.message.GroupChatActivity;
import com.example.administrator.lubanone.activity.message.NewFriendActivity;
import com.example.administrator.lubanone.bean.message.Address;
import com.example.administrator.lubanone.utils.ChineseToEnglish;

import com.example.administrator.lubanone.utils.GlideRoundTransform;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/1/8.
 */
public class AddressAdapter extends BaseAdapter {
    private Context mContext;
    private List<Address> users;

    public AddressAdapter(Context context,List<Address> list) {
        this.mContext = context;
        //users = new ArrayList<>();
        this.users =  list;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_address_item, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.tvItem = (LinearLayout) convertView.findViewById(R.id.item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(users.get(position).getName().equals(
            mContext.getResources().getString(R.string.group_chat_list))){
            Glide.with(mContext).load(R.drawable.icon_qunliao).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);
        }else if(users.get(position).getName().equals(
            mContext.getResources().getString(R.string.new_friend_title))){
            Glide.with(mContext).load(R.drawable.icon_newfriend).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);
        }else {
            Glide.with(mContext)
                .load(users.get(position).getUserimg())
                .transform(new GlideRoundTransform(mContext, 4))
                .diskCacheStrategy(DiskCacheStrategy.ALL).
                into(viewHolder.img);
        }
        viewHolder.tvName.setText(users.get(position).getName());
        viewHolder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = users.get(position).getName();
                if(str!=null&&!str.equals("")){
                    if(str.equals(mContext.getResources().getString(R.string.group_chat_list))){
                        //点击进入群组页面
                        Intent intent = new Intent();
                        intent.setClass(mContext,GroupChatActivity.class);
                        mContext.startActivity(intent);
                    }else if(str.equals(mContext.getResources().getString(R.string.new_friend_title))){
                        //点击进入好友申请列表页面
                        Intent intent = new Intent();
                        intent.setClass(mContext,NewFriendActivity.class);
                        mContext.startActivity(intent);
                    }else{
                        //Toast.makeText(mContext,users.get(position).getName(), Toast.LENGTH_SHORT).show();
                        //点击进入会员信息页面
                        Intent intent = new Intent();
                        intent.putExtra("targetId",users.get(position).getUserId());
                        intent.putExtra("userName",users.get(position).getName());
                        intent.setClass(mContext,FriendInfoActivity.class);
                        mContext.startActivity(intent);
                    }
                }
            }
        });
        //当前的item的title与上一个item的title不同的时候回显示title(A,B,C......)
        if((position == getFirstLetterPosition(position) && !users.get(position).getLetter().equals("@"))||
            users.get(position).getLetter().equals("☆")){
          if(users.get(position).getLetter().equals("☆")){
              if(getFirstLetterPosition(users.get(position).getLetter().toString())==position){
                  viewHolder.tvTitle.setVisibility(View.VISIBLE);
                  viewHolder.tvTitle.setText(mContext.getString(R.string.star_sign_friend));
              }else {
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
        TextView tvName;
        TextView tvTitle;
        ImageView img;
        LinearLayout tvItem;
    }

}

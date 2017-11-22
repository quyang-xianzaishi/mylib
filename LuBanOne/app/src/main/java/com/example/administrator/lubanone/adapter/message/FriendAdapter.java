package com.example.administrator.lubanone.adapter.message;

import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.message.Address;
import com.example.administrator.lubanone.utils.ChineseToEnglish;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class FriendAdapter extends BaseAdapter {
    private Context mContext;
    private List<Address> users;
    private List chooseFriends;
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
        return 0;
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
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tvItem = (LinearLayout) convertView.findViewById(R.id.item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(users.get(position).getName());
        viewHolder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = users.get(position).getName();
                if(str!=null&&!str.equals("")){
                        Toast.makeText(mContext,users.get(position).getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(chooseFriends!=null&&chooseFriends.size()>0){
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
        }
        viewHolder.tvState.setTag(position);
        viewHolder.tvState.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(chooseFriends!=null&&chooseFriends.size()>0){
                    for(int i = 0;i<chooseFriends.size();i++){
                        if(users.get(position).getUserId().equals(chooseFriends.get(i))){
                            ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.i_agree_back_2x));
                            chooseFriends.remove(users.get(position).getUserId());
                            break;
                        }
                        if(i==chooseFriends.size()-1){
                            ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.i_agree_pre_x));
                            chooseFriends.add(users.get(position).getUserId());
                            break;
                        }
                    }
                }else {
                    ((TextView)v).setBackground(mContext.getResources().getDrawable(R.mipmap.i_agree_pre_x));
                    chooseFriends.add(users.get(position).getUserId());
                }
            }
        });
        //当前的item的title与上一个item的title不同的时候回显示title(A,B,C......)
        if(position == getFirstLetterPosition(position) && !users.get(position).getLetter().equals("@")){
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText(users.get(position).getLetter().toUpperCase());
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
        }
        return -1;
    }

    class ViewHolder {
        TextView tvState;
        TextView tvName;
        TextView tvTitle;
        LinearLayout tvItem;
    }

}

package com.example.administrator.lubanone.adapter.message;

import android.content.Context;
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
import com.example.administrator.lubanone.bean.message.Group;

import com.example.administrator.lubanone.utils.GlideRoundTransform;
import io.rong.imkit.RongIM;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\6\29 0029.
 */

public class GroupAdapter extends BaseAdapter {

    private Context mContext;
    private List<Group> mList;

    public GroupAdapter(Context context,List list) {
        this.mContext = context;
        //mList = new ArrayList();
        this.mList = list;
    }

    public void setData(List<Group> data){
        this.mList.clear();
        this.mList.addAll(data);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_group_chat_item, null);
            viewHolder.groupItemImg = (ImageView) convertView.findViewById(R.id.group_item_img);
            viewHolder.groupItemName = (TextView) convertView.findViewById(R.id.group_item_name);
            viewHolder.groupItem = (LinearLayout) convertView.findViewById(R.id.group_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext)
            .load(mList.get(position).getGroupimg())
            .transform(new GlideRoundTransform(mContext, 4))
            .placeholder(R.mipmap.b)
            .error(R.mipmap.b)
            .diskCacheStrategy(DiskCacheStrategy.ALL).
            into(viewHolder.groupItemImg);
        viewHolder.groupItemName.setText(mList.get(position).getGroupname());
        viewHolder.groupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mList.get(position).getGroupname();
                if(str!=null&&!str.equals("")){
                    //进入群聊页面
                    RongIM.getInstance().startGroupChat(mContext, mList.get(position).getGroupid(),
                        mList.get(position).getGroupname());
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView  groupItemImg;
        TextView groupItemName;
        LinearLayout groupItem;
    }
}

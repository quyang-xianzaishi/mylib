package com.example.administrator.lubanone.adapter.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.message.GroupMemberActivity;
import com.example.administrator.lubanone.activity.message.UploadComplainEvidenceActivity;
import com.example.administrator.lubanone.bean.message.ComplainReasonBean;
import com.example.administrator.lubanone.bean.message.Group;
import io.rong.imkit.RongIM;
import java.util.List;

/**
 * Created by Administrator on 2017\6\29 0029.
 */

public class ComplainReasonAdapter extends BaseAdapter {

    private Context mContext;
    private List<ComplainReasonBean> mList;
    private String targetId;
    private String type;
    private String complainId;

    public ComplainReasonAdapter(Context context,List list) {
        this.mContext = context;
        this.mList = list;
    }
    public ComplainReasonAdapter(Context context,List list,String targetId,String complainId,String type) {
        this.mContext = context;
        this.mList = list;
        this.targetId = targetId;
        this.type = type;
        this.complainId = complainId;
    }

    public void setData(List<ComplainReasonBean> data){
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_complain_reason, null);
            viewHolder.reason = (TextView) convertView.findViewById(R.id.complain_reason);
            viewHolder.reasonItem = (LinearLayout) convertView.findViewById(R.id.complain_reason_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.reason.setText(mList.get(position).getReason());
        viewHolder.reasonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //投诉
                if(type!=null&&type.length()>0){
                    if(type.equals("friend")){
                        //针对个人
                        Intent intent = new Intent();
                        intent.putExtra("complainId",targetId);
                        intent.putExtra("reasonId",mList.get(position).getReasonid());
                        intent.setClass(mContext,UploadComplainEvidenceActivity.class);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();
                    }else if (type.equals("group")){
                        //针对群组
                        Intent intent = new Intent();
                        intent.putExtra("groupId",targetId);
                        intent.putExtra("complainId",complainId);
                        intent.putExtra("type","complain");
                        intent.putExtra("reasonId",mList.get(position).getReasonid());
                        intent.setClass(mContext, UploadComplainEvidenceActivity.class);
                        //intent.setClass(mContext, GroupMemberActivity.class);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();
                    }
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView reason;
        LinearLayout reasonItem;
    }
}

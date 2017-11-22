package com.example.qlibrary.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.qlibrary.adapter.listview.ViewHolder;

import java.util.List;

/**
 * listview的adapter
 * Created by Administrator on 2017/6/6.
 */

public abstract class CustomAdapter<DatrType> extends BaseAdapter {

    public Context mContext;//activity
    public List<DatrType> mList;

    public CustomAdapter(Context context, List<DatrType> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public DatrType getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, position, parent, getListViewLayoutId(), convertView);
        DatrType item = getItem(position);
        setData2Views(viewHolder, item, position);
        return viewHolder.getConvertView();
    }


    /**
     * listview的itemview的布局id
     *
     * @return
     */
    public abstract int getListViewLayoutId();

    /**
     * //        setData2Views(viewHolder,item);
     * //        TextView view = (TextView) viewHolder.getView(2);
     * //        view.setText("");
     *
     * @param viewHolder
     * @param item
     * @param position
     */
    public abstract void setData2Views(ViewHolder viewHolder, DatrType item, int position);


}

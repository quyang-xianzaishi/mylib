package com.example.qlibrary.adapter.listview;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * listview的viewholder
 * Created by Administrator on 2017/6/6.
 */

public class ViewHolder {

    private Context mContext;

    private int mPositoin;

    private ViewGroup mParent;

    private View mConvertView;

    private View mInflate;

    private SparseArray<View> mView;

    public ViewHolder(Context context, int position, ViewGroup parent, int layoutId) {
        this.mContext = context;
        this.mPositoin = position;
        this.mParent = parent;

        this.mView = new SparseArray<View>();
        mConvertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static ViewHolder getViewHolder(Context context, int position, ViewGroup parent, int layoutId, View convertView) {
        if (null == convertView) {
            return new ViewHolder(context, position, parent, layoutId);
        } else {
            return (ViewHolder) convertView.getTag();
        }
    }

    public <T extends View> T getView(int viewId) {
        View view = mView.get(viewId);
        if (null == view) {
            view = mConvertView.findViewById(viewId);
            mView.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }


}

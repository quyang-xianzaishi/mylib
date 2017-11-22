package com.jingchen.pulltorefresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class WrapRecyclerView extends RecyclerView {

    private ArrayList<View> mHeaderViews = new ArrayList<View>() ;

    private ArrayList<View> mFootViews = new ArrayList<View>() ;

    private Adapter mAdapter ;

    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addHeaderView(View view){
        mHeaderViews.clear();
        mHeaderViews.add(view);
        if (mAdapter != null){
            if (!(mAdapter instanceof RecyclerWrapAdapterPull)){
                mAdapter = new RecyclerWrapAdapterPull(mHeaderViews,mFootViews,mAdapter) ;
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void addFootView(View view){
        mFootViews.clear();
        mFootViews.add(view);
        if (mAdapter != null){
            if (!(mAdapter instanceof RecyclerWrapAdapterPull)){
                mAdapter = new RecyclerWrapAdapterPull(mHeaderViews,mFootViews,mAdapter) ;
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {

        if (mHeaderViews.isEmpty()&&mFootViews.isEmpty()){
            super.setAdapter(adapter);
        }else {
            adapter = new RecyclerWrapAdapterPull(mHeaderViews,mFootViews,adapter) ;
            super.setAdapter(adapter);
        }
        mAdapter = adapter ;
    }

}
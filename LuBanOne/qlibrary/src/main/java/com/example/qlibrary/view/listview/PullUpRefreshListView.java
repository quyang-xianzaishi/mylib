package com.example.qlibrary.view.listview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.qlibrary.R;
import com.example.qlibrary.utils.ToastUtil;

/**
 * 上拉刷新
 * <p>
 * Created by Administrator on 2017/6/6.
 */
public class PullUpRefreshListView extends ListView implements AbsListView.OnScrollListener {


    private View mFooter;

    private View mHeader;

    private LayoutInflater mInflater;

    private LayoutInflater mHeaderInflater;

    private View mLoadLayout;

    private int lastVisibleItem;

    private int totalItemCount;

    private boolean isLoading;

    public PullUpRefreshListView(Context context) {
        super(context);
        initFooter(context);
    }

    public PullUpRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFooter(context);
    }

    public PullUpRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFooter(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullUpRefreshListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFooter(context);
    }

    public void initFooter(Context context) {
        mInflater = LayoutInflater.from(context);
        mFooter = mInflater.inflate(R.layout.item_listview, null);
        mLoadLayout = mFooter.findViewById(R.id.load_layout);
        mLoadLayout.setVisibility(GONE);
        this.addFooterView(mFooter);
        this.setOnScrollListener(this);
    }

    public void setPaddingTop(int paddingTop) {
        setPadding(mHeader.getPaddingLeft(), paddingTop, mHeader.getPaddingRight(), mHeader.getPaddingBottom());
        mHeader.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && totalItemCount == lastVisibleItem) {
            if (!isLoading) {
                isLoading = true;
                mLoadLayout.setVisibility(VISIBLE);
                //加载更多数据
                mListener.isLoading();
            }
        }

    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.totalItemCount = totalItemCount;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
    }


    public interface OnListViewScrollListener {
        void isLoading();
    }

    private OnListViewScrollListener mListener;

    public void setOnListViewScrollListener(OnListViewScrollListener listener) {
        this.mListener = listener;
    }


    public void loadCompleted() {
        if (isLoading) {
            isLoading = false;
            mLoadLayout.setVisibility(GONE);
        }
    }

    public void setLoading() {
        mLoadLayout.setVisibility(VISIBLE);
    }

}

package com.example.qlibrary.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.qlibrary.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新数据
 */
public class PullDownfreshListView extends ListView implements OnScrollListener {

    public static final int STATE_PULL_TO_REFRESH = 0;
    public static final int STATE_RELEASE_TO_REFRESH = 1;
    public static final int STATE_REFRESHING = 2;

    private int mCurrentState = STATE_PULL_TO_REFRESH;

    private int headerHeight;
    private float startY;
    private View headerView;
    private ProgressBar pb;
    private TextView tvStatus;
    private RotateAnimation animationUp;
    private RotateAnimation animationDown;
    private View mLoadLayout;

    public PullDownfreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHeaderView();
    }

    public PullDownfreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public PullDownfreshListView(Context context) {
        super(context);
        initHeaderView();
    }

    private void initHeaderView() {
        headerView = View.inflate(getContext(),
                R.layout.header_item_listview, null);
        pb = (ProgressBar) headerView.findViewById(R.id.head_pb);
        tvStatus = (TextView) headerView.findViewById(R.id.head_tip);

        headerView.measure(0, 0);// 手动测量，将测量的工作交给系统来执行，我们不参与任何限制的意见

        headerHeight = headerView.getMeasuredHeight();

        headerView.setPadding(0, -headerHeight, 0, 0);

        this.addHeaderView(headerView);
        initAnimation();

    }


    private void initAnimation() {
        animationUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationUp.setDuration(200);
        animationUp.setFillAfter(true);

        animationDown = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animationDown.setDuration(200);
        animationDown.setFillAfter(true);

    }

    // 在down事件一来RefreshListView的时候，记录起始坐标
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            startY = ev.getY();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                if (startY == 0) {
                    startY = ev.getY();// down事件被viewPager消费了，造成startY的值没有进行初始化，
                    // 此时可以在dispatchTouchEvent中响应down事件来获取点击的坐标，或者在onTouchEvent中的move事件中判断startY的值，再进行赋值
                }
                // 如果此时是正在刷新的状态，直接跳出switch
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }

                float moveY = ev.getY();
                float dy = moveY - startY;

                int firstVisiblePosition = getFirstVisiblePosition();

                if (dy > 0 && firstVisiblePosition == 0) {
                    // 向下滑动
                    int paddingTop = (int) (dy - headerHeight);
                    headerView.setPadding(0, paddingTop, 0, 0);
                    int oldState = mCurrentState;
                    if (paddingTop < 0) {
                        mCurrentState = STATE_PULL_TO_REFRESH;
                         refreshState();
                    } else {
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                         refreshState();
                    }
                    // 在状态发生改变的时候才需要更新ui界面
                    if (oldState != mCurrentState) {
                        refreshState();
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:

                if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    // 隐藏头布局
                    headerView.setPadding(0, -headerHeight, 0, 0);
                } else if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    // 显示头布局，让头布局显示正常的高度
                    headerView.setPadding(0, 0, 0, 0);
                    // 变化状态
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    notifyOnRefresh();
                }

                break;

        }
        return super.onTouchEvent(ev);
    }

    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvStatus.setText("下拉刷新");
                pb.setVisibility(View.INVISIBLE);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvStatus.setText("松开刷新");
                pb.setVisibility(View.INVISIBLE);
                break;
            case STATE_REFRESHING:
                tvStatus.setText("正在刷新");
                pb.setVisibility(View.VISIBLE);
//			ivArrow.clearAnimation();// 需要移除动画，才能进行隐藏
//			ivArrow.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }
    }

    // 定义观察者
    public interface OnRefreshListener {
        public void onRefresh();
    }

    // 保存观察者
    private OnRefreshListener mListener;
    private View footerView;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    // 通知观察者
    private void notifyOnRefresh() {
        if (mListener != null) {
            mListener.onRefresh();
        }
    }

    public void onRefreshComplete(boolean success) {
        // 1、重置状态
        mCurrentState = STATE_PULL_TO_REFRESH;
        refreshState();// 在状态发生改变的时候，记得界面的刷新
        // 2、隐藏头布局
        headerView.setPadding(0, -headerHeight, 0, 0);

        if (success) {
            // 设置时间的显示
            setCurrentTime();
        }

    }

    private void setCurrentTime() {
        // 对时间进行格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentTime = sdf.format(new Date());

    }

    // 当滚动状态发生改变的时候
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }


    private int scrollState;

    // 当整个滚动的过程中都会进行的回调
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }
}

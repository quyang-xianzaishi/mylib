package com.example.administrator.lubanone.activity.message;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.YWActivity;
import com.example.administrator.lubanone.adapter.message.CircleAdapter;
import com.example.administrator.lubanone.bean.message.CircleItem;
import com.example.administrator.lubanone.bean.message.CommentConfig;
import com.example.administrator.lubanone.bean.message.CommentItem;
import com.example.administrator.lubanone.bean.message.FavortItem;
import com.example.administrator.lubanone.mvp.contract.CircleContract;
import com.example.administrator.lubanone.mvp.presenter.CirclePresenter;
import com.example.administrator.lubanone.utils.CommonUtils;
import com.example.administrator.lubanone.widgets.CommentListView;
import com.example.administrator.lubanone.widgets.DivItemDecoration;
import com.example.administrator.lubanone.widgets.dialog.UpLoadDialog;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import java.util.List;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class MomentsActivity extends YWActivity implements View.OnClickListener, CircleContract.View,
    EasyPermissions.PermissionCallbacks{

  protected static final String TAG = MomentsActivity.class.getSimpleName();
  private TextView back;
  private TextView shoot;

  private CircleAdapter circleAdapter;
  private CirclePresenter presenter;
  private UpLoadDialog uploadDialog;
  private CommentConfig commentConfig;
  private SuperRecyclerView recyclerView;
  private RelativeLayout bodyLayout;
  private LinearLayoutManager layoutManager;
  private LinearLayout title;
  private LinearLayout edittextbody;
  private EditText editText;
  private ImageView sendIv;
  private SwipeRefreshLayout.OnRefreshListener refreshListener;

  private int screenHeight;
  private int editTextBodyHeight;
  private int currentKeyboardH;
  private int selectCircleItemH;
  private int selectCommentItemOffset;

  private final static int TYPE_PULLREFRESH = 1;
  private final static int TYPE_UPLOADREFRESH = 2;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_moments);

    presenter = new CirclePresenter(MomentsActivity.this);

    initView();
    initUploadDialog();
    initPermission();

    //实现自动下拉刷新功能
    recyclerView.getSwipeToRefresh().post(new Runnable() {
      @Override
      public void run() {
        recyclerView.setRefreshing(true);//执行下拉刷新的动画
        refreshListener.onRefresh();//执行数据加载操作
      }
    });
  }

  private void initView(){
    back = (TextView) this.findViewById(R.id.activity_back);
    shoot = (TextView) this.findViewById(R.id.shoot_img);
    title =(LinearLayout) this.findViewById(R.id.activity_moments_title);

    back.setOnClickListener(this);
    shoot.setOnClickListener(this);

    recyclerView = (SuperRecyclerView) findViewById(R.id.recyclerView);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(new DivItemDecoration(2, true));
    recyclerView.getMoreProgressView()
        .getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

    recyclerView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (edittextbody.getVisibility() == View.VISIBLE) {
          updateEditTextBodyVisible(View.GONE, null);
          return true;
        }
        return false;
      }
    });

    refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            presenter.loadData(TYPE_PULLREFRESH);
          }
        }, 2000);
      }
    };

    recyclerView.setRefreshListener(refreshListener);

    recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
      }

      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
          Glide.with(MomentsActivity.this).resumeRequests();
        } else {
          Glide.with(MomentsActivity.this).pauseRequests();
        }

      }
    });

    circleAdapter = new CircleAdapter(this);
    circleAdapter.setCirclePresenter(presenter);
    recyclerView.setAdapter(circleAdapter);

    edittextbody = (LinearLayout) findViewById(R.id.editTextBodyLl);
    editText = (EditText) findViewById(R.id.circleEt);
    sendIv = (ImageView) findViewById(R.id.sendIv);
    sendIv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (presenter != null) {
          //发布评论
          String content = editText.getText().toString().trim();
          if (TextUtils.isEmpty(content)) {
            Toast.makeText(MomentsActivity.this, "评论内容不能为空...", Toast.LENGTH_SHORT).show();
            return;
          }
          presenter.addComment(content, commentConfig);
        }
        updateEditTextBodyVisible(View.GONE, null);
      }
    });

    setViewTreeObserver();
  }

  private void initPermission() {
    String[] perms = {Manifest.permission.CALL_PHONE
        , Manifest.permission.WRITE_EXTERNAL_STORAGE
        , Manifest.permission.READ_EXTERNAL_STORAGE};

    if (EasyPermissions.hasPermissions(this, perms)) {
      // Already have permission, do the thing
      // ...
    } else {
      // Do not have permissions, request them now
      EasyPermissions.requestPermissions(this, "因为功能需要，需要使用相关权限，请允许",
          100, perms);
    }
  }

  private void initUploadDialog() {
    uploadDialog = new UpLoadDialog(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.activity_back:
        MomentsActivity.this.finish();
        break;
      case R.id.shoot_img:
        //拍摄
        Intent intent = new Intent();
        intent.setClass(this,CameraActivity.class);
        this.startActivity(intent);
        break;
      default:
        break;
    }
  }


  @Override
  public void showLoading(String msg) {

  }

  @Override
  public void hideLoading() {

  }

  @Override
  public void showError(String errorMsg) {

  }

  @Override
  public void update2DeleteCircle(String circleId) {
    Toast.makeText(this, "我要删除这条动态啦。。。。。。。。", Toast.LENGTH_LONG).show();
    List<CircleItem> circleItems = circleAdapter.getDatas();
    for (int i = 0; i < circleItems.size(); i++) {
      if (circleId.equals(circleItems.get(i).getId())) {
        circleItems.remove(i);
        circleAdapter.notifyDataSetChanged();
        //circleAdapter.notifyItemRemoved(i+1);
        return;
      }
    }

  }

  @Override
  public void update2AddFavorite(int circlePosition, FavortItem addItem) {
    Toast.makeText(this, "我要点赞啦。。。。。。。。。。", Toast.LENGTH_LONG).show();
    if (addItem != null) {
      CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
      item.getFavorters().add(addItem);
      circleAdapter.notifyDataSetChanged();
      //circleAdapter.notifyItemChanged(circlePosition+1);
    }

  }

  @Override
  public void update2DeleteFavort(int circlePosition, String favortId) {
    Toast.makeText(this, "我要取消点赞啦。。。。。。。。。。。。。。。", Toast.LENGTH_LONG).show();
    CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
    List<FavortItem> items = item.getFavorters();
    for (int i = 0; i < items.size(); i++) {
      if (favortId.equals(items.get(i).getId())) {
        items.remove(i);
        circleAdapter.notifyDataSetChanged();
        //circleAdapter.notifyItemChanged(circlePosition+1);
        return;
      }
    }

  }

  @Override
  public void update2AddComment(int circlePosition, CommentItem addItem) {
    Toast.makeText(this, "我要评论啦。。。。。。。。", Toast.LENGTH_LONG).show();
    if (addItem != null) {
      CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
      item.getComments().add(addItem);
      circleAdapter.notifyDataSetChanged();
      //circleAdapter.notifyItemChanged(circlePosition+1);
    }
    //清空评论文本
    editText.setText("");

  }

  @Override
  public void update2DeleteComment(int circlePosition, String commentId) {
    Toast.makeText(this, "我要删除评论啦。。。。。。。。", Toast.LENGTH_LONG).show();
    CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
    List<CommentItem> items = item.getComments();
    for (int i = 0; i < items.size(); i++) {
      if (commentId.equals(items.get(i).getId())) {
        items.remove(i);
        circleAdapter.notifyDataSetChanged();
        //circleAdapter.notifyItemChanged(circlePosition+1);
        return;
      }
    }

  }

  @Override
  public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {

    this.commentConfig = commentConfig;
    edittextbody.setVisibility(visibility);

    measureCircleItemHighAndCommentItemOffset(commentConfig);

    if (View.VISIBLE == visibility) {
      editText.requestFocus();
      //弹出键盘
      CommonUtils.showSoftInput(editText.getContext(), editText);

    } else if (View.GONE == visibility) {
      //隐藏键盘
      CommonUtils.hideSoftInput(editText.getContext(), editText);
    }

  }

  @Override
  public void update2loadData(int loadType, List<CircleItem> datas) {

    if (loadType == TYPE_PULLREFRESH) {
      recyclerView.setRefreshing(false);
      circleAdapter.setDatas(datas);
    } else if (loadType == TYPE_UPLOADREFRESH) {
      circleAdapter.getDatas().addAll(datas);
    }
    circleAdapter.notifyDataSetChanged();

    if (circleAdapter.getDatas().size() < 45 + CircleAdapter.HEADVIEW_SIZE) {
      recyclerView.setupMoreListener(new OnMoreListener() {
        @Override
        public void onMoreAsked(int overallItemsCount, int itemsBeforeMore,
            int maxLastVisiblePosition) {

          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              presenter.loadData(TYPE_UPLOADREFRESH);
            }
          }, 2000);

        }
      }, 1);
    } else {
      recyclerView.removeMoreListener();
      recyclerView.hideMoreProgress();
    }

  }

  @Override
  public void onPermissionsGranted(int requestCode, List<String> perms) {

  }

  @Override
  public void onPermissionsDenied(int requestCode, List<String> perms) {
    Toast.makeText(this, "您拒绝了相关权限，可能会导致相关功能不可用", Toast.LENGTH_LONG).show();

  }

  private void setViewTreeObserver() {
    bodyLayout = (RelativeLayout) findViewById(R.id.bodyLayout);
    final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
    swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {

        Rect r = new Rect();
        bodyLayout.getWindowVisibleDisplayFrame(r);
        int statusBarH = getStatusBarHeight();//状态栏高度
        int screenH = bodyLayout.getRootView().getHeight();
        if (r.top != statusBarH) {
          //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
          r.top = statusBarH;
        }
        int keyboardH = screenH - (r.bottom - r.top);
        Log.d(TAG, "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom
            + " &top=" + r.top + " &statusBarH=" + statusBarH);

        if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
          return;
        }

        currentKeyboardH = keyboardH;
        screenHeight = screenH;//应用屏幕的高度
        editTextBodyHeight = edittextbody.getHeight();

        if (keyboardH < 150) {//说明是隐藏键盘的情况
          updateEditTextBodyVisible(View.GONE, null);
          return;
        }
        //偏移listview
        if (layoutManager != null && commentConfig != null) {
          layoutManager.scrollToPositionWithOffset(
              commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE,
              getListviewOffset(commentConfig));
        }
      }
    });
  }

  /**
   * 获取状态栏高度
   */
  private int getStatusBarHeight() {
    int result = 0;
    int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }

  /**
   * 测量偏移量
   */
  private int getListviewOffset(CommentConfig commentConfig) {
    if (commentConfig == null) {
      return 0;
    }
    //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
    //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
    int listviewOffset =
        screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - title.getHeight()-25;
    if (commentConfig.commentType == CommentConfig.Type.REPLY) {
      //回复评论的情况
      listviewOffset = listviewOffset + selectCommentItemOffset;
    }
    Log.i(TAG, "listviewOffset : " + listviewOffset);
    return listviewOffset;
  }

  private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
    if (commentConfig == null) {
      return;
    }

    int firstPosition = layoutManager.findFirstVisibleItemPosition();
    //只能返回当前可见区域（列表可滚动）的子项
    View selectCircleItem = layoutManager
        .getChildAt(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE - firstPosition);

    if (selectCircleItem != null) {
      selectCircleItemH = selectCircleItem.getHeight();
    }

    if (commentConfig.commentType == CommentConfig.Type.REPLY) {
      //回复评论的情况
      CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
      if (commentLv != null) {
        //找到要回复的评论view,计算出该view距离所属动态底部的距离
        View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
        if (selectCommentItem != null) {
          //选择的commentItem距选择的CircleItem底部的距离
          selectCommentItemOffset = 0;
          View parentView = selectCommentItem;
          do {
            int subItemBottom = parentView.getBottom();
            parentView = (View) parentView.getParent();
            if (parentView != null) {
              selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
            }
          } while (parentView != null && parentView != selectCircleItem);
        }
      }
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
      if (edittextbody != null && edittextbody.getVisibility() == View.VISIBLE) {
        //edittextbody.setVisibility(View.GONE);
        updateEditTextBodyVisible(View.GONE, null);
        return true;
      }
    }
    return super.onKeyDown(keyCode, event);
  }

}

package com.example.administrator.lubanone.activity.training.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.training.TrainPlayBackAdapter;
import com.example.administrator.lubanone.bean.training.TrainPlayBackBean;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class TrainPlayBackMoreActivity extends AppCompatActivity implements View.OnClickListener{

  private TextView back;
  private TextView title;
  private PullToRefreshLayout trainPlayBackMoreRefresh;
  private PullableListView trainPlayBackMoreList;
  private List<TrainPlayBackBean> mList;
  private TrainPlayBackAdapter mTrainPlayBackAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_train_play_back_more);
    initView();
  }

  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    trainPlayBackMoreRefresh = (PullToRefreshLayout) this.findViewById(R.id.train_play_back_more_refresh);
    trainPlayBackMoreList = (PullableListView) this.findViewById(R.id.train_play_back_more_list);
    title.setText("培训回放");

    back.setOnClickListener(this);

    trainPlayBackMoreRefresh.setOnPullListener(new MyRefreshListener());
    trainPlayBackMoreList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    trainPlayBackMoreRefresh.setPullUpEnable(true);//设置是否让上拉加载

    mList = new ArrayList<>();
    initData();

  }

  private void initData(){
    mList.add(new TrainPlayBackBean("对方根本就大街口","45","67",
        "https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg"));
    mList.add(new TrainPlayBackBean("发挥巨大变革","905","89",
        "https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg"));
    mList.add(new TrainPlayBackBean("女飞到昆明v你的肌肤不v","400","870",
        "https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg"));
    mTrainPlayBackAdapter = new TrainPlayBackAdapter(this, mList);
    trainPlayBackMoreList.setAdapter(mTrainPlayBackAdapter);
  }




  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.activity_back:
        TrainPlayBackMoreActivity.this.finish();
        break;
      default:
        break;
    }
  }

  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      trainPlayBackMoreRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainPlayBackAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      trainPlayBackMoreRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainPlayBackAdapter.notifyDataSetChanged();
    }
  }


}

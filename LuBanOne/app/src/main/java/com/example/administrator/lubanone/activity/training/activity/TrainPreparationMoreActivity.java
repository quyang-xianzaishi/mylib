package com.example.administrator.lubanone.activity.training.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.training.TrainPreparationMoreAdapter;
import com.example.administrator.lubanone.bean.training.TrainPreparationBean;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class TrainPreparationMoreActivity extends AppCompatActivity implements View.OnClickListener{

  private TextView back;
  private TextView title;
  private PullToRefreshLayout trainPreparationRefresh;
  private PullableListView trainPreparationMoreList;
  private List<TrainPreparationBean> mList;
  private TrainPreparationMoreAdapter mTrainPreparationMoreAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_train_preparation_more);
    initView();
  }

  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    trainPreparationRefresh = (PullToRefreshLayout) this.findViewById(R.id.train_preparation_refresh);
    trainPreparationMoreList = (PullableListView) this.findViewById(R.id.train_preparation_list);
    title.setText(this.getResources().getString(R.string.training_pre));

    back.setOnClickListener(this);

    trainPreparationRefresh.setOnPullListener(new MyRefreshListener());
    trainPreparationMoreList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    trainPreparationRefresh.setPullUpEnable(true);//设置是否让上拉加载

    mList = new ArrayList<>();
    initData();

  }

  private void initData(){
    mList.add(new TrainPreparationBean("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg"
        ,"sdfsfgfh","XXs时XX分XX秒","开播提醒"));
    mList.add(new TrainPreparationBean("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg"
        ,"设计开发的贺年卡更好","XXX天XX时XX分XX秒","取消提醒"));
    mList.add(new TrainPreparationBean("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg"
        ,"我是XXXXXXXXXXXXX","XX时XX分XX秒","开播提醒"));
    mTrainPreparationMoreAdapter = new TrainPreparationMoreAdapter(this, mList);
    trainPreparationMoreList.setAdapter(mTrainPreparationMoreAdapter);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.activity_back:
        TrainPreparationMoreActivity.this.finish();
        break;
      default:
        break;
    }

  }

  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      trainPreparationRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainPreparationMoreAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      trainPreparationRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainPreparationMoreAdapter.notifyDataSetChanged();

    }

  }
}

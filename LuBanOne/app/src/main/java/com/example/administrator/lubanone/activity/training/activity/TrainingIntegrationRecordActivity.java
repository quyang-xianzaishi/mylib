package com.example.administrator.lubanone.activity.training.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.training.TrainingIntegrationRecordAdapter;
import com.example.administrator.lubanone.bean.training.IntegrationRecordBean;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\7\12 0012.
 */

public class TrainingIntegrationRecordActivity extends AppCompatActivity implements OnClickListener {

  private TextView back;
  private TextView title;
  private PullToRefreshLayout trainingIntegrationRecordRefresh;
  private PullableListView trainingIntegrationRecordMoreList;
  private List<IntegrationRecordBean> mList;
  private TrainingIntegrationRecordAdapter  mTrainingIntegrationRecordAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_integration_record);
    initView();
  }

  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    trainingIntegrationRecordRefresh = (PullToRefreshLayout) this.findViewById(
        R.id.activity_integration_record_pullToRefreshLayout);
    trainingIntegrationRecordMoreList = (PullableListView) this.findViewById(
        R.id.activity_integration_record_listview);

    back.setOnClickListener(this);
    title.setText("培训积分记录");

    trainingIntegrationRecordRefresh.setOnPullListener(new MyRefreshListener());
    trainingIntegrationRecordMoreList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    trainingIntegrationRecordRefresh.setPullUpEnable(true);//设置是否让上拉加载

    mList = new ArrayList<>();
    initData();

  }

  private void initData() {

    mList.add(new IntegrationRecordBean("2017.04.12 12:08:23","完成考试","5"));
    mList.add(new IntegrationRecordBean("2017.07.12 17:08:23","完成培训","12"));
    mList.add(new IntegrationRecordBean("2017.05.12 16:48:03","完成考试","6"));
    mList.add(new IntegrationRecordBean("2017.02.12 10:23:56","上传推广材料","9"));
    mTrainingIntegrationRecordAdapter = new TrainingIntegrationRecordAdapter(this, mList);
    trainingIntegrationRecordMoreList.setAdapter(mTrainingIntegrationRecordAdapter);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.activity_back:
        TrainingIntegrationRecordActivity.this.finish();
        break;
      default:
        break;
    }
  }

  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      trainingIntegrationRecordRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainingIntegrationRecordAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      trainingIntegrationRecordRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainingIntegrationRecordAdapter.notifyDataSetChanged();

    }

  }


}

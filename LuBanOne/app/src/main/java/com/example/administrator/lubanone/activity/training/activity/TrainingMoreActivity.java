package com.example.administrator.lubanone.activity.training.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.training.TrainingMoreAdapter;
import com.example.administrator.lubanone.bean.training.TrainingBean;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\7\7 0007.
 */

public class TrainingMoreActivity extends AppCompatActivity implements View.OnClickListener{

  private TextView back;
  private TextView title;
  private PullToRefreshLayout trainingMoreRefresh;
  private PullableListView trainingMoreList;
  private List<TrainingBean> mList;
  private TrainingMoreAdapter mTrainingMoreAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_training_more);
    initView();
  }

  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    trainingMoreRefresh = (PullToRefreshLayout) this.findViewById(R.id.training_more_refresh);
    trainingMoreList = (PullableListView) this.findViewById(R.id.training_more_list);
    title.setText(this.getResources().getString(R.string.tranining_hint));

    back.setOnClickListener(this);

    trainingMoreRefresh.setOnPullListener(new MyRefreshListener());
    trainingMoreList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    trainingMoreRefresh.setPullUpEnable(true);//设置是否让上拉加载

    mList = new ArrayList<>();
    initData();
  }

  private void initData(){
    mList.add(new TrainingBean("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg",
        "俄个人个人体会","几个分蘖骨和瑞哦工行功夫呢房间的空间飞入肌肤感觉估计会让梵蒂冈的风格"));
    mList.add(new TrainingBean("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg",
        "fgjnghmkjhkhk,l","尽管不能减肥的公司读过后感觉很感人就可不能放几个回复你呢色法啊阿"));
    mTrainingMoreAdapter = new TrainingMoreAdapter(this, mList);
    trainingMoreList.setAdapter(mTrainingMoreAdapter);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.activity_back:
        TrainingMoreActivity.this.finish();
        break;
      default:
        break;
    }

  }


  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      trainingMoreRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainingMoreAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      trainingMoreRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainingMoreAdapter.notifyDataSetChanged();

    }

  }

}

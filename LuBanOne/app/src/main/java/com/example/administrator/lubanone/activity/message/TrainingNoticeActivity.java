package com.example.administrator.lubanone.activity.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.message.TrainingNoticeAdapter;
import com.example.administrator.lubanone.bean.message.TrainingNoticeBean;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\7\6 0006.
 */

public class TrainingNoticeActivity extends AppCompatActivity implements View.OnClickListener{

  private TextView back;
  private TextView title;
  private PullToRefreshLayout trainingNoticeRefresh;
  private PullableListView trainingNoticeList;
  private List<TrainingNoticeBean> mList;
  private TrainingNoticeAdapter mTrainingNoticeAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_training_notice);
    initView();
  }

  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    trainingNoticeRefresh = (PullToRefreshLayout) this.findViewById(R.id.training_notice_refresh);
    trainingNoticeList = (PullableListView) this.findViewById(R.id.training_notice_list);
    title.setText("培训通告");

    back.setOnClickListener(this);

    trainingNoticeRefresh.setOnPullListener(new MyRefreshListener());
    trainingNoticeList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    trainingNoticeRefresh.setPullUpEnable(true);//设置是否让上拉加载

    mList = new ArrayList<>();
    initData();
  }

  private void initData(){
    mList.clear();
    mList.add(new TrainingNoticeBean("开播提醒","2017-04-17 15:00",
        "尊敬的XXX会员，您关注的如何发展团队培训课程马上开播","","","","1","立即进入---"));
    mList.add(new TrainingNoticeBean("系统邀请","", "尊敬的XXX会员，XXX邀请您参加一下培训课程"
        + "，点击查看预告","如何发展团队","哈哈哈哈哈哈","2017-04-17 16:00","2","点击查看预告"));
    mList.add(new TrainingNoticeBean("系统审核","", "尊敬的XXX会员，您发起的XXX培训课程，"
        + "我们会尽快审核，通过后会短信通知您。","纷纷为法国","发表观点时不会发生变化","2017-04-20 16:00","3",""));
    mList.add(new TrainingNoticeBean("审核通过","", "尊敬的XXX会员，您发起的XXX培训课程，"
        + "已通过审核，前往列表去通知伞下会员。","打个比方打狗吧","陈独秀在v对方不v辅导费","2017-04-21 15:00",
        "4","前往列表去通知伞下会员"));
    mList.add(new TrainingNoticeBean("申请被拒","", "尊敬的XXX会员，您发起的XXX培训课程，"
        + "由于内容重复，不能通过。可前往列表申请新的培训主题","对方的都不敢","对方并非国内那么后果很"
        ,"2017-04-28 15:00","5","前往列表申请新的培训主题"));
    mTrainingNoticeAdapter = new TrainingNoticeAdapter(this, mList);
    trainingNoticeList.setAdapter(mTrainingNoticeAdapter);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.activity_back:
        TrainingNoticeActivity.this.finish();
        break;
      default:
        break;
    }

  }


  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      trainingNoticeRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainingNoticeAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      trainingNoticeRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTrainingNoticeAdapter.notifyDataSetChanged();

    }

  }
}

package com.example.administrator.lubanone.activity.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.message.TeamMessageAdapter;
import com.example.administrator.lubanone.bean.message.TeamMessageBean;
import com.example.administrator.lubanone.bean.message.TrainingNoticeBean;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\7\6 0006.
 */

public class TeamMessageActivity extends AppCompatActivity implements View.OnClickListener{

  private TextView back;
  private TextView title;
  private PullToRefreshLayout teamMessageRefresh;
  private PullableListView teamMessageList;
  private List<TeamMessageBean> mList;
  private TeamMessageAdapter mTeamMessageAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_team_message);
    initView();
  }

  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    title = (TextView) this.findViewById(R.id.activity_title);
    teamMessageRefresh = (PullToRefreshLayout) this.findViewById(R.id.team_message_refresh);
    teamMessageList = (PullableListView) this.findViewById(R.id.team_message_list);
    title.setText("团队消息");

    back.setOnClickListener(this);

    teamMessageRefresh.setOnPullListener(new MyRefreshListener());
    teamMessageList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    teamMessageRefresh.setPullUpEnable(true);//设置是否让上拉加载

    mList = new ArrayList<>();
    initData();
  }

  private void initData(){
    mList.add(new TeamMessageBean("培训通知：","4月10日","尊敬的会员，您向您伞下会员"
        + "XX,XXX,XX,XX,XX,XX,XX,XX发出了关于您申请的培训通知，培训信息如下：",
        "如何发展团队","发展团队的重要性"+'\n'+"发展团队的三个途径"+'\n'+"途经的展开介绍","2017-09-12 15:00"));
    mList.add(new TeamMessageBean("培训通知：","4月10日","尊敬的会员，您向您伞下会员"
        + "XX,XXX,XX,XX发出了关于您申请的培训通知，培训信息如下：",
        "如何发展团队","发展团队的重要性"+'\n'+"发展团队的三个途径"+'\n'+"途经的展开介绍","2017-09-12 15:00"));
    mTeamMessageAdapter = new TeamMessageAdapter(this, mList);
    teamMessageList.setAdapter(mTeamMessageAdapter);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.activity_back:
        TeamMessageActivity.this.finish();
        break;
      default:
        break;
    }

  }

  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      teamMessageRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTeamMessageAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      teamMessageRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mTeamMessageAdapter.notifyDataSetChanged();

    }

  }
}

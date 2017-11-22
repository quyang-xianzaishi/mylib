package com.example.administrator.lubanone.activity.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.StringUtil;

/**
 * Created by Administrator on 2017\8\23 0023.
 */

public class SellSeedsRecordDetailActivity extends BaseActivity {

  private TextView sellSeedsCount;
  private TextView orderId;
  private TextView orderCount;
  private TextView orderTime;
  private TextView orderMember;
  private TextView orderScore;
  private TextView orderState;
  private ImageView back;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_sell_seeds_record_detail;
  }

  @Override
  public void initView() {
    sellSeedsCount = (TextView) this.findViewById(R.id.sell_seeds_count);
    orderId = (TextView) this.findViewById(R.id.order_id);
    orderCount = (TextView) this.findViewById(R.id.order_count);
    orderTime = (TextView) this.findViewById(R.id.order_time);
    orderMember = (TextView) this.findViewById(R.id.order_member);
    orderScore = (TextView) this.findViewById(R.id.order_score);
    orderState = (TextView) this.findViewById(R.id.order_state);

    back = (ImageView) this.findViewById(R.id.iv_back);

    back.setOnClickListener(this);

  }

  @Override
  public void loadData() {
    if(getIntent()!=null){
      if(getIntent().hasExtra("orderCount")&&getIntent().getStringExtra("orderCount")!=null){

//        StringUtil.setTextSize(null,,25,);
        String s = DebugUtils.convert(getIntent().getStringExtra("orderCount"), "0") + " PCS";
        StringUtil.setTextSize(s, sellSeedsCount, 25, 11);

        String s1 = DebugUtils.convert(getIntent().getStringExtra("orderCount"),"0") + " PCS";
        StringUtil.setTextSize(s1, orderCount, 11, 9);
      }
      if(getIntent().hasExtra("orderTime")&&getIntent().getStringExtra("orderTime")!=null){
        orderTime.setText(DebugUtils.convert(getIntent().getStringExtra("orderTime"), ""));
      }
      if(getIntent().hasExtra("orderMember")&&getIntent().getStringExtra("orderMember")!=null){
        orderMember.setText(DebugUtils.convert(getIntent().getStringExtra("orderMember"), ""));
      }
      if(getIntent().hasExtra("orderScore")&&getIntent().getStringExtra("orderScore")!=null){
        orderScore.setText(DebugUtils.convert(getIntent().getStringExtra("orderScore"), "0"));
      }
      if(getIntent().hasExtra("orderId")&&getIntent().getStringExtra("orderId")!=null){
        orderId.setText(DebugUtils.convert(getIntent().getStringExtra("orderId"), "0"));
      }
      orderState.setText(getString(R.string.sell_seeds_record_item_state));
    }

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.iv_back:
        this.finish();
        break;
      default:
        break;
    }
  }
}

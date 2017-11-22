package com.example.administrator.lubanone.activity.message;

import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.adapter.message.OrderMessageAdapter;
import com.example.administrator.lubanone.bean.message.OrderMessageBean;
import com.example.administrator.lubanone.bean.message.OrderMessageResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableImageView;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\7\5 0005.
 */

public class OrderMessageActivity extends BaseActivity implements View.OnClickListener {

  private TextView back;
  private PullToRefreshLayout orderMessageRefresh;
  private PullToRefreshLayout noData;
  private PullableImageView loadingFail;
  private PullableListView orderMessageList;
  private List<OrderMessageBean> mList;
  private OrderMessageAdapter mOrderMessageAdapter;
  private int page;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.activity_order_message);
    initView();
  }

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_order_message;
  }

  public void initView() {

    back = (TextView) this.findViewById(R.id.activity_back);
    back.setOnClickListener(this);

    noData = (PullToRefreshLayout) this.findViewById(R.id.no_data);
    loadingFail = (PullableImageView) this.findViewById(R.id.no_data_image);
    orderMessageRefresh = (PullToRefreshLayout) this.findViewById(R.id.order_message_refresh);
    orderMessageList = (PullableListView) this.findViewById(R.id.order_message_list);

    back.setOnClickListener(this);

    orderMessageRefresh.setOnPullListener(new MyRefreshListener());
    noData.setOnPullListener(new MyRefreshListener());
    orderMessageList.setFriction(ViewConfiguration.getScrollFriction() * 20);
    orderMessageRefresh.setPullUpEnable(true);//设置是否让上拉加载

    mList = new ArrayList<>();
    mOrderMessageAdapter = new OrderMessageAdapter(this, mList);
    orderMessageList.setAdapter(mOrderMessageAdapter);

  }

  @Override
  public void loadData() {
   getOrderMessageList();

  }

  @Override
  protected void onResume() {
    page = 1;
    mList.clear();
    super.onResume();
  }

  @Override
  public void onClick(View v) {
      switch (v.getId()){
        case R.id.activity_back:
          OrderMessageActivity.this.finish();
          break;
        default:
          break;
      }
  }

  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      mList = new ArrayList<>();
      page = 1;
      loadData();
      noData.refreshFinish(PullToRefreshLayout.SUCCEED);
      orderMessageRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mOrderMessageAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      loadData();
      noData.refreshFinish(PullToRefreshLayout.SUCCEED);
      orderMessageRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mOrderMessageAdapter.notifyDataSetChanged();

    }
  }


  private RequestListener getOrderMessageListListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<OrderMessageResultBean> result = GsonUtil
            .processJson(jsonObject, OrderMessageResultBean.class);
        if(result!=null&& ResultUtil.isSuccess(result)) {
          if (result.getResult() != null) {
            if(result.getResult().getData()!=null&&result.getResult().getData().size()>0){
              for(int i = 0;i<result.getResult().getData().size();i++) {
                mList.add(new OrderMessageBean(
                    result.getResult().getData().get(i).getTitle(),
                    result.getResult().getData().get(i).getPushtime(),
                    result.getResult().getData().get(i).getContent(),
                    result.getResult().getData().get(i).getMember(),
                    result.getResult().getData().get(i).getMemberid(),
                    result.getResult().getData().get(i).getOrderid(),
                    result.getResult().getData().get(i).getOrdertype(),
                    result.getResult().getData().get(i).getTime(),
                    result.getResult().getData().get(i).getOtherdescription()));
              }
              mOrderMessageAdapter.notifyDataSetChanged();
              page++;
              orderMessageRefresh.setVisibility(View.VISIBLE);
              noData.setVisibility(View.GONE);
              if (result.getResult().getData().size() < 10) {
                orderMessageRefresh.setPullUpEnable(false);
              } else {
                orderMessageRefresh.setPullUpEnable(true);
              }
            }else {
              if(page>1){
                ToastUtil.showShort(getResources().getString(R.string.no_more_message),
                    OrderMessageActivity.this);
              }else {
                orderMessageRefresh.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                loadingFail.setImageDrawable(OrderMessageActivity.this.
                    getResources().getDrawable(R.drawable.no_data));
              }
            }
          }else {
            if(page>1){
              ToastUtil.showShort(getResources().getString(R.string.no_more_message),
                  OrderMessageActivity.this);
            }else {
              orderMessageRefresh.setVisibility(View.GONE);
              noData.setVisibility(View.VISIBLE);
              loadingFail.setImageDrawable(OrderMessageActivity.this.
                  getResources().getDrawable(R.drawable.no_data));
            }
          }
        }else {
          //接口异常
          Toast.makeText(OrderMessageActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
              getString(R.string.get_order_message_list_fail)), Toast.LENGTH_LONG).show();
          orderMessageRefresh.setVisibility(View.GONE);
          noData.setVisibility(View.VISIBLE);
          loadingFail.setImageDrawable(OrderMessageActivity.this.
              getResources().getDrawable(R.drawable.loading_fail));
        }
      } catch (Exception e) {
        ToastUtil.showShort(getResources().getString(R.string.get_order_message_list_fail),
            OrderMessageActivity.this);
        orderMessageRefresh.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
        loadingFail.setImageDrawable(OrderMessageActivity.this.
            getResources().getDrawable(R.drawable.loading_fail));
      }

    }

    @Override
    public void onFail(String errorMsf) {
      ToastUtil.showShort(getResources().getString(R.string.get_order_message_list_fail),
          OrderMessageActivity.this);
      orderMessageRefresh.setVisibility(View.GONE);
      noData.setVisibility(View.VISIBLE);
      loadingFail.setImageDrawable(OrderMessageActivity.this.
          getResources().getDrawable(R.drawable.loading_fail));
    }
  };

  private void getOrderMessageList(){
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
    RequestParams paramsPage = new RequestParams("page",Integer.toString(page));
    list.add(paramsToken);
    list.add(paramsPage);
    RequestNet requestNet = new RequestNet((MyApplication)getApplication(), OrderMessageActivity.this, list,
        Urls.MSG_ORDER_LIST, getOrderMessageListListener, RequestNet.POST);
  }



}

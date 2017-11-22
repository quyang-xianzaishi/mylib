package com.example.administrator.lubanone.fragment.us;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.us.CommonListDetailsActivity;
import com.example.administrator.lubanone.adapter.UsSpreadDownloadGvAdapter;
import com.example.administrator.lubanone.bean.model.UsChildLVCommonBean;
import com.example.administrator.lubanone.fragment.BaseFragment;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.rxjava.MySubscriber;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.administrator.lubanone.utils.HouToast;
import com.example.qlibrary.utils.SPUtils;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableGridView;
import com.jingchen.pulltorefresh.PullableImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 2017/7/3.
 */

public class ListSpreadDownloadDataFragment extends BaseFragment {

//  private RecyclerView mRecyclerView;

  private PullToRefreshLayout noDataLayout;
  private PullableImageView noDataImage;
  private PullToRefreshLayout mRefreshLayout;
  //  private PullableRecyclerView mRecyclerView;
//  private UsSpreadDownloadAdapter mAdapter;
  private PullableGridView mGridView;
  private UsSpreadDownloadGvAdapter mAdapter;
  private List<UsChildLVCommonBean.UsChildLvList> datas;
  private int pageNo = 1;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_spread_download, null);

    mRefreshLayout = (PullToRefreshLayout) view
        .findViewById(R.id.us_spread_download_refresh_layout);
//    mRecyclerView = (PullableRecyclerView) view.findViewById(R.id.us_spread_download_recycler_view);
    mGridView = (PullableGridView) view.findViewById(R.id.us_spread_download_grid_view);
    noDataImage = (PullableImageView) view.findViewById(R.id.us_download_no_data_image);
    noDataLayout = (PullToRefreshLayout) view.findViewById(R.id.us_download_no_data);
    MyRefreshListener myRefreshListener = new MyRefreshListener();
    mRefreshLayout.setOnPullListener(myRefreshListener);
    noDataLayout.setPullUpEnable(false);
    noDataLayout.setOnPullListener(myRefreshListener);
    datas = new ArrayList<>();
//    mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//    mAdapter = new UsSpreadDownloadAdapter(getActivity(), datas);
//    mRecyclerView.setAdapter(mAdapter);
    mAdapter = new UsSpreadDownloadGvAdapter(getActivity(), datas);
    mGridView.setAdapter(mAdapter);

    mGridView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), CommonListDetailsActivity.class);
        intent.putExtra("if_id", datas.get(position).getIf_id());
        intent.putExtra("theme", datas.get(position).getTitle());
        intent.putExtra("content", datas.get(position).getDatetime());
        intent.putExtra("is_download", true);
        startActivity(intent);
      }
    });

    return view;
  }

  //刷新监听
  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      pageNo = 1;
      getDataRetrofit(pullToRefreshLayout);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      pageNo++;
      getDataRetrofit(pullToRefreshLayout);
    }
  }

  @Override
  public void initData() {
    getDataRetrofit(mRefreshLayout);
  }

  //获取数据
  private void getDataRetrofit(final PullToRefreshLayout refreshLayout) {
    Subscriber subscriber = new MySubscriber<UsChildLVCommonBean>(getActivity()) {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        noDataLayout.setVisibility(View.VISIBLE);
        noDataImage.setImageResource(R.drawable.loading_fail);
        HouLog.d("推广下载error", e.toString());
      }

      @Override
      public void onNext(UsChildLVCommonBean usListViewModel) {
        noDataLayout.setVisibility(View.GONE);
        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        if (pageNo == 1) {
          datas.clear();
        }
        if (usListViewModel != null && usListViewModel.getAboutlist().size() > 0) {
          datas.addAll(usListViewModel.getAboutlist());
          HouLog.d("推广下载result结果",
              usListViewModel.getAboutlist().size() + "  " + usListViewModel.toString());
        } else {
          if (pageNo > 1) {
            HouToast.showLongToast(getActivity(), getInfo(R.string.no_more_message));
            pageNo--;
          } else {
            noDataImage.setImageResource(R.drawable.no_data);
            noDataLayout.setVisibility(View.VISIBLE);
          }
        }
        mAdapter.notifyDataSetChanged();
        HouLog.d("页数：", String.valueOf(pageNo));
      }

    };
    Map<String, String> map = new HashMap<>();
    map.put("token", SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""));
    map.put("if_type", "download");
    map.put("page", String.valueOf(pageNo));
    HouLog.d("推广下载请求参数", map.toString());
    MyApplication.rxNetUtils.getUsListViewService().getListView(map)
        .map(new BaseModelFunc<UsChildLVCommonBean>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);

  }
}

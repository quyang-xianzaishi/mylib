package com.example.administrator.lubanone.activity.training;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.training.TrainingInfomationAdapter;
import com.example.administrator.lubanone.bean.training.TrainingInfoBean;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\6\22 0022.
 */

public class TrainingInfomationFragment extends Fragment{

    private List<TrainingInfoBean> mList;
    private PullToRefreshLayout trainInfomationRefresh;
    private PullableListView trainInfomationList;
    private TrainingInfomationAdapter mTrainingInfomationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training_infomation, container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        trainInfomationRefresh = (PullToRefreshLayout) getActivity().findViewById(R.id.training_infomation_refresh);
        trainInfomationList = (PullableListView) getActivity().findViewById(R.id.training_infomation_list);

        trainInfomationRefresh.setOnPullListener(new MyRefreshListener());
        trainInfomationList.setFriction(ViewConfiguration.getScrollFriction() * 20);
        trainInfomationRefresh.setPullUpEnable(true);//设置是否让上拉加载
        //网络请求数据
        mList = new ArrayList<>();
        initData();
    }

    protected void initData(){
        mList.add(new TrainingInfoBean("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg",
            "俄个人个人体会","几个分蘖骨和瑞哦工行功夫呢房间的空间飞入肌肤感觉估计会让梵蒂冈的风格"));
        mList.add(new TrainingInfoBean("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg",
            "fgjnghmkjhkhk,l","尽管不能减肥的公司读过后感觉很感人就可不能放几个回复你呢色法啊阿"));
        mTrainingInfomationAdapter = new TrainingInfomationAdapter(getActivity(), mList);
        trainInfomationList.setAdapter(mTrainingInfomationAdapter);

    }

    class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            initData();
            trainInfomationRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
            mTrainingInfomationAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            initData();
            trainInfomationRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
            mTrainingInfomationAdapter.notifyDataSetChanged();
        }
    }

}

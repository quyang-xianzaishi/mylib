package com.example.administrator.lubanone.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.task.TaskCollectActivity;
import com.example.administrator.lubanone.activity.task.TaskDetailsActivity;
import com.example.administrator.lubanone.adapter.task.TaskListViewAdapter;
import com.example.administrator.lubanone.bean.TasksBean;
import com.example.administrator.lubanone.customview.progressdialog.BaseProgressDialog;
import com.example.administrator.lubanone.customview.progressdialog.MyProgressDialog;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.utils.HouLog;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableImageView;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment implements View.OnClickListener {
  private final static String TAG = "TaskFragment";

  private Context context;
  private PullToRefreshLayout pullToRefreshLayout;
  private PullableListView pullableListView;
  private TaskListViewAdapter taskListViewAdapter;
  private ImageView collectBtn;
  private EditText searchEdit;
  private ImageView searchClear;
  private List<TasksBean> tasksBeen;
  private PullToRefreshLayout noDataLayout;
  private PullableImageView noDataImage;

  private int pageNo = 1;             //请求页数
  private String searchField = "";    //搜索字段

  private void getListData(final PullToRefreshLayout refreshLayout) {
    Subscriber subscriber = new Subscriber<List<TasksBean>>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        noDataLayout.setVisibility(View.VISIBLE);
        refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        if (e.getMessage().equals("3")) {
//          ToastUtil.showShort("type=3", getActivity());
          MyApplication.getInstance().safeOut((MyApplication) getActivity().getApplication(),getActivity());
        }
        HouLog.d(TAG+"任务列表onError ", e.getMessage());
      }

      @Override
      public void onNext(List<TasksBean> tasksBeanList) {
        noDataLayout.setVisibility(View.GONE);
        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        if (pageNo == 1) {
          tasksBeen.clear();
        }
        HouLog.d(TAG+"任务列表:", tasksBeanList.size() + "  " + tasksBeanList.toString());
        if (tasksBeanList != null && tasksBeanList.size() > 0) {//没数据返回[]，判断是否为空且大小大于0
          tasksBeen.addAll(tasksBeanList);
        }else {
          if (pageNo > 1) {
            ToastUtil.showShort("无更多数据", getActivity());
            pageNo--;
          } else {
            noDataImage.setImageResource(R.drawable.no_data);
            noDataLayout.setVisibility(View.VISIBLE);
          }
        }
        taskListViewAdapter.notifyDataSetChanged();
        HouLog.d(TAG+"页数：", String.valueOf(pageNo));
      }
    };
    Map<String, String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("page", String.valueOf(pageNo));
    HouLog.d(TAG,"任务列表请求参数:"+params.toString());
    MyApplication.rxNetUtils.getTaskService().getTaskListOld(params)
        .map(new BaseModelFunc<List<TasksBean>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  private void getSearchData(final PullToRefreshLayout refreshLayout) {
    Subscriber subscriber = new Subscriber<List<TasksBean>>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        noDataLayout.setVisibility(View.VISIBLE);
        refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        if (e.getMessage().equals("3")) {
          ToastUtil.showShort("type=3", getActivity());
        }
        HouLog.d(TAG+"任务列表onError", e.getMessage());
      }

      @Override
      public void onNext(List<TasksBean> tasksBeanList) {
        noDataLayout.setVisibility(View.GONE);
        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        if (pageNo == 1) {
          tasksBeen.clear();
        }
        HouLog.d(TAG+"任务列表:", tasksBeanList.size() + "  " + tasksBeanList.toString());
        if (tasksBeanList != null && tasksBeanList.size() > 0) {
          tasksBeen.addAll(tasksBeanList);
        }else {
          if (pageNo > 1) {
            ToastUtil.showShort("无更多数据", getActivity());
            pageNo--;
          } else {
            noDataImage.setImageResource(R.drawable.no_data);
            noDataLayout.setVisibility(View.VISIBLE);
          }
        }
        taskListViewAdapter.notifyDataSetChanged();
        HouLog.d(TAG+"页数：", String.valueOf(pageNo));
      }
    };
    Map<String,String> params = new HashMap<>();
    params.put("token", SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""));
    params.put("keywords", searchField);
    params.put("page", String.valueOf(pageNo));
    HouLog.d(TAG,"任务搜索列表请求参数:"+params.toString());
    MyApplication.rxNetUtils.getTaskService().getTaskSearchList(params)
        .map(new BaseModelFunc<List<TasksBean>>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }


  //=========================================
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  public TaskFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment TaskFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static TaskFragment newInstance(String param1, String param2) {
    TaskFragment fragment = new TaskFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }
  //==========================================


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getActivity();
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_task, container, false);
    initViews(view);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    searchEdit.setText("");
    searchField = "";
  }

  /**
   * 初始化组件
   */
  private void initViews(View view) {
    noDataImage = (PullableImageView) view.findViewById(R.id.task_fragment_no_data_image);
    noDataLayout = (PullToRefreshLayout) view.findViewById(R.id.task_fragment_no_data);
    searchEdit = (EditText) view.findViewById(R.id.task_fragment_search_et);
    searchClear = (ImageView) view.findViewById(R.id.task_fragment_search_clear);
    searchClear.setOnClickListener(this);
    collectBtn = (ImageView) view.findViewById(R.id.task_fragment_collect);
    pullToRefreshLayout = (PullToRefreshLayout) view
        .findViewById(R.id.task_fragment_pullToRefreshLayout);
    pullableListView = (PullableListView) view.findViewById(R.id.task_fragment_listview);
    collectBtn = (ImageView) view.findViewById(R.id.task_fragment_collect);
    collectBtn.setOnClickListener(this);
    tasksBeen = new ArrayList<>();

    final MyRefreshListener myRefreshListener = new MyRefreshListener();//拖动监听
    pullToRefreshLayout.setOnPullListener(myRefreshListener);
    noDataLayout.setOnPullListener(myRefreshListener);
    noDataLayout.setPullUpEnable(false);
    //设置下拉动画
    //         pullableListView = (PullableListView) RefreshSetting.setListProcessRefresh(context,pullToRefreshLayout);
    //设置摩擦力，减慢滑动速度
    pullableListView.setFriction(ViewConfiguration.getScrollFriction() * 20);
    //        pullableListView.setBackgroundColor(Color.WHITE);
    pullToRefreshLayout.setPullUpEnable(true);//设置是否让上拉加载
    taskListViewAdapter = new TaskListViewAdapter(context, tasksBeen);
    pullableListView.setAdapter(taskListViewAdapter);

    //listview item 事件监听
    pullableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HouLog.d("----item if_id---->>>", tasksBeen.get(position).getIf_id());
        Intent intent = new Intent(getActivity(), TaskDetailsActivity.class);
        intent.putExtra("which_Activity", "0");//0任务界面，1收藏界面
        intent.putExtra("click_if_id", tasksBeen.get(position).getIf_id());
        intent.putExtra("if_title", tasksBeen.get(position).getIf_title());
        startActivity(intent);
      }
    });

    //搜索
    searchEdit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
          searchClear.setVisibility(View.INVISIBLE);
          pageNo = 1;
          getListData(pullToRefreshLayout);
        } else {
          searchField = s.toString();
          pageNo = 1;
          getSearchData(pullToRefreshLayout);
          searchClear.setVisibility(View.VISIBLE);
        }

      }
    });

  }

  /**
   * 下拉刷新监听类
   */
  public class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      pageNo = 1;
      if (searchField.isEmpty()) {
        getListData(pullToRefreshLayout);
      } else {
        getSearchData(pullToRefreshLayout);
      }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      pageNo++;
      if (searchField.isEmpty()) {
        getListData(pullToRefreshLayout);
      } else {
        getSearchData(pullToRefreshLayout);
      }
    }
  }


  //组件事件监听
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.task_fragment_collect:
        Intent intent = new Intent(getActivity(), TaskCollectActivity.class);
//        Intent intent = new Intent(getActivity(), TaskDetailsActivity.class);
//        Intent intent = new Intent(getActivity(), CommonListDetailsActivity.class);
        startActivity(intent);
        break;
      case R.id.task_fragment_search_clear:
        searchEdit.setText("");
        searchField = "";
        break;
      case R.id.task_fragment_no_data:
        break;
      default:
        break;
    }
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {

    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}

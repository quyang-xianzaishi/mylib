package com.example.administrator.lubanone.activity.training;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.CommonTabLayoutFragmentAdapter;
import com.example.administrator.lubanone.adapter.FragmentVpAdapter;
import com.example.administrator.lubanone.fragment.BaseFragment;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017\6\24 0024.
 */

public class MyTrainingPagerFragment extends BaseFragment implements OnClickListener {

  private final static String TAG = "MyTrainingPagerFragment";

  private LinearLayout backBtn;

  private TextView iJoinTrain, iUploadTrain, myCollect;
  private ArrayList<Fragment> fragments;
  private TabLayout mTabLayout;
  private CommonTabLayoutFragmentAdapter tabLayoutAdapter;
  private String[] pagerTitles;

  private MyJoinTrainingFragment myJoinTrainingFragment;
  private MyUploadTrainFragment myTrainingFragment;
  private MyCollectionFragment myCollectionFragment;
  private FragmentManager fm;
  private FragmentVpAdapter adapter;

  private ViewPager mViewPager;
  private int mCurrentItem = 0;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_my_training_pager, null);
    backBtn = (LinearLayout) view.findViewById(R.id.train_mine_back_btn);
    mTabLayout = (TabLayout) view.findViewById(R.id.train_mine_tab_layout);
    backBtn.setOnClickListener(this);

    iJoinTrain = (TextView) view.findViewById(R.id.train_i_join);
    iUploadTrain = (TextView) view.findViewById(R.id.train_i_upload);
    myCollect = (TextView) view.findViewById(R.id.train_my_collect);
    mViewPager = (ViewPager) view.findViewById(R.id.my_training_pager);
    iJoinTrain.setOnClickListener(this);
    iUploadTrain.setOnClickListener(this);
    myCollect.setOnClickListener(this);

    pagerTitles = new String[]{getInfo(R.string.i_join_train), getInfo(R.string.i_upload_train),
        getInfo(R.string.mine_collect)};
    fragments = new ArrayList<>();
    myJoinTrainingFragment = new MyJoinTrainingFragment();
    myTrainingFragment = new MyUploadTrainFragment();
    myCollectionFragment = new MyCollectionFragment();
    fragments.add(myJoinTrainingFragment);
    fragments.add(myTrainingFragment);
    fragments.add(myCollectionFragment);
    fm = getChildFragmentManager();
    tabLayoutAdapter = new CommonTabLayoutFragmentAdapter(fm, fragments, pagerTitles);
    mViewPager.setAdapter(tabLayoutAdapter);
    mTabLayout.setupWithViewPager(mViewPager);

    adapter = new FragmentVpAdapter(fm, fragments);
    mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        iJoinTrain.setSelected(false);
        iUploadTrain.setSelected(false);
        myCollect.setSelected(false);
        switch (position) {
          case 0:
            iJoinTrain.setSelected(true);
            break;
          case 1:
            iUploadTrain.setSelected(true);
            break;
          case 2:
            myCollect.setSelected(true);
            break;
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    return view;
  }

  public void onResume() {
    super.onResume();
//    mViewPager.setCurrentItem(mCurrentItem);
    switch (mCurrentItem) {
      case 0:
        iJoinTrain.setSelected(true);
        break;
      case 1:
        iUploadTrain.setSelected(true);
        break;
      case 2:
        myCollect.setSelected(true);
        break;
      default:
        break;
    }

  }

  @Override
  public void initData() {

  }

  @Override
  public void onClick(View v) {
    iJoinTrain.setSelected(false);
    iUploadTrain.setSelected(false);
    myCollect.setSelected(false);
    switch (v.getId()) {
      case R.id.train_i_join:
        mViewPager.setCurrentItem(0);
        iJoinTrain.setSelected(true);
        mCurrentItem = 0;
        break;
      case R.id.train_i_upload:
        mViewPager.setCurrentItem(1);
        iUploadTrain.setSelected(true);
        mCurrentItem = 1;
        break;
      case R.id.train_my_collect:
        mViewPager.setCurrentItem(2);
        myCollect.setSelected(true);
        mCurrentItem = 2;
        break;
      case R.id.train_mine_back_btn:
        getActivity().finish();
        break;
      default:
        break;
    }
  }

  public interface OnFragmentInteractionListener {

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

}

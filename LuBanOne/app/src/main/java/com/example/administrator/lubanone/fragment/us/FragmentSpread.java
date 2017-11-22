package com.example.administrator.lubanone.fragment.us;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.CommonTabLayoutFragmentAdapter;
import com.example.administrator.lubanone.fragment.BaseFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hou on 2017/6/28.
 */

public class FragmentSpread extends BaseFragment implements OnClickListener {

  private TabLayout iacTabLayout;
  private ViewPager iacViewPager;
  private LinearLayout backBtn;
  private CommonTabLayoutFragmentAdapter adapter;
  private String[] pagerTitles;
  private List<Fragment> fragments;
  private ListSpreadUploadDataFragment uploadDataFragment;
  private ListSpreadDownloadDataFragment downloadDataFragment;
  private ListSpreadNewDataFragment newDataFragment;
  private FragmentManager fm;
  private TextView fragmentTitle;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_us_common, null);

    backBtn = (LinearLayout) view.findViewById(R.id.us_common_fragment_back);
    iacTabLayout = (TabLayout) view.findViewById(R.id.us_common_fragment_tablayout);
    iacViewPager = (ViewPager) view.findViewById(R.id.us_common_fragment_viewpager);
    backBtn.setOnClickListener(this);

    fragmentTitle = (TextView) view.findViewById(R.id.us_common_fragment_title);
    fragmentTitle.setText(getInfo(R.string.us_spread_title));

    uploadDataFragment = new ListSpreadUploadDataFragment();
    downloadDataFragment = new ListSpreadDownloadDataFragment();
    newDataFragment = new ListSpreadNewDataFragment();
    fragments = new ArrayList<>();
    fm = getChildFragmentManager();
    fragments.add(uploadDataFragment);
    fragments.add(downloadDataFragment);
    fragments.add(newDataFragment);
    pagerTitles = new String[]{getInfo(R.string.us_spread_upload_data),
        getInfo(R.string.us_spread_download_data), getInfo(R.string.us_spread_new_data)};
    adapter = new CommonTabLayoutFragmentAdapter(fm, fragments, pagerTitles);
    iacViewPager.setAdapter(adapter);
    //将TabLayout与ViewPager绑定在一起
    iacTabLayout.setupWithViewPager(iacViewPager);

    return view;
  }

  @Override
  public void initData() {

  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.us_common_fragment_back:
        getActivity().finish();
        break;
      default:
        break;
    }
  }
}

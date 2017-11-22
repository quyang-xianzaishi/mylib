package com.example.administrator.lubanone.fragment.us;

import android.view.View;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.fragment.BaseFragment;

/**
 * Created by hou on 2017/9/14.
 */

public class DetailsFundPoorFragment extends BaseFragment {

  private TextView fundPoorNumber;

  @Override
  public View initView() {
    View view = mInflater.inflate(R.layout.fragment_us_fund_poor, null);
    fundPoorNumber = (TextView) view.findViewById(R.id.us_fund_poor_number);
    return view;
  }

  @Override
  public void initData() {

  }
}

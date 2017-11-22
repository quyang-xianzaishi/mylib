package com.example.administrator.lubanone.adapter.homepage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.home.AlwaysProblemsActivity;
import com.example.qlibrary.utils.ToastUtil;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class ExpandListViewAdapter extends BaseExpandableListAdapter {


  Activity mActivity;
  private List<String> mStrings;
  private List<String> mChildStrings;
  private HashMap<String, List<String>> mMap;

  public ExpandListViewAdapter(Activity activity,
      List<String> headList, List<String> childsList) {
    this.mActivity = activity;
    mStrings = headList;
    mChildStrings = childsList;

    mMap = new HashMap<>();

    List<String> chileOne = new ArrayList<>();
    List<String> chileTwo = new ArrayList<>();
    List<String> chileThree = new ArrayList<>();
    List<String> chileFour = new ArrayList<>();

    chileOne.add(mChildStrings.get(0));
    chileTwo.add(mChildStrings.get(1));
    chileThree.add(mChildStrings.get(2));
    chileFour.add(mChildStrings.get(3));

    mMap.put(mStrings.get(0), chileOne);
    mMap.put(mStrings.get(1), chileTwo);
    mMap.put(mStrings.get(2), chileThree);
    mMap.put(mStrings.get(3), chileFour);
  }

  @Override
  public int getGroupCount() {
    return mMap.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return mMap.get(mStrings.get(groupPosition)).size();
  }

  @Override
  public Object getGroup(int groupPosition) {
    return mMap.get(mStrings.get(groupPosition));
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return mMap.get(mStrings.get(groupPosition)).get(childPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
      ViewGroup parent) {
    View view = LayoutInflater.from(mActivity)
        .inflate(R.layout.expand_lv_head_layout, null);
    TextView headView = (TextView) view.findViewById(R.id.tv_head);
    headView.setText(mStrings.get(groupPosition));
    return view;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
      View convertView, ViewGroup parent) {
    View view = LayoutInflater.from(mActivity)
        .inflate(R.layout.expand_lv_item_layout, null);
    TextView tv = (TextView) view.findViewById(R.id.tv);
    String child = (String) getChild(groupPosition, childPosition);
    tv.setText(child);
    return view;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return false;
  }

  private class MyOnClickListener implements View.OnClickListener {

    private int groupPosition;

    public MyOnClickListener(int groupPosition) {
      this.groupPosition = groupPosition;
    }

    @Override
    public void onClick(View v) {
      ToastUtil.showShort("" + groupPosition, mActivity);
    }
  }
}

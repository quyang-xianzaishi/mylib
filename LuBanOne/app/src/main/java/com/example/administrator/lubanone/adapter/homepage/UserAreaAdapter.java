package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.home.AreaSetActivity;
import com.example.administrator.lubanone.bean.homepage.AreaResultBean.CountrylistBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import java.util.List;

/**
 * Created by quyang on 2017/6/26 0026.
 */

public class UserAreaAdapter extends CustomAdapter<CountrylistBean> {

  private AreaSetActivity mActivity;
  private OnListViewItemListener mListener;
  private String code;

  public UserAreaAdapter(Context context, List<CountrylistBean> list,
      OnListViewItemListener listener, String code) {
    super(context, list);
    mActivity = (AreaSetActivity) context;
    this.mListener = listener;
    this.code = code;
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.area_item;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, CountrylistBean item, int position) {
    View root = viewHolder.getView(R.id.root);
    TextView area = viewHolder.getView(R.id.info_girl);
    View view = viewHolder.getView(R.id.line);
    ImageView leftChoosen = viewHolder.getView(R.id.iv_choosen);

    if (position != getCount() - 1) {
      view.setVisibility(View.VISIBLE);
    }

    String countrycode = item.getCountrycode();
    String nativeArea = mActivity.getCountryType();
    if (countrycode != null && countrycode.equals(nativeArea)) {
      Log.e("UserAreaAdapter", "setData2Views=true");
      leftChoosen.setVisibility(View.VISIBLE);
    } else {
      leftChoosen.setVisibility(View.INVISIBLE);
    }

    if (mActivity.getSelectedPosotion() != null && position == mActivity.getSelectedPosotion()) {
      leftChoosen.setVisibility(View.VISIBLE);
    } else if (mActivity.getSelectedPosotion() != null && position != mActivity.getSelectedPosotion()){
      leftChoosen.setVisibility(View.INVISIBLE);
    }

    area.setText(item.getCountryname());

    root.setOnClickListener(new MyOnClickListener(position));
  }

  private class MyOnClickListener implements OnClickListener {


    private int positon;

    public MyOnClickListener(int position) {
      this.positon = position;
    }

    @Override
    public void onClick(View v) {
      mListener.onItem(getItem(positon), positon);
    }
  }
}

package com.example.administrator.lubanone.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.example.administrator.lubanone.R;

/**
 * Created by admistrator on 2017/7/19.
 */

public class AddCardListView extends ListView {

  private Context mContext;
  private View mFootView;

  public AddCardListView(Context context) {
    super(context);
    this.mContext = context;
//    init();
  }

  public AddCardListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
//    init();
  }

  public AddCardListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.mContext = context;
//    init();
  }

  @TargetApi(VERSION_CODES.LOLLIPOP)
  public AddCardListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.mContext = context;
  }

  public void init() {
    LayoutInflater inflater = (LayoutInflater) mContext
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mFootView = inflater.inflate(R.layout.add_card_layout, null);
    addFooterView(mFootView);
  }

  public View getHeadView() {
    return mFootView;
  }
}

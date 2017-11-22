package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.bean.homepage.AlwaysProblesResulBean.ResultBean;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.TextUitl;
import java.util.List;

public class AlwaysProblemDetailsActivity extends BaseActivity {

  @BindView(R.id.iv_back)
  ImageView mIvBack;
  @BindView(R.id.tv_back)
  TextView mTvBack;
  @BindView(R.id.frameLayout)
  RelativeLayout mFrameLayout;
  @BindView(R.id.tv_title)
  TextView mTvTitle;
  @BindView(R.id.tv_content)
  TextView mTvContent;
  private ResultBean mItem;
  @BindView(R.id.tv_note)
  TextView tv_note;

  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_always_problem_details;
  }

  @Override
  public void initView() {

  }

  @Override
  public void loadData() {

    Intent intent = getIntent();
    if (null != intent) {
      Bundle item = intent.getBundleExtra("item");
      if (null != item) {
        mItem = (ResultBean) item.getSerializable("item");

        mTvTitle.setText("Q1、" + DebugUtils.convert(mItem.getTitle(), ""));

        List<String> content = mItem.getContent();
        StringBuilder builder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(content)) {
          for (int i = 0; i < content.size(); i++) {
            if (TextUitl.isEmpty(content.get(i))) {
              continue;
            }
            builder.append(content.get(i)).append("\n");
          }

        }

        mTvContent.setText(DebugUtils.convert(builder.toString(), ""));

        List<String> note = mItem.getNote();
        StringBuilder builderOne = new StringBuilder("注：\n");
        if (CollectionUtils.isNotEmpty(note)) {
          for (int i = 0; i < note.size(); i++) {
            if (TextUitl.isEmpty(note.get(i))) {
              continue;
            }
            builderOne.append(note.get(i)).append("\n");
          }
          tv_note.setText(builderOne.toString());
        }

      }
    }

  }

  @Override
  public void onClick(View v) {

  }

  @OnClick({R.id.iv_back, R.id.tv_back})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back:
      case R.id.tv_back:
        finish();
        break;
    }
  }
}

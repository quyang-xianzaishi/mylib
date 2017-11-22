package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.DreamFoundNewsBean.AboutlistBean;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.TextUitl;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

/**
 * Created by quyang on 2017/7/3.
 */

public class DreamFoundAdapter extends CustomAdapter<AboutlistBean> {

  private OnListViewItemListener mListener;

  public DreamFoundAdapter(Context context, List<AboutlistBean> list,
      OnListViewItemListener listener) {
    super(context, list);
    this.mListener = listener;
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.dream_found_item;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, AboutlistBean item, int position) {

    View root = viewHolder.getView(R.id.root);

    SimpleDraweeView ivIcon = viewHolder.getView(R.id.iv_icon);
    TextView tvTitle = viewHolder.getView(R.id.tv_title);
    TextView tvContent = viewHolder.getView(R.id.tv_content);

    if (TextUitl.isEmpty(item.getThumimg())) {
//      ivIcon.setBackgroundResource(R.mipmap.pho_tx);
    } else {
//      Glide.with(mContext).load(item.getThumimg()).diskCacheStrategy(DiskCacheStrategy.ALL)
//          .into(ivIcon);
//      GlideManager.glideWith4Round1(mContext, item.getThumimg(), ivIcon, 10);
      ivIcon.setImageURI(Uri.parse(item.getThumimg()));
    }

    tvTitle.setText(item.getTitle());
    tvContent.setText(item.getDatetime());

    root.setOnClickListener(new MyOnClickListener(item, position));

  }

  private class MyOnClickListener implements View.OnClickListener {

    private AboutlistBean mItem;
    private int mPosition;

    public MyOnClickListener(AboutlistBean item, int position) {
      this.mItem = item;
      this.mPosition = position;
    }

    @Override
    public void onClick(View v) {
      mListener.onItem(mItem, mPosition);
    }
  }
}

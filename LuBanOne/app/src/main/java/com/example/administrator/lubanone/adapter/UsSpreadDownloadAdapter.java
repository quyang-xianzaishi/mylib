package com.example.administrator.lubanone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.model.UsChildLVCommonBean;
import java.util.List;

/**
 * Created by hou on 2017/8/29.
 */

public class UsSpreadDownloadAdapter extends
    RecyclerView.Adapter<UsSpreadDownloadAdapter.MyViewHolder> {

  private Context context;
  private List<UsChildLVCommonBean.UsChildLvList> datas;

  public UsSpreadDownloadAdapter(Context context,
      List<UsChildLVCommonBean.UsChildLvList> datas) {
    this.context = context;
    this.datas = datas;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    MyViewHolder holder = new MyViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_us_spread_download, parent, false));

    return holder;
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    Glide.with(context).load(datas.get(position).getThumimg()).into(holder.image);
    holder.title.setText(datas.get(position).getTitle());
  }

  @Override
  public int getItemCount() {
    return datas.size();
  }

  public class MyViewHolder extends ViewHolder {

    private ImageView image;
    private TextView title;

    public MyViewHolder(View itemView) {
      super(itemView);
      image = (ImageView) itemView.findViewById(R.id.us_spread_download_item_image);
      title = (TextView) itemView.findViewById(R.id.us_spread_download_item_text);
    }
  }

}

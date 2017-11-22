package com.example.administrator.lubanone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;
import java.util.List;

/**
 * Created by hou on 2017/8/28.
 */

public class UsUploadImageAdapter extends RecyclerView.Adapter<UsUploadImageAdapter.MyViewHolder> {

  private List<String> datas;
  private Context mContext;

  public UsUploadImageAdapter(List<String> datas, Context mContext) {
    this.datas = datas;
    this.mContext = mContext;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    MyViewHolder holder = new MyViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.item_us_upload_image, parent, false));

    return holder;
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, final int position) {

    if (position == 9) {
      //9张图后隐藏添加按钮
      holder.imageAddLayout.setVisibility(View.GONE);
      holder.frameLayout.setVisibility(View.GONE);
    } else {
      //0-8的处理情况
      if (position == datas.size()) {
        //position为最后为一位的时候，显示添加按钮
        holder.imageAddLayout.setVisibility(View.VISIBLE);
        holder.frameLayout.setVisibility(View.GONE);
        holder.imageAddLayout.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            onAddImageListener.onAddImage();
          }
        });
      } else {
        //position为之前几位的时候，显示图片
        holder.imageAddLayout.setVisibility(View.GONE);
        holder.frameLayout.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(datas.get(position)).into(holder.image);
        holder.deleteBtn.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            onDeleteImageListener.onDeleteImage(position);
          }
        });
      }
    }

  }

  public interface OnDeleteImageListener {

    void onDeleteImage(int position);
  }

  public interface OnAddImageListener {

    void onAddImage();
  }

  private OnDeleteImageListener onDeleteImageListener;
  private OnAddImageListener onAddImageListener;

  public void setOnDeleteImageListener(OnDeleteImageListener onDeleteImageListener) {
    this.onDeleteImageListener = onDeleteImageListener;
  }

  public void setOnAddImageListener(OnAddImageListener onAddImageListener) {
    this.onAddImageListener = onAddImageListener;
  }

  @Override
  public int getItemCount() {
    return datas.size() + 1;
  }

  class MyViewHolder extends ViewHolder {

    ImageView image;
    ImageView deleteBtn;
    TextView mediaType;
    FrameLayout frameLayout;
    RelativeLayout imageAddLayout, videoAddLayout;

    public MyViewHolder(View itemView) {
      super(itemView);
      image = (ImageView) itemView.findViewById(R.id.item_us_spread_upload_image);
      deleteBtn = (ImageView) itemView.findViewById(R.id.item_us_spread_delete_image);
      mediaType = (TextView) itemView.findViewById(R.id.item_us_spread_media_type);
      frameLayout = (FrameLayout) itemView.findViewById(R.id.item_us_spread_frame_layout);
      imageAddLayout = (RelativeLayout) itemView.findViewById(R.id.item_us_spread_add_image_layout);
      videoAddLayout = (RelativeLayout) itemView.findViewById(R.id.item_us_spread_add_video_layout);
    }
  }

}

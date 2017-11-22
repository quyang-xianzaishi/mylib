package com.example.administrator.lubanone.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.ArrayList;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.R;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;

/**
 * Created by hou on 2017.7.14
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

  private ArrayList<String> photoPaths = new ArrayList<>();
  private LayoutInflater inflater;

  private boolean isPreview;
  private Context mContext;

  public final static int TYPE_ADD = 1;
  public final static int TYPE_PHOTO = 2;

  public final static int MAX = 9;

  public PhotoAdapter(Context mContext, ArrayList<String> photoPaths, boolean isPreview) {
    this.photoPaths = photoPaths;
    this.mContext = mContext;
    this.isPreview = isPreview;
    inflater = LayoutInflater.from(mContext);

  }


  @Override
  public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = null;
    switch (viewType) {
      case TYPE_ADD:
        itemView = inflater
            .inflate(com.example.administrator.lubanone.R.layout.task_upload_griditem_addpic,
                parent, false);
        itemView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            PhotoPicker.builder()
                .setPhotoCount(PhotoAdapter.MAX)
                .setShowCamera(true)
                .setPreviewEnabled(false)
                .setSelected(photoPaths)
                .start((Activity) mContext);
          }
        });
        break;
      case TYPE_PHOTO:
        itemView = inflater.inflate(R.layout.item_us_upload_image, parent, false);
        break;
    }
    return new PhotoViewHolder(itemView);
  }


  @Override
  public void onBindViewHolder(final PhotoViewHolder holder, final int position) {

    if (getItemViewType(position) == TYPE_PHOTO) {
      Uri uri = Uri.fromFile(new File(photoPaths.get(position)));

      boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.uploadImg.getContext());

      if (canLoadImage) {
        Glide.with(mContext)
            .load(uri)
            .centerCrop()
            .thumbnail(0.1f)
            .placeholder(R.drawable.__picker_ic_photo_black_48dp)
            .error(R.drawable.__picker_ic_broken_image_black_48dp)
            .into(holder.uploadImg);
      }
      if (isPreview) {
        holder.itemView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            PhotoPreview.builder()
                .setPhotos(photoPaths)
                .setCurrentItem(position)
                .start((Activity) mContext);
          }
        });
      }
      holder.detele.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          onDeleteImageListener.onDeleteImage(position);
        }
      });
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
    int count = photoPaths.size() + 1;
    if (count > MAX) {
      count = MAX;
    }
    return count;
  }

  @Override
  public int getItemViewType(int position) {
    return (position == photoPaths.size() && position != MAX) ? TYPE_ADD : TYPE_PHOTO;
  }

  public static class PhotoViewHolder extends RecyclerView.ViewHolder {

    /*private ImageView ivPhoto;
    private View vSelected;*/
    private ImageView uploadImg;
    private ImageView detele;

    public PhotoViewHolder(View itemView) {
      super(itemView);
      /*ivPhoto   = (ImageView) itemView.findViewById(R.id.iv_photo);
      vSelected = itemView.findViewById(R.id.v_selected);
      if (vSelected != null) vSelected.setVisibility(View.GONE);*/
      uploadImg = (ImageView) itemView.findViewById(R.id.item_us_spread_upload_image);
      detele = (ImageView) itemView.findViewById(R.id.item_us_spread_delete_image);
    }
  }

}

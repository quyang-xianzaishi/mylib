package com.example.qlibrary.image.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.qlibrary.R;
import java.io.File;

/**
 * 图片加载管理累
 * Created by Administrator on 2017/6/17.
 */

public class GlideManager {

  private static GlideManager instance;

  private GlideManager() {
  }

  public static GlideManager getInstance() {
    if (null == instance) {
      synchronized (GlideManager.class) {
        if (null == instance) {
          instance = new GlideManager();
        }
      }
    }
    return instance;
  }


  /**
   * @param roundDp 圆角dp
   */
  public static void glideDisplayImage(String url, Context context, ImageView imageView,
      int roundDp) {
    glideWithRound(context.getApplicationContext(), url, imageView, roundDp);
  }


  private static void glide(Context context, String url, ImageView imageView) {
    Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.em_chat_video_call_normal)//加载前显示
        .error(R.drawable.head_2x)//加载失败后显示
        .into(imageView);
  }

  /**
   * @param dp 圆角dp
   */
  public static void glideWithRound(Context context, String url, ImageView imageView, int dp) {
    Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .error(R.drawable.head_2x)//加载失败后显示
        .transform(new GlideRoundTransform(context, dp))//圆角dp，glide版本不能是4.0
        .into(imageView);
  }

  /**
   * @param dp 圆角dp
   */
  public static void glideWithRound(Context context, File iconFile, ImageView imageView, int dp) {
    Glide.with(context)
        .load(iconFile)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .error(R.drawable.head_2x)//加载失败后显示
        .transform(new GlideRoundTransform(context, dp))//圆角dp，glide版本不能是4.0
        .into(imageView);
  }


  /**
   * 将图片加载位4角位圆角
   */
  public static void glideWith4Round(final Context context, String url, final ImageView iv,
      final float roundPx) {
    Glide.with(context)
        .load(url)
        .asBitmap()
        .error(R.drawable.em_actionbar_camera_icon)
//                .placeholder(R.drawable.em_add_public_group)
        .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
        .into(new BitmapImageViewTarget(iv) {
          @Override
          protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                        circularBitmapDrawable.setCircular(true);
            circularBitmapDrawable.setCornerRadius(roundPx);
            iv.setImageDrawable(circularBitmapDrawable);
          }
        });
  }

  /**
   * 将图片加载位4角位圆角
   */
  public static void glideWith4Round1(final Context context, String url, final ImageView iv,
      final float roundPx) {
    Glide.with(context)
        .load(url)
        .asBitmap()
        .error(R.drawable.em_actionbar_camera_icon)
//                .placeholder(R.drawable.em_add_public_group)
        .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
        .transform(new RoundedCornersTransformation(context, (int) roundPx, 0))
        .into(iv);
  }

}

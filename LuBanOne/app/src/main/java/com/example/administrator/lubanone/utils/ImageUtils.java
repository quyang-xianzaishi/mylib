package com.example.administrator.lubanone.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by hou on 2017/7/19.
 */

public class ImageUtils {


  /**
   * 缩小图片的长宽来压缩图片
   */
  public static Bitmap decodeSampledBitmapFromFilePath(String pathName, int reqWidth,
      int reqHeight) {
    BitmapFactory.Options options = new Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(pathName, options);
    options.inSampleSize = calculateInsampleSize(options, reqWidth, reqHeight);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(pathName, options);
  }

  /**
   * 计算inSampleSize值
   *
   * @param options 用于获取原图的长宽
   * @param reqWidth 要求压缩后的图片宽度
   * @param reqHeight 要求压缩后的图片长度
   */
  public static int calculateInsampleSize(BitmapFactory.Options options, int reqWidth,
      int reqHeight) {
    int height = options.outHeight;
    int width = options.outWidth;
    int inSampleSize = 1;
    if (height > reqHeight || width > reqWidth) {

      int halfHeight = height / 2;
      int halfWidth = width / 2;

      while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
        inSampleSize *= 2;
      }
    }
    return inSampleSize;
  }

  public static Bitmap compressBitmap(Bitmap bitmap, long sizeLimit) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int quality = 100;
    /**
     * CompressFormat 指定的Bitmap被压缩成的图片格式，只支持JPEG，PNG，WEBP三种
     * quality 图片压缩质量的控制，范围为0~100，0表示压缩后体积最小，但是质量也是最差，100表示压缩后体积最大，但是质量也是最好的（个人认为相当于未压缩），有些格式，例如png，它是无损的，所以会忽略这个值。
     * OutputStream 压缩后的数据会写入这个字节流中
     * 返回值表示返回的字节流是否可以使用BitmapFactory.decodeStream()解码成Bitmap，至于返回值是怎么得到的，因为是Native的代码，没法找到逻辑。
     */
    bitmap.compress(CompressFormat.JPEG, quality, baos);
    //循环判断压缩后图片是否超过限制大小
    while (baos.toByteArray().length / 1024 > sizeLimit) {
      //清空baos
      baos.reset();
      bitmap.compress(CompressFormat.JPEG, quality, baos);
      quality -= 10;
    }
    Bitmap newBitmap = BitmapFactory
        .decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);

    return newBitmap;
  }

}

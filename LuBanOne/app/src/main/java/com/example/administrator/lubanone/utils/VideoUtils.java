package com.example.administrator.lubanone.utils;

import static com.yalantis.ucrop.util.FileUtils.getDataColumn;
import static com.yalantis.ucrop.util.FileUtils.isDownloadsDocument;
import static com.yalantis.ucrop.util.FileUtils.isExternalStorageDocument;
import static com.yalantis.ucrop.util.FileUtils.isMediaDocument;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by hou on 2017/7/10.
 */
@SuppressLint("NewApi")
public class VideoUtils {

  public static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_img/";

  //得到视频缩略图
  public static Bitmap getVideoThumbnail(String filePath) {
    Bitmap bitmap = null;
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    try {
      retriever.setDataSource(filePath);
      bitmap = retriever.getFrameAtTime();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (RuntimeException e) {
      e.printStackTrace();
    } finally {
      try {
        retriever.release();
      } catch (RuntimeException e) {
        e.printStackTrace();
      }
    }
    return bitmap;
  }

  //获取视频时长
  public static String getDuration(String path) {
    String duration;
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    retriever.setDataSource(path);
    duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    return duration;
  }

  //保存图片到本地
  public static void saveFile(Bitmap bm, String imgName) {
    File dirFile = new File(ALBUM_PATH);
    if (!dirFile.exists()) {
      dirFile.mkdir();
    }

    File myFile = new File(ALBUM_PATH + imgName);
    try {
      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myFile));
      bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
      bos.flush();
      bos.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  /**
   * Get a file path from a Uri. This will get the the path for Storage Access
   * Framework Documents, as well as the _data field for the MediaStore and
   * other file-based ContentProviders.
   *
   * @param context The context.
   * @param uri The Uri to query.
   */
  public static String getPath(final Context context, final Uri uri) {

    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
      // ExternalStorageProvider
      if (isExternalStorageDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
          return Environment.getExternalStorageDirectory() + "/" + split[1];
        }

        // TODO handle non-primary volumes
      }
      // DownloadsProvider
      else if (isDownloadsDocument(uri)) {

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        return getDataColumn(context, contentUri, null, null);
      }
      // MediaProvider
      else if (isMediaDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[] {
            split[1]
        };

        return getDataColumn(context, contentUri, selection, selectionArgs);
      }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {
      return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
      return uri.getPath();
    }

    return null;
  }


}

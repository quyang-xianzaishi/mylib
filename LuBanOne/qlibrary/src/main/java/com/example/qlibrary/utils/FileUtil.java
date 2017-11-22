package com.example.qlibrary.utils;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by admistrator on 2017/8/4.
 */

public class FileUtil {

  private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

  /**
   * 计算文件大小 单位kb
   */
  public static long fileKSize(File file) {
    if (null == file) {
      return 0;
    }
    return file.length() / 1024;
  }

  /**
   * 计算文件大小 单位Mb
   */
  public static long fileMSize(File file) {
    if (null == file) {
      return 0;
    }
    return file.length() / 1024 / 1024;
  }


  /**
   * 批量压缩文件（夹）
   *
   * @param resFileList 要压缩的文件（夹）列表
   * @param zipFile 生成的压缩文件
   * @param comment 压缩文件的注释
   * @throws IOException 当压缩过程出错时抛出
   */
  public static void zipFiles(Collection<File> resFileList, File zipFile, String comment)
      throws IOException {
    ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
        zipFile), BUFF_SIZE));
    for (File resFile : resFileList) {
      zipFile(resFile, zipout, "");
    }
    zipout.setComment(comment);
    zipout.close();
  }

  /**
   * 压缩文件
   *
   * @param resFile 需要压缩的文件（夹）
   * @param zipout 压缩的目的文件
   * @param rootpath 压缩的文件路径
   * @throws FileNotFoundException 找不到文件时抛出
   * @throws IOException 当压缩过程出错时抛出
   */
  private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath)
      throws FileNotFoundException, IOException {
    rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator)
        + resFile.getName();
    rootpath = new String(rootpath.getBytes(), "utf-8");
    if (resFile.isDirectory()) {
      File[] fileList = resFile.listFiles();
      for (File file : fileList) {
        zipFile(file, zipout, rootpath);
      }
    } else {
      byte buffer[] = new byte[BUFF_SIZE];
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile),
          BUFF_SIZE);
      zipout.putNextEntry(new ZipEntry(rootpath));
      int realLength;
      while ((realLength = in.read(buffer)) != -1) {
        zipout.write(buffer, 0, realLength);
      }
      in.close();
      zipout.flush();
      zipout.closeEntry();
    }
  }


  public static void zip(String[] files, File zipFile) throws IOException {
    BufferedInputStream origin = null;
    ZipOutputStream out = new ZipOutputStream(
        new BufferedOutputStream(new FileOutputStream(zipFile)));
    try {
      byte data[] = new byte[BUFF_SIZE];

      for (int i = 0; i < files.length; i++) {
        FileInputStream fi = new FileInputStream(files[i]);
        origin = new BufferedInputStream(fi, BUFF_SIZE);
        try {
          ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
          out.putNextEntry(entry);
          int count;
          while ((count = origin.read(data, 0, BUFF_SIZE)) != -1) {
            out.write(data, 0, count);
          }
        } finally {
          origin.close();
        }
      }
    } finally {
      out.close();
    }
  }

  public static void unzip(String zipFile, String location) throws IOException {
    try {
      File f = new File(location);
      if (!f.isDirectory()) {
        f.mkdirs();
      }
      ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
      try {
        ZipEntry ze = null;
        while ((ze = zin.getNextEntry()) != null) {
          String path = location + ze.getName();

          if (ze.isDirectory()) {
            File unzipFile = new File(path);
            if (!unzipFile.isDirectory()) {
              unzipFile.mkdirs();
            }
          } else {
            FileOutputStream fout = new FileOutputStream(path, false);
            try {
              for (int c = zin.read(); c != -1; c = zin.read()) {
                fout.write(c);
              }
              zin.closeEntry();
            } finally {
              fout.close();
            }
          }
        }
      } finally {
        zin.close();
      }
    } catch (Exception e) {
      Log.e("TAG", "Unzip exception", e);
    }
  }


  /**
   * 创建图片的存储file
   */
  public static File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES);
    if (null == storageDir || !storageDir.exists()) {
      storageDir = Environment.getExternalStoragePublicDirectory(
          Environment.DIRECTORY_DCIM);
    }

    File image = File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */
    );

    // Save a file: path for use with ACTION_VIEW intents
//    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
    return image;
  }
}

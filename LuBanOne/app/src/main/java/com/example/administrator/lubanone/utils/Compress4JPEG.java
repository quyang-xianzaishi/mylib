package com.example.administrator.lubanone.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import com.example.administrator.lubanone.interfaces.LccImageCallback;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hou on 17/6/27.
 * 自制压缩算法,压缩成jpeg格式。
 */

public class Compress4JPEG {

  private static volatile Compress4JPEG INSTANCE;
  public static String DEFAULT_DISK_CACHE_DIR = "luban";
  private final File mCacheDir;
  private static final String TAG = "YYYYYYYc";
  //    private OnCompressListener compressListener;
  private File mFile;
  private ArrayList<File> mImageFiles = new ArrayList<>();
  private LccImageCallback lccCallback;

  Compress4JPEG(File cacheDir) {
    mCacheDir = cacheDir;
  }

  public static Compress4JPEG get(Context context) {
    if (INSTANCE == null) {
      INSTANCE = new Compress4JPEG(Compress4JPEG.getPhotoCacheDir(context));
    }
    return INSTANCE;
  }

  public static File getPhotoCacheDir(Context context) {
    return getPhotoCacheDir(context, Compress4JPEG.DEFAULT_DISK_CACHE_DIR);
  }

  public Compress4JPEG setCompressListener(LccImageCallback listener) {
    lccCallback = listener;
    return this;
  }

  /**
   * 压缩后图片存储路径
   */
  public static File getPhotoCacheDir(Context context, String cacheName) {
    File cacheDir = Environment.getExternalStorageDirectory();
    if (cacheDir != null) {
      File result = new File(cacheDir, cacheName);
      if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
        // File wasn't able to create a directory, or the result exists but not a directory
        return null;
      }
      return result;
    }
    if (Log.isLoggable(TAG, Log.ERROR)) {
      Log.e(TAG, "default disk cache dir is null");
    }
    return null;
  }


  public Compress4JPEG loadsFiles(ArrayList<File> files) {
    mImageFiles = files;
    return this;
  }

  public Compress4JPEG load(File file) {
    mFile = file;
    mImageFiles.add(file);
    return this;
  }


  public Compress4JPEG launch() {
//        checkNotNull(mImageFiles, "the image file cannot be null, please call .load() before this method!");
    if (lccCallback != null) {
      lccCallback.onStart();
    }

    if (mImageFiles != null && mImageFiles.size() > 0) {
      Observable.from(mImageFiles)
          .flatMap(new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File bean) {
              return createObservable(bean);
            }
          })
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
              if (lccCallback != null) {
                lccCallback.onError(throwable);
              }
            }
          })
          .onErrorResumeNext(Observable.<File>empty())
          .filter(new Func1<File, Boolean>() {
            @Override
            public Boolean call(File file) {
              return file != null;
            }
          })
          .subscribe(new Action1<File>() {
            @Override
            public void call(File file) {
              if (lccCallback != null) {
                lccCallback.onFileSuccess(file);
              }
            }
          });
    }

    return this;
  }

  private Observable<File> createObservable(final File bean) {
    return Observable.create(new Observable.OnSubscribe<File>() {
      @Override
      public void call(Subscriber<? super File> subscriber) {
        File file = compressImg(bean);
        subscriber.onNext(file);
        subscriber.onCompleted();
      }
    });
  }


  //设置压缩参数
  private File compressImg(@NonNull File file) {
    String thumb = mCacheDir.getAbsolutePath() + "/" + randomString(8) + ".jpg";

    String filePath = file.getAbsolutePath();

    int angle = getImageSpinAngle(filePath);
    int width = getImageSize(filePath)[0];
    int height = getImageSize(filePath)[1];
    if (height > width) {
      if (height > 1280 && height / width < 2) {
        width = width * 1280 / height;
        return webpcompress(filePath, thumb, width, 1280, angle);
      } else if (height < 1280) {
        return webpcompress(filePath, thumb, height, width, angle);
      } else {
        return webpcompress(filePath, thumb, height, width, angle);
      }


    } else {
      if (width > 1280 && width / height < 2) {
        height = height * 1280 / width;
        return webpcompress(filePath, thumb, 1280, height, angle);
      } else if (width < 1280) {
        return webpcompress(filePath, thumb, height, width, angle);
      } else {
        return webpcompress(filePath, thumb, height, width, angle);
      }

    }
  }


  public int[] getImageSize(String imagePath) {
    int[] res = new int[2];

    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    options.inSampleSize = 1;
    BitmapFactory.decodeFile(imagePath, options);

    res[0] = options.outWidth;
    res[1] = options.outHeight;

    return res;
  }

  private Bitmap compress(String imagePath, int width, int height) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(imagePath, options);

    int outH = options.outHeight;
    int outW = options.outWidth;
    int inSampleSize = 1;

    if (outH > height || outW > width) {
      int halfH = outH / 2;
      int halfW = outW / 2;

      while ((halfH / inSampleSize) > height && (halfW / inSampleSize) > width) {
        inSampleSize *= 2;
      }
    }

    options.inSampleSize = inSampleSize;

    options.inJustDecodeBounds = false;

    int heightRatio = (int) Math.ceil(options.outHeight / (float) height);
    int widthRatio = (int) Math.ceil(options.outWidth / (float) width);

    if (heightRatio > 1 || widthRatio > 1) {
      if (heightRatio > widthRatio) {
        options.inSampleSize = heightRatio;
      } else {
        options.inSampleSize = widthRatio;
      }
    }
    options.inJustDecodeBounds = false;

    return BitmapFactory.decodeFile(imagePath, options);
  }

  private int getImageSpinAngle(String path) {
    int degree = 0;
    try {
      ExifInterface exifInterface = new ExifInterface(path);
      int orientation = exifInterface
          .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
      switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
          degree = 90;
          break;
        case ExifInterface.ORIENTATION_ROTATE_180:
          degree = 180;
          break;
        case ExifInterface.ORIENTATION_ROTATE_270:
          degree = 270;
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return degree;
  }

  /**
   * 指定参数压缩图片
   * create the thumbnail with the true rotate angle
   *
   * @param largeImagePath the big image path
   * @param thumbFilePath the thumbnail path
   * @param width width of thumbnail
   * @param height height of thumbnail
   * @param angle rotation angle of thumbnail
   */


  private File webpcompress(String largeImagePath, String thumbFilePath, int width, int height,
      int angle) {
    Bitmap thbBitmap = zoomImage(largeImagePath, width, height, angle);
    return savewebpImage(thumbFilePath, thbBitmap);
  }

  public Bitmap zoomImage(String largeImagePath, double newWidth, double newHeight, int angle) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(largeImagePath, options);

    int outH = options.outHeight;
    int outW = options.outWidth;
    if (outH > outW) {
      if (outH > 2560) {
        options.inSampleSize = 2;
        if (outH > 5120) {
          options.inSampleSize = 4;
        }
      }
    } else {
      if (outW > 2560) {
        options.inSampleSize = 2;
        if (outW > 5120) {
          options.inSampleSize = 4;
        }
      }
    }
    options.inJustDecodeBounds = false;
//    options.inPreferredConfig = Bitmap.Config.RGB_565;
    Bitmap bgimage = BitmapFactory.decodeFile(largeImagePath, options);
    Log.d("加载图片所需内存", "" + bgimage.getRowBytes() * bgimage.getHeight());
    // 获取这个图片的宽和高
    float width = bgimage.getWidth();
    float height = bgimage.getHeight();
    // 创建操作图片用的matrix对象
    Matrix matrix = new Matrix();
    // 计算宽高缩放率
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    // 缩放图片动作
    matrix.postScale(scaleWidth, scaleHeight);
    matrix.postRotate(angle);

    Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);

    return bitmap;
  }


  /**
   * 旋转图片
   * rotate the image with specified angle
   *
   * @param angle the angle will be rotating 旋转的角度
   * @param bitmap target image               目标图片
   */
  private static Bitmap rotatingImage(int angle, Bitmap bitmap) {
    //rotate image
    Matrix matrix = new Matrix();
    matrix.postRotate(angle);

    //create a new image
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
  }

  /**
   * 保存图片到指定路径
   * Save image with specified size
   *
   * @param filePath the image file save path 储存路径
   * @param bitmap the image what be save   目标图片
   */
  private File savewebpImage(String filePath, Bitmap bitmap) {
//        YcLog.d("savewebpImage 111===========>", System.currentTimeMillis() + "   ");

    File result = new File(filePath.substring(0, filePath.lastIndexOf("/")));

    if (!result.exists() && !result.mkdirs()) {
      return null;
    }

    ByteArrayOutputStream stream = new ByteArrayOutputStream();

    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

    try {
      FileOutputStream fos = new FileOutputStream(filePath);
      fos.write(stream.toByteArray());
      fos.flush();
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new File(filePath);
  }


  /**
   * 产生随机字符串
   */
  private static Random randGen = null;
  private static char[] numbersAndLetters = null;

  public static final String randomString(int length) {
    if (length < 1) {
      return null;
    }
    if (randGen == null) {
      randGen = new Random();
      numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
          "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
    }
    char[] randBuffer = new char[length];
    for (int i = 0; i < randBuffer.length; i++) {
      randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
      //randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
    }
    return new String(randBuffer);
  }

}

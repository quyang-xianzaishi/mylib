package com.example.administrator.lubanone.activity.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.R;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

  private static final int REQUEST_ALBUM_OK = 1;
  @BindView(R.id.view_pager)
  ViewPager mViewPager;

  private List<String> mList = Arrays
      .asList("http://192.168.3.208:8080/a2.jpg", "http://192.168.3.208:8080/a3.jpg",
          "http://192.168.3.208:8080/a4.jpg", "http://192.168.3.208:8080/a.JPG");


  // 创建Handler消息机制,让viewpager动起来
  Handler handler = new Handler() {
    private int next;

    public void handleMessage(android.os.Message msg) {
      int currentItem = mViewPager.getCurrentItem();
      next = currentItem + 1;
      if (next > mViewPager.getAdapter().getCount() - 1) {
        next = 0;
      }
      mViewPager.setCurrentItem(next, true);

      // 重新发送延时消息,使viewpager不停的动起来
      handler.sendEmptyMessageDelayed(0, 2000);

    }
  };
  private ImageView mIv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);
    ButterKnife.bind(this);
//    initViewPager();

    mIv = (ImageView) findViewById(R.id.iv);

    Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
    albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    startActivityForResult(albumIntent, REQUEST_ALBUM_OK);


  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_ALBUM_OK:
        try {
          Uri dataData = data.getData();

          Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "SampleCropImage.jpeg"));
          UCrop.Options options = new UCrop.Options();
          options.setMaxScaleMultiplier(5);
          options.setCompressionQuality(70);
          options.setCompressionFormat(CompressFormat.JPEG);

          UCrop.of(dataData, destinationUri).withOptions(options).withAspectRatio(1,1).withMaxResultSize(500,500)
              .start(this);
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      default:
        break;

    }

    if (resultCode == RESULT_OK) {
      //裁切成功
      if (requestCode == UCrop.REQUEST_CROP) {
        Uri croppedFileUri = UCrop.getOutput(data);
        //获取默认的下载目录
        String downloadsDirectoryPath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(),
            croppedFileUri.getLastPathSegment());
        File saveFile = new File(downloadsDirectoryPath, filename);
        //保存下载的图片
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
          inStream = new FileInputStream(new File(croppedFileUri.getPath()));
          outStream = new FileOutputStream(saveFile);
          inChannel = inStream.getChannel();
          outChannel = outStream.getChannel();
          inChannel.transferTo(0, inChannel.size(), outChannel);

          Bitmap bitmap = BitmapFactory.decodeFile(saveFile.getPath());
          int i = bitmap.getRowBytes() * bitmap.getHeight();
          System.out.println("quyang=" + i/1024);

          Glide.with(this).load(saveFile).into(mIv);
          Toast.makeText(this, "裁切后的图片保存在：" + saveFile.getAbsolutePath(), Toast.LENGTH_LONG)
              .show();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          try {
            outChannel.close();
            outStream.close();
            inChannel.close();
            inStream.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    //裁切失败
    if (resultCode == UCrop.RESULT_ERROR) {
      Toast.makeText(this, "裁切图片失败", Toast.LENGTH_LONG).show();
    }

  }


  private void initViewPager() {
    // 发送延时消息
    mViewPager.setAdapter(new PagerAdapter() {

      @Override
      public int getCount() {
        return Integer.MAX_VALUE;
      }

      @Override
      public boolean isViewFromObject(View view, Object object) {
        return view == object;
      }

      @Override
      public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
      }

      @Override
      public Object instantiateItem(ViewGroup container, int position) {
        position = position % mList.size(); //获得数据中的每一项
        ImageView iv = new ImageView(Main2Activity.this);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(Main2Activity.this).load(mList.get(position)).diskCacheStrategy(
            DiskCacheStrategy.ALL).into(iv);
        container.addView(iv);

        return iv;
      }
    });

    // 发送延时消息
    handler.removeMessages(0);// 发行前先移除以前的延时消息
    handler.sendEmptyMessageDelayed(0, 2000);
  }
}

package com.example.administrator.lubanone.activity.task;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.utils.TaskProcessImageUtils;

/**
 * Created by hou on 2017/6/27.
 */

public class TaskUploadAddPicProcessActivity extends BaseActivity {

    private TextView addPic;                         //添加图片按钮
    private ImageView imageShow;                      //显示图片
    private Bitmap bmp;                                 //载入图片
    private Bitmap mbmp;                                 //复制模板
    private Bitmap alteredBitmap;                       //备份图片
    private Canvas canvas;                                 //画布
    private Paint paint;                                       //画刷
    private TaskProcessImageUtils imageUtils= null;//自定义引用图像处理类
    private Matrix matrix = new Matrix();

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.task_upload_add_process;
    }

    @Override
    public void initView() {

        addPic = (TextView) findViewById(R.id.task_upload_process_add_pic);
        imageShow = (ImageView) findViewById(R.id.task_upload_process_imageView);

        addPic.setOnClickListener(this);
        //载入数据
        Intent intent = getIntent();
        //Toast.makeText(this, "传递参数", Toast.LENGTH_SHORT).show();
        String path = intent.getStringExtra("path"); //对应putExtra("path", path);
        //自定义函数 显示图片
        ShowPhotoByImageView(path);
        imageUtils = new TaskProcessImageUtils(bmp);

    }

    /*
     * 函数功能 显示图片
	 * 参数 String path 图片路径,源自MainActivity选择传参
	 */
    private void ShowPhotoByImageView(String path) {
        if (null == path) {
            Toast.makeText(this, "载入图片失败", Toast.LENGTH_LONG).show();
            finish();
        }
		/*
		 * 问题:
		 * 获取Uri不知道getStringExtra()没对应uri参数
		 * 使用方法Uri uri=Uri.parse(path)获取路径不能显示图片
		 * mBitmap=BitmapFactory.decodeFile(path)方法不能适应大小
		 * 解决:
		 * decodeFile(path,opts)函数可以实现
		 */
        //获取分辨率
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;    //屏幕水平分辨率
        int height = dm.heightPixels;  //屏幕垂直分辨率
        try {
            //Load up the image's dimensions not the image itself
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            bmp = BitmapFactory.decodeFile(path, bmpFactoryOptions);
            int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
            int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);
            //压缩显示
            if (heightRatio > 1 && widthRatio > 1) {
                if (heightRatio > widthRatio) {
                    bmpFactoryOptions.inSampleSize = heightRatio * 2;
                } else {
                    bmpFactoryOptions.inSampleSize = widthRatio * 2;
                }
            }
            //图像真正解码
            bmpFactoryOptions.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeFile(path, bmpFactoryOptions);
            mbmp = bmp.copy(Bitmap.Config.RGB_565, true);
            imageShow.setImageBitmap(bmp); //显示照片
	        /*
	         * [失败] 动态设置属性
	         当设置android:scaleType="matrix"后图像显示左上角
	         设置图片居中 起点=未使用屏幕/2=(屏幕分辨率-图片宽高)/2
	         int widthCenter=imageShow.getWidth()/2-bmp.getWidth()/2;
	         int heightCenter=imageShow.getHeight()/2-bmp.getHeight()/2;
	         Matrix matrix = new Matrix();
	         matrix.postTranslate(widthCenter, heightCenter);
	         imageShow.setImageMatrix(matrix);
	         imageShow.setImageBitmap(bmp);
	         */
            //加载备份图片 绘图使用
            alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp
                    .getHeight(), bmp.getConfig());
            canvas = new Canvas(alteredBitmap);  //画布
            paint = new Paint(); //画刷
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(5);
            paint.setTextSize(30);
            paint.setTypeface(Typeface.DEFAULT_BOLD);  //无线粗体
            matrix = new Matrix();
            canvas.drawBitmap(bmp, matrix, paint);
            //imageShow.setImageBitmap(alteredBitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.task_upload_process_add_pic:
                try {
                    if(mbmp == null) { //防止出现mbmp空
                        mbmp = bmp;
                    }
                    //图像添加 先保存 后传递图片路径
                    Uri uri = imageUtils.loadBitmap(mbmp);
                    Intent intent  = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(uri);
                    sendBroadcast(intent);
                    //添加图片*
                    Intent intentPut = new Intent(TaskUploadAddPicProcessActivity.this, TaskUploadImgActivity.class);
                    String pathImage = null;
                    intentPut.putExtra("pathProcess", imageUtils.pathPicture );
	    				/*
	    				 * 返回活动使用setResult 使用startActivity总是显示一张图片并RunTime
	    				 * startActivity(intentPut);
	    				 * 在onActivityResult中获取数据
	    				 */
                    setResult(RESULT_OK, intentPut);
                    //返回上一界面
                    Toast.makeText(TaskUploadAddPicProcessActivity.this, "图片添加成功" , Toast.LENGTH_LONG).show();
                    TaskUploadAddPicProcessActivity.this.finish();
                } catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(TaskUploadAddPicProcessActivity.this, "图像添加失败", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

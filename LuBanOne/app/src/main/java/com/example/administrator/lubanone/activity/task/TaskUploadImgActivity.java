package com.example.administrator.lubanone.activity.task;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.qlibrary.utils.ToastUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hou on 2017/6/26.
 */

public class TaskUploadImgActivity extends BaseActivity {

    private LinearLayout backBtn;
    private TextView commitBtn;
    private GridView gridView;//网格显示缩略图
    private final int IMAGE_OPEN = 1;//打开图片标记
    private final int GET_DATA = 2;//获取处理后图片标记
    private final int TAKE_PHOTO = 3;//拍照标记
    private String pathImage;//选择图片路径
    private Bitmap bmp;                             //导入临时图片
    private Uri imageUri;                            //拍照Uri
    private String pathTakePhoto;              //拍照路径
    private ProgressDialog mpDialog;         //进度对话框
    private int count = 0;                           //计算上传图片个数 线程调用
    private int flagThread = 0;                    //线程循环标记变量 否则会上个线程没执行完就进行下面的
    private int flagThreadUpload = 0;         //上传图片控制变量
    private int flagThreadDialog = 0;          //对话框标记变量

    //获取图片上传URL路径 文件夹名+时间命名图片
    private String[] urlPicture;
    //存储Bmp图像
    private ArrayList<HashMap<String, Object>> imageItem;
    //适配器
    private SimpleAdapter simpleAdapter;
    //插入PublishId通过Json解析
    private String publishIdByJson;

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_task_upload_img;
    }

    @Override
    public void initView() {

//        gridView = (GridView) findViewById(R.id.task_upload_image_gv);
        backBtn = (LinearLayout) findViewById(R.id.task_upload_image_back_icon);
        commitBtn = (TextView) findViewById(R.id.task_upload_picture_commit);
        commitBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);


        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.task_upload_griditem_addpic为布局
         */
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.task_upload_gridview_addpic); //加号
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        map.put("pathImage", "add_pic");
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.task_upload_griditem_addpic,
                new String[]{"itemImage"}, new int[]{R.id.task_upload_griditem_imageView});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(simpleAdapter);
        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imageItem.size() == 10 && position == 0) { //第一张为默认图片
                    ToastUtil.showShort("图片数9张已满", mContext);
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
                    //Toast.makeText(mContext, "添加图片", Toast.LENGTH_SHORT).show();
                    AddImageDialog();
                } else {
                    DeleteDialog(position);
                    //Toast.makeText(mContext, "点击第" + (position + 1) + " 号图片",
                    //		Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //获取图片路径 响应startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[]{MediaStore.Images.Media.DATA},
                        null,
                        null,
                        null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                String path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                //向处理活动传递数据
                //Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, TaskUploadAddPicProcessActivity.class); //主活动->处理活动
                intent.putExtra("path", path);
                //startActivity(intent);
                startActivityForResult(intent, GET_DATA);
            } else {
                Intent intent = new Intent(this, TaskUploadAddPicProcessActivity.class); //主活动->处理活动
                intent.putExtra("path", uri.getPath());
                //startActivity(intent);
                startActivityForResult(intent, GET_DATA);
            }
        }  //end if 打开图片
        //获取图片
        if (resultCode == RESULT_OK && requestCode == GET_DATA) {
            //获取传递的处理图片在onResume中显示
            pathImage = data.getStringExtra("pathProcess");
        }
        //拍照
        if (resultCode == RESULT_OK && requestCode == TAKE_PHOTO) {
            Intent intent = new Intent("com.android.camera.action.CROP"); //剪裁
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            //广播刷新相册
            Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intentBc.setData(imageUri);
            this.sendBroadcast(intentBc);
            //向处理活动传递数据
            Intent intentPut = new Intent(this, TaskUploadAddPicProcessActivity.class); //主活动->处理活动
            intentPut.putExtra("path", pathTakePhoto);
            //startActivity(intent);
            startActivityForResult(intentPut, GET_DATA);
        }
    }

    /*
     * 添加图片 可通过本地添加、拍照添加
     */
    protected void AddImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskUploadImgActivity.this);
        builder.setTitle("添加图片");
        builder.setIcon(R.drawable.ic_launcher);
        builder.setCancelable(false); //不响应back按钮
        builder.setItems(new String[]{"本地相册选择", "手机相机添加", "取消选择图片"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0: //本地相册
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, IMAGE_OPEN);
                                //通过onResume()刷新数据
                                break;
                            case 1: //手机相机
                                dialog.dismiss();
                                File outputImage = new File(Environment.getExternalStorageDirectory(), "suishoupai_image.jpg");
                                pathTakePhoto = outputImage.toString();
                                try {
                                    if (outputImage.exists()) {
                                        outputImage.delete();
                                    }
                                    outputImage.createNewFile();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                imageUri = Uri.fromFile(outputImage);
                                Intent intentPhoto = new Intent("android.media.action.IMAGE_CAPTURE"); //拍照
                                intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intentPhoto, TAKE_PHOTO);
                                break;
                            case 2: //取消添加
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
        //显示对话框
        builder.create().show();
    }

    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void DeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskUploadImgActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.task_upload_image_back_icon:
                finish();
                break;
            case R.id.task_upload_picture_commit:
                ToastUtil.showShort("提交",mContext);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取传递的处理图片在onResume中显示
        //Intent intent = getIntent();
        //pathImage = intent.getStringExtra("pathProcess");
        //适配器动态显示图片
        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            map.put("pathImage", pathImage);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.task_upload_griditem_addpic,
                    new String[]{"itemImage"}, new int[]{R.id.task_upload_griditem_imageView});
            //接口载入图片
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放,防止手机休眠后自动添加
            pathImage = null;
        }
    }

    /*
        * 开启上传图片的线程
        * 第一个参数是文件完整路径（包括文件名）
        * 第二个参数是要放在服务器哪个文件夹下
        */
    private void upload_SSP_Pic(final String path, final String dirname) {
    }

    /*
     * 插入表 参数SQL语句 Type=1表示插入 2查询
     */
    private void SavePublish(final String type, final String sqlexe) {
    }

    /*
     * 解析SQL查询数据
     */
    private void jsonjiexi(String jsondata) {
    }


  	/*
       * End
  	 */
}

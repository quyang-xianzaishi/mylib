package com.example.administrator.lubanone.activity.home;


import android.Manifest;
import android.Manifest.permission;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.BaseActivity;
import com.example.administrator.lubanone.activity.land.LandActivity;
import com.example.administrator.lubanone.activity.register.MainActivity;
import com.example.administrator.lubanone.bean.homepage.AccountCenterResultBean;
import com.example.administrator.lubanone.bean.message.User;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.administrator.lubanone.utils.UriUtils;
import com.example.administrator.lubanone.utils.VideoUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.image.glide.GlideManager;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;

/**
 * 账户中心
 */
public class AccountCenterActivity extends BaseActivity {

  private static final int REQUEST_ALBUM_OK = 123;
  private static final int REQUEST_CAMERA = 124;
  private static final int CAMERA_CODE = 125;


  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      startNewActivityThenFinish(AccountCenterActivity.this, MainActivity.class);
    }
  };

  //上传头像
  private RequestListener mUploadListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Log.e("AccountCenterActivity", "testSuccess=" + jsonObject);
        Result<String> result = GsonUtil
            .processJson(jsonObject, String.class);
        uploadResult(result);
      } catch (Exception e) {
        dismissUploadingDialog();
        show(getInfo(R.string.upload_pic_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      dismissUploadingDialog();
      show(getInfo(R.string.upload_pic_fail));
    }
  };

  RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<AccountCenterResultBean> result = GsonUtil
            .processJson(jsonObject, AccountCenterResultBean.class);
        updatePage(result);
      } catch (Exception e) {
        show(getInfo(R.string.request_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
      show(errorMsf);
    }
  };

  private ImageView mHeadIcon;
  private TextView mInfoUserName;
  private TextView mInfoRealName;
  private TextView mInfoPhone;
  private TextView mInfoUserLevel;
  private TextView mInfoNickName;
  private TextView mInfoSex;
  private TextView mInfoArea;
  private TextView mInfoAssign;
  private TextView mInfoMyCard;
  private TextView mInfoPwdManager;
  private Dialog mDialog;
  private boolean isChangIcon;

  private String pathImage;//选择图片路径
  private final int TAKE_PHOTO = 5;//拍照标记
  private Uri imageUri;                            //拍照Uri
  private String pathTakePhoto;              //拍照路径

  private File mSaveFile;
  private String mSexType;
  private String mCountryType;
  private String mAutograph;
  private String mNick;
  private String mRegister;
  private String mAlipay;
  private String mAccount;
  private String mErweima;

  private void updatePage(Result<AccountCenterResultBean> result) {
    if (null == result) {
      showMsg(getInfo(R.string.request_fail));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      adapterUserInfo(result.getResult());
    } else {
      showMsg(
          DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.GET_DATE_FAIL)));
    }
  }

  private void adapterUserInfo(AccountCenterResultBean result) {
    if (null == result) {
      showMsg(getInfo(R.string.request_fail));
      return;
    }

    mAccount = result.getAccount();
    mErweima = result.getQrcode();

    mNick = result.getNick();
    mSexType = result.getSex();
    mInfoSex
        .setText("0".equals(result.getSex()) ? getString(R.string.man) : getString(R.string.woman));
    mCountryType = result.getCountrycode();
    mAutograph = result.getAutograph();
    if (mAutograph != null) {
      mInfoAssign
          .setText(
              mAutograph.length() > 10 ? mAutograph.substring(0, 8) + "..."
                  : result.getAutograph());
    } else {
      mInfoAssign.setText("");
    }

    DebugUtils.setStringValue(result.getAccount(), "", mInfoUserName);
    DebugUtils.setStringValue(result.getTruename(), "", mInfoRealName);
    DebugUtils.setStringValue(result.getPhone(), "", mInfoPhone);
    DebugUtils.setStringValue(result.getLevelname(), "", mInfoUserLevel);
    DebugUtils.setStringValue(result.getNick(), "", mInfoNickName);
    DebugUtils.setStringValue(result.getCountry(), "", mInfoArea);

  }


  @Override
  protected void beforeSetContentView() {

  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_account_center;
  }


  @Override
  public void initView() {

    String country = Locale.getDefault().getCountry();
    System.out.println("AccountCenterActivity.initView=country=" + country);

    checkPerssiom();
    TextView tvBack = (TextView) findViewById(R.id.tv_back);
    ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
    tvBack.setOnClickListener(this);
    ivBack.setOnClickListener(this);

    ViewGroup headPicLayout = (ViewGroup) findViewById(R.id.head_picture);
    //头像
    mHeadIcon = (ImageView) headPicLayout.findViewById(R.id.head_pic);
    //进入图标
    ImageView headInto = (ImageView) headPicLayout.findViewById(R.id.into);
    TextView headTitle = (TextView) headPicLayout.findViewById(R.id.title);
    headTitle.setText(getString(R.string.head_portrait));

    mHeadIcon.setOnClickListener(this);
    headInto.setOnClickListener(this);

    //用户名称
    ViewGroup userNameLayout = (ViewGroup) findViewById(R.id.user_name);
    TextView titleUserName = (TextView) userNameLayout.findViewById(R.id.title);
    mInfoUserName = (TextView) userNameLayout.findViewById(R.id.tv_info);
    titleUserName.setText(getInfo(R.string.user_name));

    //真实姓名
    ViewGroup realNameLayout = (ViewGroup) findViewById(R.id.real_name);
    TextView titleRealName = (TextView) realNameLayout.findViewById(R.id.title);
    mInfoRealName = (TextView) realNameLayout.findViewById(R.id.tv_info);
    titleRealName.setText(getInfo(R.string.real_name_center));

    //手机号码
    ViewGroup phoneLayout = (ViewGroup) findViewById(R.id.phone);
    TextView titlePhone = (TextView) phoneLayout.findViewById(R.id.title);
    mInfoPhone = (TextView) phoneLayout.findViewById(R.id.tv_info);
    titlePhone.setText(getInfo(R.string.phone_number));

    //用户级别
    ViewGroup userLevelLayout = (ViewGroup) findViewById(R.id.user_level);
    TextView titleUserLevel = (TextView) userLevelLayout.findViewById(R.id.title);
    mInfoUserLevel = (TextView) userLevelLayout.findViewById(R.id.tv_info);
    titleUserLevel.setText(getInfo(R.string.member_level));

    //用户昵称
    ViewGroup nickNameLayout = (ViewGroup) findViewById(R.id.nick_name);
    TextView titleNickName = (TextView) nickNameLayout.findViewById(R.id.title);
    mInfoNickName = (TextView) nickNameLayout.findViewById(R.id.tv_info);
    ImageView nickInto = (ImageView) nickNameLayout.findViewById(R.id.into);
    titleNickName.setText(getInfo(R.string.nickname));
    nickInto.setOnClickListener(new MyOnClickListener(0));
    mInfoNickName.setOnClickListener(new MyOnClickListener(0));
    nickNameLayout.setOnClickListener(new MyOnClickListener(0));

    //用户性别
    ViewGroup sexLayout = (ViewGroup) findViewById(R.id.sex);
    TextView titleSex = (TextView) sexLayout.findViewById(R.id.title);
    mInfoSex = (TextView) sexLayout.findViewById(R.id.tv_info);
    ImageView intoSex = (ImageView) sexLayout.findViewById(R.id.into);
    titleSex.setText(getInfo(R.string.sex));
    mInfoSex.setOnClickListener(new MyOnClickListener(1));
    intoSex.setOnClickListener(new MyOnClickListener(1));
    sexLayout.setOnClickListener(new MyOnClickListener(1));

    //地区
    ViewGroup areaLayout = (ViewGroup) findViewById(R.id.area);
    TextView titleArea = (TextView) areaLayout.findViewById(R.id.title);
    mInfoArea = (TextView) areaLayout.findViewById(R.id.tv_info);
    ImageView intoArea = (ImageView) sexLayout.findViewById(R.id.into);
    titleArea.setText(getInfo(R.string.user_area));
    mInfoArea.setOnClickListener(new MyOnClickListener(2));
    intoArea.setOnClickListener(new MyOnClickListener(2));
    areaLayout.setOnClickListener(new MyOnClickListener(2));

    //个性签名
    ViewGroup assignLayout = (ViewGroup) findViewById(R.id.assign);
    TextView titleAssign = (TextView) assignLayout.findViewById(R.id.title);
    mInfoAssign = (TextView) assignLayout.findViewById(R.id.tv_info);
    ImageView intoAssign = (ImageView) sexLayout.findViewById(R.id.into);
    titleAssign.setText(getInfo(R.string.user_assign));
    mInfoAssign.setOnClickListener(new MyOnClickListener(3));
    intoAssign.setOnClickListener(new MyOnClickListener(3));
    assignLayout.setOnClickListener(new MyOnClickListener(3));

    //我的银行卡
    ViewGroup myCarLayout = (ViewGroup) findViewById(R.id.my_car);
    TextView titleMyCard = (TextView) myCarLayout.findViewById(R.id.title);
    mInfoMyCard = (TextView) myCarLayout.findViewById(R.id.tv_info);
    ImageView intoMyCard = (ImageView) myCarLayout.findViewById(R.id.into);
    titleMyCard.setText(getInfo(R.string.my_card_one));
    mInfoMyCard.setText("");
    intoMyCard.setOnClickListener(new MyOnClickListener(4));
    myCarLayout.setOnClickListener(new MyOnClickListener(4));

    //alipay
    ViewGroup ali_pay = (ViewGroup) findViewById(R.id.ali_pay);
    TextView tvAli = (TextView) ali_pay.findViewById(R.id.title);
    TextView mInfoMyCardAli = (TextView) ali_pay.findViewById(R.id.tv_info);
    ImageView intoMyCardAli = (ImageView) ali_pay.findViewById(R.id.into);
    tvAli.setText(getInfo(R.string.ali_pay));
    mInfoMyCardAli.setText("");
    intoMyCardAli.setOnClickListener(new MyOnClickListener(6));
    ali_pay.setOnClickListener(new MyOnClickListener(6));

    //密码管理
    ViewGroup pwdManagerLayout = (ViewGroup) findViewById(R.id.pwd_manager);
    TextView titlePwdManager = (TextView) pwdManagerLayout.findViewById(R.id.title);
    mInfoPwdManager = (TextView) pwdManagerLayout.findViewById(R.id.tv_info);
    ImageView intoPwdManager = (ImageView) pwdManagerLayout.findViewById(R.id.into);
    titlePwdManager.setText(getInfo(R.string.pwd_manager));
    mInfoPwdManager.setText("");
    intoPwdManager.setOnClickListener(new MyOnClickListener(5));
    pwdManagerLayout.setOnClickListener(new MyOnClickListener(5));

    TextView safeOut = (TextView) findViewById(R.id.btn_safe_out);
    safeOut.setOnClickListener(this);

    ImageView tvMainPage = (ImageView) findViewById(R.id.tv_main_page);
    tvMainPage.setOnClickListener(this);

    Intent intent = getIntent();
    if (null != intent && "register".equals(intent.getStringExtra("register"))) {
      mRegister = intent.getStringExtra("register");
      mHeadIcon.setBackgroundResource(R.mipmap.pho_photos);
    } else {
      String path = SPUtils
          .getStringValue(this, Config.USER_INFO, Config.HEAD_ICON_PATH, null);
      if (TextUtils.isEmpty(path)) {
        mHeadIcon.setBackgroundResource(R.mipmap.pho_photos);
      } else {
        GlideManager.glideWithRound(this, path, mHeadIcon, 48);
      }
    }
  }

  @Override
  public void loadData() {
    try {
      judgeNet();
      List<RequestParams> list = new ArrayList<>();
      RequestParams params = new RequestParams(Config.TOKEN, SPUtils
          .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      list.add(params);
      RequestNet requestNet = new RequestNet(myApp, this, list, Urls.ACCOUNT_SET, mListener,
          RequestNet.POST);
    } catch (DefineException e) {
      showMsg(e.getMessage());
    }
  }

  @Override
  public void onClick(View v) {
    try {
      switch (v.getId()) {
        case R.id.iv_back:
        case R.id.tv_back:
          finish();
          break;
        case R.id.head_pic:
        case R.id.into:
          choosePic();
          break;
        case R.id.btn_safe_out:
          safeOut();
          break;
        case R.id.tv_main_page:
          handler.sendEmptyMessageDelayed(1, 1000);
          break;
        default:
          break;
      }
    } catch (DefineException e) {
      ToastUtil.showShort(e.getMessage(), this);
    }
  }

  private void safeOut() {

    StytledDialog
        .showIosAlert(true, ColorUtil.getColor(this, R.color.c333), this,
            getInfo(R.string.commit_out),
            null, getInfo(R.string.cancel), getInfo(R.string.confirm), "",
            false,
            true,
            new MyDialogListener() {
              @Override
              public void onFirst(DialogInterface dialog) {
              }

              @Override
              public void onSecond(DialogInterface dialog) {
                dialog.dismiss();

                if (RongIM.getInstance() != null) {
                  RongIM.getInstance().logout();
                }

                myApp.removeAllActivity();
                SPUtils.removeData(getApplicationContext(), Config.USER_INFO, Config.USER_ACCOUNT);
                SPUtils.removeData(getApplicationContext(), Config.USER_INFO, Config.RONG_TOKEN);
                SPUtils.removeData(getApplicationContext(), Config.USER_INFO, Config.USER_ID);
                RongIM.getInstance().logout();
                Intent intent = new Intent(AccountCenterActivity.this, LandActivity.class);
                intent.setType("reland");
                startActivity(intent);
                finish();
              }
            });
  }


  private void saveAndFinish() {
    try {
      showUploadingDialog();
      RequestNet requestNet = new RequestNet(myApp, this, mSaveFile,
          SPUtils
              .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""),
          Urls.UPLOAD_ICON, "image", SPUtils
          .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN,
              "") + ".jpg", mUploadListener);
    } catch (Exception e) {
      showMsg(getString(R.string.upload_pic_fail));
    }

  }

  private void choosePic() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

      if (!checkPermissionAllGranted(new String[]{permission.CAMERA,
          permission.READ_EXTERNAL_STORAGE,
          permission.WRITE_EXTERNAL_STORAGE})) {

        ActivityCompat
            .requestPermissions(this, new String[]{permission.CAMERA,
                permission.READ_EXTERNAL_STORAGE,
                permission.WRITE_EXTERNAL_STORAGE}, CAMERA_CODE);
      } else {
        showSelectDialog();
      }
    } else {
      showSelectDialog();
    }
  }

  private void showSelectDialog() {
    ArrayList<String> list = new ArrayList<>();
    list.add(getString(R.string.camera));
    list.add(getString(R.string.select_pic));

    StytledDialog.showBottomItemDialog(this, list, getString(R.string.cancel), true, true,
        new MyItemDialogListener() {
          @Override
          public void onItemClick(String text, int position) {
            openPic(position);
          }
        });
  }

  private void openPic(int position) {

    if (0 == position) {
      cameraPic();
    }
    if (1 == position) {
      xiangce();
    }
  }

  /**
   * 检查所有需要的权限
   */
  private boolean checkPermissionAllGranted(String[] permissions) {
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(this, permission)
          != PackageManager.PERMISSION_GRANTED) {
        // 只要有一个权限没有被授予, 则直接返回 false
        return false;
      }
    }
    return true;
  }

  private void cameraPic() {
    takePic();
  }

  private boolean isAllGranted = true;

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case CAMERA_CODE:

        // 判断是否所有的权限都已经授予了
        if (grantResults.length > 0) {
          for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
              isAllGranted = false;
              break;
            }
          }

          if (isAllGranted) {
            showSelectDialog();
          } else {
            Toast.makeText(getApplicationContext(), getString(R.string.camera_deny),
                Toast.LENGTH_LONG).show();
          }
        } else {
          Toast.makeText(getApplicationContext(), getString(R.string.camera_deny),
              Toast.LENGTH_LONG).show();
        }

        break;
      default:
        break;
    }
  }

  private void takePic() {
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
    imageUri = UriUtils.getUri(outputImage, this);
    Intent intentPhoto = new Intent("android.media.action.IMAGE_CAPTURE"); //拍照
    intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    startActivityForResult(intentPhoto, TAKE_PHOTO);
  }


  private File photoFile;

  private void xiangce() {
//    Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
//    albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//    startActivityForResult(albumIntent, REQUEST_ALBUM_OK);
//

    Matisse.from(this)
        .choose(MimeType.ofImage())
        .countable(false)
        .maxSelectable(1)
        .forResult(11);

  }


  private class MyOnClickListener implements View.OnClickListener {

    private int a;

    public MyOnClickListener(int a) {
      this.a = a;
    }

    @Override
    public void onClick(View v) {
      switch (a) {
        case 0://昵称
          setNickName();
          break;
        case 1://性别
          setSex();
          break;
        case 2://地区
          setArea();
          break;
        case 3://签名
          setAssigin();
          break;
        case 4://银行卡
          setCard();
          break;
        case 5://密码
          startNewActivity(AccountCenterActivity.this, ManagerPwdActivity.class);
          break;
        case 6://支付宝
          Intent intent = new Intent(AccountCenterActivity.this, ZhiFuBaoAccountActivity.class);
//          Intent intent = new Intent(AccountCenterActivity.this, AliActivity.class);
          intent.putExtra("user_name", DebugUtils.convert(mAccount, ""));
          intent.putExtra("url", DebugUtils.convert(mErweima, null));
          startActivity(intent);
          break;
        default:
          break;
      }
    }
  }

  private void setCard() {
    Intent intent = new Intent(AccountCenterActivity.this, BankCardListActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (mDialog != null) {
      mDialog.dismiss();
    }
  }

  private void setAssigin() {
    Intent intent = new Intent(this, UserAssignActivity.class);
    intent.putExtra("assign", mAutograph);
    startActivity(intent);
  }

  private void setArea() {
    Intent intent = new Intent(this, AreaSetActivity.class);
    intent.putExtra("area", mCountryType);
    startActivity(intent);
  }

  private void setNickName() {
    String nickName = mInfoNickName.getText().toString().trim();
    Intent intent = new Intent(this, UserInfoSetActivity.class);
    intent.putExtra("nick_name", nickName);
    startActivity(intent);
  }

  private void setSex() {
    Intent intent = new Intent(this, UserSexSetActivity.class);
    intent.putExtra("sex", mSexType);
    startActivity(intent);
  }

  private void galleryAddPic(File f) {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    Uri contentUri = Uri.fromFile(f);
    mediaScanIntent.setData(contentUri);
    this.sendBroadcast(mediaScanIntent);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case 11:
        if (null != data) {

          List<Uri> uris = Matisse.obtainResult(data);
          String videoPath = VideoUtils.getPath(this, uris.get(0));
          if (TextUitl.isNotEmpty(videoPath) && new File(videoPath).exists()) {
            startCrop(Uri.fromFile(new File(videoPath)));
          }
        }
        break;
      case 2:
        break;
      case TAKE_PHOTO:
        String path = imageUri
            .getPath();//content://com.example.administrator.lubanone.my.package.name.provider/external_files/suishoupai_image.jpg

        Uri srcUri = null;
        if (VERSION.SDK_INT >= 24) {
          File file = new File(Environment.getExternalStorageDirectory(),
              "suishoupai_image.jpg");
          srcUri = Uri.fromFile(file);
        } else {
          srcUri = Uri.fromFile(new File(path));
        }

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "SampleCropImage.jpeg"));

        UCrop.Options options = new UCrop.Options();
        options.setMaxScaleMultiplier(5);
        options.setCompressionQuality(70);
        options.setCompressionFormat(CompressFormat.JPEG);

        if (null != srcUri) {
          UCrop.of(imageUri, destinationUri).withOptions(options).withAspectRatio(1, 1)
              .withMaxResultSize(500, 500)
              .start(this);
        }
        break;
      case REQUEST_ALBUM_OK:
        try {
          Uri dataData = data.getData();
          startCrop(dataData);
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;

      default:
        break;

    }

    if (resultCode == RESULT_OK) {
      //裁切成功
      if (requestCode == UCrop.REQUEST_CROP && null != data) {
        Uri croppedFileUri = UCrop.getOutput(data);
        //获取默认的下载目录
        String downloadsDirectoryPath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(),
            croppedFileUri.getLastPathSegment());
        mSaveFile = new File(downloadsDirectoryPath, filename);

        Log.e("AccountCenterActivity", "onActivityResult=" + filename);
        if (new File(downloadsDirectoryPath).isDirectory()) {
          Log.e("AccountCenterActivity", "onActivityResult=true");
        }

        //保存下载的图片
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
          inStream = new FileInputStream(new File(croppedFileUri.getPath()));
          outStream = new FileOutputStream(mSaveFile);
          inChannel = inStream.getChannel();
          outChannel = outStream.getChannel();
          inChannel.transferTo(0, inChannel.size(), outChannel);

          Bitmap bitmap = BitmapFactory.decodeFile(mSaveFile.getPath());
          int i = bitmap.getRowBytes() * bitmap.getHeight();
          System.out.println("quyang=" + i / 1024);

          //上传icon
          saveAndFinish();
        } catch (Exception e) {
          e.printStackTrace();

          Log.e("AccountCenterActivity", "onActivityResult=" + e.getMessage());
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
      Toast.makeText(this, getString(R.string.crop_fail), Toast.LENGTH_LONG).show();
    }
  }

  private void startCrop(Uri dataData) {
    Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "SampleCropImage.jpeg"));
    UCrop.Options options = new UCrop.Options();
    options.setMaxScaleMultiplier(5);
    options.setCompressionQuality(70);
    options.setCompressionFormat(CompressFormat.JPEG);

    UCrop.of(dataData, destinationUri).withOptions(options).withAspectRatio(1, 1)
        .withMaxResultSize(500, 500)
        .start(this);
  }

  public void show(String msg) {
    ToastUtil.showShort(msg, this);
  }


  private void uploadResult(Result<String> result) {
    dismissUploadingDialog();
    if (ResultUtil.isSuccess(result) && TextUitl.isNotEmpty(result.getResult())) {
      showMsg(getInfo(R.string.upload_success));
      User user = new User(SPUtils.getStringValue(
          AccountCenterActivity.this.getApplicationContext(),Config.USER_INFO,Config.USER_ID,""),
          mInfoNickName.getText().toString().trim(),result.getResult().toString());
      RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getId(),
          user.getName(),Uri.parse(user.getHeadUrl())));
      GlideManager.glideWithRound(this, result.getResult(), mHeadIcon, 48);
      SPUtils
          .putStringValue(getApplicationContext(), Config.USER_INFO, Config.HEAD_ICON_PATH,
              DebugUtils.convert(result.getResult(), null));
    } else {
      showMsg(DebugUtils.convert(ResultUtil.getErrorMsg(result), getString(R.string.upload_fail)));
    }
  }

  @Override
  public void finish() {
    boolean b = myApp.hasActivity(UserCenterActivity.class);
    if (!b) {
      Intent intent = new Intent(this, UserCenterActivity.class);
      if (TextUitl.isNotEmpty(mRegister)) {
        intent.putExtra("register", "register");
      }
      startActivity(intent);
    }
    super.finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    myApp.removeActivity(this);
  }


  private static int REQUEST_PERSSION = 0;

  private void checkPerssiom() {

    String[] perms = {
        Manifest.permission.WRITE_EXTERNAL_STORAGE
        , Manifest.permission.READ_EXTERNAL_STORAGE
        , Manifest.permission.CAMERA};

    for (int i = 0; i < perms.length; i++) {
      if (ContextCompat.checkSelfPermission(this, perms[i])
          != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
            perms[i])) {
          //用户拒绝过该权限
          Toast.makeText(this, getString(R.string.deny_tips), Toast.LENGTH_LONG).show();
          ActivityCompat.requestPermissions(this, new String[]{perms[i]},
              0);
        } else {
          //首次提醒用户权限设置
          ActivityCompat.requestPermissions(this, new String[]{perms[i]},
              0);

        }
      }
    }
  }

}

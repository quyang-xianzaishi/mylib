package com.example.administrator.lubanone.fragment;

import static android.app.Activity.RESULT_OK;
import static com.umeng.facebook.FacebookSdk.getApplicationContext;

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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.home.BuySeedProcessActivity;
import com.example.administrator.lubanone.activity.home.NoMoneyDetailsActivity;
import com.example.administrator.lubanone.activity.home.SeedsMemberInfoActivity;
import com.example.administrator.lubanone.adapter.homepage.ProcessNoMoneyTimerAdapterNew;
import com.example.administrator.lubanone.bean.homepage.BuyMoneyListResultBean;
import com.example.administrator.lubanone.bean.homepage.BuyMoneyListResultBean.PPaylistBean;
import com.example.administrator.lubanone.interfaces.OnBuyVIPListener;
import com.example.administrator.lubanone.interfaces.OnListViewItemListener;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
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
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;
import com.example.qlibrary.utils.WindoswUtil;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by quyang on 2017/6/30.
 */

public class MoneyFragment extends BaseFragment implements OnMoneyPayListener<PPaylistBean>,
    OnBuyVIPListener, OnItemClickListener {

  private PullableListView mLv;
  private PullToRefreshLayout pull;
  private int currentPage = 1;
  private List<PPaylistBean> mPPaylist = new ArrayList<>();
  private PullToRefreshLayout mEmptyLayout;
  private static final int REQUEST_ALBUM_OK = 123;
  private File mSaveFile;
  private int size = 5;
  private Boolean getMore;
  private int itemCount;
  private int currentPosition;


  private Boolean uploadPingZheng;
  private Boolean getDownMore;
  private int uploadPosition;
  private String mOrderid;
  private String mProlongtime;


  //上传付款凭证
  private RequestListener mUploadListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        uploadResult(result);
      } catch (Exception e) {
        System.out.println("MoneyFragment.uploadPic=" + e.getMessage());
        dismissUploadingDialog();
        if (isAdded()) {
          showMsg(getInfo(R.string.upload_proof_fail));
        }
        resetUpload(false);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      System.out.println("MoneyFragment.uploadPic=" + errorMsf);
      dismissUploadingDialog();
      if (isAdded()) {
        showMsg(getString(R.string.upload_proof_fail));
      }
      resetUpload(false);
    }
  };


  //延长申请时间
  private RequestListener mLongListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        dismissRequestDialog();
        System.out.println("MoneyFragment.testSuccess====" + jsonObject);
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        if (ResultUtil.isSuccess(result)) {
          showOkDialog(getString(R.string.commit_shengqing));
        } else {
          showMsg(
              DebugUtils
                  .convert(ResultUtil.getErrorMsg(result),
                      getString(R.string.commit_shengqing_fail)));
          resetLong(false);
        }
      } catch (Exception e) {
        dismissRequestDialog();
        resetLong(false);
        if (isAdded()) {
          showMsg(getString(R.string.long_pay_time_fail));
        }
      }
    }

    @Override
    public void onFail(String errorMsf) {
      dismissRequestDialog();
      resetLong(false);
      showMsg(getString(R.string.commit_shengqing_fail));
    }
  };


  //listview
  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      dismissLoadingDialog();
      try {
        resetGetDownMore();
        Result<BuyMoneyListResultBean> result = GsonUtil
            .processJson(jsonObject, BuyMoneyListResultBean.class);
        getListInfo(result);
      } catch (Exception e) {
        fail();
        if (CollectionUtils.isEmpty(mPPaylist)) {
          showEmptyLayout(true);
        }
        if (null != getMore && getMore) {
          getMore = false;
          mLv.setSelection(currentPosition);
        }
        resetUpload(false);
        resetLong(false);
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
        resetGetDownMore();
      }
    }

    @Override
    public void onFail(String errorMsf) {
      fail();
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
      }
      if (CollectionUtils.isEmpty(mPPaylist)) {
        showEmptyLayout(true);
      }
      dismissLoadingDialog();
      resetUpload(false);
      resetLong(false);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      resetGetDownMore();
    }
  };
  private String mPrice;


  private void updatePage(BuyMoneyListResultBean result) {
    updateTopInfo(result);
    List<PPaylistBean> pPaylist = result.getP_paylist();
    mPPaylist.clear();
    if (CollectionUtils.isNotEmpty(pPaylist)) {
      success();
      setEmptyLayoutSuccess();
      resetEmptyRefresh(false);
      mPPaylist.addAll(pPaylist);
    }
    if (!CollectionUtils.isEmpty(mPPaylist)) {
      success();
      showEmptyLayout(false);
      mPrice = result.getPrice();
      ProcessNoMoneyTimerAdapterNew mAdapterd = new ProcessNoMoneyTimerAdapterNew(
          MyApplication.getContext(), mPPaylist,
          new MyOnListViewItemListener(), this, this, mPrice);
      mLv.setAdapter(mAdapterd);
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
        if (itemCount == mPPaylist.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      resetUpload(true);
      resetLong(true);
      itemCount = mPPaylist.size();
    } else {
      fail();
      showEmptyLayout(true);
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetUpload(false);
      resetLong(false);
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
    }
  }

  public void setEmptyLayoutSuccess() {
    if (null != mEmptyLayout && mEmptyLayout.isShown()) {
      mEmptyLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
  }


  public void setEmptyLayoutFail() {
    if (null != mEmptyLayout && mEmptyLayout.isShown()) {
      mEmptyLayout.refreshFinish(PullToRefreshLayout.FAIL);
    }
  }

  private void updateTopInfo(BuyMoneyListResultBean result) {
    if (null == result) {
      return;
    }
    mProlongtime = result.getProlongtime();
    String tgbzlist = result.getTgbzlist();
    String listcount = result.getListcount();
    String p_confirmlist = result.getP_confirmlist();
    String p_dpingjialist = result.getP_dpingjialist();
    BuySeedProcessActivity activity = (BuySeedProcessActivity) mActivity;
    activity.onitem(tgbzlist, listcount, p_confirmlist, p_dpingjialist);
  }

  private Boolean emptyRefresh;

  public void showEmptyMsg() {
    if (null != emptyRefresh && emptyRefresh) {
      showMsg(getString(R.string.no_more_message));
    }
  }


  public void resetEmptyRefresh(boolean showMsg) {
    if (showMsg) {
      showEmptyMsg();
    }
    if (null != emptyRefresh && emptyRefresh) {
      emptyRefresh = false;
    }
  }

  @Override
  public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
    view.findViewById(R.id.ll_top).setOnClickListener(new MyOnClickListener(parent, position));
    view.findViewById(R.id.ll_mid).setOnClickListener(new MyOnClickListener(parent, position));
    view.findViewById(R.id.ll_down).setOnClickListener(new MyOnClickListener(parent, position));
  }

  public class EmptyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      emptyRefresh = true;
      getInitList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
    }
  }


  @Override
  public View initView() {

    Log.e("MoneyFragment", "initView=");

    View view = mInflater.inflate(R.layout.list_view_only, null);

    mEmptyLayout = (PullToRefreshLayout) view.findViewById(R.id.empty_layout);
    EmptyRefreshListener listener1 = new EmptyRefreshListener();
    mEmptyLayout.setOnPullListener(listener1);
    mEmptyLayout.setPullUpEnable(false);

    mLv = (PullableListView) view.findViewById(R.id.lv);
    pull = (PullToRefreshLayout) view.findViewById(R.id.task_fragment_pullToRefreshLayout);

    //滑动监听
    RefreshListener listener = new RefreshListener();
    pull.setOnPullListener(listener);
    pull.setPullUpEnable(true);//设置不让上拉加载

    mLv.setOnItemClickListener(this);
    mLv.setOnScrollListener(new OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {

      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
          int totalItemCount) {
        currentPosition = view.getLastVisiblePosition();
      }
    });

    return view;
  }

  @Override
  public void initData() {
    getInitList();
  }

  private void getInitList() {
    if (!NetUtil.isConnected(MyApplication.getContext())) {
      ToastUtil.showShort(MyApplication.getContext().getString(R.string.NET_ERROR),
          MyApplication.getContext());
      showEmptyLayout(true);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      resetGetDownMore();
      return;
    }
    List<RequestParams> list = new ArrayList<>();
    RequestParams paramsToken = new RequestParams(Config.TOKEN,
        SPUtils.getStringValue(MyApplication.getContext(), Config.USER_INFO, Config.TOKEN, ""));

    if (null != getMore && getMore) {
      size = mPPaylist.size() + 5;
    } else {
      size = mPPaylist.size() == 0 ? size : mPPaylist.size() + 5;
    }
    RequestParams paramsPage = new RequestParams("number", size + "");
//    RequestParams paramsPage = new RequestParams("page", currentPage + "");
    list.add(paramsToken);
    list.add(paramsPage);
    if (mActivity != null && mListener != null) {
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.BUY_NO_MONEY, mListener, RequestNet.POST);
    }
  }

  private void getListInfo(Result<BuyMoneyListResultBean> result) {
    if (null == result) {
      fail();
      if (CollectionUtils.isEmpty(mPPaylist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetUpload(false);
      resetLong(false);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      updatePage(result.getResult());
    } else {
      fail();
      if (CollectionUtils.isEmpty(mPPaylist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetUpload(false);
      resetLong(false);
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
    }
  }


  public class RefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      getDownMore = true;
      getInitList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      getMore = true;
      getInitList();
    }

  }


  private void showOkDialog(String text) {
    final Dialog dialog = StytledDialog.showDuiHaoDialog(getContext(), true, true, text);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
        getInitList();
      }
    }, 2000);
  }


  private int longPosition = -1;
  private Boolean longPay;


  public void resetLong(boolean toTargetPositinon) {
    if (longPay != null && longPay) {
      longPay = false;
      if (toTargetPositinon) {
        mLv.setSelection(longPosition - 1 >= 0 ? longPosition - 1 : 0);
      }
    }
  }

  //延长付款回调
  private class MyOnListViewItemListener implements OnListViewItemListener {

    @Override
    public void onItem(Object object, int position) {
      showDialog((PPaylistBean) object, position);
    }
  }


  //联系卖家
  @Override
  public void onVIPItemClick(Object item, int position) {
    PPaylistBean pPaylistBean = (PPaylistBean) item;
    Intent intent = new Intent();
    intent.putExtra("userId", DebugUtils.convert(pPaylistBean.getSellmember(), ""));
    intent.setClass(mActivity, SeedsMemberInfoActivity.class);
    startActivityForResult(intent, 101);
  }


  public void resetUpload(boolean toTargetPosion) {
    if (null != uploadPingZheng && uploadPingZheng) {
      uploadPingZheng = false;
      if (toTargetPosion) {
        mLv.setSelection(uploadPosition - 1 >= 0 ? uploadPosition - 1 : 0);
      }
    }
  }

  //上传付款凭证回调
  @Override
  public void onCuiPayClick(PPaylistBean item, int position) {
    uploadPic(item, position);
  }


  private void uploadPic(Object object, int position) {
    PPaylistBean confirmlistBean = (PPaylistBean) object;
    mOrderid = confirmlistBean.getOrderid();

    uploadPosition = position;
    uploadPingZheng = true;

//    //打开相册
//    Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
//    albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//    startActivityForResult(albumIntent, REQUEST_ALBUM_OK);

    choosePic();


  }

  /**
   * 检查所有需要的权限
   */
  private boolean checkPermissionAllGranted(String[] permissions) {
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(mActivity, permission)
          != PackageManager.PERMISSION_GRANTED) {
        // 只要有一个权限没有被授予, 则直接返回 false
        return false;
      }
    }
    return true;
  }

  private static final int CAMERA_CODE = 125;

  private void choosePic() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

      if (!checkPermissionAllGranted(new String[]{permission.CAMERA,
          permission.READ_EXTERNAL_STORAGE,
          permission.WRITE_EXTERNAL_STORAGE})) {

        ActivityCompat
            .requestPermissions(mActivity, new String[]{permission.CAMERA,
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

    StytledDialog.showBottomItemDialog(mActivity, list, getString(R.string.cancel), true, true,
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

  private void cameraPic() {
    takePic();
  }

  private String pathTakePhoto;              //拍照路径
  private Uri imageUri;                            //拍照Uri
  private final int TAKE_PHOTO = 5;//拍照标记

  private void takePic() {
    File outputImage = new File(Environment.getExternalStorageDirectory(), "suishoupai_image1.jpg");
    pathTakePhoto = outputImage.toString();
    try {
      if (outputImage.exists()) {
        outputImage.delete();
      }
      outputImage.createNewFile();
    } catch (Exception e) {
      e.printStackTrace();
    }
    imageUri = UriUtils.getUri(outputImage, mActivity);
    Intent intentPhoto = new Intent("android.media.action.IMAGE_CAPTURE"); //拍照
    intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    startActivityForResult(intentPhoto, TAKE_PHOTO);
  }

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


  //初次确认
  public void showDialog(final PPaylistBean object, final int position) {
    StytledDialog.showIosAlert(getActivity(),
        MyApplication.getContext().getResources().getString(R.string.is_longer_new),
        getString(R.string.long_tips) + DebugUtils.convert(mProlongtime, Config.long_time)
            + getString(R.string.long_tips_one), getString(R.string.cancels),
        getString(R.string.confirm), "", true, true,
        new MyDialogListener() {
          @Override
          public void onFirst(DialogInterface dialog) {
            dialog.dismiss();
          }

          @Override
          public void onSecond(DialogInterface dialog) {
            dialog.dismiss();
            longerPayTime(object, position);
          }
        });
  }

  //延长打款
  private void longerPayTime(PPaylistBean object, int position) {

    longPosition = position;
    longPay = true;

    try {

      //PPaylistBean
      if (!NetUtil.isConnected(getActivity())) {
        showMsg(getString(R.string.NET_ERROR));
        return;
      }
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderId = new RequestParams("orderid",
          DebugUtils.convert(object.getOrderid(), ""));
      list.add(paramsToken);
      list.add(paramsOrderId);
      showRequestDialog();
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.LONG_PAY_TIME, mLongListener,
          RequestNet.POST);
    } catch (Exception e) {
      dismissRequestDialog();
      if (isAdded()) {
        showMsg(getString(R.string.shen_qing_fail));
      }
      resetLong(false);
    }

  }


  //再次确认
  private void dialog() {
    final Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);
    View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_layout, null);
    TextView tv = (TextView) view.findViewById(R.id.tv);
    tv.setText(MyApplication.getContext().getResources().getString(R.string.commit_success_tips));
    dialog.setContentView(view);
    dialog.getWindow().setLayout(WindoswUtil.getWindowWidth(getActivity()) - 200,
        WindowManager.LayoutParams.WRAP_CONTENT);
    dialog.show();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        dialog.dismiss();
        //TODO 曲洋刷新界面
        RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(),
            mActivity, null, Urls.TEST_URL, mListener,
            Config.isOnline ? RequestNet.POST : RequestNet.GET);
      }
    }, 2000);

  }

  public void success() {
    if (null != pull && pull.isShown()) {
      pull.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
  }


  public void fail() {
    if (null != pull && pull.isShown()) {
      pull.refreshFinish(PullToRefreshLayout.FAIL);
    }
  }

  public void showEmptyLayout(boolean show) {
    if (show) {
      mEmptyLayout.setVisibility(View.VISIBLE);
      pull.setVisibility(View.GONE);
    } else {
      mEmptyLayout.setVisibility(View.GONE);
      pull.setVisibility(View.VISIBLE);
    }
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case REQUEST_ALBUM_OK:
        try {
          Uri dataData = data.getData();
          startCrop(dataData);
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      case 101:
      case 100:
        getInitList();
        break;
      case TAKE_PHOTO:
        String path = imageUri
            .getPath();//content://com.example.administrator.lubanone.my.package.name

        File file = new File(Environment.getExternalStorageDirectory(),
            "suishoupai_image1.jpg");//suishoupai_image1

        if (file.exists()) {
          System.out.println("MoneyFragment.onActivityResultddd 1");
        }

        uploadPic(file);

        break;
      case 11:
        if (null != data) {
          List<Uri> uris = Matisse.obtainResult(data);
          String videoPath = VideoUtils.getPath(mActivity, uris.get(0));
          if (TextUitl.isNotEmpty(videoPath) && new File(videoPath).exists()) {
            System.out.println("MoneyFragment.onActivityResultddd 2");
//            saveAndFinish(new File(videoPath));
            uploadPic(new File(videoPath));
          }
        }
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
        mSaveFile = new File(downloadsDirectoryPath, filename);
        if (mSaveFile.exists()) {
          showMsg("ewtt");
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

          //上传图片
          uploadPic(mSaveFile);

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
      showMsg(getString(R.string.crop_pic_fail));
    }
  }

  //上传头像
  private void saveAndFinish(File saveFile) {
    try {
      showUploadingDialog();
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          saveFile,
          SPUtils
              .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""),
          Urls.UPLOAD_ICON, "image", SPUtils
          .getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN,
              "") + ".jpg", mUploadListener);
    } catch (Exception e) {
      dismissUploadingDialog();
      showMsg(getString(R.string.upload_pic_fail));
    }

  }


  private void startCrop(Uri dataData) {
    boolean equals = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    if (!equals) {
      showMsg(getString(R.string.sd_card_tips));
      return;
    }
    Uri destinationUri = Uri
        .fromFile(new File(getActivity().getCacheDir(), "SampleCropImage.jpeg"));
    UCrop.Options options = new UCrop.Options();
    options.setMaxScaleMultiplier(5);
    options.setCompressionQuality(70);
    options.setCompressionFormat(CompressFormat.JPEG);

    UCrop.of(dataData, destinationUri).withOptions(options).withAspectRatio(9, 16)
        .withMaxResultSize(500, 500)
        .start(getActivity(), this);
  }


  //上传付款凭证
  private void uploadPic(File saveFile) {
    if (TextUtils.isEmpty(mOrderid)) {
      showMsg(getString(R.string.order_id_empty));
      return;
    }
    try {
      showUploadingDialog();
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          saveFile,
          SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""), mOrderid,
          Urls.UPLOAD_PAY_PIC, "image",
          "pay_proof.jpg", mUploadListener);
    } catch (Exception e) {
      System.out.println("MoneyFragment.uploadPic=" + e.getMessage());
      dismissUploadingDialog();
      resetUpload(false);
    }

  }

  private void uploadResult(Result<Object> result) {
    dismissUploadingDialog();
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.upload_proof_success));
      getInitList();
    } else {
      System.out.println("MoneyFragment.uploadPic=l=" + ResultUtil.getErrorMsg(result));
      showMsg(DebugUtils
          .convert(ResultUtil.getErrorMsg(result), getString(R.string.upload_proof_fail)));
      resetUpload(false);
    }
  }

  @Override
  public void onVisible() {
    System.out.println("MoneyFragment.onVisible");
    initData();
  }

  private void resetGetDownMore() {
    if (getDownMore != null && getDownMore) {
      getDownMore = false;
    }
  }

  private class MyOnClickListener implements OnClickListener {


    private final AdapterView<?> mParent;
    private final int mPosition;

    public MyOnClickListener(AdapterView<?> parent, int position) {
      mParent = parent;
      mPosition = position;
    }

    @Override
    public void onClick(View v) {
      PPaylistBean itemAtPosition = (PPaylistBean) mParent.getItemAtPosition(mPosition);
      Intent intent = new Intent(mActivity, NoMoneyDetailsActivity.class);
      Bundle bundle = new Bundle();
      bundle.putSerializable("item", itemAtPosition);
      bundle.putString("price", mPrice);
      intent.putExtra("item", bundle);
      startActivityForResult(intent, 100);
    }
  }
}

package com.example.administrator.lubanone.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.home.BuySeedProcessActivity;
import com.example.administrator.lubanone.activity.home.ConfirmDetailActivity;
import com.example.administrator.lubanone.activity.home.SeedsMemberInfoActivity;
import com.example.administrator.lubanone.activity.message.CameraActivity;
import com.example.administrator.lubanone.activity.message.CustomMessageActivity;
import com.example.administrator.lubanone.adapter.homepage.ProcessConfirmTimerAdapterNew;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean;
import com.example.administrator.lubanone.bean.homepage.BuyConfirmResultBean.PConfirmlistBean;
import com.example.administrator.lubanone.bean.homepage.UploadVideo;
import com.example.administrator.lubanone.interfaces.OnItemListener;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.rxjava.BaseModelFunc;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.administrator.lubanone.utils.VideoUtils;
import com.example.qlibrary.dialog.StytledDialog;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.exception.DefineException;
import com.example.qlibrary.interfaces.OnClickItemListener;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.FileUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.NetUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ToastUtil;
import com.google.gson.Gson;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.json.JSONObject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ComfirmFragment extends BaseFragment implements OnMoneyPayListener<PConfirmlistBean>,
    OnClickItemListener, OnItemClickListener {


  private PullableListView mLv;
  private PullToRefreshLayout pull;
  private List<PConfirmlistBean> mConfirmlist = new ArrayList<>();
  private static final int REQUEST_ALBUM_OK = 123;
  private File mSaveFile;
  private int size = 5;
  private Boolean getMore;
  private int currentPosition;
  private int itemCount;
  private Boolean emptyRefresh;
  private Boolean getDownMore;
  private PullToRefreshLayout mEmptyLayout;
  private int uploadPosition;

  private static final int VIDEO_CODE = 33;
  private static final int CAPTURE_VIDEO = 34;
  private String orderid;


  //崔确认
  private RequestListener mCuiComfrimListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        dismissRequestDialog();
        if (ResultUtil.isSuccess(result)) {
          showMsg(getString(R.string.cui_comfrim_success));
          getInitList();
        } else {
          showMsg(DebugUtils
              .convert(ResultUtil.getErrorMsg(result), getString(R.string.cui_comfrim_fail)));
          resetCui(false);
        }
      } catch (Exception e) {
        if (isAdded()) {
          showMsg(getString(R.string.cui_comfrim_fail));
        }
        dismissRequestDialog();
        resetCui(false);
      }

    }

    @Override
    public void onFail(String errorMsf) {
      dismissRequestDialog();
      showMsg(getString(R.string.cui_comfrim_fail));
    }
  };


  //上视频凭证
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
        hideCommitDialog();
        if (isAdded()) {
          showMsg(getInfo(R.string.upload_fail));
        }
      }
    }

    @Override
    public void onFail(String errorMsf) {
      hideCommitDialog();
      if (isAdded()) {
        showMsg(getString(R.string.upload_fail));
      }
    }
  };
  private int mPosition;
  private String mPrice;

  private void uploadResult(Result<Object> result) {
    hideCommitDialog();
    if (ResultUtil.isSuccess(result)) {
      showMsg(getString(R.string.upload_proof_success));
    } else {
      showMsg(ResultUtil.getErrorMsg(result));
    }
  }

  //第一次请求数据列表
  private RequestListener mListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }

    @Override
    public void testSuccess(String jsonObject) {
      dismissLoadingDialog();
      resetGetDownMore();
      try {
        System.out.println("ComfirmFragment" + jsonObject);
        Result<BuyConfirmResultBean> result = GsonUtil
            .processJson(jsonObject, BuyConfirmResultBean.class);
        getListInfo(result);
      } catch (Exception e) {
        fail();
        if (CollectionUtils.isEmpty(mConfirmlist)) {
          showEmptyLayout(true);
        }
        if (null != getMore && getMore) {
          getMore = false;
          mLv.setSelection(currentPosition);
        }
        resetCui(false);
        resetUploadVideo(false);
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
        resetItem(false);
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
      if (CollectionUtils.isEmpty(mConfirmlist)) {
        showEmptyLayout(true);
      }
      resetCui(false);
      resetUploadVideo(false);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      resetItem(false);
      resetGetDownMore();
    }
  };

  //联系客服
  @Override
  public void onClick(int position) {
    PConfirmlistBean pConfirmlistBean = mConfirmlist.get(position);
    Intent intent = new Intent(mActivity, CustomMessageActivity.class);
    startActivity(intent);
  }

  @Override
  public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
    final PConfirmlistBean itemAtPosition = (PConfirmlistBean) parent.getItemAtPosition(position);

    view.findViewById(R.id.rl).setOnClickListener(new MyOnClickListener(itemAtPosition, position));
    view.findViewById(R.id.ll_top).setOnClickListener(new MyOnClickListener(itemAtPosition, position));
    view.findViewById(R.id.ll_containernew)
        .setOnClickListener(new MyOnClickListener(itemAtPosition, position));
//    view.setOnClickListener(new MyOnClickListener(itemAtPosition, position));

    view.findViewById(R.id.tv_lianxi).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(mActivity, SeedsMemberInfoActivity.class);
        intent.putExtra("userId", DebugUtils.convert(itemAtPosition.getSellmember(), ""));
        startActivity(intent);
      }
    });
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

  public void resetEmptyRefresh(boolean showMsg) {
    if (showMsg) {
      showEmptyMsg();
    }
    if (null != emptyRefresh && emptyRefresh) {
      emptyRefresh = false;
    }
  }

  public void showEmptyMsg() {
    if (null != emptyRefresh && emptyRefresh) {
      showMsg(getString(R.string.no_more_message));
    }
  }


  @Override
  public View initView() {
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
    try {
      if (!NetUtil.isConnected(MyApplication.getContext())) {
        ToastUtil.showShort(MyApplication.getContext().getString(R.string.NET_ERROR),
            MyApplication.getContext());
        showEmptyLayout(true);
        resetUploadVideo(false);
        resetEmptyRefresh(false);
        setEmptyLayoutFail();
        resetItem(false);
        resetGetDownMore();
        return;
      }
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(MyApplication.getContext(), Config.USER_INFO, Config.TOKEN, ""));

      if (null != getMore && getMore) {
        size = mConfirmlist.size() + 5;
      } else {
        size = mConfirmlist.size() == 0 ? size : mConfirmlist.size() + 5;
      }
      RequestParams paramsPage = new RequestParams("number", size + "");
      list.add(paramsToken);
      list.add(paramsPage);
      if (null != mActivity && null != mListener) {
        RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(),
            mActivity,
            list, Urls.BUY_CONFIRM, mListener, RequestNet.POST);
      }
    } catch (Exception e) {
      if (isAdded()) {
        showMsg(getString(R.string.GET_DATE_FAIL));
      }
      showEmptyLayout(true);
      resetUploadVideo(false);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      resetItem(false);
      resetGetDownMore();
    }
  }


  private void getListInfo(Result<BuyConfirmResultBean> result) {
    if (null == result) {
      fail();
      if (CollectionUtils.isEmpty(mConfirmlist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetCui(false);
      resetUploadVideo(false);
      resetEmptyRefresh(true);
      setEmptyLayoutFail();
      resetItem(false);
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      updatePage(result.getResult());
    } else {
      fail();
      if (CollectionUtils.isEmpty(mConfirmlist)) {
        showEmptyLayout(true);
      }
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetCui(false);
      resetUploadVideo(false);
      resetEmptyRefresh(false);
      setEmptyLayoutFail();
      resetItem(false);
    }
  }


  //下拉监听
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


  //更新界面
  private void updatePage(BuyConfirmResultBean result) {
    updateTopInfo(result);
    List<PConfirmlistBean> pConfirmlist = result.getP_confirmlist();
    mConfirmlist.clear();
    if (CollectionUtils.isNotEmpty(pConfirmlist)) {
      success();
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      showEmptyLayout(false);
      mConfirmlist.addAll(pConfirmlist);
    }
    if (!CollectionUtils.isEmpty(mConfirmlist)) {
      showEmptyLayout(false);
      mPrice = result.getPrice();
      ProcessConfirmTimerAdapterNew adapter = new ProcessConfirmTimerAdapterNew(
          MyApplication.getContext(), mConfirmlist,
          new MyOnItemListener(), this, this, mPrice);
      mLv.setAdapter(adapter);
      if (null != getMore && getMore) {
        getMore = false;
        mLv.setSelection(currentPosition);
        if (itemCount == mConfirmlist.size()) {
          showMsg(getString(R.string.no_more_message));
        }
      }
      resetCui(true);
      resetUploadVideo(true);
      itemCount = mConfirmlist.size();
      resetItem(true);
    } else {
      showEmptyLayout(true);
      fail();
      if (null != getMore && getMore) {
        getMore = false;
      }
      resetCui(false);
      resetUploadVideo(false);
      resetEmptyRefresh(false);
      setEmptyLayoutSuccess();
      resetItem(false);
    }
  }

  private void updateTopInfo(BuyConfirmResultBean result) {
    if (null == result) {
      return;
    }
    String tgbzlist = result.getTgbzlist();
    String p_paylist = result.getP_paylist();
    String listcount = result.getListcount();
    String p_dpingjialist = result.getP_dpingjialist();
    BuySeedProcessActivity activity = (BuySeedProcessActivity) mActivity;
    activity.onitem(tgbzlist, p_paylist, listcount, p_dpingjialist);
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

  private Boolean cuiConfim;
  private int cuiPosition;

  public void resetCui(boolean toTargetPosition) {
    if (null != cuiConfim && cuiConfim) {
      cuiConfim = false;
      if (toTargetPosition) {
        mLv.setSelection(cuiPosition);
      }
    }
  }

  //催确认 按钮回调
  private class MyOnItemListener implements OnItemListener {

    @Override
    public void onItem(Object object, int position, int type) {
      if (0 == type) {
        cuiPosition = position;
        cuiConfim = true;

        //催确认
        confirm(object, position);
      }
      if (1 == type) {
        //上传视频凭证
        alertSelectVideoDialog(object);
      }
    }
  }

  private void alertSelectVideoDialog(final Object object) {

    ArrayList<String> objects = new ArrayList<>();
    objects.add(getString(R.string.upload_video));
    objects.add(getString(R.string.select_video_resposity));
    objects.add(getString(R.string.record_video));

    StytledDialog
        .showBottomItemVideoDialog(mActivity, objects, getString(R.string.cancels), true, true,
            new MyItemDialogListener() {
              @Override
              public void onItemClick(String text, int position) {
                selectVideo(position, object);
              }
            });

  }

  private void selectVideo(int position, Object object) {
    if (1 == position) {
      uploadVedio(object, position);
    }
    if (2 == position) {
      PConfirmlistBean confirmlistBean = (PConfirmlistBean) object;
      orderid = confirmlistBean.getOrderid();

      Intent intent = new Intent(mActivity, CameraActivity.class);
      startActivityForResult(intent, CAPTURE_VIDEO);
    }
  }


  private void uploadVedio(Object object, int position) {

    PConfirmlistBean confirmlistBean = (PConfirmlistBean) object;
    orderid = confirmlistBean.getOrderid();

    uploadPosition = position;

    Matisse.from(this)
        .choose(MimeType.ofVideo())
        .countable(false)
        .maxSelectable(1)
        .forResult(VIDEO_CODE);
  }


  @Override
  public String setTip() {
    return getString(R.string.cuiing);
  }

  //崔确认
  private void confirm(Object object, int position) {
    PConfirmlistBean confirmlistBean = (PConfirmlistBean) object;
    try {
      if (!NetUtil.isConnected(mActivity)) {
        throw new DefineException(getString(R.string.NET_ERROR));
      }
      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(mActivity, Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsOrderId = new RequestParams("orderid",
          DebugUtils.convert(confirmlistBean.getOrderid(), ""));
      list.add(paramsOrderId);
      list.add(paramsToken);
      showRequestDialog();
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.CUI_COMFRIM, mCuiComfrimListener,
          RequestNet.POST);
    } catch (DefineException e) {
      if (isAdded()) {
        showMsg(e.getMessage());
      }
      dismissRequestDialog();
      resetCui(false);
    }
  }


  //会员信息回调
  @Override
  public void onCuiPayClick(PConfirmlistBean item, int position) {
    Intent intent = new Intent();
    intent.putExtra("userId", DebugUtils.convert(item.getSellmember(), ""));
    intent.setClass(mActivity, SeedsMemberInfoActivity.class);
    startActivity(intent);
  }

  private Boolean item;

  private void resetItem(boolean toPosition) {
    if (null != item && item) {
      item = false;
      if (toPosition && mPosition != -1) {
        mLv.setSelection(mPosition);
      }
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
      case VIDEO_CODE://上传视频
        if (null != data) {
          List<Uri> uris = Matisse.obtainResult(data);
          String videoPath = VideoUtils.getPath(mActivity, uris.get(0));
          if (TextUitl.isNotEmpty(videoPath) && new File(videoPath).exists()) {
            System.out
                .println("ComfirmFragment.onActivityResult=file大小=" + new File(videoPath).length());
            if (FileUtil.fileMSize(new File(videoPath)) > 50) {
              showMsg(getString(R.string.video_too_big_50m));
            } else {
              uploadTextVideo(videoPath);
            }

          }
        }
        break;
      case 102:
        mPosition = data.getIntExtra("position", -1);
        getInitList();
        break;
      case CAPTURE_VIDEO:
        if (null != data) {
          String url = data.getStringExtra("url");
          File file = new File(url);
          if (file.exists()) {
            uploadTextVideo(url);
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
          System.out.println("quyang1111111111=" + i / 1024 / 1024);

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

  private void uploadPic(File saveFile) {
    try {
      judgeNet();
      showCommitDialog();
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          saveFile,
          SPUtils.getStringValue(getContext(), Config.USER_INFO, Config.TOKEN, ""),
          Urls.UPLOAD_PAY_PIC, "image",
          "pay.jpg", mUploadListener);
    } catch (Exception e) {
      if (isAdded()) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.upload_fail)));
      }


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

  public void showEmptyLayout(boolean show) {
    if (show) {
      mEmptyLayout.setVisibility(View.VISIBLE);
      pull.setVisibility(View.GONE);
    } else {
      mEmptyLayout.setVisibility(View.GONE);
      pull.setVisibility(View.VISIBLE);
    }
  }


  private Boolean uploadVideo;


  public void resetUploadVideo(boolean toTargetPositin) {
    if (null != uploadVideo && uploadVideo) {
      uploadVideo = false;
      if (toTargetPositin) {
        System.out.println("ComfirmFragment.uploadPosition=" + uploadPosition);
        mLv.setSelection(uploadPosition);
      }
    }
  }


  private void uploadTextVideo(String videoPath) {
    try {
      judgeNet();
      showUploadingDialog();
      uploadVideo = true;

      System.out.println("ComfirmFragment.uploadTextVideo=" + SPUtils
          .getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, "") + ", =" + orderid);

      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          new File(videoPath),
          SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""), orderid,
          Urls.upload_video, "video", System.currentTimeMillis() + "_clowm.mp4", videoListener);

    } catch (Exception e) {
      System.out.println("ComfirmFragment.uploadTextVideo=" + e.getMessage());
      if (isAdded()) {
      showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.upload_proof_fail)));
      }
      dismissUploadingDialog();
      resetUploadVideo(true);
    }
  }

  private RequestListener videoListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {

      try {
        dismissUploadingDialog();
        Gson gson = new Gson();
        UploadVideo uploadVideo = gson.fromJson(jsonObject, UploadVideo.class);
        if (uploadVideo != null && "1".equals(uploadVideo.getType())) {
          showMsg(getString(R.string.upload_proof_success));
          getInitList();
        } else {
          resetUploadVideo(true);
          showMsg(DebugUtils.convert(uploadVideo.getMsg(), getString(R.string.upload_proof_fail)));
        }

      } catch (Exception e) {
        if (isAdded()) {
        showMsg(getString(R.string.upload_proof_fail));
        }
        resetUploadVideo(true);
      }
    }

    @Override
    public void onFail(String errorMsf) {
      dismissUploadingDialog();
      resetUploadVideo(true);
      if (isAdded()) {
      showMsg(getString(R.string.upload_proof_fail));
      }
    }
  };


  private void retrofitUploadVideo(String videoPath) {
    Subscriber subscriber = new Subscriber<UploadVideo>() {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {
        dismissUploadingDialog();
        resetUploadVideo(false);
        showMsg("上传失败，" + e.getCause().getMessage() + ".=" + e.getStackTrace().toString());
        System.out.println(
            "ComfirmFragment.onError=" + e.getCause().getMessage() + ".=" + e.getStackTrace()
                .toString());
      }

      @Override
      public void onNext(UploadVideo uploadPicture) {
        dismissUploadingDialog();
        resetUploadVideo(false);
        Gson gson = new Gson();
        String s = gson.toJson(uploadPicture);
        showMsg(getString(R.string.upload_success));
        getInitList();
      }
    };

    File videoFile = new File(videoPath);
    RequestBody requestBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
    MultipartBody.Part part = MultipartBody.Part
        .createFormData("file", videoFile.getName(), requestBody);

    RequestBody token = RequestBody
        .create(MediaType.parse("text/plain"),
            SPUtils.getStringValue(mActivity, Config.USER_INFO, Config.TOKEN, ""));
    RequestBody title = RequestBody.create(MediaType.parse("text/plain"), orderid);

    System.out.println("ComfirmFragment.retrofitUploadVideo=token=" + SPUtils
        .getStringValue(mActivity, Config.USER_INFO, Config.TOKEN, "") + ",orderid=" + orderid);

    MyApplication.rxNetUtils.getUploadService()
        .usUploadVideo(token, title, part)
        .map(new BaseModelFunc<Object>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }

  @Override
  public void onVisible() {
    System.out.println("ComfirmFragment.onVisible");
    initData();
  }


  private void resetGetDownMore() {
    if (getDownMore != null && getDownMore) {
      getDownMore = false;
    }
  }

  private class MyOnClickListener implements OnClickListener {

    private final PConfirmlistBean mItemAtPosition;
    private final int mPosition;

    public MyOnClickListener(PConfirmlistBean itemAtPosition, int position) {
      mItemAtPosition = itemAtPosition;
      mPosition = position;
    }

    @Override
    public void onClick(View v) {
      item = true;
      Intent intent = new Intent(mActivity, ConfirmDetailActivity.class);
      Bundle bundle = new Bundle();
      bundle.putSerializable("item", mItemAtPosition);
      bundle.putInt("position", mPosition);
      bundle.putString("price", mPrice);
      intent.putExtra("item", bundle);
      startActivityForResult(intent, 102);
    }
  }
}

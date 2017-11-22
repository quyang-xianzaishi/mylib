package com.example.administrator.lubanone.rxjava;

import com.example.administrator.lubanone.bean.model.BaseModel;
import com.example.administrator.lubanone.bean.model.CommonModel;
import com.example.administrator.lubanone.bean.model.TrainContinueUpload;
import com.example.administrator.lubanone.bean.model.UploadPicture;
import com.example.administrator.lubanone.bean.model.UsSpreadUploadTextModel;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by hou on 2017/7/8.
 */

public interface UploadService {

  @Multipart
  @POST("Task/uploadPic")
  Observable<String> taskUploadImgs(@Part("token") RequestBody token,
      @PartMap Map<String, MultipartBody.Part> map);

  @Multipart
  @POST("News/uploadnews")
  Observable<BaseModel<UploadPicture>> usUploadVideo(@Part("token") RequestBody token,
      @Part("type") RequestBody type,
      @Part("title") RequestBody title, @Part("introduction") RequestBody introduction,
      @Part("content") RequestBody content, @Part MultipartBody.Part file);

  @Multipart
  @POST("index/uploadproofvideo")
  Observable<BaseModel<Object>> usUploadVideo(@Part("token") RequestBody token,
      @Part("orderid") RequestBody title, @Part MultipartBody.Part file);

  //上传推广标题，内容简介，正文
  @FormUrlEncoded
  @POST("news/uploadtext")
  Observable<BaseModel<UsSpreadUploadTextModel>> spreadUploadText(
      @FieldMap Map<String, String> map);

  //推广取消上传
  @FormUrlEncoded
  @POST("news/cancleupload")
  Observable<BaseModel<CommonModel>> cancelUpload(@FieldMap Map<String, String> map);

  //推广续传
  @FormUrlEncoded
  @POST("news/getchunk")
  Observable<BaseModel<TrainContinueUpload>> continueUpload(@FieldMap Map<String, String> map);


  //推广通知伞下会员
  @FormUrlEncoded
  @POST("News/set_is_notice")
  Observable<BaseModel<CommonModel>> notifyMember(@FieldMap Map<String, String> map);
}

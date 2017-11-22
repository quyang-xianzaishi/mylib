package com.example.administrator.lubanone.rxjava;

import com.example.administrator.lubanone.bean.model.BaseModel;
import com.example.administrator.lubanone.bean.model.CommonModel;
import com.example.administrator.lubanone.bean.model.TrainContinueUpload;
import com.example.administrator.lubanone.bean.model.TrainDataModel;
import com.example.administrator.lubanone.bean.model.TrainDetailsModel;
import com.example.administrator.lubanone.bean.model.TrainMyUploadModel;
import com.example.administrator.lubanone.bean.model.TrainTestDetailsModel;
import com.example.administrator.lubanone.bean.model.TrainTestLIstModel;
import com.example.administrator.lubanone.bean.model.TrainUploadModel;
import com.example.administrator.lubanone.bean.model.TrainVipGrade;
import com.example.administrator.lubanone.bean.model.UploadPicture;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by hou on 2017/8/4.
 */

public interface TrainService {

  //获取培训资料列表
  @FormUrlEncoded
  @POST("train/traindata")
  Observable<BaseModel<TrainDataModel>> getTrainList(@FieldMap Map<String, String> map);

  //获取培训详情
  @FormUrlEncoded
  @POST("train/traindetail")
  Observable<BaseModel<TrainDetailsModel>> getTrainDetails(@FieldMap Map<String, String> map);

  //获取考试中心列表
  @FormUrlEncoded
  @POST("train/testcenter")
  Observable<BaseModel<TrainTestLIstModel>> getTestList(@FieldMap Map<String, String> map);

  //获取考试详情
  @FormUrlEncoded
  @POST("train/testdetail")
  Observable<BaseModel<TrainTestDetailsModel>> getTestDetails(@FieldMap Map<String, String> map);

  //获取我参加过的培训列表
  @FormUrlEncoded
  @POST("train/jointrain")
  Observable<BaseModel<TrainDataModel>> getJoinTrain(@FieldMap Map<String, String> map);

  //获取我的收藏列表
  @FormUrlEncoded
  @POST("train/collecttrain")
  Observable<BaseModel<TrainDataModel>> getCollectTrain(@FieldMap Map<String, String> map);

  //获取我上传的培训列表
  @FormUrlEncoded
  @POST("train/uploadtrain")
  Observable<BaseModel<TrainMyUploadModel>> getUploadList(@FieldMap Map<String, String> map);

  //上传培训主题，简介，试题
  @FormUrlEncoded
  @POST("train/uploaddata")
  Observable<BaseModel<TrainUploadModel>> uploadData(@FieldMap Map<String, String> map);

  //上传培训音视频
  @Multipart
  @POST("train/uploadvideo")
  Observable<BaseModel> uploadMedia(@Part("token") RequestBody token,
      @Part("trainid") RequestBody trainId, @Part("ext") RequestBody ext,
      @Part("chunknum") RequestBody chunkNum, @Part MultipartBody.Part file);

  //点击收藏
  @FormUrlEncoded
  @POST("train/clickcollect")
  Observable<BaseModel<UploadPicture>> getCollectInfo(@FieldMap Map<String, String> map);

  //培训续传
  @FormUrlEncoded
  @POST("train/getchunk")
  Observable<BaseModel<TrainContinueUpload>> continueUpload(@FieldMap Map<String, String> map);

  //培训取消上传
  @FormUrlEncoded
  @POST("train/cancleupload")
  Observable<BaseModel> cancleUpload(@FieldMap Map<String, String> map);

  //删除未通过审核的上传条目
  @FormUrlEncoded
  @POST("train/deltrain")
  Observable<BaseModel<CommonModel>> deleteTrain(@FieldMap Map<String, String> map);

  //获取会员等级
  @FormUrlEncoded
  @POST("train/getlevel")
  Observable<BaseModel<TrainVipGrade>> getVipGrade(@FieldMap Map<String, String> map);

  //通知伞下会员
  @FormUrlEncoded
  @POST("Train/set_is_notice")
  Observable<BaseModel<CommonModel>> notifyMember(@FieldMap Map<String, String> map);

}

package com.example.administrator.lubanone.rxjava;

import com.example.administrator.lubanone.bean.model.BaseModel;
import com.example.administrator.lubanone.bean.model.UsIntroduceBean;
import java.util.Map;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by hou on 2017/7/6.
 */

public interface UsIntroduceService {

//  @Multipart
//  @POST("news/newsDetail")
//  //    sign=bb8faf39292dd54d889a901d6ebf3e20, st=1489133016, uid=00000MultipartBody.Part
//  Observable<BaseModel<UsIntroduceBean>> uploadCircleimg(@Part("uid") RequestBody uid,
//      @Part("st") RequestBody st, @Part("sign") RequestBody sign, @Part("token") RequestBody token,
//      @Part MultipartBody.Part file);

  //获取介绍详情
  @FormUrlEncoded
  @POST("news/newsDetail")
  Observable<BaseModel<UsIntroduceBean>> getIntroduceContent(@FieldMap Map<String, String> map);

  //获取项目制度
  @FormUrlEncoded
  @POST("Taskdetail/questiondetail/qid")
  Observable<BaseModel<UsIntroduceBean>> getInstitution(@FieldMap Map<String, String> map);

  //获取下载详情
  @FormUrlEncoded
  @POST("news/downloaddetail")
  Observable<BaseModel<UsIntroduceBean>> getDownloadContent(@FieldMap Map<String, String> map);

  //获取问答详情
  @FormUrlEncoded
  @POST("news/ask")
  Observable<BaseModel<UsIntroduceBean>> getQaContent(@FieldMap Map<String, String> map);

}

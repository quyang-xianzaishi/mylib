package com.example.administrator.lubanone.rxjava;

import com.example.administrator.lubanone.bean.TaskCollectBean;
import com.example.administrator.lubanone.bean.TasksBean;
import com.example.administrator.lubanone.bean.TasksDetailsBean;
import com.example.administrator.lubanone.bean.model.BaseModel;
import com.example.administrator.lubanone.bean.model.TaskModel;
import com.example.administrator.lubanone.bean.model.UploadPicture;
import java.util.List;
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
 * Created by hou on 2017/8/8.
 */

public interface TaskService {

  //获取任务列表
  @FormUrlEncoded
  @POST("Task/tasklist")
  Observable<BaseModel<TaskModel>> getTaskListOne(@FieldMap Map<String, String> map);

  //获取任务列表
  @FormUrlEncoded
  @POST("Task/selftask")
  Observable<BaseModel<TaskModel>> getTaskListTwo(@FieldMap Map<String, String> map);

  //获取任务列表
  @FormUrlEncoded
  @POST("Task/tasklist")
  Observable<BaseModel<List<TasksBean>>> getTaskListOld(@FieldMap Map<String, String> map);

  //获取任务搜索列表
  @FormUrlEncoded
  @POST("Task/infoSearch")
  Observable<BaseModel<List<TasksBean>>> getTaskSearchList(@FieldMap Map<String, String> map);

  //获取收藏列表
  @FormUrlEncoded
  @POST("Task/collection")
  Observable<BaseModel<TaskCollectBean>> getTaskCollectList(@FieldMap Map<String, String> map);

  //获取收藏搜索列表
  @FormUrlEncoded
  @POST("Task/infoCollectSearch")
  Observable<BaseModel<List<TaskCollectBean>>> getTaskCollectSearchList(@FieldMap Map<String, String> map);

  //获取任务详情
  @FormUrlEncoded
  @POST("Task/taskdetail")
  Observable<BaseModel<TasksDetailsBean>> getTaskDetails(@FieldMap Map<String, String> map);

  //获取任务详情
  @FormUrlEncoded
  @POST("Task/clickcollection")
  Observable<BaseModel<UploadPicture>> getTaskCollectStatus(@FieldMap Map<String, String> map);

  //隐藏分享按钮
  @FormUrlEncoded
  @POST("Task/infoshare")
  Observable<BaseModel<List<String>>> hideShareBtn(@FieldMap Map<String, String> map);

  //上传分享截图
  @Multipart
  @POST("Task/uploadPic")
  Observable<BaseModel<List<String>>> uploadImage(@Part("token") RequestBody token,
      @Part("if_id") RequestBody ifId, @Part MultipartBody.Part file);

}

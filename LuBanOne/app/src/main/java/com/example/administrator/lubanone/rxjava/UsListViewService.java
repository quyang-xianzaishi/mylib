package com.example.administrator.lubanone.rxjava;

import com.example.administrator.lubanone.bean.model.BaseModel;
import com.example.administrator.lubanone.bean.model.CommonModel;
import com.example.administrator.lubanone.bean.model.UploadRecord;
import com.example.administrator.lubanone.bean.model.UsChildLVCommonBean;
import java.util.List;
import java.util.Map;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by hou on 2017/7/7.
 */

public interface UsListViewService {

  @FormUrlEncoded
  @POST("news/newsList")
  Observable<BaseModel<UsChildLVCommonBean>> getListView(@FieldMap Map<String, String> map);

  @FormUrlEncoded
  @POST("News/uploadnewsList")
  Observable<BaseModel<List<UploadRecord>>> getUploadListView(@FieldMap Map<String, String> map);

  @FormUrlEncoded
  @POST("news/delnews")
  Observable<BaseModel<List<CommonModel>>> getDeleteInfo(@FieldMap Map<String, String> map);

}

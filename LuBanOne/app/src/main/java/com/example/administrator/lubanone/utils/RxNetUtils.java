package com.example.administrator.lubanone.utils;

import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.rxjava.TaskService;
import com.example.administrator.lubanone.rxjava.TrainService;
import com.example.administrator.lubanone.rxjava.UploadService;
import com.example.administrator.lubanone.rxjava.UsIntroduceService;
import com.example.administrator.lubanone.rxjava.UsListViewService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by hou on 17/3/1.
 */

public class RxNetUtils {

    private Retrofit retrofit;
    private OkHttpClient client;
    /////////////////
    //所有的网络访问的接口
//    private UploadService uploadSrevice;
    private UsIntroduceService usIntroduceService;
    private UsListViewService usListViewService;
    private UploadService uploadService;
    private TrainService trainService;
    private TaskService taskService;
//    private String url = MyApplication.getHost();
    private String url = Urls.ROOT_URL+"api.php/Home/";

    ///////////////////////
    public RxNetUtils(OkHttpClient client) {
        this.client = client;

        this.retrofit = new Retrofit.Builder()
                .client(client)//设置OkHttpClient为网络客户端
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())//配置转化库，Gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//配置回调库，RxJava
                .build();
    }

    public OkHttpClient getClient() {
        return client;
    }

    public Retrofit getRetrofit() {
        if (retrofit != null)
            return retrofit;
        return new Retrofit.Builder()
                .client(new OkHttpClient())
                //               .baseUrl("http://www.greatchef.com.cn/m/")
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }



//    ///上传图片相关
//    public UploadService getuploadService() {
//        if (uploadSrevice == null) {
//            uploadSrevice = retrofit.create(UploadService.class);
//        }
//        return uploadSrevice;
//    }

    //关于我们详情介绍
    public UsIntroduceService getUsIntroduceService(){
        if (usIntroduceService == null){
            usIntroduceService = retrofit.create(UsIntroduceService.class);
        }
        return usIntroduceService;
    }

    //关于我们列表展示数据
    public UsListViewService getUsListViewService(){
        if (usListViewService == null){
            usListViewService = retrofit.create(UsListViewService.class);
        }
        return usListViewService;
    }

    //上传图片视频
    public UploadService getUploadService(){
        if (uploadService == null){
            uploadService = retrofit.create(UploadService.class);
        }
        return uploadService;
    }

    //获取培训资料
    public TrainService getTrainService(){
        if (trainService == null){
            trainService = retrofit.create(TrainService.class);
        }
        return trainService;
    }

    //任务
    public TaskService getTaskService(){
        if (taskService == null){
            taskService = retrofit.create(TaskService.class);
        }
        return taskService;
    }

}

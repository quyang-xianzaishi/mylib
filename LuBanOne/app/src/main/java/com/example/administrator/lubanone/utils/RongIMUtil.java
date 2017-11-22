package com.example.administrator.lubanone.utils;

import static com.umeng.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.util.Log;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.bean.message.GetRongTokenResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.manager.JyActivityManager;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\6\30 0030.
 */

public class RongIMUtil {

    private Context mContext;
    private String token;
    private MyApplication mMyApplication;

    public RongIMUtil(Context context,String token){
        this.mContext = context;
        this.token = token;
    }

    public  void initRong() {

        /*if (mContext.getApplicationInfo().packageName.equals(getCurProcessName(
            mContext.getApplicationContext())) || "io.rong.push".equals(
                getCurProcessName(getApplicationContext()))) {
            *//**
             * IMKit SDK调用第一步 初始化
             *//*
            RongIM.init(mContext);
        }*/

        if (true) {
            //如果用户已经登录，本地保存用户id,token不为空，连接融云服务器
            RongIM.connect(token
                , new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    //getRongToken();
                }
                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("RongLog", "--onSuccess" + userid);
                }
                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    ToastUtil.showShort(errorCode.toString(), getApplicationContext());
                }
            });
        }
    }

    //请求数据
    private RequestListener getRongTokenListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }

        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<GetRongTokenResultBean> result = GsonUtil
                    .processJson(jsonObject, GetRongTokenResultBean.class);
                if (ResultUtil.isSuccess(result) && null != result && null != result.getResult()) {

                } else {
                    ToastUtil.showShort(ResultUtil.getErrorMsg(result),
                        mContext.getApplicationContext());
                }
            } catch (Exception e) {
                ToastUtil.showShort(mContext.getResources().getString(R.string.get_address_list_fail),
                    mContext.getApplicationContext());
            }
        }

        @Override
        public void onFail(String errorMsf) {
            ToastUtil.showShort(errorMsf,
                mContext.getApplicationContext());
        }
    };


    private void getRongToken(){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        list.add(paramsToken);
        RequestNet requestNet = new RequestNet(mMyApplication, JyActivityManager.getInstance().getCurrentActivity(), list,
            Urls.GET_RONG_TOKEN, getRongTokenListener, RequestNet.POST);

    }


}

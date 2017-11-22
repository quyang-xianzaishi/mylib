package com.example.administrator.lubanone.activity.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.adapter.message.ComplainReasonAdapter;
import com.example.administrator.lubanone.bean.message.ComplainReasonBean;
import com.example.administrator.lubanone.bean.message.GetComplainReasonResultBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\6\30 0030.
 */

public class ComplainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView back;
    private TextView close;
    private ListView complainReason;
    private LinearLayout complain_romour_item;
    private LinearLayout complain_fraud_item;
    private List<ComplainReasonBean> mComplainReasonBeanList;

    private ComplainReasonAdapter mComplainReasonAdapter;

    private String type;
    private String targetId;
    private String complainMemberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_complain);

        if(getIntent()!=null&&getIntent().hasExtra("targetId")){
            targetId = getIntent().getStringExtra("targetId");
        }
        if(getIntent()!=null&&getIntent().hasExtra("type")){
            type = getIntent().getStringExtra("type");
        }
        if(getIntent()!=null&&getIntent().hasExtra("complainId")){
            complainMemberId = getIntent().getStringExtra("complainId");
        }
        initView();
    }

    private void initView() {

        back = (TextView) this.findViewById(R.id.back);
        close = (TextView) this.findViewById(R.id.close);
        complain_romour_item = (LinearLayout) this.findViewById(R.id.complain_rumour);
        complain_fraud_item = (LinearLayout) this.findViewById(R.id.complain_fraud);
        complainReason = (ListView) this.findViewById(R.id.complain_list);

        back.setOnClickListener(this);
        close.setOnClickListener(this);
        complain_romour_item.setOnClickListener(this);
        complain_fraud_item.setOnClickListener(this);

        getComplainReason();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                ComplainActivity.this.finish();
                break;
            case R.id.close:
                ComplainActivity.this.finish();
                break;
            case R.id.complain_rumour:
                //传播谣言
                break;
            case R.id.complain_fraud:
                //欺诈
                break;
            default:
                break;
        }
    }


    private RequestListener getComplainReasonListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<GetComplainReasonResultBean> result = GsonUtil.processJson(jsonObject, GetComplainReasonResultBean.class);
                if (ResultUtil.isSuccess(result)) {
                    if(result.getResult().getData()!=null){
                        mComplainReasonBeanList = new ArrayList<>();
                        for(int i=0;i<result.getResult().getData().size();i++){
                            mComplainReasonBeanList.add(new ComplainReasonBean(Integer.toString(i),
                                result.getResult().getData().get(i).toString()));
                        }
                        mComplainReasonAdapter = new ComplainReasonAdapter(ComplainActivity.this
                            ,mComplainReasonBeanList,targetId,complainMemberId,type);
                        complainReason.setAdapter(mComplainReasonAdapter);
                    }else {
                        ToastUtil.showShort(getResources().getString(R.string.get_complain_reason_fail),
                            getApplicationContext());
                    }
                } else {
                    Toast.makeText(ComplainActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                        getString(R.string.get_complain_reason_fail)), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(ComplainActivity.this, getString(R.string.get_complain_reason_fail)
                    ,Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onFail(String errorMsf) {
            Toast.makeText(ComplainActivity.this, DebugUtils.convert(errorMsf,
                getString(R.string.delete_group_member_fail)),Toast.LENGTH_LONG).show();
        }
    };

    private void getComplainReason(){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        list.add(paramsToken);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), ComplainActivity.this, list,
            Urls.GET_COMPLAIN_LIST, getComplainReasonListener, RequestNet.POST);
    }

}

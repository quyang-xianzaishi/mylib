package com.example.administrator.lubanone.activity.message;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import com.example.qlibrary.utils.WindoswUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\6\29 0029.
 */

public class FriendTestActivity extends AppCompatActivity implements View.OnClickListener  {

    private TextView send;
    private TextView back;
    private EditText applyMessage;
    private ImageView editClear;
    private String applyId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_friend_test);

        if(getIntent()!=null&&getIntent().hasExtra("applyId")){
            applyId = getIntent().getStringExtra("applyId");
        }
        initView();
    }

    private void initView(){

        back = (TextView) this.findViewById(R.id.activity_back);
        send = (TextView) this.findViewById(R.id.activity_send);
        applyMessage = (EditText) this.findViewById(R.id.apply_friend_message);
        editClear = (ImageView) this.findViewById(R.id.edit_clear);

        send.setOnClickListener(this);
        back.setOnClickListener(this);
        editClear.setOnClickListener(this);

        applyMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null&&s.length()>0){
                    editClear.setVisibility(View.VISIBLE);
                    send.setTextColor(getResources().getColor(R.color.white));
                    send.setClickable(true);
                }else {
                    editClear.setVisibility(View.GONE);
                    send.setTextColor(getResources().getColor(R.color._888888));
                    send.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.activity_back:
                FriendTestActivity.this.finish();
                break;
            case R.id.activity_send:
                //发送好友申请
                applyFriend();
                break;
            case R.id.edit_clear:
                applyMessage.setText("");
                break;
            default:
                break;

        }
    }

    private RequestListener applyListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }

        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
                commitResult(result);
            } catch (Exception e) {
                ToastUtil.showShort(getResources().getString(R.string.apply_friend_fail),
                    getApplicationContext());
            }

        }

        @Override
        public void onFail(String errorMsf) {
            ToastUtil.showShort(getResources().getString(R.string.apply_friend_fail),
                getApplicationContext());
        }
    };

    private void applyFriend(){
        if(applyMessage.getText()!=null&&applyMessage.getText().length()>0){
            //调后台接口申请添加好友
            List<RequestParams> list = new ArrayList<>();
            RequestParams paramsToken = new RequestParams(Config.TOKEN,
                SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
            RequestParams paramsUserId = new RequestParams("userid", applyId);
            RequestParams paramsApplyMess = new RequestParams("applymess",
                applyMessage.getText().toString().trim());
            list.add(paramsToken);
            list.add(paramsUserId);
            list.add(paramsApplyMess);
            RequestNet requestNet = new RequestNet((MyApplication) getApplication(), this, list,
                Urls.APPLY_FRIEND, applyListener, RequestNet.POST);
        }else{
            //验证信息为空
            ToastUtil.showShort(this.getResources().getString(R.string.apply_friend_message_null),
                getApplicationContext());
        }
    }

    private void commitResult(Result<Object> result) {
        if (ResultUtil.isSuccess(result)) {
            showSuccessDialog();
        } else {
            Toast.makeText(this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                getString(R.string.apply_friend_fail)),Toast.LENGTH_LONG).show();
        }
    }


    private void showSuccessDialog() {

        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText(getResources().getString(R.string.apply_friend_success));
        dialog.setContentView(view);
        dialog.getWindow()
            .setLayout(WindoswUtil.getWindowWidth(this) - 200, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                finish();
            }
        }, 2000);

    }
}

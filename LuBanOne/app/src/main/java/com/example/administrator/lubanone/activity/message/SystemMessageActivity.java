package com.example.administrator.lubanone.activity.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.lubanone.R;
import com.example.qlibrary.utils.ToastUtil;

/**
 * Created by Administrator on 2017\6\30 0030.
 */

public class SystemMessageActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView back;
    private TextView send;
    private EditText systemMessageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        initView();
    }

    private void initView() {

        back = (TextView) this.findViewById(R.id.back);
        send = (TextView) this.findViewById(R.id.send);
        systemMessageContent = (EditText) this.findViewById(R.id.send_system_message_content);

        back.setOnClickListener(this);
        send.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back:
                SystemMessageActivity.this.finish();
                break;
            case R.id.send:
                //发送系统消息
                sendSysMessage();
                break;
            default:
                break;
        }

    }

    private void sendSysMessage(){
        if(systemMessageContent.getText()!=null&&systemMessageContent.getText().length()>0){
            if(systemMessageContent.getText().length()<=200){
                //请求后台

            }else{
                ToastUtil.showShort(this.getResources().getString(R.string.system_message_over),
                        getApplicationContext());
            }
        }else{
            ToastUtil.showShort(this.getResources().getString(R.string.system_message_null),
                    getApplicationContext());
        }
    }
}

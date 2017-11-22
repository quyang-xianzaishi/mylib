package com.example.administrator.lubanone.activity.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\7\3 0003.
 */

public class GroupNoticeActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView back;
    private TextView save;
    private TextView groupNoticeText;
    private EditText groupNoticeEdit;

    private String notice;
    private String groupmaster;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      Window window = this.getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_group_notice);

        if(getIntent()!=null&&getIntent().hasExtra("groupNotice")){
            notice = getIntent().getStringExtra("groupNotice");
        }
        if(getIntent()!=null&&getIntent().hasExtra("groupmaster")){
            groupmaster = getIntent().getStringExtra("groupmaster");
        }
        if(getIntent()!=null&&getIntent().hasExtra("groupId")){
            groupId = getIntent().getStringExtra("groupId");
        }
        initView();
    }


    public void initView() {
        back = (TextView) this.findViewById(R.id.back);
        save = (TextView) this.findViewById(R.id.save);
        groupNoticeEdit = (EditText) this.findViewById(R.id.group_notice_edit);
        groupNoticeText = (TextView) this.findViewById(R.id.group_notice_text);

        back.setOnClickListener(this);
        save.setOnClickListener(this);
        groupNoticeEdit.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          if(s!=null&&s.length()>0){
            save.setTextColor(getResources().getColor(R.color.white));
            save.setClickable(true);
          }else {
            save.setTextColor(getResources().getColor(R.color._888888));
            save.setClickable(false);
          }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
      });

        if(groupmaster!=null&&groupmaster.length()>0){
            if(groupmaster.equals("1")){
                //群主
                groupNoticeEdit.setVisibility(View.VISIBLE);
                groupNoticeText.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                if(notice!=null&&notice.length()>0){
                    groupNoticeEdit.setText(notice);
                    save.setTextColor(getResources().getColor(R.color.white));
                    save.setClickable(true);
                }
            }else if (groupmaster.equals("0")){
                //非群主
                groupNoticeEdit.setVisibility(View.GONE);
                groupNoticeText.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                if(notice!=null&&notice.length()>0){
                    groupNoticeText.setText(notice);
                }
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                GroupNoticeActivity.this.finish();
                break;
          case R.id.save:
            if(groupNoticeEdit.getText()!=null&&groupNoticeEdit.getText().length()>0){
              if(groupNoticeEdit.getText().toString().equals(notice)){
                ToastUtil.showShort(this.getResources().getString(R.string.set_group_notice_same),
                    getApplicationContext());
              }else if(groupNoticeEdit.getText().length()>200){
                ToastUtil.showShort(this.getResources().getString(R.string.set_group_notice_over),
                    getApplicationContext());
              }else {
                //调后台接口设置名称
                setGroupNotice(groupNoticeEdit.getText().toString());
              }
            }else{
              ToastUtil.showShort(this.getResources().getString(R.string.set_group_notice_null),
                  getApplicationContext());
            }
            break;
            default:
                break;
        }
    }

  private RequestListener setGroupNoticeListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {
    }
    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
        if (ResultUtil.isSuccess(result)) {
          ToastUtil.showShort(getResources().getString(R.string.set_group_notice_success),
              getApplicationContext());
          GroupNoticeActivity.this.finish();
        } else {
          Toast.makeText(GroupNoticeActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
              getString(R.string.set_group_notice_fail)),Toast.LENGTH_LONG).show();
        }
      } catch (Exception e) {
        Toast.makeText(GroupNoticeActivity.this, getString(R.string.set_group_notice_fail)
            ,Toast.LENGTH_LONG).show();
      }
    }
    @Override
    public void onFail(String errorMsf) {
      Toast.makeText(GroupNoticeActivity.this, DebugUtils.convert(errorMsf,
          getString(R.string.set_group_notice_fail)),Toast.LENGTH_LONG).show();
    }
  };


  private void setGroupNotice(String notice){
      List<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
      RequestParams paramsGroupId = new RequestParams("groupid", groupId);
      RequestParams paramsGroupNotice = new RequestParams("groupnotice", notice);
      list.add(paramsToken);
      list.add(paramsGroupId);
      list.add(paramsGroupNotice);
      RequestNet requestNet = new RequestNet((MyApplication)getApplication(), GroupNoticeActivity.this, list,
          Urls.SET_GROUP_NOTICE, setGroupNoticeListener, RequestNet.POST);
    }
}

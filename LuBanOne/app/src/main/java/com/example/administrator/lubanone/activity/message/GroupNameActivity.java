package com.example.administrator.lubanone.activity.message;

import android.net.Uri;
import android.os.Bundle;
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
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\6\30 0030.
 */

public class GroupNameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView back;
    private EditText groupName;
    private TextView groupNameSet;
    private TextView groupNameText;
    private String name;
    private String groupmaster;
    private String groupId;
    private String groupimg;
    private ImageView editClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_group_name);
        if(getIntent()!=null&&getIntent().hasExtra("groupName")){
            name = getIntent().getStringExtra("groupName");
        }
        if(getIntent()!=null&&getIntent().hasExtra("groupmaster")){
            groupmaster = getIntent().getStringExtra("groupmaster");
        }
        if(getIntent()!=null&&getIntent().hasExtra("groupId")){
            groupId = getIntent().getStringExtra("groupId");
        }
        if(getIntent()!=null&&getIntent().hasExtra("groupimg")){
            groupimg = getIntent().getStringExtra("groupimg");
        }
        initView();
    }

    private void initView() {

        back = (TextView) this.findViewById(R.id.back);
        groupName = (EditText) this.findViewById(R.id.group_name);
        groupNameSet = (TextView) this.findViewById(R.id.group_name_set);
        groupNameText = (TextView) this.findViewById(R.id.group_name_text);
        editClear = (ImageView) this.findViewById(R.id.edit_clear);

        if(groupmaster!=null&&groupmaster.length()>0){
            if(groupmaster.equals("1")){
                //群主
                groupNameSet.setVisibility(View.VISIBLE);
                groupNameText.setVisibility(View.GONE);
                groupName.setVisibility(View.VISIBLE);
                if(name!=null&&name.length()>0){
                    groupName.setText(name);
                    groupNameSet.setTextColor(getResources().getColor(R.color.white));
                    groupNameSet.setClickable(true);
                    editClear.setVisibility(View.VISIBLE);
                }
            }else if (groupmaster.equals("0")){
                //非群主
                groupNameSet.setVisibility(View.INVISIBLE);
                groupNameText.setVisibility(View.VISIBLE);
                groupName.setVisibility(View.GONE);
                if(name!=null&&name.length()>0){
                    groupNameText.setText(name);
                }
            }
        }
        back.setOnClickListener(this);
        groupNameSet.setOnClickListener(this);
        editClear.setOnClickListener(this);

        groupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null&&s.length()>0){
                    editClear.setVisibility(View.VISIBLE);
                    groupNameSet.setTextColor(getResources().getColor(R.color.white));
                    groupNameSet.setClickable(true);
                }else {
                    editClear.setVisibility(View.GONE);
                    groupNameSet.setTextColor(getResources().getColor(R.color._888888));
                    groupNameSet.setClickable(false);
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
            case R.id.back:
                GroupNameActivity.this.finish();
                break;
            case R.id.group_name_set:
                //设置群聊名称
                setGroupName();
                break;
            case R.id.edit_clear:
                groupName.setText("");
                break;
            default:
                break;
        }
    }

    private void setGroupName(){
        if(groupName.getText()!=null&&groupName.getText().length()>0){
            if(groupName.getText().toString().equals(name)){
                ToastUtil.showShort(this.getResources().getString(R.string.set_group_name_same),
                    getApplicationContext());
            }else {
                //调后台接口设置名称
                setName(groupName.getText().toString());
            }
        }else{
            ToastUtil.showShort(this.getResources().getString(R.string.group_name_null),
                    getApplicationContext());
        }

    }

    private RequestListener setGroupNameListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }
        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<Object> result = GsonUtil.processJson(jsonObject, Object.class);
                if (ResultUtil.isSuccess(result)) {
                    ToastUtil.showShort(getResources().getString(R.string.set_group_name_success),
                        getApplicationContext());
                    RongIM.getInstance().refreshGroupInfoCache(new Group(groupId,groupName.getText().toString(),
                        Uri.parse(groupimg)));
                    GroupNameActivity.this.finish();
                } else {
                    Toast.makeText(GroupNameActivity.this, DebugUtils.convert(ResultUtil.getErrorMsg(result),
                        getString(R.string.set_group_name_fail)),Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(GroupNameActivity.this, getString(R.string.set_group_name_fail)
                    ,Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onFail(String errorMsf) {
            Toast.makeText(GroupNameActivity.this, DebugUtils.convert(errorMsf,
                getString(R.string.set_group_name_fail)),Toast.LENGTH_LONG).show();
        }
    };

    private void setName(String name){
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getApplicationContext(), Config.USER_INFO, Config.TOKEN, ""));
        RequestParams paramsGroupId = new RequestParams("groupid", groupId);
        RequestParams paramsGroupName = new RequestParams("groupname", name);
        list.add(paramsToken);
        list.add(paramsGroupId);
        list.add(paramsGroupName);
        RequestNet requestNet = new RequestNet((MyApplication)getApplication(), GroupNameActivity.this, list,
            Urls.SET_GROUP_NAME, setGroupNameListener, RequestNet.POST);
    }


}

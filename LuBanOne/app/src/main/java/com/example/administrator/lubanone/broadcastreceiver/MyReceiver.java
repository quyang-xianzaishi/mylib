package com.example.administrator.lubanone.broadcastreceiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import com.example.administrator.lubanone.activity.land.LandActivity;
import com.example.administrator.lubanone.activity.message.MessageActivity;
import com.example.administrator.lubanone.activity.message.OrderMessageActivity;
import com.example.administrator.lubanone.activity.message.RecommendFriendListActivity;
import com.example.administrator.lubanone.activity.message.SystemMessageListActivity;
import com.example.administrator.lubanone.activity.message.TrainMessageActivity;
import com.example.administrator.lubanone.activity.register.MainActivity;
import com.example.administrator.lubanone.activity.register.SplashActivity;
import com.example.administrator.lubanone.utils.ExampleUtil;
import com.example.qlibrary.utils.SPUtils;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JIGUANG-Example";

  private String typeid;//通知消息类型

  @Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        Log.e(TAG, "[MyReceiver] 接收到推送下来的通知标题: " + bundle.getString(JPushInterface.EXTRA_TITLE));
        Log.e(TAG, "[MyReceiver] 接收到推送下来的通知内容: " + bundle.getString(JPushInterface.EXTRA_ALERT));
				Log.e(TAG, "[MyReceiver] 接收到推送下来的通知EXTRA_ALERT_TYPE: " + bundle.getString(JPushInterface.EXTRA_ALERT_TYPE));
				Log.e(TAG, "[MyReceiver] 接收到推送下来的通知EXTRA_EXTRA: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				Log.e(TAG, "[MyReceiver] 用户点击打开了通知");
				JSONObject jsonObject = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
				if(jsonObject!=null&&jsonObject.has("typeid")){
					typeid = jsonObject.getString("typeid");
				}
				//打开自定义的Activity
				receiveNotification(context,bundle,typeid);
				Log.e(TAG, "[MyReceiver] 用户点击打开了通知typeid " + typeid);
				/*Intent i = new Intent(context, MainActivity.class);
				i.putExtras(bundle);
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);*/

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}
	}

	private boolean isRuningApp(Context context) {
		boolean isAppRunning=false;
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list=am.getRunningTasks(100);
		for(RunningTaskInfo info:list){
			if(info.topActivity.getPackageName().equals("com.example.administrator.lubanone")
					&&info.baseActivity.getPackageName().equals(context.getPackageName())){
				isAppRunning=true;
				break;
			}
		}
		return isAppRunning;
	}

	private void  receiveNotification(Context context,Bundle bundle,String typeid){

		if(isRuningApp(context)){
			// 打开自定义的Activity
			if(SPUtils.getStringValue(context.getApplicationContext(), "user_info", "token", "")==null
					||SPUtils.getStringValue(context.getApplicationContext(), "user_info", "token", "").length()<=0){
				Intent intent1 = new Intent(context, LandActivity.class);
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
				context.startActivity(intent1);
			}else{
        Intent i = null;
        if(typeid!=null&&typeid.length()>0){
          int type = Integer.parseInt(typeid);
          if(type>=0&&type<100){
            //订单消息
            i = new Intent(context, OrderMessageActivity.class);
          }else if(type>=100&&type<200){
            //系统消息
            i = new Intent(context, SystemMessageListActivity.class);
          }else if(type>=200&&type<300){
            //培训消息
            i = new Intent(context, TrainMessageActivity.class);
          }else if(type>=300&&type<400){
            //推荐好友
            i = new Intent(context, RecommendFriendListActivity.class);
          }else {
            i = new Intent(context, MessageActivity.class);
          }
        }else {
          i = new Intent(context, MessageActivity.class);
        }
				i.putExtras(bundle);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
			}
		}else {
			Intent i = new Intent(context, SplashActivity.class);
			i.putExtras(bundle);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);
		}
	}


}

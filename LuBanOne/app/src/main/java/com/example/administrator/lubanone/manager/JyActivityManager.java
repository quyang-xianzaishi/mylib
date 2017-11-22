package com.example.administrator.lubanone.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 *
 */
public class JyActivityManager {

    private static JyActivityManager sInstance = new JyActivityManager();
    private WeakReference<Activity> sCurrentActivityWeakRef;
    private static Stack<Activity> activityStack;


    private JyActivityManager() {

    }

    public static JyActivityManager getInstance() {
        return sInstance;
    }

    //获取当前Activity
    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        if (getCurrentActivity() != null) {
            if(!getCurrentActivity().isFinishing()){
                finishActivity(getCurrentActivity());
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if(!activity.isFinishing()){
                activityStack.remove(activity);
                activity.finish();
                activity = null;
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            if(activity.isFinishing()){
                if(activityStack.contains(activity)){
                    activityStack.remove(activity);
                    activity = null;
                }
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishNameActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                if(!activityStack.get(i).isFinishing()){
                    activityStack.get(i).finish();
                }
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void exitApp(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }

    //得到当前activity名称
    public String getRunningActivityName(){
        String runningActivity = null;
        if(getCurrentActivity()!=null&&!getCurrentActivity().isFinishing()){
            ActivityManager activityManager=(ActivityManager)getCurrentActivity().getSystemService(Context.ACTIVITY_SERVICE);
            runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        }
        return runningActivity;
    }

    //
    public void closeActivityBesideMainAc(){
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                if(!activityStack.get(i).isFinishing()){
                    Log.e("JyActivityManager","activityStack.get(i).getClass().getName() "
                            +activityStack.get(i).getClass().getName());
                    if(!activityStack.get(i).getClass().getName().equals("com.example.administrator.lubanone.activity.register.MainActivity")){
                        activityStack.get(i).finish();

                    }
                }
            }
        }
    }
}

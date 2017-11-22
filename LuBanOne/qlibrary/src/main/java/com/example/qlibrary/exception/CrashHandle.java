package com.example.qlibrary.exception;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.example.qlibrary.R;
import com.example.qlibrary.interfaces.CrashListener;
import com.example.qlibrary.utils.NetUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/31.
 */

public class CrashHandle implements Thread.UncaughtExceptionHandler {

    private static String PATH = Environment.getExternalStorageDirectory().getPath() + "/crash/logs/";

    private static String FILE_NAME = "crash";

    private static String FILE_NAME_SUFFIEX = ".trace";


    private Context mContext;

    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    private CrashListener mCrashListener;

    private static CrashHandle mCrashHanle;

    private CrashHandle() {
    }

    public static CrashHandle getInstance() {
        if (null == mCrashHanle) {
            synchronized (CrashHandle.class) {
                if (null == mCrashHanle) {
                    mCrashHanle = new CrashHandle();
                }
            }
        }
        return mCrashHanle;
    }

    public void init(Context context, CrashListener listener) {
        this.mCrashListener = listener;
        this.mContext = context.getApplicationContext();
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


    private void sendException2Server(Throwable ex) {
        if (!NetUtil.isConnected(mContext)) {
            return;
        }
        //请求网络，发送crash
        final StringBuffer sb = new StringBuffer();
        sb.append(getPhoneInfos() + ",  ");
        Throwable cause = ex.getCause();
        StackTraceElement[] stackTrace = cause.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            String s = element.toString();
            sb.append(s + ", ");
        }
        String errorMsg = sb.toString();

        //请求网络
        mCrashListener.onCrashListener(errorMsg);
    }

    private void dumpException2SD(Throwable e) {
        if (Environment.MEDIA_UNMOUNTED.equals(Environment.getExternalStorageState())) {
            return;
        }

        File file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        long currentTimeMillis = System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date(currentTimeMillis));

        File newFile = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIEX);

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(newFile)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            e.printStackTrace(pw);
            pw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private void dumpPhoneInfo(PrintWriter pw) {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            pw.println("手机系统： Android");
            pw.println("APP version_name: " + pi.versionName);
            pw.println("App version_code: " + pi.versionCode);
            pw.println("手机制造商：" + Build.MANUFACTURER);
            pw.println("手机型号： " + Build.MODEL);
            pw.println("cup架构： " + Build.CPU_ABI);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            pw.println("获取手机信息时失败： " + e.getMessage());
        }
    }

    public String getPhoneInfos() {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = null;
        StringBuffer sb = new StringBuffer();
        try {
            pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            sb.append("手机系统： Android" + ", ");
            sb.append("APP version_name: " + pi.versionName + ", ");
            sb.append("App version_code: " + pi.versionCode + ", ");
            sb.append("手机制造商：" + Build.MANUFACTURER + ", ");
            sb.append("手机型号： " + Build.MODEL + ", ");
            sb.append("cup架构： " + Build.CPU_ABI + "  ");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            sb.append("获取手机信息失败：" + e.getMessage());
        }
        return sb.toString();
    }
}

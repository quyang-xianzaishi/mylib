package com.example.administrator.lubanone.utils;


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.administrator.lubanone.R;
import com.jingchen.pulltorefresh.PullToRefreshLayout;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by hou on 2017/6/22.
 *
 * 设置下拉动画
 */
public class RefreshSetting {

    public static ListView setListViewRefresh(PullToRefreshLayout pullLayout, Context context, PullToRefreshLayout.OnPullListener listener) {

        // 设置带gif动画的上拉头与下拉头
        try {
            pullLayout.setGifRefreshView(new GifDrawable(context.getResources(), R.drawable.refresh_test));
            //            pullLayout.setGifLoadmoreView(new GifDrawable(context.getResources(), R.drawable.wt));
            //            可以用下面的方式进行自定义刷新/加载时候的View设计
            //            View view = LayoutInflater.from(context).inflate(R.layout.refresh_pic, null);
            //            TextView text = (TextView) view.findViewById(R.id.refresh_pic_text_time);
            //            text.setText(Time.getCurrentTimezone());
            //            pullLayout.setCustomLoadmoreView(view);
            //            pullLayout.setCustomRefreshView(view);
        } catch (Resources.NotFoundException e) {
            //添加文件读取异常捕获
            Log.d(e.getMessage(), "gif文件读取异常");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(e.getMessage(), "IO异常");
            e.printStackTrace();
        }
        return (ListView) pullLayout.getPullableView();
    }

    public static View setListProcessRefresh(Context context, PullToRefreshLayout pullLayout) {
        GifDrawable gifDrawable = null;
        try {
            gifDrawable = new GifDrawable(context.getResources(), R.drawable.refresh_test);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PullToRefreshLayout.GifOnPullProcessListener listener = pullLayout.new GifOnPullProcessListener(gifDrawable);
        pullLayout.setGifRefreshView(gifDrawable);
        pullLayout.setOnRefreshProcessListener(listener);
        return pullLayout.getPullableView();
    }


}



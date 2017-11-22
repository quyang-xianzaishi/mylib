package com.example.administrator.lubanone.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.R;

import java.util.List;

/**
 * Created by Administrator on 2017/6/20.
 */

public class SplashViewPagerAdapter extends PagerAdapter {

    private List<String> mStrings;

    private Activity mContext;


    public SplashViewPagerAdapter(List<String> list, Activity context) {
        this.mStrings = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        //return Integer.MAX_VALUE;
        return mStrings.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Drawable[] fail = new Drawable[]{mContext.getResources().getDrawable(R.mipmap.splash_viewpager_fail1)
            ,mContext.getResources().getDrawable(R.mipmap.splash_viewpager_fail1),
            mContext.getResources().getDrawable(R.mipmap.splash_viewpager_fail1)};
        position = position % mStrings.size();
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        Glide.with(mContext)
                .load(mStrings.get(position))
                .placeholder(fail[position])
                .error(fail[position])
                .diskCacheStrategy(DiskCacheStrategy.NONE).
                into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}

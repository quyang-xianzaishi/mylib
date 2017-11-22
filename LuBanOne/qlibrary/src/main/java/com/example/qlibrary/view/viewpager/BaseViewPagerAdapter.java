package com.example.qlibrary.view.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by quyang
 */

public abstract class BaseViewPagerAdapter<Type> extends PagerAdapter {

    private List<Type> mList;

    public BaseViewPagerAdapter(List<Type> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getView();
        initData(view, position, mList.get(position));
        container.addView(view);
        return view;
    }

    /**
     * 仅获取view
     *
     * @return
     */
    protected abstract View getView();

    /**
     * 适配数据
     *
     * @param view
     * @param position
     * @param type
     */
    protected abstract void initData(View view, int position, Type type);
}

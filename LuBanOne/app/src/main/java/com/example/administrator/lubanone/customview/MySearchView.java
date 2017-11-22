package com.example.administrator.lubanone.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.lubanone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hou on 2017/6/23.
 */

public class MySearchView<T> extends LinearLayout {

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 编辑框
     */
    private EditText mEditText;
    /**
     * 清除按钮
     */
    private ImageView mClearImg;
    /**
     * 搜索图标
     */
    private ImageView mSearchBarImg;
    /**
     * 适配器
     */
    private BaseAdapter mAdapter;
    /**
     * 数据源
     */
    private List<T> mDatas = new ArrayList<T>();
    /**
     * 数据源副本
     */
    private List<T> mDupDatas = new ArrayList<T>();

    /**
     * 筛选后的数据源
     */
    private List<T> mFilterDatas = new ArrayList<T>();
    /**
     * 筛选后的数据源副本
     */
    private List<T> mDupFilterDatas = new ArrayList<T>();

    /**
     * 搜索图标
     */
    private Bitmap mSearchIcon;
    /**
     * 搜索框距离左边边距
     */
    private int mSearchIconMarginLeft;
    /**
     * 搜索框距离右边边距
     */
    private int mSearchIconMarginRight;
    /**
     * 清除图标
     */
    private Bitmap mClearIcon;
    /**
     * 清除图标距离左边边距
     */
    private int mClearIconMarginLeft;
    /**
     * 清除图标距离右边边距
     */
    private int mClearIconMarginRight;
    /**
     * 搜索文字大小
     */
    private int mSearchTextSize;
    /**
     * 搜索文字颜色
     */
    private int mSearchTextColor;

    /**
     * @param <T>
     */
    public interface SearchDatas<T> {
        /**
         * 参数一：全部数据，参数二：筛选后的数据，参数三：输入的内容
         *
         * @param datas
         * @param filterdatas
         * @param inputstr
         * @return
         */
        List<T> filterDatas(List<T> datas, List<T> filterdatas, String inputstr);
    }

    /**
     * 回调
     */
    private SearchDatas<T> mListener;

    /**
     * 设置回调
     *
     * @param listener
     */
    public void setSearchDataListener(SearchDatas<T> listener) {
        mListener = listener;

    }


    public MySearchView(Context context) {
        this(context, null);
    }

    public MySearchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MySearchView);
        Drawable searchD = ta.getDrawable(R.styleable.MySearchView_SearchBarIconSrc);
        mSearchIcon = drawableToBitmap(searchD);
        mSearchIconMarginLeft = px2dip(context, ta.getDimensionPixelOffset(R.styleable.MySearchView_SearchBarIconMarginLeft, 0));
        mSearchIconMarginRight = px2dip(context, ta.getDimensionPixelOffset(R.styleable.MySearchView_SearchBarIconMarginRight, 0));
        Drawable clearD = ta.getDrawable(R.styleable.MySearchView_ClearIconSrc);
        mClearIcon = drawableToBitmap(clearD);
        mClearIconMarginLeft = px2dip(context, ta.getDimensionPixelOffset(R.styleable.MySearchView_ClearIconMarginLeft, 0));
        mClearIconMarginRight = px2dip(context, ta.getDimensionPixelOffset(R.styleable.MySearchView_ClearIconMarginRight, 0));
        mSearchTextSize = px2sp(context, ta.getDimensionPixelOffset(R.styleable.MySearchView_SearchTextSize, 0));
        mSearchTextColor = ta.getColor(R.styleable.MySearchView_SearchTextColor, 0);
        ta.recycle();
        //绑定布局文件
        LayoutInflater.from(context).inflate(R.layout.searchview_layout, this);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mEditText = (EditText) findViewById(R.id.task_search_et);
        mSearchBarImg = (ImageView) findViewById(R.id.task_search_icon);
        mClearImg = (ImageView) findViewById(R.id.task_search_clear);
        // 处理自定义属性
        if (0 != mSearchIconMarginLeft || 0 != mSearchIconMarginRight) {
            mSearchBarImg.setPadding(mSearchIconMarginLeft, 0, mSearchIconMarginRight, 0);
        }
        if (0 != mClearIconMarginLeft || 0 != mClearIconMarginRight) {
            mClearImg.setPadding(mClearIconMarginLeft, 0, mClearIconMarginRight, 0);
        }
        if (null != mSearchIcon) {
            mSearchBarImg.setImageBitmap(mSearchIcon);
        }
        if (null != mClearIcon) {
            mClearImg.setImageBitmap(mClearIcon);
        }
        if (0 != mSearchTextSize) {
            mEditText.setTextSize(mSearchTextSize);
        }
        if (0 != mSearchTextColor) {
            mEditText.setTextColor(mSearchTextColor);
        }
        // 清空按钮处理事件
        mClearImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
                mClearImg.setVisibility(View.GONE);
                if (null != mDatas) {
                    mDatas.clear();
                }
                mDatas.addAll(mDupDatas);
                mAdapter.notifyDataSetChanged();
                resetDatas();
            }
        });
        // 搜索栏处理事件
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 获取筛选后的数据
                if(mDupDatas!=null&&charSequence.toString()!=null&&mListener!=null){
                    mFilterDatas = mListener.filterDatas(mDupDatas, mFilterDatas, charSequence.toString());
                }
                if (charSequence.toString().length() > 0 && !charSequence.toString().equals("")) {
                    mClearImg.setVisibility(View.VISIBLE);
                } else {
                    mClearImg.setVisibility(View.GONE);
                }
                if (null != mDatas) {
                    mDatas.clear();
                }
                mDatas.addAll(mFilterDatas);
                if(mAdapter!=null){
                    mAdapter.notifyDataSetChanged();
                }
                resetDatas();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    /**
     * 获取筛选后的数据
     *
     * @return
     */
    public List<T> getFilterDatas() {
        return (null != mDupFilterDatas && mDupFilterDatas.size() > 0) ? mDupFilterDatas : mDupDatas;
    }

    /**
     * 重置数据
     */
    private void resetDatas() {
        if (null != mFilterDatas) {
            if (null != mDupFilterDatas) {
                mDupFilterDatas.clear();
                mDupFilterDatas.addAll(mFilterDatas);
            }
            mFilterDatas.clear();
        }
    }

    /**
     * 设置数据源
     *
     * @param datas
     */
    public void setDatas(List<T> datas) {
        if (null == datas) {
            return;
        }
        if (null != mDatas) {
            mDatas.clear();
        }
        if (null != mDupDatas) {
            mDupDatas.clear();
        }
        mDatas = datas;
        mDupDatas.addAll(mDatas);
    }


    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        if (null == adapter) {
            return;
        }
        mAdapter = adapter;
    }

    /**
     * drawable 转 bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (null == drawable) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 将px值转换成dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);

    }

}

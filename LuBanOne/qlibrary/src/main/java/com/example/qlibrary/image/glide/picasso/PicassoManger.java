package com.example.qlibrary.image.glide.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.qlibrary.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2017/6/17.
 */

public class PicassoManger {

    private static PicassoManger instance;

    private PicassoManger() {
    }

    public static PicassoManger getInstance() {
        if (null == instance) {
            synchronized (PicassoManger.class) {
                if (null == instance) {
                    instance = new PicassoManger();
                }
            }
        }
        return instance;
    }


    /**
     * picasso加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void picasso(Context context, String url, ImageView imageView) {
        Picasso.with(context)
                .load(url)
                .error(R.drawable.em_add_public_group)
                .placeholder(R.drawable.em_chat_video_call_normal)
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }



}

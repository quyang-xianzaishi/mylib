package com.example.administrator.lubanone.adapter.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;

import java.util.ArrayList;

/**
 * Created by hou on 2017/6/30.
 */

public class TaskUploadGvAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> listUrls;

    public TaskUploadGvAdapter(Context context,ArrayList<String> urls) {
        this.context = context;
        this.listUrls = urls;
    }

    @Override
    public int getCount() {
        return listUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return listUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_view_image,null);
            vh = new ViewHolder();
            vh.imageView = (ImageView) convertView.findViewById(R.id.item_grid_view_image_view);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(getItem(position)).into(vh.imageView);

        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
    }
}

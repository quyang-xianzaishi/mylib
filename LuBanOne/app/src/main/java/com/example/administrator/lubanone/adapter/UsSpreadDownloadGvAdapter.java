package com.example.administrator.lubanone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.model.UsChildLVCommonBean;
import com.example.administrator.lubanone.bean.model.UsChildLVCommonBean.UsChildLvList;
import java.util.List;

/**
 * Created by hou on 2017/8/31.
 */

public class UsSpreadDownloadGvAdapter extends BaseAdapter {

  private Context mContext;
  private List<UsChildLVCommonBean.UsChildLvList> datas;

  public UsSpreadDownloadGvAdapter(Context mContext,
      List<UsChildLvList> datas) {
    this.mContext = mContext;
    this.datas = datas;
  }

  @Override
  public int getCount() {
    if (datas != null) {
      return datas.size();
    }
    return 0;
  }

  @Override
  public Object getItem(int position) {
    return datas.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder vh;
    if (convertView == null) {
      convertView = LayoutInflater.from(mContext)
          .inflate(R.layout.item_us_spread_download, null);
      vh = new ViewHolder();
      vh.itemImage = (ImageView) convertView.findViewById(R.id.us_spread_download_item_image);
      vh.itemTitle = (TextView) convertView.findViewById(R.id.us_spread_download_item_text);
      convertView.setTag(vh);
    } else {
      vh = (ViewHolder) convertView.getTag();
    }

    Glide.with(mContext).load(datas.get(position).getThumimg()).into(vh.itemImage);
//    Glide.with(mContext).load(
//        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504156201205&di=e09bde3af2fbcdf7ec4d82c04d31279a&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201511%2F04%2F20151104131417_KQ3Ba.jpeg")
//        .into(vh.itemImage);
    vh.itemTitle.setText(datas.get(position).getTitle());
    return convertView;
  }

  class ViewHolder {

    ImageView itemImage;
    TextView itemTitle;
  }
}

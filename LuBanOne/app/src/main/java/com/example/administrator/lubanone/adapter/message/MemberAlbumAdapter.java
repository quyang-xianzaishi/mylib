package com.example.administrator.lubanone.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.message.ImagePagerActivity;
import com.example.administrator.lubanone.bean.message.MemberAlbumBean;
import com.example.administrator.lubanone.bean.message.MemberInfoHeadBean;
import com.example.qlibrary.utils.CollectionUtils;
import java.util.List;

/**
 * Created by Administrator on 2017\7\28 0028.
 */

public class MemberAlbumAdapter extends BaseAdapter {

  private List<MemberAlbumBean> datas;
  private Context context;
  private MemberInfoHeadBean mMemberInfoHeadBean;
  private final static int TITLE = 0;
  private final static int List = 1;

  public MemberAlbumAdapter(Context context, List<MemberAlbumBean> datas,MemberInfoHeadBean memberInfoHeadBean) {
    this.context = context;
    this.datas = datas;
    this.mMemberInfoHeadBean = memberInfoHeadBean;
  }

  @Override
  public int getItemViewType(int position) {
    if (0 == position) {
      return TITLE;
    } else {
      return List;
    }
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }
  @Override
  public int getCount() {
    if (CollectionUtils.isEmpty(datas)) {
      return 1;
    }
    return datas.size() + 1;
  }

  @Override
  public Object getItem(int position) {
    if (0 == position) {
      return mMemberInfoHeadBean;
    } else {
      return datas.get(position - 1);
    }
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    TitleViewHolder titleViewHolder;
    int type = getItemViewType(position);
    switch (type){
      case TITLE:
        if (convertView == null) {
          convertView = LayoutInflater.from(context).inflate(R.layout.album_title_head, null);
          titleViewHolder = new TitleViewHolder();
          //实例化
          titleViewHolder.albumBg = (ImageView) convertView.findViewById(R.id.user_bg_img);
          titleViewHolder.userImg = (ImageView) convertView.findViewById(R.id.user_img);
          titleViewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
          convertView.setTag(titleViewHolder);
        } else {
          titleViewHolder = (TitleViewHolder) convertView.getTag();
        }

        //设置资源
        Glide.with(context)
            .load(mMemberInfoHeadBean.getAlbumBg())
            .into(titleViewHolder.albumBg);
        Glide.with(context)
            .load(mMemberInfoHeadBean.getUserImg())
            .into(titleViewHolder.userImg);
        titleViewHolder.userName.setText(mMemberInfoHeadBean.getUserName());
        break;
      case List:
        if (convertView == null) {
          convertView = LayoutInflater.from(context).inflate(R.layout.activity_album_item, null);
          viewHolder = new ViewHolder();
          //实例化
          viewHolder.albumItemTime = (TextView) convertView.findViewById(R.id.album_item_time);
          viewHolder.albumItemGrid = (GridView) convertView.findViewById(R.id.album_item_girdview);
          viewHolder.albumItemContent = (TextView) convertView.findViewById(R.id.album_item_content);
          viewHolder.albumItemImg = (ImageView)convertView.findViewById(R.id.album_item_img);
          viewHolder.albumItemImgNum = (TextView) convertView.findViewById(R.id.album_img_num);
          convertView.setTag(viewHolder);
        } else {
          viewHolder = (ViewHolder) convertView.getTag();
        }
        //设置资源
        viewHolder.albumItemTime.setText(datas.get(position-1).getTime());
        viewHolder.albumItemContent.setText(datas.get(position-1).getContent());
        Glide.with(context)
            .load(datas.get(position-1).getImgList().get(0))
            .into(viewHolder.albumItemImg);
        viewHolder.albumItemImgNum.setText("共"+Integer.toString(datas.get(position-1).getImgList().size())+"张");
        viewHolder.albumItemImg.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(v.getMeasuredWidth(), v.getMeasuredHeight());
            ImagePagerActivity.startImagePagerActivity(context, datas.get(position-1).getImgList(), position-1, imageSize);
          }
        });
        break;
      default:
        break;
    }
    return convertView;
  }

  class ViewHolder {
    TextView albumItemTime;
    GridView albumItemGrid;
    TextView albumItemContent;
    ImageView albumItemImg;
    TextView albumItemImgNum;
  }

  class TitleViewHolder{
    ImageView albumBg;
    ImageView userImg;
    TextView userName;
  }
}

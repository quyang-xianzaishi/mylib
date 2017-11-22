package com.example.administrator.lubanone.activity.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.adapter.message.MemberAlbumAdapter;
import com.example.administrator.lubanone.bean.message.AlbumBean;

import com.example.administrator.lubanone.bean.message.MemberAlbumBean;
import com.example.administrator.lubanone.bean.message.MemberInfoHeadBean;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\7\4 0004.
 */

public class AlbumActivity extends AppCompatActivity implements View.OnClickListener {

  private TextView back;
  private ImageView userBg;
  private ImageView userImg;
  private TextView userName;
  private RecyclerView albumRecyler;
  private List<AlbumBean> albumList;
  private PullToRefreshLayout albumRefresh;
  private PullableListView albumListView;
  private MemberAlbumAdapter mMemberAlbumAdapter;
  private List<MemberAlbumBean> mMemberAlbumBeanList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_album);
    initView();
  }

  public void initView() {
    back = (TextView) this.findViewById(R.id.back);
    userBg = (ImageView) this.findViewById(R.id.user_bg_img);
    userImg = (ImageView) this.findViewById(R.id.user_img);
    userName = (TextView) this.findViewById(R.id.user_name);
    albumRecyler = (RecyclerView) this.findViewById(R.id.album_recycler);

    albumRefresh = (PullToRefreshLayout) this.findViewById(R.id.album_refresh);
    albumListView = (PullableListView) this.findViewById(R.id.album_list);

    albumRefresh.setOnPullListener(new MyRefreshListener());
    albumRefresh.setPullDownEnable(false);
    albumListView.setFriction(ViewConfiguration.getScrollFriction() * 20);
    albumRefresh.setPullUpEnable(true);//设置是否让上拉加载
    //网络请求数据

    back.setOnClickListener(this);

    mMemberAlbumBeanList = new ArrayList<>();
    albumList = new ArrayList<>();
    initData();
    albumRecyler.setLayoutManager(new LinearLayoutManager(this));
    albumRecyler.addItemDecoration(new DividerItemDecoration(
        this, DividerItemDecoration.HORIZONTAL));
    albumRecyler.setAdapter(new AlbumAdapter());
  }

  private void initData() {

    List<String> list = new ArrayList<>();
    list.add("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg");
    List<String> list1 = new ArrayList<>();
    list1.add("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg");
    list1.add("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg");
    List<String> list2 = new ArrayList<>();
    list2.add("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg");
    list2.add("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg");
    list2.add("https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg");
    albumList.add(new AlbumBean("昨天", list, "weh回我北方常见的是不付出就会发v补充点过后出发吧" +
        "v核聚变的粗糙程度就"));
    albumList.add(new AlbumBean("昨天", list1, "weh回我北方常见的是不付出就会发v补充点过后出发吧" +
        "v核聚变的粗糙程度就"));
    albumList.add(new AlbumBean("昨天", list2, "weh回我北方常见的是不付出就会发v补充点过后出发吧" +
        "v核聚变的粗糙程度就"));

    mMemberAlbumBeanList.add(new MemberAlbumBean("昨天","1", list, "hahahahahahahahahahhahahahahhahahaha" ));
    mMemberAlbumBeanList.add(new MemberAlbumBean("昨天","2", list1, "   今天周五了，明天还要继续上班，杭州的天气太热了。。。。"));
    mMemberAlbumBeanList.add(new MemberAlbumBean("昨天","3", list2, "weh回我北方常见的是不付出就会发v补充点过后出发吧" +
        "v核聚变的粗糙程度就"));
    mMemberAlbumBeanList.add(new MemberAlbumBean("昨天","2", list1, "   今天周五了，明天还要继续上班，杭州的天气太热了。。。。"));
    mMemberAlbumBeanList.add(new MemberAlbumBean("昨天","3", list2, "weh回我北方常见的是不付出就会发v补充点过后出发吧" +
        "v核聚变的粗糙程度就"));

    mMemberAlbumAdapter = new MemberAlbumAdapter(this, mMemberAlbumBeanList,new MemberInfoHeadBean(
        "https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg",
        "https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg","郑连"));
    albumListView.setAdapter(mMemberAlbumAdapter);
    //setListViewHeightBasedOnChildren(albumListView);

  }

  @Override
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.back:
        AlbumActivity.this.finish();
        break;
      default:
        break;

    }

  }

  class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
          AlbumActivity.this).inflate(R.layout.activity_album_item, parent,
          false));
      return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
      holder.albumItemTime.setText(albumList.get(position).getTime());
      holder.albumItemContent.setText(albumList.get(position).getContent());
      Glide.with(AlbumActivity.this)
          .load(albumList.get(position).getImgList().get(0))
          .into(holder.albumItemImg);
      holder.albumItemImgNum
          .setText("共" + Integer.toString(albumList.get(position).getImgList().size()) + "张");
      holder.albumItemImg.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(
              v.getMeasuredWidth(), v.getMeasuredHeight());
          ImagePagerActivity
              .startImagePagerActivity((AlbumActivity.this), albumList.get(position).getImgList(),
                  position, imageSize);
        }
      });
      if (albumList.get(position).getImgList() != null &&
          albumList.get(position).getImgList().size() > 0) {
                /*if(albumList.get(position).getImgList().size()>1){
                    holder.albumItemGrid.setNumColumns(2);
                }else {
                    holder.albumItemGrid.setNumColumns(1);
                }*/
        BaseAdapter sim_adapter = new BaseAdapter() {
          @Override
          public int getCount() {
            return albumList.get(position).getImgList().size();
          }

          @Override
          public Object getItem(int position1) {
            return albumList.get(position).getImgList().get(position1);
          }

          @Override
          public long getItemId(int position1) {
            return position1;
          }

          @Override
          public View getView(int position1, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
              convertView = LayoutInflater.from(AlbumActivity.this)
                  .inflate(R.layout.album_gridview_item, null);
              viewHolder = new ViewHolder();
              viewHolder.mImg = (ImageView) convertView.findViewById(R.id.album_gridview_item_img);
                            /*if(albumList.get(position).getImgList().size()>1){
                                convertView.setLayoutParams(new GridView.LayoutParams(70,70));
                                //viewHolder.mImg.setLayoutParams(new LinearLayout.LayoutParams(50,50));
                            }else {
                                //viewHolder.mImg.setLayoutParams(new LinearLayout.LayoutParams(100,100));
                                convertView.setLayoutParams(new GridView.LayoutParams(140, 140));
                            }*/
              convertView.setTag(viewHolder);
            } else {
              viewHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(AlbumActivity.this)
                .load(albumList.get(position).getImgList().get(position1))
                .into(viewHolder.mImg);
            return convertView;
          }

          class ViewHolder {

            ImageView mImg;
          }
        };
        //配置适配器
        holder.albumItemGrid.setAdapter(sim_adapter);
      }
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
      });
    }

    @Override
    public int getItemCount() {
      if (albumList != null) {
        return albumList.size();
      } else {
        return 0;
      }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

      TextView albumItemTime;
      GridView albumItemGrid;
      TextView albumItemContent;
      ImageView albumItemImg;
      TextView albumItemImgNum;

      public MyViewHolder(View view) {
        super(view);
        albumItemTime = (TextView) view.findViewById(R.id.album_item_time);
        albumItemGrid = (GridView) view.findViewById(R.id.album_item_girdview);
        albumItemContent = (TextView) view.findViewById(R.id.album_item_content);
        albumItemImg = (ImageView) view.findViewById(R.id.album_item_img);
        albumItemImgNum = (TextView) view.findViewById(R.id.album_img_num);
      }
    }
  }

  class MyRefreshListener implements PullToRefreshLayout.OnPullListener {

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
      albumList = new ArrayList<>();
      initData();
      albumRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mMemberAlbumAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
      initData();
      albumRefresh.refreshFinish(PullToRefreshLayout.SUCCEED);
      mMemberAlbumAdapter.notifyDataSetChanged();
    }
  }

  public void setListViewHeightBasedOnChildren(ListView listView) {
    MemberAlbumAdapter memberAlbumAdapter = (MemberAlbumAdapter) listView.getAdapter();
    if (memberAlbumAdapter == null) {
      // pre-condition
      return;
    }

    int totalHeight =0;
    for (int i = 0; i < memberAlbumAdapter.getCount(); i++) {
      View listItem = memberAlbumAdapter.getView(i, null, listView);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (memberAlbumAdapter.getCount() - 1));
    listView.setLayoutParams(params);
  }


}

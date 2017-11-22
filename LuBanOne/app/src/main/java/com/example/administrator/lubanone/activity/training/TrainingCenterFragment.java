package com.example.administrator.lubanone.activity.training;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.training.activity.TrainPreparationMoreActivity;
import com.example.administrator.lubanone.activity.training.activity.TrainingBackPlayActivity;
import com.example.administrator.lubanone.activity.training.activity.TrainingMoreActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017\6\22 0022.
 */

public class TrainingCenterFragment extends Fragment {

  private ImageView traningMore;
  private ImageView traningPreMore;
//  private ImageView traningBackMore;
  private EditText searchEdit;
  private RecyclerView trainingRecycleView;
  private RecyclerView trainingPreRecycleView;
//  private RecyclerView trainingBackRecycleView;
  private List<Map<String, String>> trainingList;
  private List<Map<String, String>> trainingPreList;
//  private List<Map<String, String>> trainingBackList;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_training_center, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    traningMore = (ImageView) getActivity().findViewById(R.id.training_more_image);
    traningPreMore = (ImageView) getActivity().findViewById(R.id.training_pre_more_image);
//    traningBackMore = (ImageView) getActivity().findViewById(R.id.training_back_more_image);
    searchEdit = (EditText) getActivity().findViewById(R.id.search_edittext);
    trainingRecycleView = (RecyclerView) getActivity().findViewById(R.id.training_recyclerview);
    trainingPreRecycleView = (RecyclerView) getActivity()
        .findViewById(R.id.training_pre_recyclerview);
//    trainingBackRecycleView = (RecyclerView) getActivity()
//        .findViewById(R.id.training_playback_recyclerview);
    traningMore.setOnClickListener(new myOnclickListener());
    traningPreMore.setOnClickListener(new myOnclickListener());
//    traningBackMore.setOnClickListener(new myOnclickListener());
    searchEdit.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        //搜素输入框文字变化实时搜索

      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    //网络请求数据
    initData();
    trainingRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    trainingRecycleView.setAdapter(new TrainingAdapter());
    trainingPreRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    trainingPreRecycleView.setAdapter(new TrainingpreAdapter());
//    trainingBackRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
//    trainingBackRecycleView.setAdapter(new TrainingBackAdapter());
  }

  protected void initData() {
    trainingList = new ArrayList<>();
    trainingPreList = new ArrayList<>();
//    trainingBackList = new ArrayList<>();

    Map map1 = new HashMap<>();
    map1.put("title", "符合那个姐姐就能好看吗");
    map1.put("time", "XXs时XX分XX秒");
    map1.put("img_url",
        "https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg");
    map1.put("state", "取消提醒");
    trainingPreList.add(map1);
    trainingPreList.add(map1);
    trainingPreList.add(map1);
    trainingPreList.add(map1);
    trainingPreList.add(map1);

    Map map2 = new HashMap<>();
    map2.put("title", "俄个人个人体会");
    map2.put("content", "几个分蘖骨和瑞哦工行功夫呢房间的空间飞入肌肤感觉估计会让梵蒂冈的风格");
    map2.put("img_url",
        "https://b-ssl.duitang.com/uploads/item/201312/05/20131205172455_cVx8y.jpeg");
    trainingList.add(map2);
    trainingList.add(map2);
    trainingList.add(map2);
    trainingList.add(map2);
    trainingList.add(map2);
    trainingList.add(map2);

  }

  private class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.MyViewHolder> {

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
          getActivity()).inflate(R.layout.training_item_layout, parent,
          false));
      return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
      //holder.trainingItemImage.setImageBitmap(trainingList.get(position).get(""));
      holder.trainingItemTitle.setText(trainingList.get(position).get("title"));
      holder.trainingItemContent.setText(trainingList.get(position).get("content"));
      Glide.with(getActivity())
          .load(trainingList.get(position).get("img_url"))
          .placeholder(R.mipmap.b)
          .error(R.mipmap.b)
          .diskCacheStrategy(DiskCacheStrategy.ALL).
          into(holder.trainingItemImage);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//          startActivity(new Intent(getActivity(), TrainContentActivity.class));
          startActivity(new Intent(getActivity(), TrainingBackPlayActivity.class));
        }
      });
    }

    @Override
    public int getItemCount() {
      if (trainingList != null) {
        if (trainingList.size() > 2) {
          return 2;
        } else {
          return trainingList.size();
        }
      } else {
        return 0;
      }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

      ImageView trainingItemImage;
      TextView trainingItemTitle;
      TextView trainingItemContent;
      ImageView trainingItemDetail;

      public MyViewHolder(View view) {
        super(view);
        trainingItemImage = (ImageView) view.findViewById(R.id.training_item_image);
        trainingItemTitle = (TextView) view.findViewById(R.id.training_item_title);
        trainingItemContent = (TextView) view.findViewById(R.id.training_item_content);
        trainingItemDetail = (ImageView) view.findViewById(R.id.training_item_detail);
      }
    }
  }

  private class TrainingpreAdapter extends RecyclerView.Adapter<TrainingpreAdapter.MyViewHolder> {

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
          getActivity()).inflate(R.layout.training_pre_item_layout, parent,
          false));
      return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      //holder.trainingPreItemImage.setImageBitmap(trainingPreList.get(position).get(""));
      holder.trainingPreItemTitle.setText(trainingPreList.get(position).get("title"));
      holder.trainingPreItemTime.setText(trainingPreList.get(position).get("time"));
      //holder.trainingPreItemRemind.setText(trainingPreList.get(position).get(""));
      String str = trainingPreList.get(position).get("state");
      if (str != null && str.equals("开播提醒")) {
        holder.trainingPreItemRemind.setBackground(getActivity().getResources().
            getDrawable(R.drawable.training_play_notify_bg));
      } else if (str != null && str.equals("取消提醒")) {
        holder.trainingPreItemRemind.setBackground(getActivity().getResources().
            getDrawable(R.drawable.training_play_cancle_notify_bg));
      }
      holder.trainingPreItemRemind.setText(str);
      Glide.with(getActivity())
          .load(trainingPreList.get(position).get("img_url"))
          .placeholder(R.mipmap.b)
          .error(R.mipmap.b)
          .diskCacheStrategy(DiskCacheStrategy.ALL).
          into(holder.trainingPreItemImage);
      holder.trainingPreItemRemind.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          String str = (String) ((TextView) v).getText();
          if (str != null && str.equals("开播提醒")) {
            //培训预告点击开播提醒通知伞下会员
            ((TextView) v).setText("取消提醒");
            trainingPreList.get(position).put("state", "取消提醒");
            ((TextView) v).setBackground(getActivity().getResources().
                getDrawable(R.drawable.training_play_cancle_notify_bg));
          } else if (str != null && str.equals("取消提醒")) {
            //取消提醒
            ((TextView) v).setText("开播提醒");
            trainingPreList.get(position).put("state", "开播提醒");
            ((TextView) v).setBackground(getActivity().getResources().
                getDrawable(R.drawable.training_play_notify_bg));
          }
        }
      });
    }

    @Override
    public int getItemCount() {
      if (trainingPreList != null) {
        if (trainingPreList.size() > 2) {
          return 2;
        } else {
          return trainingPreList.size();
        }
      } else {
        return 0;
      }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

      ImageView trainingPreItemImage;
      TextView trainingPreItemTitle;
      TextView trainingPreItemTime;
      TextView trainingPreItemRemind;

      public MyViewHolder(View view) {
        super(view);
        trainingPreItemImage = (ImageView) view.findViewById(R.id.training_pre_item_image);
        trainingPreItemTitle = (TextView) view.findViewById(R.id.training_pre_item_title);
        trainingPreItemTime = (TextView) view.findViewById(R.id.training_pre_item_time);
        trainingPreItemRemind = (TextView) view.findViewById(R.id.training_pre_item_remind);
      }
    }
  }

//  private class TrainingBackAdapter extends RecyclerView.Adapter<TrainingBackAdapter.MyViewHolder> {
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//      MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
//          getActivity()).inflate(R.layout.training_back_item_layout, parent,
//          false));
//      return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//
//      holder.trainingBackItemTitle.setText(trainingBackList.get(position).get("title"));
//      holder.trainingBackItemPlay.setText("回放");
//      holder.trainingBackItemAudience.setText(trainingBackList.get(position).get("audience") +
//          getActivity().getResources().getString(R.string.training_audience));
//      holder.trainingBackItemPointCount.setText(trainingBackList.get(position).get("point")
//          + getActivity().getResources().getString(R.string.training_point_count));
//      //holder.trainingBackItemImage.setImageBitmap(trainingPreList.get(position).get(""));
//      Glide.with(getActivity())
//          .load(trainingBackList.get(position).get("img_url"))
//          .placeholder(R.mipmap.b)
//          .error(R.mipmap.b)
//          .diskCacheStrategy(DiskCacheStrategy.ALL).
//          into(holder.trainingBackItemImage);
//
//      holder.trainingBackItemPlay.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          //培训回放点击回放
//          Intent intent = new Intent();
//          intent.setClass(getActivity(), TrainingBackPlayActivity.class);
//          getActivity().startActivity(intent);
//        }
//      });
//    }
//
//    @Override
//    public int getItemCount() {
//      if (trainingBackList != null) {
//        if (trainingBackList.size() > 2) {
//          return 2;
//        } else {
//          return trainingBackList.size();
//        }
//      } else {
//        return 0;
//      }
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//
//      TextView trainingBackItemTitle;
//      TextView trainingBackItemPlay;
//      TextView trainingBackItemAudience;
//      TextView trainingBackItemPointCount;
//      ImageView trainingBackItemImage;
//
//      public MyViewHolder(View view) {
//        super(view);
//        trainingBackItemTitle = (TextView) view.findViewById(R.id.training_back_item_title);
//        trainingBackItemPlay = (TextView) view.findViewById(R.id.training_back_item_play);
//        trainingBackItemAudience = (TextView) view.findViewById(R.id.training_back_item_audience);
//        trainingBackItemPointCount = (TextView) view
//            .findViewById(R.id.training_back_item_point_count);
//        trainingBackItemImage = (ImageView) view.findViewById(R.id.training_back_item_image);
//      }
//    }
//  }

  private class myOnclickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.training_more_image:
          //正在培训中点击更多
          Intent intent = new Intent();
          intent.setClass(getActivity(), TrainingMoreActivity.class);
          getActivity().startActivity(intent);
          break;
        case R.id.training_pre_more_image:
          //培训预告点击更多
          Intent intent1 = new Intent();
          intent1.setClass(getActivity(), TrainPreparationMoreActivity.class);
          getActivity().startActivity(intent1);
          break;
//        case R.id.training_back_more_image:
//          //培训回放点击更多
//          Intent intent2 = new Intent();
//          intent2.setClass(getActivity(), TrainPlayBackMoreActivity.class);
//          getActivity().startActivity(intent2);
//          break;
        default:
          break;
      }

    }
  }

}





package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BewHomeResultBean.BuylistBean;
import com.example.administrator.lubanone.interfaces.OnRecyclerItemListener;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.StringUtil;
import java.util.List;

/**
 * Created by admistrator on 2017/8/16.
 */

public class RecyclerHomeTopAdapter extends RecyclerView.Adapter<ViewHolder> {


  private List<BuylistBean> mList;
  private Context mContext;
  private OnRecyclerItemListener mListener;

  public RecyclerHomeTopAdapter(List<BuylistBean> buylist, Context context,
      OnRecyclerItemListener listener) {
    this.mList = buylist;
    this.mContext = context;
    this.mListener = listener;

  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == 0) {
      MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
          mContext).inflate(R.layout.recycler_item, parent,
          false));
      return holder;
    } else {
      AddViewHolder holder = new AddViewHolder(LayoutInflater.from(
          mContext).inflate(R.layout.recycler_item_add, parent,
          false));
      return holder;
    }
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {

    if (holder instanceof MyViewHolder) {
      BuylistBean buylistBean = mList.get(position);
      MyViewHolder myViewHolder = (MyViewHolder) holder;

      StringUtil
          .setTextSize(DebugUtils.convert(buylistBean.getSeedcount(),"0") + mContext.getString(R.string.pcs), myViewHolder.tv_digital_left, 22, 10);
      DebugUtils.setStringValue(buylistBean.getSeedprice(), "0", myViewHolder.tv_digital_right);
      DebugUtils.setStringValue(getStatus(buylistBean.getStatus()), "", myViewHolder.tv_text);

      myViewHolder.setOnClickListener(myViewHolder.root, position, buylistBean);

    } else if (holder instanceof AddViewHolder) {

      AddViewHolder addViewHolder = (AddViewHolder) holder;
      addViewHolder.setOnClickListener(addViewHolder.add, position, null);

      if (CollectionUtils.isEmpty(mList)) {
        TextView viewById = (TextView) addViewHolder.add.findViewById(R.id.tv_digital_left);
        viewById.setText(mContext.getString(R.string.now_no_msg));
        viewById.setClickable(false);
      }
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (position == getItemCount() - 1) {
      return 1;
    } else {
      return 0;
    }
  }


  public String getStatus(int status) {
    if (0 == status) {
      return mContext.getString(R.string.buy_text_one);
    }

    if (1 == status) {
      return mContext.getString(R.string.buy_text_two);
    }

    if (2 == status) {
      return mContext.getString(R.string.buy_text_three);
    }

    if (3 == status) {
      return mContext.getString(R.string.buy_text_four);
    }
    return "";

  }

  @Override
  public int getItemCount() {
    return CollectionUtils.isEmpty(mList) ? 1 : mList.size() + 1;
  }

  class MyViewHolder extends ViewHolder {

    TextView tv_digital_left;
    TextView tv_digital_right;
    TextView tv_text;
    View root;

    public MyViewHolder(View view) {
      super(view);
      tv_digital_left = (TextView) view.findViewById(R.id.tv_digital_left);
      tv_digital_right = (TextView) view.findViewById(R.id.tv_digital_right);
      tv_text = (TextView) view.findViewById(R.id.tv_text);
      root = view.findViewById(R.id.root);
    }

    public void setOnClickListener(final View root, final int position,
        final BuylistBean buylistBean) {

      root.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

          mListener.onItem(root, buylistBean, position);
        }
      });
    }
  }

  class AddViewHolder extends ViewHolder {

    View add;

    public AddViewHolder(View view) {
      super(view);
      add = view.findViewById(R.id.add);
    }

    public void setOnClickListener(final View root, final int position,
        final BuylistBean buylistBean) {

      root.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

          if (CollectionUtils.isNotEmpty(mList)) {
            mListener.onItem(root, mList.get(position - 1),
                position);
          }
        }
      });
    }
  }
}



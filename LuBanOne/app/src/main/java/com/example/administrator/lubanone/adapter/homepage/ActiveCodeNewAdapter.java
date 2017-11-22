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
import com.example.administrator.lubanone.bean.finance.ActiveCodeResultBean.DeallistBean;
import com.example.administrator.lubanone.interfaces.OnItemListener;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DebugUtils;
import java.util.List;

/**
 * Created by Administrator on 2017\8\23 0023.
 */

public class ActiveCodeNewAdapter extends RecyclerView.Adapter<ViewHolder> {

  private Context mContext;
  private List<DeallistBean> mList;
  private Boolean mMore;
  private OnItemListener mListener;

  public ActiveCodeNewAdapter(Context mContext, List<DeallistBean> mList, Boolean more,
      OnItemListener listener) {
    this.mContext = mContext;
    this.mList = mList;
    this.mMore = more;
    this.mListener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == 0) {
      return new MyViewHolder(LayoutInflater.from(
          mContext).inflate(R.layout.active_code_item, parent,
          false));
    } else {
      return new MyFootViewHolder(LayoutInflater.from(
          mContext).inflate(R.layout.active_more_layout, parent,
          false));
    }
  }

  @Override
  public int getItemViewType(int position) {
    return position == getItemCount() - 1 ? 1
        : 0;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {

    if (holder instanceof MyViewHolder) {//正常布局

      DeallistBean deallistBean = mList.get(position);
      MyViewHolder myViewHolder = (MyViewHolder) holder;

      DebugUtils.setStringValue(deallistBean.getDate(), "", myViewHolder.tvDate);
      DebugUtils.setStringValue(deallistBean.getTradeobject(), "", myViewHolder.tvTradeObj);
      DebugUtils.setStringValue(deallistBean.getCount(), "", myViewHolder.tvTradeSize);

      if ("0".equals(deallistBean.getColor())) {
//      myViewHolder.tvDate.setTextColor(ColorUtil.getColor(R.color.c666, mContext));
//      myViewHolder.tvTradeObj.setTextColor(ColorUtil.getColor(R.color.c666, mContext));

        myViewHolder.tvTradeSize.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
      } else if ("1".equals(deallistBean.getColor())) {
//      myViewHolder.tvDate.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
//      myViewHolder.tvTradeObj.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));

        myViewHolder.tvTradeSize.setTextColor(ColorUtil.getColor(R.color.cEA5412, mContext));
      }

      if (position % 2 == 0) {
        myViewHolder.root.setBackgroundColor(mContext.getResources().getColor(R.color.white));
      } else {
        myViewHolder.root.setBackgroundColor(mContext.getResources().getColor(R.color.cf9f9f9));
      }


    } else if (holder instanceof MyFootViewHolder) {//footview
      MyFootViewHolder footViewHolder = (MyFootViewHolder) holder;
      if (null != mMore && mMore) {
        footViewHolder.root.setText(mContext.getString(R.string.look_more));
        footViewHolder.root.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            mListener.onItem(null, 0, 0);
          }
        });
      } else {
        footViewHolder.root.setText(mContext.getString(R.string.no_more_message));
        footViewHolder.root.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            mListener.onItem(null, 0, 1);
          }
        });
      }
    }
  }

  @Override
  public int getItemCount() {
    return mList.size() + 1;
  }

  public class MyViewHolder extends ViewHolder {

    View root;
    TextView tvDate;
    TextView tvTradeObj;
    TextView tvTradeSize;

    public MyViewHolder(View itemView) {
      super(itemView);

      root = (View) itemView.findViewById(R.id.root_normal);
      tvDate = (TextView) itemView.findViewById(R.id.tv_date);
      tvTradeObj = (TextView) itemView.findViewById(R.id.tv_trade_obj);
      tvTradeSize = (TextView) itemView.findViewById(R.id.tv_trade_size);
    }
  }


  public class MyFootViewHolder extends ViewHolder {

    TextView root;


    public MyFootViewHolder(View itemView) {
      super(itemView);
      root = (TextView) itemView.findViewById(R.id.tv);

    }
  }
}

package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.bean.homepage.BindCardResultBean.BankaccountlistBean;
import com.example.administrator.lubanone.interfaces.OnMoneyPayListener;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DpUtil;
import com.example.qlibrary.utils.DrableUtil;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.example.qlibrary.utils.ViewUtil;
import java.util.List;

/**
 * Created by quyang on 2017/7/12.
 */

public class CardListAdapter extends CustomAdapter<BankaccountlistBean> {


  private OnMoneyPayListener<BankaccountlistBean> mListener;

  public CardListAdapter(Context context, List<BankaccountlistBean> list,
      OnMoneyPayListener<BankaccountlistBean> listener) {
    super(context, list);
    this.mListener = listener;
  }

  @Override
  public int getListViewLayoutId() {
    return R.layout.card_list_item;
  }

  @Override
  public void setData2Views(ViewHolder viewHolder, BankaccountlistBean item, int position) {
    TextView tvInfo = viewHolder.getView(R.id.tv_info);//银行名称
    TextView account = viewHolder.getView(R.id.tv_account);//账号
    LinearLayout ll_container = viewHolder.getView(R.id.ll_container);//ll_container
    View root = viewHolder.getView(R.id.root);//ll_container

    try {
      GradientDrawable shape = DrableUtil
          .getShapeNew(mContext, Color.parseColor(item.getBankcolor()), DpUtil.dp2px(mContext, 10));
      ViewUtil.setBackground(root, shape);
    } catch (Exception e) {
      GradientDrawable shape = DrableUtil
          .getShape(mContext, R.color.cf6410b, DpUtil.dp2px(mContext, 10));
      ViewUtil.setBackground(root, shape);
    }

    //设置银行名称
    tvInfo.setText(StringUtil.getBufferString(DebugUtils.convert(item.getBankname(), ""), "\n",
        DebugUtils.convert(item.getType(), "")));

    //设置银行账号
    if (TextUitl.isNotEmpty(item.getBankaccount())) {
      ll_container.setVisibility(View.VISIBLE);
      account.setText(getTarget(item.getBankaccount()));
    } else {
      ll_container.setVisibility(View.GONE);
    }

    String bankaccount = item.getBankaccount();

    if (!TextUtils.isEmpty(bankaccount) && bankaccount.length() > 10) {
      int length = bankaccount.length();
      String substring = bankaccount.substring(length - 4, length);
      //tvInfo.setText("银行账户\n" + item.getBankname() + "(" + substring + ")");
      tvInfo.setText(item.getBankname());
    } else {
      //tvInfo.setText("银行账户\n" + item.getBankname() + "(" + bankaccount + ")");
      tvInfo.setText(item.getBankname());
    }

    root.setOnClickListener(new MyOnClickListener(position));
  }

  private String getTarget(String bankaccount) {
    if (TextUitl.isEmpty(bankaccount)) {
      return "";
    }
    if (bankaccount.length() <= 4) {
      return bankaccount;
    } else {
      return bankaccount.substring(bankaccount.length() - 4, bankaccount.length());
    }
  }

  private class MyOnClickListener implements OnClickListener {

    private int position;

    public MyOnClickListener(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      mListener.onCuiPayClick(getItem(position), position);
    }
  }
}

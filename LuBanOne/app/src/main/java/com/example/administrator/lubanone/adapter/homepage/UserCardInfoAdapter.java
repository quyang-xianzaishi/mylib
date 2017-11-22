package com.example.administrator.lubanone.adapter.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.home.BankAccountDetailsActivity;
import com.example.administrator.lubanone.bean.homepage.UserCardInfoBean;
import com.example.qlibrary.adapter.listview.CustomAdapter;
import com.example.qlibrary.adapter.listview.ViewHolder;

import java.util.List;

/**
 * 用户已添加银行卡list adapter
 * Created by quyang on 2017/6/26.
 */

public class UserCardInfoAdapter extends CustomAdapter<UserCardInfoBean> {


    public UserCardInfoAdapter(Context context, List<UserCardInfoBean> list) {
        super(context, list);
    }

    @Override
    public int getListViewLayoutId() {
        return R.layout.card_info_item;
    }

    @Override
    public void setData2Views(ViewHolder viewHolder, UserCardInfoBean item, int position) {
        TextView tvBankName = viewHolder.getView(R.id.tv_bank_name);
        ImageView ivNextPage = viewHolder.getView(R.id.iv_next_page);

        tvBankName.setText(item.getCardBand());
        ivNextPage.setOnClickListener(new MyOnClickListener(position));

    }

    private class MyOnClickListener implements View.OnClickListener {


        private int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            UserCardInfoBean item = getItem(position);
            Intent intent = new Intent(mContext, BankAccountDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("item", item);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }
}

package com.example.administrator.lubanone.activity.land;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.administrator.lubanone.R;
import com.example.qlibrary.utils.DateUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends Activity implements OnClickListener {

  private List<String> mData = new ArrayList<>();

  private ListView lvItems;
  private List<Product> lstProducts;
  private List<Product> lstProductsOne;

  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (1 == msg.what) {
        mView.setText(DateUtil.getDateString((Long) msg.obj));
      }
    }
  };
  private TextView mView;
  private ListView mListView1;


  private List<String> mList = new ArrayList<>();


  @Override

  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);

    lvItems = (ListView) findViewById(R.id.lv);
    lstProducts = new ArrayList<>();
    lstProductsOne = new ArrayList<>();

    lstProducts.add(new Product("A", System.currentTimeMillis() + 10000));
    lstProducts.add(new Product("B", System.currentTimeMillis() + 20000));
    lstProducts.add(new Product("C", System.currentTimeMillis() + 20000));
    lstProducts.add(new Product("D", System.currentTimeMillis() + 20000));
    lstProducts.add(new Product("E", System.currentTimeMillis() + 20000));
    lstProducts.add(new Product("F", System.currentTimeMillis() + 20000));
    lstProducts.add(new Product("G", System.currentTimeMillis() + 30000));
    lstProducts.add(new Product("H", System.currentTimeMillis() + 20000));
    lstProducts.add(new Product("I", System.currentTimeMillis() + 20000));
    lstProducts.add(new Product("J", System.currentTimeMillis() + 40000));
    lstProducts.add(new Product("K", System.currentTimeMillis() + 20000));
    lstProducts.add(new Product("L", System.currentTimeMillis() + 50000));
    lstProducts.add(new Product("M", System.currentTimeMillis() + 60000));
    lstProducts.add(new Product("N", System.currentTimeMillis() + 20000));
    lstProducts.add(new Product("O1", System.currentTimeMillis() + 30000));
    lstProducts.add(new Product("O2", System.currentTimeMillis() + 60000));
    lstProducts.add(new Product("O3", System.currentTimeMillis() + 90000));
    lstProducts.add(new Product("O4", System.currentTimeMillis() + 13000));
    lstProducts.add(new Product("O5", System.currentTimeMillis() + 10000));
    lstProducts.add(new Product("O6", System.currentTimeMillis() + 10020));
    lstProducts.add(new Product("O7", System.currentTimeMillis() + 12000));
    lstProducts.add(new Product("O8", System.currentTimeMillis() + 10000));

    lstProductsOne.add(new Product("A11", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("B11", System.currentTimeMillis() + 20000));
    lstProductsOne.add(new Product("C11", System.currentTimeMillis() + 20000));
    lstProductsOne.add(new Product("D11", System.currentTimeMillis() + 20000));
    lstProductsOne.add(new Product("E11", System.currentTimeMillis() + 20000));
    lstProductsOne.add(new Product("F11", System.currentTimeMillis() + 20000));
    lstProductsOne.add(new Product("G11", System.currentTimeMillis() + 30000));
    lstProductsOne.add(new Product("H11", System.currentTimeMillis() + 20000));
    lstProductsOne.add(new Product("I11", System.currentTimeMillis() + 20000));
    lstProductsOne.add(new Product("J11", System.currentTimeMillis() + 40000));
    lstProductsOne.add(new Product("K11", System.currentTimeMillis() + 20000));
    lstProductsOne.add(new Product("L11", System.currentTimeMillis() + 50000));
    lstProductsOne.add(new Product("M11", System.currentTimeMillis() + 60000));
    lstProductsOne.add(new Product("N11", System.currentTimeMillis() + 20000));
    lstProductsOne.add(new Product("O11", System.currentTimeMillis() + 30000));
    lstProductsOne.add(new Product("O12", System.currentTimeMillis() + 60000));
    lstProductsOne.add(new Product("O13", System.currentTimeMillis() + 90000));
    lstProductsOne.add(new Product("O14", System.currentTimeMillis() + 13000));
    lstProductsOne.add(new Product("O15", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("O16", System.currentTimeMillis() + 10020));
    lstProductsOne.add(new Product("O17", System.currentTimeMillis() + 12000));
    lstProductsOne.add(new Product("O18", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("O91", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("O1112", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("O12", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("O13", System.currentTimeMillis() + 30000));
    lstProductsOne.add(new Product("O14", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("O15", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("a11", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("a12", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("a433", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("a453", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("435", System.currentTimeMillis() + 50000));
    lstProductsOne.add(new Product("rer", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("feO", System.currentTimeMillis() + 110000));
    lstProductsOne.add(new Product("Odfe", System.currentTimeMillis() + 120000));
    lstProductsOne.add(new Product("Oeet", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("Ofef", System.currentTimeMillis() + 140000));
    lstProductsOne.add(new Product("Oetert", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("Orwer", System.currentTimeMillis() + 140000));
    lstProductsOne.add(new Product("Otw", System.currentTimeMillis() + 10000));
    lstProductsOne.add(new Product("Oett", System.currentTimeMillis() + 10500));

    lvItems.setAdapter(null);
//    lvItems.setAdapter(new CountdownAdapter(TestActivity.this, lstProducts));
//    new Handler().postDelayed(new Runnable() {
//      @Override
//      public void run() {
//        ToastUtil.showShort("切换了", getApplicationContext());
//        lvItems.setAdapter(new CountdownAdapter(TestActivity.this, lstProductsOne));
//      }
//    }, 10 * 1000);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.btn) {
    }
  }


  public class CountdownAdapter extends ArrayAdapter<Product> {

    private LayoutInflater lf;
    private List<ViewHolder> lstHolders;
    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
      @Override
      public void run() {
        synchronized (lstHolders) {
          long currentTime = System.currentTimeMillis();
          for (ViewHolder holder : lstHolders) {
            holder.updateTimeRemaining(currentTime);
          }
        }
      }
    };

    public CountdownAdapter(Context context, List<Product> objects) {
      super(context, 0, objects);
      lf = LayoutInflater.from(context);
      lstHolders = new ArrayList<>();
      startUpdateTimer();
    }

    private void startUpdateTimer() {
      Timer tmr = new Timer();
      tmr.schedule(new TimerTask() {
        @Override
        public void run() {
          mHandler.post(updateRemainingTimeRunnable);
        }
      }, 1000, 1000);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder = null;
      if (convertView == null) {
        holder = new ViewHolder();
        convertView = lf.inflate(R.layout.test_lv_item, parent, false);
        holder.tvProduct = (TextView) convertView.findViewById(R.id.tvProduct);
        holder.tvTimeRemaining = (TextView) convertView.findViewById(R.id.tvTimeRemaining);
        convertView.setTag(holder);
        synchronized (lstHolders) {
          lstHolders.add(holder);
        }
      } else {
        holder = (ViewHolder) convertView.getTag();
      }

      holder.setData(getItem(position));

      return convertView;
    }
  }

  private class ViewHolder {

    TextView tvProduct;
    TextView tvTimeRemaining;
    Product mProduct;

    public void setData(Product item) {
      mProduct = item;
      tvProduct.setText(item.name);
      updateTimeRemaining(System.currentTimeMillis());
    }

    public void updateTimeRemaining(long currentTime) {
      long timeDiff = mProduct.time - currentTime;
      if (timeDiff > 0) {
        int seconds = (int) (timeDiff / 1000) % 60;
        int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
        int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);

        tvTimeRemaining.setText(hours + " hrs " + minutes + " mins " + seconds + " sec");
      } else {
        tvTimeRemaining.setText("Expired!!");
      }
    }
  }

//  private class MyBaseAdapter extends BaseAdapter {
//
//
//    private List<String> mList;
//
//    MyBaseAdapter(List<String> list) {
//      this.mList = list;
//
//    }
//
//    @Override
//    public int getCount() {
//      return mList == null ? 0 : mList.size();
//    }
//
//    @Override
//    public String getItem(int position) {
//      return mList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//      return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//      ViewHolder viewHolder = null;
//      if (null == convertView) {
//        viewHolder = new ViewHolder();
//
//        convertView = LayoutInflater.from(TestActivity.this).inflate(R.layout.test_item, null);
//
//        viewHolder.name = (TextView) convertView.findViewById(R.id.tv);
//        final ViewHolder finalViewHolder = viewHolder;
//        viewHolder.mHandler = new Handler() {
//          @Override
//          public void handleMessage(Message msg) {
//            finalViewHolder.name
//                .setText(getItem(position) + DateUtil.getDateString(new Date().getTime()));
//          }
//        };
//        viewHolder.mTimer = new Timer();
//        final ViewHolder finalViewHolder1 = viewHolder;
//        viewHolder.mTimerTask = new TimerTask() {
//          @Override
//          public void run() {
//            finalViewHolder1.mHandler.sendEmptyMessage(0);
//          }
//        };
//        viewHolder.mTimer.schedule(viewHolder.mTimerTask, 1000, (long) (Math.random() * 5000));
//        convertView.setTag(viewHolder);
//      } else {
//        viewHolder = (ViewHolder) convertView.getTag();
//      }
//
//      return convertView;
//    }
//  }

//  static class ViewHolder {
//
//    private TextView name;
//    private Handler mHandler;
//    private Timer mTimer;
//    private TimerTask mTimerTask;
//
//  }
}

package com.example.administrator.lubanone.fragment;

import static com.example.administrator.lubanone.R.id.tv_month_info;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.bigkoo.pickerview.TimePickerView;
import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.activity.home.BuySeedsRecordActivity;
import com.example.administrator.lubanone.activity.home.SellSeedsRecordActivity;
import com.example.administrator.lubanone.bean.finance.AllScanResultBean;
import com.example.administrator.lubanone.bean.finance.AllScanResultBean.ChartBean;
import com.example.administrator.lubanone.bean.finance.AllScanResultBean.ChartBean.SeedinBean;
import com.example.administrator.lubanone.bean.finance.AllScanResultBean.ChartBean.SeedinBeanOut;
import com.example.administrator.lubanone.bean.homepage.CharResultBean;
import com.example.administrator.lubanone.bean.homepage.SpotDot;
import com.example.administrator.lubanone.bean.homepage.SpotDot.BuylistBean;
import com.example.administrator.lubanone.bean.homepage.SpotDot.SelllistBean;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.ColorUtil;
import com.example.qlibrary.utils.DateUtil;
import com.example.qlibrary.utils.DebugUtils;
import com.example.qlibrary.utils.DemicalUtil;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.StringUtil;
import com.example.qlibrary.utils.TextUitl;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;

/**
 * 总览
 * Created by quyang on 2017/6/27.
 */

public class AllScaleFragment extends BaseFragment implements OnClickListener {


  private RequestListener mCharListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<CharResultBean> spotDotResult = GsonUtil
            .processJson(jsonObject, CharResultBean.class);
        getCharResult(spotDotResult);
      } catch (Exception e) {
        if (isAdded()) {
          showMsg(getString(R.string.get_char_date_fail));
        }
      }
    }

    @Override
    public void onFail(String errorMsf) {
      showMsg(getString(R.string.get_char_date_fail));
    }
  };

  private void getCharResult(Result<CharResultBean> spotDotResult) {
    if (ResultUtil.isSuccess(spotDotResult)) {
      if (spotDotResult.getResult() == null) {
        showMsg(getString(R.string.get_char_date_fail));
      } else {
        CharResultBean result = spotDotResult.getResult();
        CharResultBean.ChartBean chart = result.getChart();

        SpotDot spotDot = new SpotDot();
        List<CharResultBean.ChartBean.SeedinBean> seedin = chart.getSeedin();
        List<CharResultBean.ChartBean.SeedinBeanOut> seedout = chart.getSeedout();

        if (CollectionUtils.isEmpty(seedin) && CollectionUtils.isEmpty(seedout)) {
          showMsg(getString(R.string.no_in_out_msg));
          return;
        }

        spotDot.setBuylist(getBuyList1(seedin));
        spotDot.setSelllist(getSellList1(seedout));
        adapterChar(spotDot);
      }
    } else {
      showMsg(DebugUtils.convert(
          ResultUtil.getErrorMsg(spotDotResult), getString(R.string.get_char_date_fail)));
    }
  }


  private void adapterChar(SpotDot result) {

    CharManager manager = new CharManager(getActivity());
    LineData mLineData = getLineData(30, 50, result);
    showChart(mChart, mLineData);
    manager.configChartAxis(mChart);
  }


  Unbinder unbinder;
  @BindView(R.id.buy_btn_click)
  View mBuyBtnClick;
  @BindView(R.id.sell_btn_click)
  View mSellBtnClick;
  @BindView(R.id.tv_dream_package)
  TextView mTvDreamPackage;
  @BindView(R.id.tv_money_package)
  TextView mTvMoneyPackage;
  @BindView(R.id.tv_active_code)
  TextView mTvActiveCode;
  @BindView(R.id.tv_cuihuaji)
  TextView mTvCuihuaji;
  @BindView(R.id.tv_my_all_earnings)
  TextView mTvMyAllEarnings;
  @BindView(R.id.tv_all_buy_seeds)
  TextView mTvAllBuySeeds;
  @BindView(R.id.tv_sell_seeds)
  TextView mTvSellSeeds;
  @BindView(R.id.tv_dai_sell_seeds)
  TextView mTvDaiSellSeeds;
  @BindView(R.id.tv_month)
  TextView mTvMonth;
  Unbinder unbinder1;
  private Unbinder mBind;
  private TextView mTvAllMoney;
  @BindView(tv_month_info)
  TextView mTvMonthInfo;
  @BindView(R.id.iv_back_click)
  ImageView iv_back_click;
  private LineChart mChart;


  private Long time;

  //请求总览数据
  private RequestListener mAllScanListener = new RequestListener() {
    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void testSuccess(String jsonObject) {
      try {
        Result<AllScanResultBean> result = GsonUtil
            .processJson(jsonObject, AllScanResultBean.class);
        getAllScanData(result);
      } catch (Exception e) {
//        showMsg(getInfo(R.string.get_user_info_fail));
      }
    }

    @Override
    public void onFail(String errorMsf) {
//      showMsg(getString(R.string.get_user_info_fail));
    }
  };


  @Override
  public View initView() {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.all_scan_fragment_layout, null);
    mBind = ButterKnife.bind(this, view);

    mTvAllBuySeeds.setText(getString(R.string.all_buy_seeds));
    mTvSellSeeds.setText(getString(R.string.sell_seeds_number_one));
    mTvDaiSellSeeds.setText(getString(R.string.dai_sell_seeds_number_one));

    String path = SPUtils
        .getStringValue(mActivity, Config.USER_INFO, Config.HEAD_ICON_PATH, null);

    mTvAllMoney = (TextView) view.findViewById(R.id.tv_all_money);

    //获取折线图数据
//    getCharList();

//    //图标
    mChart = (LineChart) view.findViewById(R.id.chart_line);

    mChart.setOnChartValueSelectedListener(new MyOnChartValueSelectedListener());

    return view;
  }

  private void getCharList() {
    try {
      judgeNet();
      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));

      RequestParams paramsTime = new RequestParams("timechoice", getTime());

      Log.e("AllScaleFragment", "getCharList=time=" + getTime());
      list.add(paramsToken);
      list.add(paramsTime);

      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(),
          mActivity, list, Urls.GET_CHAR, mCharListener, RequestNet.POST);

    } catch (Exception e) {
      if (isAdded()) {
        showMsg(DebugUtils.convert(e.getMessage(), getString(R.string.get_char_date_fail)));
      }
    }
  }

  private String getTime() {
    if (null == time) {
      return DateUtil.getDateYM(new Date().getTime());
    } else {
      return DateUtil.getDateYM(time);
    }
  }

  /**
   * 生成一个数据
   *
   * @param count 表示图表中有多少个坐标点
   * @param range 用来生成range以内的随机数
   */
  private LineData getLineData(int count, float range, SpotDot spotDot) {
    ArrayList<String> xValues = new ArrayList<String>();
    for (int i = 0; i < count; i++) {
      // x轴显示的数据，这里默认使用数字下标显示
      xValues.add("" + i);
    }

    ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();

//    if (CollectionUtils.isNotEmpty(spotDot.getSelllist())) {
    LineDataSet lineDataSet1 = getLineDataSet(spotDot.getSelllist(), range, Color.RED,
        getString(R.string.red_standfor));
    lineDataSets.add(lineDataSet1);
//    }

//    if (CollectionUtils.isNotEmpty(spotDot.getBuylist())) {
    LineDataSet lineDataSet2 = getLineDataSet(spotDot.getBuylist(), count, range, Color.BLUE,
        getString(R.string.blue_standfor));
    lineDataSets.add(lineDataSet2);
//    }

    return new LineData(xValues, lineDataSets);
  }


  public LineDataSet getLineDataSet(List<SelllistBean> list, float range, int lineColor,
      String lable) {

    if (CollectionUtils.isNotEmpty(list)) {
      // y轴的数据
      ArrayList<Entry> yValues = new ArrayList<Entry>();
      for (int i = 0; i < list.size(); i++) {
        yValues.add(new Entry(Float.parseFloat(list.get(i).getY()),
            Integer.parseInt(list.get(i).getX())));//value为y轴的值，i表示x轴的值
//      yValues.add(new Entry(value, i));//value为y轴的值，i表示x轴的值
      }

      // 一条折线
      LineDataSet lineDataSet = new LineDataSet(yValues, "");

      //设置线的参数
      lineDataSet.setLineWidth(1.5f); // 线宽
      lineDataSet.setCircleSize(3f);// 显示的圆形大小
      lineDataSet.setColor(lineColor);// 线的显示颜色
      lineDataSet.setCircleColor(lineColor);// 圆形的颜色
//    lineDataSet.setHighLightColor(Color.RED); // 高亮的线的颜色
      lineDataSet.setLabel(lable);

      return lineDataSet;

    } else {
      // y轴的数据
      ArrayList<Entry> yValues = new ArrayList<Entry>();
      yValues.add(new Entry(Float.parseFloat("0"), 0));//value为y轴的值，i表示x轴的值

      // 一条折线
      LineDataSet lineDataSet = new LineDataSet(yValues, "");

      //设置线的参数
      lineDataSet.setLineWidth(1.5f); // 线宽
      lineDataSet.setCircleSize(0f);// 显示的圆形大小
      lineDataSet.setColor(lineColor);// 线的显示颜色
      lineDataSet.setCircleColor(lineColor);// 圆形的颜色
      lineDataSet.setDrawCircles(false);
      lineDataSet.setDrawValues(false);
//    lineDataSet.setHighLightColor(Color.RED); // 高亮的线的颜色
      lineDataSet.setLabel(lable);

      return lineDataSet;
    }

  }

  public LineDataSet getLineDataSet(List<BuylistBean> list, int count, float range, int lineColor,
      String lable) {

    if (CollectionUtils.isNotEmpty(list)) {
      // y轴的数据
      ArrayList<Entry> yValues = new ArrayList<Entry>();
      for (int i = 0; i < list.size(); i++) {
        yValues.add(new Entry(Float.parseFloat(list.get(i).getY()),
            Integer.parseInt(list.get(i).getX())));//value为y轴的值，i表示x轴的值
//      yValues.add(new Entry(value, i));//value为y轴的值，i表示x轴的值
      }

      // 一条折线
      LineDataSet lineDataSet = new LineDataSet(yValues, "");

      //设置线的参数
      lineDataSet.setLineWidth(1.5f); // 线宽
      lineDataSet.setCircleSize(3f);// 显示的圆形大小
      lineDataSet.setColor(lineColor);// 线的显示颜色
      lineDataSet.setCircleColor(lineColor);// 圆形的颜色
//    lineDataSet.setHighLightColor(Color.RED); // 高亮的线的颜色
      lineDataSet.setLabel(lable);

      return lineDataSet;
    } else {
      // y轴的数据
      ArrayList<Entry> yValues = new ArrayList<Entry>();
      yValues.add(new Entry(Float.parseFloat("0"), 0));//value为y轴的值，i表示x轴的值

      // 一条折线
      LineDataSet lineDataSet = new LineDataSet(yValues, "");

      //设置线的参数
      lineDataSet.setLineWidth(1.5f); // 线宽
      lineDataSet.setCircleSize(0f);// 显示的圆形大小
      lineDataSet.setColor(lineColor);// 线的显示颜色
      lineDataSet.setCircleColor(lineColor);// 圆形的颜色
      lineDataSet.setDrawCircles(false);
      lineDataSet.setDrawValues(false);
//    lineDataSet.setHighLightColor(Color.RED); // 高亮的线的颜色
      lineDataSet.setLabel(lable);

      return lineDataSet;
    }

  }

  // 设置显示的样式
  private void showChart(LineChart lineChart, LineData lineData) {
    lineChart.setDrawBorders(false);  //是否在折线图上添加边框

    lineChart.setDescription("");// 数据描述

    // 如果没有数据的时候，会显示这个，类似listview的emtpyview
    lineChart.setNoDataTextDescription("");

    // 是否取消后面的格子
    lineChart.setDrawGridBackground(true); // 是否显示表格颜色
    lineChart.setGridBackgroundColor(
        ColorUtil.getColor(R.color.transparenc_all, mActivity)); // 表格的的颜色，在这里是是给颜色设置一个透明度

    // enable touch gestures
    lineChart.setTouchEnabled(true); // 设置是否可以触摸

    // enable scaling and dragging
    lineChart.setDragEnabled(true);// 是否可以拖拽
    lineChart.setScaleEnabled(true);// 是否可以缩放

    // if disabled, scaling can be done on x- and y-axis separately
    lineChart.setPinchZoom(true);//

    lineChart.setBackgroundColor(ColorUtil.getColor(R.color
        .white, getActivity()));// 表格背景背景

    lineChart.setData(lineData); // 设置数据

    Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

    mLegend.setForm(LegendForm.CIRCLE);// 样式
    mLegend.setFormSize(6f);// 字体
    mLegend.setTextColor(Color.BLACK);// 下面提示文字的颜色
//      mLegend.setTypeface(mTf);// 字体

    XAxis xAxis = lineChart.getXAxis();
//    xAxis.setTextColor(Color.RED);
    xAxis.setEnabled(true);
    xAxis.setDrawGridLines(true);

    lineChart.animateX(2500); // 立即执行的动画,x轴
  }

  @Override
  public void initData() {
    try {
      judgeNet();
      ArrayList<RequestParams> list = new ArrayList<>();
      RequestParams paramsToken = new RequestParams(Config.TOKEN,
          SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
      list.add(paramsToken);
      RequestNet requestNet = new RequestNet((MyApplication) mActivity.getApplication(), mActivity,
          list, Urls.ALL_SCAN, mAllScanListener, RequestNet.POST);
    } catch (Exception e) {
//      showMsg(TextUtils.isEmpty(e.getMessage()) ? getString(R.string.get_user_info_fail)
//          : e.getMessage());
    }
  }


  private void getAllScanData(Result<AllScanResultBean> result) {
    if (null == result || result.getResult() == null) {
      showMsg(getInfo(R.string.get_user_info_fail));
      return;
    }
    if (ResultUtil.isSuccess(result)) {
      updatePage(result);
    } else {
      showMsg(getInfo(R.string.get_user_info_fail));
    }
  }

  private void updatePage(Result<AllScanResultBean> result) {
    if (null == result || null == result.getResult()) {
      return;
    }
    AllScanResultBean resultBean = result.getResult();

    String price = result.getResult().getPrice();
    String month = result.getResult().getTimechoice();

    mTvMonthInfo.setText(DebugUtils.convert(month, ""));

    ChartBean chart = result.getResult().getChart();
    if (null != chart) {
      List<SeedinBean> seedin = chart.getSeedin();
      List<SeedinBeanOut> seedout = chart.getSeedout();

      SpotDot spotDot = new SpotDot();
      spotDot.setBuylist(getBuyList(seedin));
      spotDot.setSelllist(getSellList(seedout));
      adapterChar(spotDot);
    }

    mTvAllMoney.setText(DebugUtils.convert(resultBean.getTotalassets(), ""));
    mTvDreamPackage.setText(DebugUtils.convert(resultBean.getDreambag(), ""));
    mTvMoneyPackage.setText(DebugUtils.convert(resultBean.getRewardbag(), ""));

    setCount(resultBean.getActivecode(), "", mTvActiveCode);
    setCount(resultBean.getCatalyst(), "", mTvCuihuaji);

    mTvMyAllEarnings.setText(DebugUtils.convert(resultBean.getTotalincome(), ""));

    StringUtil.setTextSizeNew(
        getString(R.string.all_buy_seeds) + "       " + resultBean.getBuyseedcount() + "PCS",
        mTvAllBuySeeds, 14, resultBean.getBuyseedcount().charAt(0), 9,
        ColorUtil.getColor(R.color.c333, mActivity));

    StringUtil.setTextSizeNew(
        getString(R.string.sell_seeds_number_one) + "   " + resultBean.getSellseedcount()
            + "PCS",
        mTvSellSeeds, 14, resultBean.getSellseedcount().charAt(0), 9,
        ColorUtil.getColor(R.color.c333, mActivity));

    StringUtil.setTextSizeNew(
        getString(R.string.dai_sell_seeds_number_one) + "   " + resultBean.getWaitsell() + "PCS",
        mTvDaiSellSeeds, 14, resultBean.getWaitsell().charAt(0), 9,
        ColorUtil.getColor(R.color.c333, mActivity));

  }

  private List<SelllistBean> getSellList(List<SeedinBeanOut> seedout) {
    ArrayList<SelllistBean> objects = new ArrayList<>();
    if (!CollectionUtils.isEmpty(seedout)) {
      for (SeedinBeanOut bean : seedout) {
        if (null == bean || TextUitl.isEmpty(bean.getSeedx()) || TextUitl
            .isEmpty(bean.getSeedy())) {
          continue;
        }
        SelllistBean buy = new SelllistBean();
        buy.setX(bean.getSeedx());
        buy.setY(bean.getSeedy());

        objects.add(buy);
      }
    }
    return objects;
  }

  private List<BuylistBean> getBuyList(List<SeedinBean> seedin) {
    ArrayList<BuylistBean> objects = new ArrayList<>();
    if (!CollectionUtils.isEmpty(seedin)) {
      for (SeedinBean bean : seedin) {
        if (null == bean || TextUitl.isEmpty(bean.getSeedx()) || TextUitl
            .isEmpty(bean.getSeedy())) {
          continue;
        }
        BuylistBean buy = new BuylistBean();
        buy.setX(bean.getSeedx());
        buy.setY(bean.getSeedy());

        objects.add(buy);
      }
    }
    return objects;
  }

  private List<SelllistBean> getSellList1(List<CharResultBean.ChartBean.SeedinBeanOut> seedout) {
    ArrayList<SelllistBean> objects = new ArrayList<>();
    if (!CollectionUtils.isEmpty(seedout)) {
      for (CharResultBean.ChartBean.SeedinBeanOut bean : seedout) {
        if (null == bean || TextUitl.isEmpty(bean.getSeedx()) || TextUitl
            .isEmpty(bean.getSeedy())) {
          continue;
        }
        SelllistBean buy = new SelllistBean();
        buy.setX(bean.getSeedx());
        buy.setY(bean.getSeedy());

        objects.add(buy);
      }
    }
    return objects;
  }

  private List<BuylistBean> getBuyList1(List<CharResultBean.ChartBean.SeedinBean> seedin) {
    ArrayList<BuylistBean> objects = new ArrayList<>();
    if (!CollectionUtils.isEmpty(seedin)) {
      for (CharResultBean.ChartBean.SeedinBean bean : seedin) {
        if (null == bean || TextUitl.isEmpty(bean.getSeedx()) || TextUitl
            .isEmpty(bean.getSeedy())) {
          continue;
        }
        BuylistBean buy = new BuylistBean();
        buy.setX(bean.getSeedx());
        buy.setY(bean.getSeedy());

        objects.add(buy);
      }
    }
    return objects;
  }


  public void setCount(String count, String danwei, TextView textView) {
    textView.setText(StringUtil.getBufferString(DebugUtils.convert(count, "0"), danwei));
  }

  public void setPrice(String count, String price, TextView textView) {

    textView
        .setText(StringUtil.getBufferString(DebugUtils.convert(count, "0"),
            getString(R.string.ke), "(", StringUtil.getThreeString(
                DemicalUtil.multile(DebugUtils.convert(count, "0"),
                    DebugUtils.convert(price, "0"))),
            ")"));

  }

  public void setPrice(String pre, String count, String price, TextView textView) {

    textView
        .setText(StringUtil.getBufferString(pre, DebugUtils.convert(count, "0"),
            getString(R.string.ke), "(", StringUtil.getThreeString(
                DemicalUtil.multile(DebugUtils.convert(count, "0"),
                    DebugUtils.convert(price, "0"))),
            ")"));

  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (null != unbinder) {
      unbinder.unbind();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mBind.unbind();
  }

  @OnClick({R.id.iv_back_click, R.id.tv_month, R.id.buy_btn_click, R.id.sell_btn_click})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_back_click://回到奖金背包
//        Intent intent0 = new Intent(getActivity(), MainActivity.class);
//        intent0.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent0);
        mActivity.finish();
        break;
//      case R.id.iv_delete_click://回到首页
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        break;
//      case R.id.icon_click:
//      case R.id.user_name_click:
//        Intent intent1 = new Intent(getActivity(), UserCenterActivity.class);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent1);
//        break;
//      case R.id.iv_my_team_click:
//      case R.id.tv_my_team_click:
//        Intent intent2 = new Intent(getActivity(), MyTeamActivity.class);
//        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent2);
//        break;
      case R.id.buy_btn_click://买入种子记录
        Intent intent3 = new Intent(getActivity(), BuySeedsRecordActivity.class);
        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent3);
        break;
      case R.id.sell_btn_click://卖出种子记录
        Intent intent4 = new Intent(getActivity(), SellSeedsRecordActivity.class);
        intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent4);
        break;
//      case R.id.tv_finance_center_click:
//      case R.id.iv_finance_center_click:
//
//        break;
      case R.id.tv_month:
        showDialogOfMonths();
        break;
    }
  }

  private void showDialogOfMonths() {
    timePick();
  }

  private void timePick() {
    //时间选择器
    TimePickerView pvTime = new TimePickerView.Builder(mActivity,
        new TimePickerView.OnTimeSelectListener() {
          @Override
          public void onTimeSelect(Date date, View v) {//选中事件回调
            String stringData = DateUtil.getStringDataMonth(date.getTime());
            mTvMonthInfo.setText(stringData);
            time = date.getTime();

            //get date of char
            getCharList();
          }
        })
        .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
        .setContentSize(15)
        .gravity(Gravity.CENTER)
        .setLabel(getString(R.string.year), getString(R.string.month), null, null, null,
            null)//默认设置为年月日时分秒
        .build();
    pvTime.setDate(Calendar
        .getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
    pvTime.show();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
//      case R.id.rl_finance:
//        Intent intent = new Intent(getActivity(), UserDreamsActivity.class);
//        startActivity(intent);
//        break;
    }
  }

  private class MyOnChartValueSelectedListener implements OnChartValueSelectedListener {

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
      showMsg(new StringBuilder().append(getString(R.string.time) + " :").append(e.getXIndex())
          .append(", ")
          .append(getString(R.string.pay_receive) + " :").append(e.getVal()).toString());
    }

    @Override
    public void onNothingSelected() {

    }
  }
}

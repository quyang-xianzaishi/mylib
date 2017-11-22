package com.example.qlibrary.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.example.qlibrary.R;
import com.example.qlibrary.dialog.interfaces.MyDialogListener;
import com.example.qlibrary.dialog.interfaces.MyItemDialogListener;
import com.example.qlibrary.interfaces.OnDateListener;
import com.example.qlibrary.utils.CollectionUtils;
import com.example.qlibrary.utils.TextUitl;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */

public class StytledDialog {
  //android.support.v7.app.

  /**
   * 指定supportv7包,达到各版本ui效果一致
   */
  public static android.support.v7.app.AlertDialog showMdAlert(Activity activity, String title,
      String msg,
      String firstTxt, String secondTxt, String thirdTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener) {
    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
        activity);
    builder.setTitle(title)
        .setMessage(msg)
        .setCancelable(cancleable)
        .setPositiveButton(firstTxt, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            listener.onFirst(dialog);
          }
        })
        .setNegativeButton(secondTxt, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            listener.onSecond(dialog);
          }
        }).setNeutralButton(thirdTxt, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        listener.onThird(dialog);
      }
    });
    android.support.v7.app.AlertDialog dialog = builder.create();
    dialog.setCanceledOnTouchOutside(outsideCancleable);
    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
        listener.onCancle();
      }
    });
    dialog.show();
    return dialog;
  }


  public static void dismiss(Dialog dialog) {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }
  }

  public static void dismiss(AppCompatDialog dialog) {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }
  }


  /**
   * 弹出带有title和内容的dailog
   */
  public static Dialog showIosAlert(Context activity, String title, String msg,
      String firstTxt, String secondTxt, String thirdTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener) {
    return showIosAlert(activity, false, title, msg, firstTxt, secondTxt, thirdTxt,
        outsideCancleable, cancleable, listener);
  }

  /**
   * 弹出带有title和内容的dailog
   */
  public static Dialog showIosAlert(boolean msgCeter, int contetColorId, Context activity,
      String title, String msg,
      String firstTxt, String secondTxt, String thirdTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener) {
    return showIosAlertColor(msgCeter, contetColorId, activity, false, title, msg, firstTxt,
        secondTxt,
        thirdTxt,
        outsideCancleable, cancleable, listener);
  }

  /**
   * 弹出带有title和内容的dailog
   */
  public static Dialog showOneBtn(boolean msgCenter, int msgColorid, Context activity, String title,
      String msg,
      String firstTxt, String secondTxt, String thirdTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener) {
    return showOneBtnAlert(msgCenter,  msgColorid, activity,
    false, title, msg, firstTxt, secondTxt, thirdTxt,
        outsideCancleable, cancleable, listener);
  }


  public static Dialog showBuySeedDialog(Context activity, String title, String msg, String msgDown,
      String firstTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener) {
    return showBuySeed(activity, false, title, msg, firstTxt,
        outsideCancleable, cancleable, listener, msgDown);
  }


  public static Dialog showResetPwdDialog(Context activity, String title, String msg,
      String msgDown,
      String firstTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener) {
    return showBuySeed(activity, false, title, msg, firstTxt,
        outsideCancleable, cancleable, listener, msgDown);
  }

  public static Dialog showTwoBtnDialog(Context activity, String title, String msg, String firstTxt,
      String seconTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener) {
    return show2Btn(activity, false, title, msg, seconTxt,
        outsideCancleable, cancleable, listener, firstTxt);
  }

  /**
   * 弹出带有title和内容的dailog
   */
  public static Dialog showAlert(Context activity, String title, String msg,
      String firstTxt, String secondTxt, String thirdTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener, boolean center) {
    return showMyIosAlert(activity, false, title, msg, firstTxt, secondTxt, thirdTxt,
        outsideCancleable, cancleable, listener, center);
  }


  /**
   * todo
   */
  public static Dialog showIosAlertVertical(Context activity, String title, String msg,
      String firstTxt, String secondTxt, String thirdTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener) {
    return showIosAlert(activity, true, title, msg, firstTxt, secondTxt, thirdTxt,
        outsideCancleable, cancleable, listener);
  }

  private static Dialog showOneBtnAlert(boolean msgCenter, int msgColorid, Context context,
      boolean isButtonVerticle, String title,
      String msg,
      String firstTxt, String secondTxt, String thirdTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener) {

    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);

    int height = assigOneBtn(  msgCenter,
     msgColorid, context, dialog, isButtonVerticle, title, msg, firstTxt,
    secondTxt, thirdTxt, listener);

    setDialogStyle(context, dialog, height);

    dialog.show();
    return dialog;
  }

  private static Dialog show2Btn(Context context, boolean isButtonVerticle, String title,
      String msg,
      String firstTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener, String msgDown) {

    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);

    int height = assig2Btn(context, dialog, isButtonVerticle, title, msg, firstTxt,
        listener, msgDown);

    setDialogStyle(context, dialog, height);

    dialog.show();
    return dialog;
  }

  private static Dialog showBuySeed(Context context, boolean isButtonVerticle, String title,
      String msg,
      String firstTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener, String msgDown) {

    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);

    int height = assigBuySeeds(context, dialog, isButtonVerticle, title, msg, firstTxt,
        listener, msgDown);

    setDialogStyle(context, dialog, height);

    dialog.show();
    return dialog;
  }

  private static Dialog showResetPwd(Context context, boolean isButtonVerticle, String title,
      String msg,
      String firstTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener, String msgDown) {

    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);

    int height = assigResetPwd(context, dialog, isButtonVerticle, title, msg, firstTxt,
        listener, msgDown);

    setDialogStyle(context, dialog, height);

    dialog.show();
    return dialog;
  }

  private static Dialog showIosAlert(Context context, boolean isButtonVerticle, String title,
      String msg,
      String firstTxt, String secondTxt, String thirdTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener) {

    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);

    int height = assigIosAlertView(context, dialog, isButtonVerticle, title, msg, firstTxt,
        secondTxt, thirdTxt, listener);

    setDialogStyle(context, dialog, height);

    dialog.show();
    return dialog;
  }

  private static Dialog showIosAlertColor(boolean msgCenter, int contetColorId, Context context,
      boolean isButtonVerticle, String title,
      String msg,
      String firstTxt, String secondTxt, String thirdTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener) {

    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);

    int height = assigIosAlertViewColor(msgCenter, contetColorId, context, dialog, isButtonVerticle,
        title,
        msg, firstTxt,
        secondTxt, thirdTxt, listener);

    setDialogStyle(context, dialog, height);

    dialog.show();
    return dialog;
  }

  private static Dialog showMyIosAlert(Context context, boolean isButtonVerticle, String title,
      String msg,
      String firstTxt, String secondTxt, String thirdTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyDialogListener listener, boolean center) {

    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);

    int height = assigIosAlertCenterView(context, dialog, isButtonVerticle, title, msg, firstTxt,
        secondTxt, thirdTxt, listener, center);

    setDialogStyle(context, dialog, height);

    dialog.show();
    return dialog;
  }

  public static Dialog showIosSingleChoose(Context context, List<String> words,
      boolean outsideCancleable, boolean cancleable,
      final MyItemDialogListener listener) {
    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);

    int measuredHeight = assignListDialogView(context, dialog, words, listener);

    Window window = dialog.getWindow();
    window.setGravity(Gravity.CENTER);

    setDialogStyle(context, dialog, measuredHeight);

    dialog.show();
    return dialog;

  }


  public static Dialog showBankChoose(Context context, List<String> words,
      boolean outsideCancleable, boolean cancleable,
      final MyItemDialogListener listener) {
    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);

    int measuredHeight = assignlinearLayoutDialogView(context, dialog, words, listener);

    Window window = dialog.getWindow();
    window.setGravity(Gravity.BOTTOM);
    window.setWindowAnimations(R.style.mystyle);

    setDialogStyle(context, dialog, measuredHeight);

    dialog.show();
    return dialog;

  }

  private static int assignlinearLayoutDialogView(final Context context, final Dialog dialog,
      final List<String> words, final MyItemDialogListener listener) {
    View root = View.inflate(context, R.layout.dialog_item, null);
    TextView one = (TextView) root.findViewById(R.id.tv_one);
    TextView two = (TextView) root.findViewById(R.id.tv_two);
    TextView cancle = (TextView) root.findViewById(R.id.tv_cancle);

    one.setText(words.get(0));
    two.setText(words.get(1));
    cancle.setText(words.get(2));

    one.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onItemClick(words.get(0), 0);
      }
    });

    two.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onItemClick(words.get(1), 1);
      }
    });

    cancle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onItemClick(words.get(2), 2);
      }
    });

    dialog.setContentView(root);

    int height = 0;

    return height;
  }

  private static int assignListDialogView(final Context context, final Dialog dialog,
      final List<String> words, final MyItemDialogListener listener) {
    View root = View.inflate(context, R.layout.dialog_ios_center_item, null);

    ListView listView = (ListView) root.findViewById(R.id.lv);
    listView.setAdapter(new BaseAdapter() {
      @Override
      public int getCount() {
        return words.size();
      }

      @Override
      public Object getItem(int position) {
        return words.get(position);
      }

      @Override
      public long getItemId(int position) {
        return position;
      }

      @Override
      public View getView(final int position, View convertView, ViewGroup parent) {
        Button view = (Button) View.inflate(context, R.layout.item_btn_bottomalert, null);
        view.setText(words.get(position));
        view.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onItemClick(words.get(position), position);
            if (dialog != null && dialog.isShowing()) {
              dialog.dismiss();
            }
          }
        });

        return view;
      }
    });

    dialog.setContentView(root);

    int height = 0;

    return height;
  }

  /**
   * 显示一个垂直方向的下面有个取消按钮的dialog
   */
  public static Dialog showBottomItemDialog(Context context,
      List<String> words, String bottomTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyItemDialogListener listener) {
    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);
    //测量高度
    int measuredHeight = assignBottomListDialogView(context, dialog, words, bottomTxt, listener);

    Window window = dialog.getWindow();
    window.setGravity(Gravity.BOTTOM);//控制dialog的在window中的位置
    window.setWindowAnimations(R.style.mystyle);

    setDialogStyle(context, dialog, measuredHeight);

    dialog.show();
    return dialog;
  }

  /**
   * 显示一个垂直方向的下面有个取消按钮的dialog
   */
  public static Dialog showBottomItemVideoDialog(Context context,
      List<String> words, String bottomTxt,
      boolean outsideCancleable, boolean cancleable,
      final MyItemDialogListener listener) {
    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);
    //测量高度
    int measuredHeight = assignBottomLisVideotDialogView(context, dialog, words, bottomTxt,
        listener);

    Window window = dialog.getWindow();
    window.setGravity(Gravity.BOTTOM);//控制dialog的在window中的位置
    window.setWindowAnimations(R.style.mystyle);

    setDialogStyle(context, dialog, measuredHeight);

    dialog.show();
    return dialog;
  }

  /**
   * 确定一个垂直方向，下面有个取消按钮的dialog的高度
   */
  private static int assignBottomListDialogView(final Context context, final Dialog dialog,
      final List<String> words, String bottomTxt, final MyItemDialogListener listener) {
    View root = View.inflate(context, R.layout.dialog_ios_alert_bottom, null);
    Button btnBottom = (Button) root.findViewById(R.id.btn_bottom);
    if (TextUtils.isEmpty(bottomTxt)) {
      btnBottom.setVisibility(View.GONE);
    } else {
      btnBottom.setVisibility(View.VISIBLE);
      btnBottom.setText(bottomTxt);
      btnBottom.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onBottomBtnClick();
          if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
          }
        }
      });
    }

    ListView listView = (ListView) root.findViewById(R.id.lv);
    BaseAdapter adapter = new BaseAdapter() {
      @Override
      public int getCount() {
        return words.size();
      }

      @Override
      public Object getItem(int position) {
        return null;
      }

      @Override
      public long getItemId(int position) {
        return 0;
      }

      @Override
      public View getView(final int position, View convertView, ViewGroup parent) {
        Button view = (Button) View.inflate(context, R.layout.item_btn_bottomalert, null);
        view.setText(words.get(position));
        view.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onItemClick(words.get(position), position);
            if (dialog != null && dialog.isShowing()) {
              dialog.dismiss();
            }
          }
        });

        return view;
      }
    };

    listView.setAdapter(adapter);
    dialog.setContentView(root);

    int height = 0;

    return height;
  }

  /**
   * 确定一个垂直方向，下面有个取消按钮的dialog的高度
   */
  private static int assignBottomLisVideotDialogView(final Context context, final Dialog dialog,
      final List<String> words, String bottomTxt, final MyItemDialogListener listener) {
    View root = View.inflate(context, R.layout.dialog_ios_alert_bottom, null);
    Button btnBottom = (Button) root.findViewById(R.id.btn_bottom);
    btnBottom.setTextColor(Color.parseColor("#3A3AB7"));
    if (TextUtils.isEmpty(bottomTxt)) {
      btnBottom.setVisibility(View.GONE);
    } else {
      btnBottom.setVisibility(View.VISIBLE);
      btnBottom.setText(bottomTxt);
      btnBottom.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onBottomBtnClick();
          if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
          }
        }
      });
    }

    ListView listView = (ListView) root.findViewById(R.id.lv);
    BaseAdapter adapter = new BaseAdapter() {
      @Override
      public int getCount() {
        return words.size();
      }

      @Override
      public Object getItem(int position) {
        return null;
      }

      @Override
      public long getItemId(int position) {
        return 0;
      }

      @Override
      public View getView(final int position, View convertView, ViewGroup parent) {
        Button view = (Button) View.inflate(context, R.layout.item_btn_bottomalert, null);
        if (position == 0) {
          view.setTextColor(Color.parseColor("#969696"));
        } else {
          view.setTextColor(Color.parseColor("#3A3AB7"));
        }
        view.setText(words.get(position));
        view.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onItemClick(words.get(position), position);
            if (dialog != null && dialog.isShowing()) {
              dialog.dismiss();
            }
          }
        });

        return view;
      }
    };

    listView.setAdapter(adapter);
    dialog.setContentView(root);

    int height = 0;

    return height;
  }


  private static int assignCenterListDialogView(final Context context, final Dialog dialog,
      final List<String> words, final MyItemDialogListener listener) {
    View root = View.inflate(context, R.layout.dialog_ios_alert_bottom_no_cancel, null);

    RadioGroup rg = (RadioGroup) root.findViewById(R.id.rg);

    if (!CollectionUtils.isEmpty(words)) {
      for (int a = 0; a < words.size(); a++) {
        RadioButton radioButton = new RadioButton(context);
        radioButton.setText(words.get(a));
        radioButton.setTextSize(15);
        radioButton.setTextColor(Color.GRAY);
        radioButton.setCompoundDrawablePadding(15);
        radioButton.setGravity(Gravity.CENTER_VERTICAL);
        radioButton.setId(a);
        rg.addView(radioButton);
      }
    }

    rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        listener.onItemClick(words.get(checkedId), checkedId);
      }
    });

    dialog.setContentView(root);

    return 0;
  }


  /**
   * @param id height为0,weight为1的scrollview包裹的view的id,如果没有,传0或负数即可
   */
  private static int mesureHeight(View root, int id) {
    measureView(root);
    int height = root.getMeasuredHeight();
    int heightExtra = 0;
    if (id > 0) {
      View view = root.findViewById(id);
      if (view != null) {
        measureView(view);
        heightExtra = view.getMeasuredHeight();
      }

    }
    return height + heightExtra;
  }


  /**
   * 弹出dialog
   */
  public static Dialog showProgressDialog(Context context, String msg, boolean cancleable,
      boolean outsideTouchable) {

    Dialog dialog = buildDialog(context, cancleable, outsideTouchable);

    View root = View.inflate(context, R.layout.progressview_wrapconent, null);
    TextView tvMsg = (TextView) root.findViewById(R.id.message);
    tvMsg.setText(msg);
    dialog.setContentView(root);

    setDialogStyle(context, dialog, 0);
//        dialog.show();

    //根据实际view高度调整
    return dialog;
  }


  /**
   * 构建dialog
   */
  private static Dialog buildDialog(Context context, boolean cancleable, boolean outsideTouchable) {
    Dialog dialog = new Dialog(context);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(cancleable);
    dialog.setCanceledOnTouchOutside(outsideTouchable);
    return dialog;
  }


  private static void setDialogStyle(Context activity, Dialog dialog, int measuredHeight) {
    Window window = dialog.getWindow();

    //window.setWindowAnimations(R.style.dialog_center);
    //背景设为透明
    window.setBackgroundDrawable(
        new ColorDrawable(Color.TRANSPARENT));//todo keycode to show round corner

    WindowManager.LayoutParams wl = window.getAttributes();
       /* wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();*/
// 以下这两句是为了保证按钮可以水平满屏
    int width = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE))
        .getDefaultDisplay().getWidth();

    int height = (int) (
        ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
            .getHeight() * 0.9);

    // wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
    wl.width = (int) (width * 0.90);
    wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;  //TODO  一般情况下为wrapcontent,最大值为height*0.9
       /* ViewUtils.measureView(contentView);
        int meHeight = contentView.getMeasuredHeight();//height 为0,weight为1时,控件计算所得height就是0
        View textview = contentView.findViewById(R.id.tv_msg);
        ViewUtils.measureView(textview);
        int textHeight = textview.getMeasuredHeight();*/
    if (measuredHeight > height) {
      wl.height = height;
    }

    //wl.horizontalMargin= 0.2f;
// 设置显示位置
    // wl.gravity = Gravity.CENTER_HORIZONTAL;

    if (!(activity instanceof Activity)) {
      wl.type = WindowManager.LayoutParams.TYPE_TOAST;//todo keycode to improve window level
    }

    dialog.onWindowAttributesChanged(wl);
  }

  /**
   * 确定dialog高度
   */
  private static int assigIosAlertView(Context activity, final Dialog dialog,
      boolean isButtonVerticle,
      String title, String msg, String firstTxt, String secondTxt, String thirdTxt,
      final MyDialogListener listener) {
    View root = View.inflate(activity,
        isButtonVerticle ? R.layout.dialog_ios_alert_vertical : R.layout.dialog_ios_alert, null);
    TextView tvTitle = (TextView) root.findViewById(R.id.tv_title);
    if (TextUtils.isEmpty(title)) {
      tvTitle.setVisibility(View.GONE);
    } else {
      tvTitle.setVisibility(View.VISIBLE);
      tvTitle.setText(title);
    }

    TextView tvMsg = (TextView) root.findViewById(R.id.tv_msg);
    tvMsg.setText(msg);

    Button button1 = (Button) root.findViewById(R.id.btn_1);
    Button button2 = (Button) root.findViewById(R.id.btn_2);
    Button button3 = (Button) root.findViewById(R.id.btn_3);

    if (TextUtils.isEmpty(firstTxt)) {
      root.findViewById(R.id.ll_container).setVisibility(View.GONE);
      root.findViewById(R.id.line).setVisibility(View.GONE);
    } else {

      button1.setVisibility(View.VISIBLE);
      button1.setText(firstTxt);
      button1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onFirst(dialog);
          if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
          }
        }
      });

      //btn2
      if (TextUtils.isEmpty(secondTxt)) {
        root.findViewById(R.id.line_btn2).setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
      } else {
        root.findViewById(R.id.line_btn2).setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);

        button2.setText(secondTxt);
        button2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onSecond(dialog);
            if (dialog != null && dialog.isShowing()) {
              dialog.dismiss();
            }
          }
        });

        //btn 3
        if (TextUtils.isEmpty(thirdTxt)) {
          root.findViewById(R.id.line_btn3).setVisibility(View.GONE);
          button3.setVisibility(View.GONE);
        } else {
          root.findViewById(R.id.line_btn3).setVisibility(View.VISIBLE);
          button3.setVisibility(View.VISIBLE);

          button3.setText(thirdTxt);
          button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              listener.onThird(dialog);
              if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
              }
            }
          });


        }

      }

    }

    dialog.setContentView(root);

    int height = mesureHeight(root, R.id.tv_msg);

    return height;


  }

  private static int assigIosAlertViewColor(boolean msgCenter, int colorid, Context activity,
      final Dialog dialog,
      boolean isButtonVerticle,
      String title, String msg, String firstTxt, String secondTxt, String thirdTxt,
      final MyDialogListener listener) {
    View root = View.inflate(activity,
        isButtonVerticle ? R.layout.dialog_ios_alert_vertical : R.layout.dialog_ios_alert, null);
    TextView tvTitle = (TextView) root.findViewById(R.id.tv_title);
    if (TextUtils.isEmpty(title)) {
      tvTitle.setVisibility(View.GONE);
    } else {
      tvTitle.setVisibility(View.VISIBLE);
      tvTitle.setText(title);
    }

    TextView tvMsg = (TextView) root.findViewById(R.id.tv_msg);
    if (TextUitl.isEmpty(msg)) {
      tvMsg.setVisibility(View.GONE);
    } else {
      tvMsg.setVisibility(View.VISIBLE);
      tvMsg.setText(msg);
    }
    if (msgCenter) {
      tvMsg.setGravity(Gravity.CENTER);
    } else {
      tvMsg.setGravity(Gravity.START);
    }
    tvMsg.setTextColor(colorid);

    Button button1 = (Button) root.findViewById(R.id.btn_1);
    Button button2 = (Button) root.findViewById(R.id.btn_2);
    Button button3 = (Button) root.findViewById(R.id.btn_3);

    if (TextUtils.isEmpty(firstTxt)) {
      root.findViewById(R.id.ll_container).setVisibility(View.GONE);
      root.findViewById(R.id.line).setVisibility(View.GONE);
    } else {

      button1.setVisibility(View.VISIBLE);
      button1.setText(firstTxt);
      button1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onFirst(dialog);
          if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
          }
        }
      });

      //btn2
      if (TextUtils.isEmpty(secondTxt)) {
        root.findViewById(R.id.line_btn2).setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
      } else {
        root.findViewById(R.id.line_btn2).setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);

        button2.setText(secondTxt);
        button2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onSecond(dialog);
            if (dialog != null && dialog.isShowing()) {
              dialog.dismiss();
            }
          }
        });

        //btn 3
        if (TextUtils.isEmpty(thirdTxt)) {
          root.findViewById(R.id.line_btn3).setVisibility(View.GONE);
          button3.setVisibility(View.GONE);
        } else {
          root.findViewById(R.id.line_btn3).setVisibility(View.VISIBLE);
          button3.setVisibility(View.VISIBLE);

          button3.setText(thirdTxt);
          button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              listener.onThird(dialog);
              if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
              }
            }
          });


        }

      }

    }

    dialog.setContentView(root);

    int height = mesureHeight(root, R.id.tv_msg);

    return height;


  }

  /**
   * 确定dialog高度
   */
  private static int assigOneBtn(boolean msgCenter, int msgColorid, Context activity,
      final Dialog dialog,
      boolean isButtonVerticle,
      String title, String msg, String firstTxt, String secondTxt, String thirdTxt,
      final MyDialogListener listener) {
    View root = View.inflate(activity, R.layout.dialog_ios_alert, null);
    TextView tvTitle = (TextView) root.findViewById(R.id.tv_title);
    if (TextUtils.isEmpty(title)) {
      tvTitle.setVisibility(View.GONE);
    } else {
      tvTitle.setVisibility(View.VISIBLE);
      tvTitle.setText(title);
    }

    TextView tvMsg = (TextView) root.findViewById(R.id.tv_msg);
    tvMsg.setTextColor(msgColorid);
    if (msgCenter) {
      tvMsg.setGravity(Gravity.CENTER);
    } else {
      tvMsg.setGravity(Gravity.START);
    }
    if (TextUitl.isEmpty(msg)) {
      tvMsg.setVisibility(View.GONE);
    } else {
      tvMsg.setVisibility(View.VISIBLE);
      tvMsg.setText(msg);
    }

    Button button1 = (Button) root.findViewById(R.id.btn_1);
    Button button2 = (Button) root.findViewById(R.id.btn_2);
    Button button3 = (Button) root.findViewById(R.id.btn_3);
    button1.setText(firstTxt);
    button2.setText(secondTxt);
    button3.setText(thirdTxt);

    button1.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onFirst(dialog);
      }
    });

    button2.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onSecond(dialog);
      }
    });

    button3.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onThird(dialog);
      }
    });

    if (TextUitl.isEmpty(firstTxt)) {
      button1.setVisibility(View.GONE);
    } else {
      button1.setVisibility(View.VISIBLE);
    }

    if (TextUitl.isEmpty(secondTxt)) {
      button2.setVisibility(View.GONE);
    } else {
      button2.setVisibility(View.VISIBLE);
    }

    if (TextUitl.isEmpty(thirdTxt)) {
      button3.setVisibility(View.GONE);
    } else {
      button3.setVisibility(View.VISIBLE);
    }

    dialog.setContentView(root);

    int height = mesureHeight(root, R.id.tv_msg);

    return height;


  }


  /**
   * 确定dialog高度
   */
  private static int assigBuySeeds(Context activity, final Dialog dialog,
      boolean isButtonVerticle,
      String title, String msg, String firstTxt,
      final MyDialogListener listener, String msgDown) {
    View root = View.inflate(activity, R.layout.dialog_buy_seeds, null);
    TextView tvTitle = (TextView) root.findViewById(R.id.tv_title);
    tvTitle.setText(title);

    TextView tvMsg = (TextView) root.findViewById(R.id.tv_msg);
    tvMsg.setText(msg);

    Button button1 = (Button) root.findViewById(R.id.btn_1);
    TextView button2 = (TextView) root.findViewById(R.id.tv_click);
    button1.setText(firstTxt);
    button2.setText(msgDown);

    button1.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onFirst(dialog);
      }
    });

    button2.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onSecond(dialog);
      }
    });

    dialog.setContentView(root);

    int height = mesureHeight(root, R.id.tv_msg);

    return height;


  }

  /**
   * 确定dialog高度
   */
  private static int assig2Btn(Context activity, final Dialog dialog,
      boolean isButtonVerticle,
      String title, String msg, String firstTxt,
      final MyDialogListener listener, String msgDown) {
    View root = View.inflate(activity, R.layout.two_btn, null);
    TextView tvTitle = (TextView) root.findViewById(R.id.tv_title);
    tvTitle.setText(title);

    TextView tvMsg = (TextView) root.findViewById(R.id.tv_msg);
    tvMsg.setText(msg);

    Button button1 = (Button) root.findViewById(R.id.btn_1);
    TextView button2 = (TextView) root.findViewById(R.id.btn_2);
    button1.setText(msgDown);
    button2.setText(firstTxt);

    button1.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onFirst(dialog);
      }
    });

    button2.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onSecond(dialog);
      }
    });

    dialog.setContentView(root);

    int height = mesureHeight(root, R.id.tv_msg);

    return height;


  }


  /**
   * 确定dialog高度
   */
  private static int assigResetPwd(Context activity, final Dialog dialog,
      boolean isButtonVerticle,
      String title, String msg, String firstTxt,
      final MyDialogListener listener, String msgDown) {
    View root = View.inflate(activity, R.layout.dialog_reset_pwd, null);
    TextView tvTitle = (TextView) root.findViewById(R.id.tv_title);
    tvTitle.setText(title);

    TextView tvMsg = (TextView) root.findViewById(R.id.tv_msg);
    tvMsg.setText(msg);

    Button button1 = (Button) root.findViewById(R.id.btn_1);
    button1.setText(firstTxt);

    button1.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onFirst(dialog);
      }
    });

    dialog.setContentView(root);

    int height = mesureHeight(root, R.id.tv_msg);

    return height;


  }


  /**
   * 确定dialog高度
   */
  private static int assigIosAlertCenterView(Context activity, final Dialog dialog,
      boolean isButtonVerticle,
      String title, String msg, String firstTxt, String secondTxt, String thirdTxt,
      final MyDialogListener listener, boolean center) {
    View root = View.inflate(activity,
        isButtonVerticle ? R.layout.dialog_ios_alert_vertical : R.layout.dialog_ios_center_alert,
        null);
    TextView tvTitle = (TextView) root.findViewById(R.id.tv_title);
    if (TextUtils.isEmpty(title)) {
      tvTitle.setVisibility(View.GONE);
    } else {
      tvTitle.setVisibility(View.VISIBLE);
      tvTitle.setText(title);
    }

    TextView tvMsg = (TextView) root.findViewById(R.id.tv_msg);
    if (center) {
      tvMsg.setGravity(Gravity.CENTER);
    } else {
      tvMsg.setGravity(Gravity.START);
    }

    tvMsg.setText(msg);
    Button button1 = (Button) root.findViewById(R.id.btn_1);
    Button button2 = (Button) root.findViewById(R.id.btn_2);
    Button button3 = (Button) root.findViewById(R.id.btn_3);

    if (TextUtils.isEmpty(firstTxt)) {
      root.findViewById(R.id.ll_container).setVisibility(View.GONE);
      root.findViewById(R.id.line).setVisibility(View.GONE);
    } else {

      button1.setVisibility(View.VISIBLE);
      button1.setText(firstTxt);
      button1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onFirst(dialog);
          if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
          }
        }
      });

      //btn2
      if (TextUtils.isEmpty(secondTxt)) {
        root.findViewById(R.id.line_btn2).setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
      } else {
        root.findViewById(R.id.line_btn2).setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);

        button2.setText(secondTxt);
        button2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onSecond(dialog);
            if (dialog != null && dialog.isShowing()) {
              dialog.dismiss();
            }
          }
        });

        //btn 3
        if (TextUtils.isEmpty(thirdTxt)) {
          root.findViewById(R.id.line_btn3).setVisibility(View.GONE);
          button3.setVisibility(View.GONE);
        } else {
          root.findViewById(R.id.line_btn3).setVisibility(View.VISIBLE);
          button3.setVisibility(View.VISIBLE);

          button3.setText(thirdTxt);
          button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              listener.onThird(dialog);
              if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
              }
            }
          });


        }

      }

    }

    dialog.setContentView(root);

    int height = mesureHeight(root, R.id.tv_msg);

    return height;


  }


  private static void measureView(View child) {
    ViewGroup.LayoutParams p = child.getLayoutParams();
    if (p == null) {
      p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
          ,
          ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    int lpHeight = p.height;
    int lpWidth = p.width;
    int childHeightSpec;
    int childWidthSpec;
    if (lpHeight > 0) {   //如果Height是一个定值，那么我们测量的时候就使用这个定值
      childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
          View.MeasureSpec.EXACTLY);
    } else {  // 否则，我们将mode设置为不指定，size设置为0
      childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
          View.MeasureSpec.UNSPECIFIED);
    }
    if (lpWidth > 0) {
      childWidthSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
          View.MeasureSpec.EXACTLY);
    } else {
      childWidthSpec = View.MeasureSpec.makeMeasureSpec(0,
          View.MeasureSpec.UNSPECIFIED);
    }
    child.measure(childWidthSpec, childHeightSpec);
  }

  /**
   * 显示银行卡列表
   */
  public static Dialog showChoosenBankNameChoose(Context context,
      List<String> words,
      boolean outsideCancleable, boolean cancleable,
      final MyItemDialogListener listener) {
    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);
    int measuredHeight = assignCenterListDialogView(context, dialog, words, listener);

    Window window = dialog.getWindow();
    window.setGravity(Gravity.CENTER);
    window.setWindowAnimations(R.style.mystyle);

    setDialogStyle(context, dialog, measuredHeight);

    dialog.show();
    return dialog;
  }


  /**
   * 显示日历弹窗
   */
  public static Dialog showCalendarChoosen(Context context,
      boolean outsideCancleable, boolean cancleable,
      final OnDateListener listener) {
    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);
    int measuredHeight = assignCenterCalendarDialogView(context, dialog, null, listener);
    Window window = dialog.getWindow();
    window.setGravity(Gravity.CENTER);
    window.setWindowAnimations(R.style.mystyle);

    setDialogStyle(context, dialog, measuredHeight);

    dialog.show();
    return dialog;
  }


  private static int assignCenterCalendarDialogView(final Context context, final Dialog dialog,
      final List<String> words, final OnDateListener listener) {
    View root = View.inflate(context, R.layout.dialog_calendar, null);
    final MaterialCalendarView mCalendarView = (MaterialCalendarView) root
        .findViewById(R.id.calendarView);
    mCalendarView.state().edit()
        .setMinimumDate(CalendarDay.from(2000, 1, 1))
        .setMaximumDate(CalendarDay.from(2100, 1, 1))
        .setCalendarDisplayMode(CalendarMode.MONTHS)
        .commit();

    //选中的日期
    mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
      @Override
      public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date,
          boolean selected) {
        CalendarDay selectedDate = mCalendarView.getSelectedDate();
        Date date1 = selectedDate.getDate();
        listener.onDateListener(date1);
      }
    });

    dialog.setContentView(root);

    return 0;
  }

  public static Dialog showDuiHaoDialog(Context context, boolean cancleable,
      boolean outsideCancleable, String tip) {
    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);
    int measuredHeight = assignDuiDialogView(context, dialog, tip);
    Window window = dialog.getWindow();
    window.setGravity(Gravity.CENTER);
    setDialogStyle(context, dialog, measuredHeight);
    dialog.show();
    return dialog;
  }

  public static Dialog showDeleteCardDialog(Context context, boolean cancleable,
      boolean outsideCancleable, String tip) {
    Dialog dialog = buildDialog(context, cancleable, outsideCancleable);
    int measuredHeight = assignDeleteDialogView(context, dialog, tip);
    Window window = dialog.getWindow();
    window.setGravity(Gravity.CENTER);
    setDialogStyle(context, dialog, measuredHeight);
    dialog.show();
    return dialog;
  }

  private static int assignDuiDialogView(final Context context, final Dialog dialog, String tip) {
    View root = View.inflate(context, R.layout.duihao_text_layout, null);
    TextView view = (TextView) root.findViewById(R.id.tv_text);
    view.setText(tip);
    dialog.setContentView(root);

    int height = 0;
    return height;
  }

  private static int assignDeleteDialogView(final Context context, final Dialog dialog,
      String tip) {
    View root = View.inflate(context, R.layout.delete_card_layout, null);
    TextView view = (TextView) root.findViewById(R.id.tv_text);
    view.setText(tip);
    dialog.setContentView(root);

    int height = 0;
    return height;
  }
}

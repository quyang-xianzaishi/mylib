package com.example.qlibrary.dialog.interfaces;

import android.content.DialogInterface;

/**
 * Created by Administrator on 2017/6/9.
 */

public abstract class MyDialogListener {

    public abstract void onFirst(DialogInterface dialog);

    public abstract void onSecond(DialogInterface dialog);

    public void onThird(DialogInterface dialog){}

    public void onCancle(){}

    public  void onCommit(DialogInterface dialog){};

}

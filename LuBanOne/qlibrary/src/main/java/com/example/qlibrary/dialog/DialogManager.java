package com.example.qlibrary.dialog;

import android.app.Activity;
import android.app.Dialog;

import com.example.qlibrary.dialog.StytledDialog;

/**
 * Created by quyang on 2017/6/26 0026.
 */

public class DialogManager {

    private  Dialog sDialog;

    public DialogManager(Activity contxt, String msg) {
        sDialog = StytledDialog.showProgressDialog(contxt, msg, true, false);
    }

    public void dismiss() {
        sDialog.dismiss();
    }

    public void show() {
        sDialog.show();
    }
}

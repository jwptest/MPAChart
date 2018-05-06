package com.finance.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.finance.R;

/**
 * 启动dialog
 */
public class StartDialog extends Dialog {

    public StartDialog(@NonNull Context context) {
        super(context, R.style.noBackDialog);
//    <!--关键点1-->
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_start, null);
//        <!--关键点2-->
        setContentView(view);
//        <!--关键点3-->
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
//        <!--关键点4-->
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



}

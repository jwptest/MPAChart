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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.finance.R;
import com.finance.common.UserCommon;
import com.finance.common.UserShell;
import com.finance.event.DataRefreshEvent;
import com.finance.event.EventBus;
import com.finance.event.UserLoginEvent;
import com.finance.interfaces.ICallback;
import com.finance.model.ben.UserInfoEntity;
import com.finance.utils.HandlerUtil;

import org.greenrobot.eventbus.Subscribe;

/**
 * 启动dialog
 */
public class StartDialog extends Dialog {

    private Context mContext;
    private int imageId;

    public StartDialog(@NonNull Context context, int imageId) {
        super(context, R.style.fullScreenDialog);
        this.mContext = context;
        this.imageId = imageId;
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

    private boolean isLogin = false;//是否登录完成
    private boolean isData = false;//是否获取数据完成
    private boolean isCountDown = false;//倒计时是否完成
    //延迟执行操作
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.register(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_start, null);
        setContentView(view);
        Glide.with(mContext)
                .load(imageId)
                .skipMemoryCache(true)
                .into((ImageView) findViewById(R.id.ivBg));
        setCancelable(false);
        //如果10秒钟还没有请求成功，就关闭对话框
        HandlerUtil.runOnUiThreadDelay(new Runnable() {
            @Override
            public void run() {
                isCountDown = true;
                toMainActivity();
            }
        }, 2000);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                dismiss();//超时没有处理完成，关闭对话框
            }
        };
        HandlerUtil.runOnUiThreadDelay(mRunnable, 5000);
        login();
    }

    private void login() {
        UserCommon.getUserInfo(mContext, new ICallback<UserInfoEntity>() {
            @Override
            public void onCallback(int code, UserInfoEntity userInfoEntity, String message) {
                isLogin = true;
                toMainActivity();
                EventBus.post(new UserLoginEvent(UserShell.getInstance().isLogin()));
            }
        });
    }

    @Override
    public void dismiss() {
        EventBus.unregister(this);
        HandlerUtil.removeRunable(mRunnable);
        super.dismiss();
    }

    @Subscribe
    public void onEvent(DataRefreshEvent event) {
        if (event == null) return;
        isData = event.isRefresh();
        toMainActivity();
    }

    private void toMainActivity() {
        if (!isLogin || !isCountDown || !isData) return;
        dismiss();
    }

}

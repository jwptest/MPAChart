package com.finance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.finance.base.BaseActivity;
import com.finance.common.UserCommon;
import com.finance.common.UserShell;
import com.finance.interfaces.ICallback;
import com.finance.model.ben.UserInfoEntity;
import com.finance.ui.main.MainChartActivity;
import com.finance.utils.HandlerUtil;

import butterknife.BindView;

/**
 * 启动页面
 */
public class StartActivity extends BaseActivity {

    @BindView(R.id.ivBg)
    ImageView ivBg;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.ivTip)
    ImageView ivTip;
    @BindView(R.id.pbLoad)
    ProgressBar pbLoad;

    private Runnable mRunnable;

    private boolean isLogin = false;//是否登录
    private boolean isCountDown = false;//倒计时是否完成

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void onCreated() {
        pbLoad.setVisibility(View.INVISIBLE);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                setProgress();
                load();
            }
        };
        HandlerUtil.runOnUiThreadDelay(mRunnable, 2000);
        login();
    }

    private void load() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                isCountDown = true;
                setProgress();
                toMainActivity();
            }
        };
        HandlerUtil.runOnUiThreadDelay(mRunnable, 1000);
    }

    private void setProgress() {
        if (pbLoad.getVisibility() != View.VISIBLE) {
            pbLoad.setVisibility(View.VISIBLE);
            pbLoad.setProgress(20);
            return;
        }
        if ((isLogin && !isCountDown) || (!isLogin && isCountDown)) {
            pbLoad.setProgress(80);
            return;
        }
        pbLoad.setProgress(100);
    }

    private void login() {
        UserCommon.getUserInfo(mActivity, new ICallback<UserInfoEntity>() {
            @Override
            public void onCallback(int code, UserInfoEntity userInfoEntity, String message) {
                if (userInfoEntity != null) {
                    isLogin = true;
                } else {
                    isLogin = UserShell.getInstance().isLogin();
                }
                setProgress();
                toMainActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void toMainActivity() {
        if (!isLogin || !isCountDown) return;
        startActivity(new Intent(mActivity, MainChartActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mRunnable != null)
            HandlerUtil.removeRunable(mRunnable);
        super.onDestroy();
    }
}

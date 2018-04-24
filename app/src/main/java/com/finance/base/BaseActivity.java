package com.finance.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.finance.App;
import com.finance.event.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

    private boolean isEvent = false;
    private Unbinder mUnbinder;
    protected BaseActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            mUnbinder = ButterKnife.bind(this);
        }
        if (isEvent()) {
            registerEvent();
        }
        onCreated();
    }

    //注册事件
    protected void registerEvent() {
        isEvent = true;
        EventBus.register(this);
    }

    //注销事件
    protected void unregisterEvent() {
        isEvent = false;
        EventBus.unregister(this);
    }

    //是否注册事件
    protected boolean isEvent() {
        return false;
    }

    //获取当前布局Id
    abstract protected int getLayoutId();

    //子类的初始化方法
    abstract protected void onCreated();

    @Override
    protected void onDestroy() {
        if (isEvent) unregisterEvent();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        super.onDestroy();
    }

    public void showErrorMsg(String msg) {
        App.getInstance().showErrorMsg(msg);
    }

}

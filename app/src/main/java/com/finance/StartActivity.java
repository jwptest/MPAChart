package com.finance;

import com.finance.base.BaseActivity;
import com.finance.model.http.NetworkConnection;

/**
 * 启动页面
 */
public class StartActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void onCreated() {

        new NetworkConnection(this).start();

    }

}

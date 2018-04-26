package com.finance.ui.test;

import android.widget.TextView;

import com.finance.R;
import com.finance.base.BaseActivity;
import com.finance.model.http.NetworkConnection2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 */
public class NetworkActivity extends BaseActivity {

    @BindView(R.id.tvSend)
    TextView tvSend;

    private NetworkConnection2 mConnection;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_network;
    }

    @Override
    protected void onCreated() {

    }

    @OnClick(R.id.tvSend)
    public void onViewClicked() {
         new NetworkConnection2().start();
    }


}

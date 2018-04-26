package com.finance.ui.test;

import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.finance.R;
import com.finance.base.BaseActivity;
import com.google.gson.JsonElement;

import butterknife.BindView;
import butterknife.OnClick;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.Connection;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.StateChangedCallback;

/**
 *
 */
public class NetworkActivity extends BaseActivity {

    @BindView(R.id.tvSend)
    TextView tvSend;

//    private NetworkConnection mConnection;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_network;
    }

    @Override
    protected void onCreated() {

    }

    @OnClick(R.id.tvSend)
    public void onViewClicked() {
//        new NetworkConnection(this).start();
        beginConnect();
    }


    String url = "https://i.api789.top:8080/indexMark";
    Connection connection = new Connection(url);
    String Tag = "KING";

    private void beginConnect() {
        connection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(JsonElement json) {
                //先主线程后切换到子线程运行
                Log.d(Tag, "onMessageReceived: " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                Log.d(Tag, "received message: " + json.toString());
            }
        });

        connection.stateChanged(new StateChangedCallback() {
            @Override
            public void stateChanged(ConnectionState connectionState, ConnectionState connectionState1) {
                //子线程运行
                Log.d(Tag, "old state: " + connectionState + "--> state2:" + connectionState1);
                Log.d(Tag, "stateChanged: " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
            }
        });

        connection.start().done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                //子线程运行
                Log.d(Tag, "run: " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                //发送指数
                Point _point = new Point();
                Log.d(Tag, _point.toString());
                connection.send(_point);
            }
        });
    }

    private class Point {
        //        String D = "{sourcecode\":101,\"messageid\":\"7d8d4171-6133-edb3-9f07-3303c8aa06de\",\"device\":1,\"platformid\":\"2\",\"sign\":\"b5cc7a0232a6e70e838d030f27d69f91\",\"t\":1101}";
//        int T = 1101;
//        String Token = "";
        String D = "106";
        int T = 0;
        String Token = "";
    }


}

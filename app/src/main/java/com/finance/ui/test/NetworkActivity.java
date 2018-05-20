package com.finance.ui.test;

import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.finance.R;
import com.finance.base.BaseActivity;
import com.finance.model.http.BaseParams;
import com.finance.model.http.HttpConnection;
import com.finance.model.http.JsonCallback;
import com.finance.model.http.JsonCallback2;
import com.finance.model.imps.NetworkRequest;
import com.google.gson.JsonElement;

import butterknife.BindView;
import butterknife.OnClick;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.Connection;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.SignalRFuture;
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

    private HttpConnection conection;

    @Override
    protected void onCreated() {
        BaseParams baseParams = new BaseParams(102);
//        baseParams.addParam("NickName", "123456");
        baseParams.addParam("Password", "123456");
        baseParams.addParam("UserName", "tt1234567");


    }

    @OnClick(R.id.tvSend)
    public void onViewClicked() {
        BaseParams baseParams = new BaseParams(101);
//        baseParams.addParam("NickName", "123456");
//        baseParams.addParam("Password", "123456");
//        baseParams.addParam("UserName", "tt1234567");
//        HttpConnection.network(new MessageReceivedHandler() {
//            @Override
//            public void onMessageReceived(JsonElement jsonElement) {
//                Log.d("123", "received message: " + jsonElement.toString());
//            }
//        }, baseParams.getParams());

        NetworkRequest.getInstance()
                .getHttpConnection()
                .setTag(this)
                .setT(1101)
                .setParams(baseParams)
                .execute(new JsonCallback2<String>(String.class) {
                    @Override
                    public void onSuccessed(int code, String msg, boolean isFromCache, String result) {

                    }

                    @Override
                    public void onFailed(int code, String msg, boolean isFromCache) {

                    }
                });
    }

    @OnClick(R.id.tvSend2)
    public void onViewClicked2() {
//        beginConnect();


    }

    String url = "https://i.api789.top:8080/indexMark";
    Connection connection = new Connection(url);
    String Tag = "KING";
    SignalRFuture<Void> signal;

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
        signal = connection.start().done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                //子线程运行
                Log.d(Tag, "run: " + (Thread.currentThread() == Looper.getMainLooper().getThread()));
                //发送指数
                Point _point = new Point();
                connection.send(_point);
            }
        });
        //{
        // "PlatformId":9,"MessageId":"3fc6bf1a-a7d1-4136-9ca5-5b304799b03c",
        // "Password":"123456","Sign":"b48e67dbf5bdb91633afef1b24016314"}


        //{"MessageId":"3fc6bf1a-a7d1-4136-9ca5-5b304799b03c","Device":1,
        // "PlatformId":9,"Sign":"d190d31b8462af8deec7c307e11be198","Token":"",
        // "T":200,"SourceCode":102,"UserName":"tt1234567","Password":"123456"}


    }

    private class Point {
        //        String D = "{sourcecode\":101,\"messageid\":\"7d8d4171-6133-edb3-9f07-3303c8aa06de\",\"device\":1,\"platformid\":\"2\",\"sign\":\"b5cc7a0232a6e70e838d030f27d69f91\",\"t\":1101}";
//        int T = 1101;
//        String Token = "";
        String D = "106";//"{\"MessageId\":\"3fc6bf1a-a7d1-4136-9ca5-5b304799b03c\",\"Device\":1,\"PlatformId\":9,\"Sign\":\"d190d31b8462af8deec7c307e11be198\",\"Token\":\"\",\"T\":200,\"SourceCode\":102,\"UserName\":\"tt1234567\",\"Password\":\"123456\"}";
        int T = 200;
        String Token = "";
    }


}

package com.finance.model.imps;

import android.util.Log;

import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.StateChangedCallback;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static microsoft.aspnet.signalr.client.ConnectionState.Connected;
import static microsoft.aspnet.signalr.client.ConnectionState.Connecting;
import static microsoft.aspnet.signalr.client.ConnectionState.Disconnected;
import static microsoft.aspnet.signalr.client.ConnectionState.Reconnecting;

/**
 * 网络状态变化回调接口
 */
public class BaseStateChangedCallback implements StateChangedCallback {


    @Override
    public void stateChanged(ConnectionState connectionState, ConnectionState connectionState1) {
        Log.d("123", "stateChanged: " + connectionState.toString());
        Log.d("123", "stateChanged: " + connectionState1.name());
        if (connectionState == Disconnected) {
            Log.d("123", "stateChanged: 断开连接");
        } else if (connectionState == Reconnecting) {
            Log.d("123", "stateChanged: 重新连接");
        } else if (connectionState == Connected) {
            Log.d("123", "stateChanged: 连接成功");
        } else if (connectionState == Connecting) {
            Log.d("123", "stateChanged: 正在连接");
        }

    }


}

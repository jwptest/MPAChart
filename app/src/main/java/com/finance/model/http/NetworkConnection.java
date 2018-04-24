package com.finance.model.http;

import android.content.Context;
import android.content.OperationApplicationException;

import com.finance.App;
import com.zsoft.signala.Hubs.HubConnection;
import com.zsoft.signala.Hubs.HubInvokeCallback;
import com.zsoft.signala.Hubs.IHubProxy;
import com.zsoft.signala.SendCallback;
import com.zsoft.signala.Transport.StateBase;
import com.zsoft.signala.transport.longpolling.LongPollingTransport;

import java.util.ArrayList;

/**
 * 网络连接
 */
public class NetworkConnection {

    private HubConnection mConnection = null;
    private IHubProxy hub = null;

    public NetworkConnection(Context context) {
        mConnection = new HubConnection("https://i.api789.top:8080", context, new LongPollingTransport()) {
            @Override
            public void OnError(Exception exception) {
                App.getInstance().showErrorMsg(exception.getMessage());
            }

            @Override
            public void OnMessage(String message) {
                App.getInstance().showErrorMsg(message);
            }

            @Override
            public void OnStateChanged(StateBase oldState, StateBase newState) {
                App.getInstance().showErrorMsg(oldState.getState() + " -> " + newState.getState());
            }
        };
        try {
            hub = mConnection.CreateHubProxy("calculatorHub");
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }

//        mConnection.Send("参数", new SendCallback() {
//            @Override
//            public void OnSent(CharSequence messageSent) {
//
//            }
//
//            @Override
//            public void OnError(Exception ex) {
//
//            }
//        });

//        hub.On("NewCalculation", new HubOnDataCallback() {
//            @Override
//            public void OnReceived(JSONArray args) {
//                if (mShowAll) {
//                    for (int i = 0; i < args.length(); i++) {
//                        Toast.makeText(MainActivity.this, args.opt(i).toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
    }

    public void start() {
        mConnection.Start();
    }

    public void stop() {
        mConnection.Stop();
    }

    public void Invoke() {
        if (hub == null) return;
        ArrayList<String> args = new ArrayList<String>(2);
        args.add("");
        args.add("");
        hub.Invoke("", args, new HubInvokeCallback() {
            @Override
            public void OnResult(boolean succeeded, String response) {
                App.getInstance().showErrorMsg(response);
            }

            @Override
            public void OnError(Exception ex) {
                App.getInstance().showErrorMsg(ex.getMessage());
            }
        });
    }


}

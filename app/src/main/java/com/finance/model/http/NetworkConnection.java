package com.finance.model.http;

import android.content.Context;

/**
 * 网络连接
 */
public class NetworkConnection {

//    private HubConnection mConnection = null;
//    private IHubProxy hub = null;

    public NetworkConnection(Context context) {
//        mConnection = new HubConnection("https://i.api789.top:8080/indexMark", context, new LongPollingTransport()) {
//            @Override
//            public void OnError(Exception exception) {
//                Log.d("123", "OnError: " + exception.getMessage());
//                App.getInstance().showErrorMsg(exception.getMessage());
//            }
//
//            @Override
//            public void OnMessage(String message) {
//                App.getInstance().showErrorMsg(message);
//                Log.d("123", "OnMessage: " + message);
//            }
//
//            @Override
//            public void OnStateChanged(StateBase oldState, StateBase newState) {
//                switch (newState.getState()) {
//                    case Disconnected:
//                        Log.v("123", "未连接！");
//                        break;
//                    case Connecting:
//                        Log.v("123", "正在连接!");
//                        break;
//                    case Connected:
//                        Log.v("123", "连接成功!");
//                        Invoke();
//                        break;
//                    case Reconnecting:
//                        Log.v("123", "重新连接！");
//                        break;
//                    case Disconnecting:
//                        Log.v("123", "断开连接！");
//                        break;
//                }
//
//                App.getInstance().showErrorMsg(oldState.getState() + " -> " + newState.getState());
//                Log.d("123", "OnStateChanged: " + oldState.getState() + " -> " + newState.getState());
//            }
//        };
//        try {
//            hub = mConnection.CreateHubProxy("indexMark");
//        } catch (OperationApplicationException e) {
//            e.printStackTrace();
//        }
////        mConnection.Send("参数", new SendCallback() {
////            @Override
////            public void OnSent(CharSequence messageSent) {
////
////            }
////
////            @Override
////            public void OnError(Exception ex) {
////
////            }
////        });
////        hub.On("indexMark", new HubOnDataCallback() {
////            @Override
////            public void OnReceived(JSONArray args) {
////
////            }
////        });
    }

    public void start() {
//        mConnection.Start();
    }

    public void stop() {
//        mConnection.Stop();
    }

    public void Invoke() {
//        if (hub == null) return;
//        mConnection.Send("{D:\"{\"sourcecode\":101,\"messageid\":\"7d8d4171-6133-edb3-9f07-3303c8aa06de\",\"device\":1,\"platformid\":\"2\",\"sign\":\"b5cc7a0232a6e70e838d030f27d69f91\",\"t\":1101}\",T:1101,Token:\"\"}", new SendCallback() {
//            @Override
//            public void OnSent(CharSequence messageSent) {
//                Log.d("123", "OnSent: " + messageSent);
//            }
//
//            @Override
//            public void OnError(Exception ex) {
//
//            }
//        });

//        ArrayList<String> args = new ArrayList<>(3);
//        args.add("{D:\"{\"sourcecode\":101,\"messageid\":\"7d8d4171-6133-edb3-9f07-3303c8aa06de\",\"device\":1,\"platformid\":\"2\",\"sign\":\"b5cc7a0232a6e70e838d030f27d69f91\",\"t\":1101}\"");
//        args.add("T:1101");
//        args.add("Token:\"\"");
//        hub.Invoke("indexMark", args, new HubInvokeCallback() {
//            @Override
//            public void OnResult(boolean succeeded, String response) {
//                Log.d("123", "OnResult: " + response);
//                App.getInstance().showErrorMsg(response);
//            }
//            @Override
//            public void OnError(Exception ex) {
//                Log.d("123", "OnError: " + ex.getMessage());
//                App.getInstance().showErrorMsg(ex.getMessage());
//            }
//        });
    }


}

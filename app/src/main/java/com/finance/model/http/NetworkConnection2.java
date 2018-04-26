package com.finance.model.http;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 网络连接
 */
public class NetworkConnection2 {

    private WebSocket mWebSocket;

    public NetworkConnection2() {

//        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        };
//
//        OkHttpClient client = new OkHttpClient
//                .Builder()
//                .readTimeout(0,  TimeUnit.MILLISECONDS)
//                .hostnameVerifier(DO_NOT_VERIFY)
//                .build();
//        Request request = new Request.Builder().url("https://i.api789.top").build();
//        client.newWebSocket(request, new WebSocketListener() {
//            @Override
//            public void onOpen(WebSocket webSocket, Response response) {
//                super.onOpen(webSocket, response);
//            }
//
//            @Override
//            public void onMessage(WebSocket webSocket, String text) {
//                super.onMessage(webSocket, text);
//            }
//
//            @Override
//            public void onMessage(WebSocket webSocket, ByteString bytes) {
//                super.onMessage(webSocket, bytes);
//            }
//
//            @Override
//            public void onClosing(WebSocket webSocket, int code, String reason) {
//                super.onClosing(webSocket, code, reason);
//            }
//
//            @Override
//            public void onClosed(WebSocket webSocket, int code, String reason) {
//                super.onClosed(webSocket, code, reason);
//            }
//
//            @Override
//            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//                super.onFailure(webSocket, t, response);
//            }
//        });
//        client.dispatcher().executorService().shutdown();


//        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
//                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
//                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
//                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
//                .build();
//
//        //一种：参数请求体
//        FormBody paramsBody = new FormBody.Builder()
//                .add("D", "{\"sourcecode\":101,\"messageid\":\"7d8d4171-6133-edb3-9f07-3303c8aa06de\",\"device\":1,\"platformid\":\"2\",\"sign\":\"b5cc7a0232a6e70e838d030f27d69f91\",\"t\":\"1101\"}")
//                .add("T", "1101")
//                .add("Token", "")
//                .build();
//
//        //构造request对象
//        Request request = new Request.Builder()
//                .url("https://i.api789.top:8080/indexMark/")
//                .post(paramsBody)
//                .build();
//
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("123", "onResponse: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("123", "onResponse: " + response);
//            }
//        });

        //建立连接
//        mOkHttpClient.newWebSocket(request, new WebSocketListener() {
//            @Override
//            public void onOpen(WebSocket webSocket, Response response) {
//                mWebSocket = webSocket;
//                System.out.println("client onOpen");
//                System.out.println("client request header:" + response.request().headers());
//                System.out.println("client response header:" + response.headers());
//                System.out.println("client response:" + response);
//                //开启消息定时发送
//                startTask();
//            }
//
//            @Override
//            public void onMessage(WebSocket webSocket, String text) {
//                System.out.println("client onMessage");
//                System.out.println("message:" + text);
//            }
//
//            @Override
//            public void onClosing(WebSocket webSocket, int code, String reason) {
//                System.out.println("client onClosing");
//                System.out.println("code:" + code + " reason:" + reason);
//            }
//
//            @Override
//            public void onClosed(WebSocket webSocket, int code, String reason) {
//                System.out.println("client onClosed");
//                System.out.println("code:" + code + " reason:" + reason);
//            }
//
//            @Override
//            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//                //出现异常会进入此回调
//                System.out.println("client onFailure");
//                System.out.println("throwable:" + t);
//                System.out.println("response:" + response);
//            }
//        });
//        mOkHttpClient.dispatcher().executorService().shutdown();
    }

    private Timer mTimer;
    private int msgCount;

    //每秒发送一条消息
    private void startTask() {
        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mWebSocket == null) return;
                msgCount++;
                boolean isSuccessed = mWebSocket.send("msg" + msgCount + "-" + System.currentTimeMillis());
                //除了文本内容外，还可以将如图像，声音，视频等内容转为ByteString发送
                //boolean send(ByteString bytes);
            }
        };
        mTimer.schedule(timerTask, 0, 1000);
    }

    public void start() {

        Thread th = new Thread(){
            @Override
            public void run() {
                WebSocketClient webSocketClient = new WebSocketClient(URI.create("https://i.api789.top:8080/indexMark"), new Draft_17(), new HashMap<String,String>(), 10) {

                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        System.out.println("client onOpen");
                    }

                    @Override
                    public void onMessage(String s) {
                        System.out.println("client onMessage:" + s);
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        System.out.println("client onClose:" + i + " " + s + " " + b);
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println("client onError:" + e);
                    }
                };

                webSocketClient.connect();
            }
        };
        th.start();
    }

    public void stop() {
    }

    public void Invoke() {
    }

}

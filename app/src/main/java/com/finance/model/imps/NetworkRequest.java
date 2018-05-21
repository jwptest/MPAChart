package com.finance.model.imps;

import com.finance.model.http.BaseParams;
import com.finance.model.http.ISign;
import com.finance.model.https.BaseCallback3;
import com.finance.model.https.HttpConnection;
import com.finance.model.https.RequestParams;

/**
 * 普通网络请求处理
 */
public class NetworkRequest {

    private volatile static NetworkRequest sNetworkRequest;
    private volatile SignImp sSignImp;//参数格式化对象
    private volatile SignBasic sSignBasic;//参数格式化对象
//    private volatile BaseStateChangedCallback sChangedCallback;//网络状态变化回调
//    private volatile Connection mConnection;//网络请求对象
//    private MMessageReceivedHandler mMMessageReceivedHandler;

    private NetworkRequest() {
        sSignImp = new SignImp();
//        sChangedCallback = new BaseStateChangedCallback();
        sSignBasic = new SignBasic();
//        mConnection = new Connection(Constants.HTTPURL);
//        mMMessageReceivedHandler = new MMessageReceivedHandler();
//        mConnection.received(mMMessageReceivedHandler);
//        mConnection.stateChanged(sChangedCallback);
    }

    public static NetworkRequest getInstance() {
        if (sNetworkRequest == null) {
            synchronized (NetworkRequest.class) {
                if (sNetworkRequest == null) {
                    sNetworkRequest = new NetworkRequest();
                }
            }
        }
        return sNetworkRequest;
    }

    public static RequestParams getRequestParamSignImp(BaseParams params) {
        params.setISign(getInstance().sSignImp);
        return new RequestParams(params);
    }

    public static RequestParams getRequestParamSignBasic(BaseParams params) {
        params.setISign(getInstance().sSignBasic);
        return new RequestParams(params);
    }

//    public com.finance.model.http.NetworkRequest getHttpConnection() {
////        return new HttpConnection(Constants.HTTPURL)
////                .setISign(sSignImp)
////                .setT(200)
////                .setChangedCallback(sChangedCallback);
////        return new com.finance.model.http.NetworkRequest(mConnection)
////                .setISign(sSignImp)
////                .setT(200)
////                .setHandler(mMMessageReceivedHandler);
//    }
//
//    public HttpConnection getHttpConnection2() {
////        return new HttpConnection(Constants.HTTPURL)
////                .setISign(sSignImp)
////                .setT(200)
////                .setChangedCallback(sChangedCallback);
//    }

    public com.finance.model.https.HttpConnection getHttpConnection() {
        return HttpConnection.getInstance();
    }

    public ISign getSignBasic() {
        return sSignBasic;
    }

    public SignImp getsSignImp() {
        return sSignImp;
    }


    public static BaseCallback3 getBaseCallback() {
        return new BaseCallback3() {
            @Override
            public void noNetworkConnected(int sourceCode) {

            }

            @Override
            public void onMessageReceived(String data) {

            }

            @Override
            public void error(int sourceCode, String mgs) {

            }

            @Override
            public void setTag(Object object) {

            }
        };
    }

    public static abstract class BaseCallback implements BaseCallback3 {

        @Override
        public void noNetworkConnected(int sourceCode) {

        }

        @Override
        public void onMessageReceived(String data) {

        }

        @Override
        public void error(int sourceCode, String mgs) {

        }

        @Override
        public void setTag(Object object) {

        }
    }


}

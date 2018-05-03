package com.finance.model.imps;

import com.finance.common.Constants;
import com.finance.model.http.HttpConnection;
import com.finance.model.http.ISign;

/**
 * 普通网络请求处理
 */
public class NetworkRequest {

    private volatile static NetworkRequest sNetworkRequest;
    private volatile static SignImp sSignImp;
    private volatile static SignBasic sSignBasic;
    private volatile static BaseStateChangedCallback sChangedCallback;

    private NetworkRequest() {
    }

    public static NetworkRequest getInstance() {
        if (sNetworkRequest == null) {
            synchronized (NetworkRequest.class) {
                if (sNetworkRequest == null) {
                    sSignImp = new SignImp();
                    sChangedCallback = new BaseStateChangedCallback();
                    sNetworkRequest = new NetworkRequest();
                }
            }
        }
        return sNetworkRequest;
    }

    public HttpConnection getHttpConnection() {
        return new HttpConnection(Constants.HTTPURL)
                .setISign(sSignImp)
                .setT(200)
                .setChangedCallback(sChangedCallback);
    }

    public ISign getSignBasic() {
        if (sSignBasic == null) {
            synchronized (NetworkRequest.class) {
                if (sSignBasic == null) {
                    sSignBasic = new SignBasic();
                }
            }
        }
        return sSignBasic;
    }


}

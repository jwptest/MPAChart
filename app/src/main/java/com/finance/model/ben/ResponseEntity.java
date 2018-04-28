package com.finance.model.ben;

import com.finance.common.Constants;

/**
 * 网络请求返回的基本数据数据
 */
public class ResponseEntity {

    //{"Token":"efa65fc8e77c491e9ad58c134ca4c28a#iCqnus5t9s1t6L+8M7eNh6SnRyvPpvvVc2S9cmjWHTMXKYTXjt9Xwc1evo1i5ISzErqfEWcjd+OQpoKU8RVONA==",
    // "ReceiveNotify":false,"PayPass":false,"MessageId":"3fc6bf1a-a7d1-4136-9ca5-5b304799b03c",
    // "SourceCode":101,"Status":0,"Message":"{\"T\":200,\"Device\":1,\"SourceCode\":105,\"PlatformId\":9,
    // \"MessageId\":\"3fc6bf1a-a7d1-4136-9ca5-5b304799b03c\",\"Sign\":\"b8a0c0b8556c95772aed4d59c0ac5aa0\",\"
    // Token\":\"74c5e830995043d29b7cd9f41a7a698d#rl7I4xiF1NG+ZJqD4on1B/9DNhMDJg2AsG9qTc0i3g6WZ0ukglzrD1TkbQwUTeVZJs2+3/FQ2S43nWm6Qt+sXQ\\u003d\\u003d\"}",
    // "Url":null,"Sign":null,"RunTime":"0:00:00.2903298","CurrDateTime":"2018-04-28T15:43:37.7433292+08:00","CNYRate":6.6155}

    private String CurrDateTime;// DateTime 服务器当前时间
    private String Message;// String 获取或设置消息处理状态消息
    private String MessageId;//String 获取或设置消息ID
    private String RunTime;//String 获取或设置消息处理时长
    private String Sign;//String 获取或设置加密认证密钥
    private int SourceCode;//Int32 获取或设置请求类型
    private int Status = Constants.DEFAULTCODE;//Status 获取或设置消息处理状态
    private String Url;// 临时订单的处理地址

    public String getCurrDateTime() {
        return (CurrDateTime != null) ? CurrDateTime : "";
    }

    public void setCurrDateTime(String currDateTime) {
        CurrDateTime = currDateTime;
    }

    public String getMessage() {
        return (Message != null) ? Message : "";
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessageId() {
        return (MessageId != null) ? MessageId : "";
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public String getRunTime() {
        return (RunTime != null) ? RunTime : "";
    }

    public void setRunTime(String runTime) {
        RunTime = runTime;
    }

    public String getSign() {
        return (Sign != null) ? Sign : "";
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public int getSourceCode() {
        return SourceCode;
    }

    public void setSourceCode(int sourceCode) {
        SourceCode = sourceCode;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getUrl() {
        return (Url != null) ? Url : "";
    }

    public void setUrl(String url) {
        Url = url;
    }


}

package com.finance.model.ben;

/**
 * 网络请求返回的基本数据数据
 */
public class ResponseEntity {

    private String CurrDateTime;// DateTime 服务器当前时间
    private String Message;// String 获取或设置消息处理状态消息
    private String MessageId;//String 获取或设置消息ID
    private String RunTime;//String 获取或设置消息处理时长
    private String Sign;//String 获取或设置加密认证密钥
    private int SourceCode;//Int32 获取或设置请求类型
    private int Status;//Status 获取或设置消息处理状态
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

package com.finance.model.ben;

/**
 * 用户登录权限
 */
public class TokenEntity extends ResponseEntity {

    private String Token;

    public String getToken() {
        return (Token != null) ? Token : "";
    }

    public void setToken(String token) {
        Token = token;
    }



}

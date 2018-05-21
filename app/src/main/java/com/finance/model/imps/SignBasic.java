package com.finance.model.imps;

import com.finance.model.http.ISign;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 *
 */
public class SignBasic implements ISign {

    @Override
    public String getSendJson(HashMap<String, Object> params,String D, int T, String Token) {
        return new Gson().toJson(params);
    }

//    @Override
//    public String getSendJson(String d, int t, String Token) {
//        return d;
//    }

    @Override
    public String getDJson(HashMap<String, Object> params) {
        return "";
    }

}

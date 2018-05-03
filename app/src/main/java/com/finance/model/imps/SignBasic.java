package com.finance.model.imps;

import com.finance.model.http.ISign;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 *
 */
public class SignBasic implements ISign {

    private Gson mGson = new Gson();

    @Override
    public String getSign(HashMap<String, Object> params, int T, String Token) {
        return mGson.toJson(params);
    }
}

package com.finance.model.imps;

import com.finance.model.http.ISign;
import com.finance.utils.MD5Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

/**
 * 生成签名
 */
public class SignImp implements ISign {

    private HashMap<String, Object> mHashMap;
    private HashMap<String, Object> sendHasMap;
    private StringBuilder mStringBuilder;
    private Comparator<String> mComparator = new MyComparator();
    private Gson mGson = new Gson();

    @Override
    public String getSign(HashMap<String, Object> params, int T, String Token) {
        ArrayList<String> keys = getKeys(params);
        String signJson = getSignJson(keys, mHashMap);
        params.put("Sign", getSignMd5(signJson));
        HashMap<String, Object> baseparams = getSendHasMap();
        baseparams.put("D", mGson.toJson(params));
        baseparams.put("T", T);
        baseparams.put("Token", Token);
        String json = mGson.toJson(baseparams);
        getHashMap();
        getStringBuilder();
        getSendHasMap();
        return json;
    }

    private ArrayList<String> getKeys(HashMap<String, Object> params) {
        HashMap<String, Object> mHashMap = getHashMap();//new HashMap<>(params.size());
        mHashMap.putAll(params);
        //移除不参与签名的字段
        Set<String> keys = mHashMap.keySet();
        ArrayList<String> arrayList = new ArrayList<>(keys);
        for (String key : arrayList) {
            if (key.equals("Sign") || key.startsWith("_"))
                mHashMap.remove(key);
        }
        //转换true和false
        keys = mHashMap.keySet();
        arrayList.clear();
        arrayList.addAll(keys);
        for (String key : arrayList) {
            if (params.get(key).equals("true"))
                params.put(key, "1");
            else if (params.get(key).equals("false"))
                params.put(key, "0");
        }
        Object value;
        //key大写转小写
        for (String key : arrayList) {
            value = mHashMap.get(key);
            mHashMap.remove(key);
            mHashMap.put(key.toLowerCase(), value);
        }
        //排序
        keys = mHashMap.keySet();
        arrayList.clear();
        arrayList.addAll(keys);
        Collections.sort(arrayList, mComparator);
        return arrayList;
    }

    private String getSignJson(ArrayList<String> keys, HashMap<String, Object> mHashMap) {
        StringBuilder stringBuilder = getStringBuilder();
        stringBuilder.append("{");
        String key;
        Object value;
        for (int i = 0, size = keys.size(); i < size; i++) {
            if (i > 0)
                stringBuilder.append(",");
            key = keys.get(i);
            value = mHashMap.get(key);
            stringBuilder.append("\"" + key + "\"");
            stringBuilder.append(":");
            if (value instanceof String) {
                stringBuilder.append("\"" + value + "\"");
            } else {
                stringBuilder.append(value);
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private String getSignMd5(String signStr) {
        return MD5Util.getInstance().getMD5String(signStr);
    }

    private StringBuilder getStringBuilder() {
        if (mStringBuilder == null) mStringBuilder = new StringBuilder();
        else mStringBuilder.setLength(0);
        return mStringBuilder;
    }

    private HashMap<String, Object> getHashMap() {
        if (mHashMap == null) mHashMap = new HashMap<>(5);
        else mHashMap.clear();
        return mHashMap;
    }

    private HashMap<String, Object> getSendHasMap() {
        if (sendHasMap == null) sendHasMap = new HashMap<>(3);
        else sendHasMap.clear();
        return sendHasMap;
    }

    private static class MyComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }


}

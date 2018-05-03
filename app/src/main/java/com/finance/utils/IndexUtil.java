package com.finance.utils;

import com.finance.model.ben.IndexMarkEntity;

import java.util.ArrayList;

/**
 * 指数工具类
 */
public class IndexUtil {

    public ArrayList<IndexMarkEntity> parseExponentially(ArrayList<String> serverDatas, int digit) {
        if (serverDatas == null || serverDatas.isEmpty()) return null;
        ArrayList<IndexMarkEntity> entitys = new ArrayList<>(serverDatas.size());
        int count = 0;
        for (String str : serverDatas) {
            IndexMarkEntity entity = parseExponentially(count, str, digit);
            if (entity == null) continue;
            entitys.add(entity);
            count++;
        }
        return entitys;
    }

    /**
     * 解析指数
     */
    public IndexMarkEntity parseExponentially(int x, String serverData, int digit) {
        if (serverData.length() != 28) {
            return null;
        }
        try {
            //"6A08D5B11A4274A540600124B751","6A08D5B11A42C0F080600124B751","6A08D5B11A430D3BC050001D4551"
            String e = serverData.substring(0, 2);
            String hex_Time = serverData.substring(2, 18);
            String hex_PointSize = serverData.substring(18, 19);
            String hex_exponent = serverData.substring(19, 27);
            String hex_isUpdate = serverData.substring(27, 28);
            int productId = Integer.parseInt(e, 16);
            long serverTime = Long.parseLong(hex_Time, 16);
            int pointSize = Integer.parseInt(hex_PointSize, 16);
            int exponent = Integer.parseInt(hex_exponent, 16);
            int isUpdate = Integer.parseInt(hex_isUpdate, 16);
            if (exponent < 0) return null;
            return new IndexMarkEntity(x, productId, serverTime, exponent, isUpdate, pointSize, digit);
        } catch (Exception var15) {
            var15.printStackTrace();
            throw var15;
        }
    }

}
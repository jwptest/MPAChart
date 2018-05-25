package com.finance.utils;

import com.finance.model.ben.IndexMarkEntity;
import com.github.mikephil.charting.data.Entry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 */
public class IndexFormatUtil {

    protected DecimalFormat decimalFormat;

    public IndexFormatUtil(int digit) {
        if (digit < 2) digit = 2;
        StringBuilder sb = new StringBuilder("0.");
        for (int i = 0; i < digit; i++) {
            sb.append("0");
        }
        decimalFormat = new DecimalFormat(sb.toString());//构造方法的字符格式这里如果小数不足2位,会以0补足.
    }

    public String format(float value) {
        return decimalFormat.format(value);
    }

    //升序排序
    public static void sortEntry(ArrayList<Entry> arrayList) {
        Collections.sort(arrayList, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                if (o2.getX() == o1.getX()) return 0;
                if (o2.getX() < o1.getX()) return 1;
                return -1;
            }
        });
    }

    //升序排序
    public static void sortIndex(ArrayList<IndexMarkEntity> arrayList) {
        Collections.sort(arrayList, new Comparator<IndexMarkEntity>() {
            @Override
            public int compare(IndexMarkEntity o1, IndexMarkEntity o2) {
                if (o2.getX() == o1.getX()) return 0;
                if (o2.getX() < o1.getX()) return 1;
                return -1;
            }
        });
    }

}

package com.finance.linechartview;

import com.finance.model.ben.XEntity;
import com.github.mikephil.charting.components.AxisBase;

import java.util.ArrayList;

/**
 * X轴显示标签格式类
 */
public class XAxisValueFormatter extends BaseAxisValueFormatter {

//    private long startTimer;
//    private int showCount;
//    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//    Calendar calendar = Calendar.getInstance();
//
//    private int hour;
//    private int min;


    private ArrayList<XEntity> startTimer;

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        lasTwo = lasOne;
        lasOne = value;
//        calendar.setTimeInMillis((long) (startTimer + (value * 500)));
//        hour = calendar.get(Calendar.HOUR_OF_DAY);
//        min = calendar.get(Calendar.MINUTE);
        return getValue(value);
    }


//    public void setStartTimer(long startTimer) {
//        String timerstr = formatter.format(new Date(startTimer));
//        try {
//            startTimer = formatter.parse(timerstr).getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        this.startTimer = startTimer;
//    }
//
//    private String getValue() {
//        if (hour < 10 && min < 10) {
//            return "0" + hour + ":0" + min;
//        } else if (hour < 10) {
//            return "0" + hour + ":" + min;
//        } else if (min < 10) {
//            return "" + hour + ":0" + min;
//        }
//        return hour + ":" + min;
//    }

    public void setStartTimer(ArrayList<XEntity> startTimer) {
        this.startTimer = startTimer;
    }

    private String getValue(float value) {
        if (startTimer == null) return "";
        for (XEntity en : startTimer) {
            if (en.getMinIndex() <= value && en.getMaxIndex() >= value) {
                return en.getValue();
            }
        }
        return "";
    }

}

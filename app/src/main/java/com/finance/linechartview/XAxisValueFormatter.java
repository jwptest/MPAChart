package com.finance.linechartview;

import com.github.mikephil.charting.components.AxisBase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * X轴显示标签格式类
 */
public class XAxisValueFormatter extends BaseAxisValueFormatter {

    private long startTimer;
    private int showCount;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Calendar calendar = Calendar.getInstance();

    private int hour;
    private int min;

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        lasTwo = lasOne;
        lasOne = value;
        calendar.setTimeInMillis((long) (startTimer + (value * 500)));
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        return getValue();
    }

    public void setStartTimer(long startTimer) {
        String timerstr = formatter.format(new Date(startTimer));
        try {
            startTimer = formatter.parse(timerstr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.startTimer = startTimer;
    }

    private String getValue() {
        if (hour < 10 && min < 10) {
            return "0" + hour + ":0" + min;
        } else if (hour < 10) {
            return "0" + hour + ":" + min;
        } else if (min < 10) {
            return "" + hour + ":0" + min;
        }
        return hour + ":" + min;
    }

}

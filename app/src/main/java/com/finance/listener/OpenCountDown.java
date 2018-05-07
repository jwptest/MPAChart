package com.finance.listener;

import android.os.CountDownTimer;

import java.util.ArrayList;

/**
 * 开奖倒计时
 */
public class OpenCountDown {

    private CountDownTimer timer;//倒计时

    private ArrayList<ICallback> mICallbacks = new ArrayList<>(2);

    private static class OpenCountDownInstance {
        private static final OpenCountDown INSTANCE = new OpenCountDown();
    }

    public static OpenCountDown getInstance() {
        return OpenCountDownInstance.INSTANCE;
    }

    public void stopCountDown() {
        if (timer == null) return;
        timer.cancel();
        timer = null;
    }

    public void startCountDown(long l) {
        stopCountDown();
        timer = new CountDownTimer(l, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                for (ICallback callback : mICallbacks) {
                    callback.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                for (ICallback callback : mICallbacks) {
                    callback.onFinish();
                }
            }
        };
        timer.start();
    }

    public void addCallback(ICallback callback) {
        mICallbacks.add(callback);
    }

    public void removeCallback(ICallback callback) {
        mICallbacks.remove(callback);
    }

    public interface ICallback {
        void startTick();

        void onTick(long millisUntilFinished);

        void onFinish();
    }

}

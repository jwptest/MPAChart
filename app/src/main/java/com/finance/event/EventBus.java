package com.finance.event;

/**
 * 事件处理
 */
public class EventBus {

    private volatile static org.greenrobot.eventbus.EventBus mEventBus;

    private EventBus() {
    }

    public static org.greenrobot.eventbus.EventBus getDefault() {
        if (mEventBus == null) {
            synchronized (EventBus.class) {
                mEventBus = new org.greenrobot.eventbus.EventBus();
            }
        }
        return mEventBus;
    }

    public static void post(Object event) {
        getDefault().post(event);
    }

    public static void register(Object event) {
        getDefault().register(event);
    }

    public static void unregister(Object event) {
        getDefault().unregister(event);
    }

}

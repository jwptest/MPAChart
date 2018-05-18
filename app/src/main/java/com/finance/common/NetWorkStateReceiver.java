package com.finance.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.finance.event.EventBus;
import com.finance.event.NetWorkStateEvent;

/**
 * 网络状态切换监听
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

//    private static long top;

    @Override
    public void onReceive(Context context, Intent intent) {
//        System.out.println("网络状态发生变化");
        //检测API是不是小于21，因为到了API21之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                postEvent(true, Constants.ALLNET);
//                Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                postEvent(true, Constants.WIFI);
//                Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                postEvent(true, Constants.MOBILE);
//                Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else {
                postEvent(false, Constants.UNNET);
//                Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
            }
        } else {
            //这里的就不写了，前面有写，大同小异
//            System.out.println("API level 大于21");
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isMobile = false, isWifi = false;
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            //用于存放网络连接信息
//            StringBuilder sb = new StringBuilder();
            //通过循环将网络信息逐个取出来
            for (int i = 0; i < networks.length; i++) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                if (networkInfo.isConnected()) {
                    if (ConnectivityManager.TYPE_MOBILE == networkInfo.getType()) {
                        isMobile = true;
                    } else if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                        isWifi = true;
                    }
                }
//                sb.append(networkInfo.getTypeName() + "connect is" + networkInfo.isConnected());
            }
            postEvent(isMobile || isWifi, isMobile ? Constants.MOBILE : isWifi ? Constants.WIFI : Constants.UNNET);
//            Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void postEvent(boolean isNetWork, int state) {
//        long current = System.currentTimeMillis();
//        if (current - top > 500) {
//            EventBus.post(new NetWorkStateEvent(isNetWork, state));
//            top = current;
//        }

        EventBus.post(new NetWorkStateEvent(isNetWork, state));
    }

    //注册广播
    // Android 7.0之静态注册广播的方式被取消
    public static NetWorkStateReceiver registerReceiver(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            NetWorkStateReceiver netBroadcastReceiver = new NetWorkStateReceiver();
            //注册广播接收
            context.registerReceiver(netBroadcastReceiver, filter);
            return netBroadcastReceiver;
        }
        return null;
    }

    //注销广播
    public static void unregisterReceiver(Context context, NetWorkStateReceiver netWorkStateReceiver) {
        context.unregisterReceiver(netWorkStateReceiver);
    }

}

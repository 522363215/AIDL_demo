package com.swun.tinama.aidl_demo.utils;


import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Administrator on 2016/5/8.
 */
public class MyUtils {

    public static String getCurrentProcessName(Context context) {

        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process_info : activityManager.getRunningAppProcesses()) {
            if (process_info.pid == pid) {
                return process_info.processName;
            }
        }
        return "";
    }
}

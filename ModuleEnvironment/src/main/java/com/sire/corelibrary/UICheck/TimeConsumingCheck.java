package com.sire.corelibrary.UICheck;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/03/01
 * Author:Sire
 * Description:
 * ==================================================
 */

public class TimeConsumingCheck {
    private static final long BLOCK_INTERVAL = 300L;
    private static TimeConsumingCheck instance = new TimeConsumingCheck();
    private static HandlerThread mHandlerThread;
    private static Handler mHandler;
    private static Runnable logRunable = new Runnable() {
        @Override
        public void run() {
            StringBuilder builder = new StringBuilder();
            StackTraceElement[] stackTraceElements = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                builder.append(stackTraceElement.toString() + "\n");
            }
            Log.e("Time consume  more than "+BLOCK_INTERVAL+"ms", builder.toString());
        }
    };


    private TimeConsumingCheck() {
        mHandlerThread = new HandlerThread("TimeConsumingCheck");
        mHandlerThread.start();
        this.mHandler = new Handler(mHandlerThread.getLooper());
    }

     static TimeConsumingCheck getInstance() {
        return instance;
    }

     static void startTrack() {
        mHandler.postDelayed(logRunable, BLOCK_INTERVAL);
    }

     static void endTrack() {
        mHandler.removeCallbacks(logRunable);
    }


}

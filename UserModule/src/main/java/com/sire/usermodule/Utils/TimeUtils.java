package com.sire.usermodule.Utils;

import android.os.Handler;
import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by SireGod on 2015/6/11.
 */
public class TimeUtils {

    private static TimerTask timerTask;




    public static void countDownTime(CountDownDelegate delegate, int totalTime) {
        mTotalTime = totalTime;
        countDownDelegate = delegate;
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mTotalTime == 0) {
                    stopCountDown();
                }
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }



    private static int mTotalTime;
    private static Timer timer;
    private static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mTotalTime--;
            if (countDownDelegate != null) {
                countDownDelegate.countDown(mTotalTime);
            }
        }
    };
    private static CountDownDelegate countDownDelegate;

    public interface CountDownDelegate {
        void countDown(int when);
    }

    public static void stopCountDown() {
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        timer = null;
        countDownDelegate = null;
    }

    public static void setTotalTime(int time) {
        mTotalTime = time;
    }


}

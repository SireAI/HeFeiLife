package com.sire.corelibrary.UICheck;

import android.os.Looper;
import android.util.Log;
import android.util.Printer;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/03/01
 * Author:Sire
 * Description:
 * ==================================================
 */

public class UIBlockTrack {
    private static final String START_LOG_STRING = ">>>>> Dispatching to";
    private static final String END_LOG_STRING = "<<<<< Finished to";
    public static void start(){
        Log.d("UIBlockTrack:","start track UI time cosume!");
        Looper.getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String logString) {
                if(logString.startsWith(START_LOG_STRING)){
                    TimeConsumingCheck.getInstance().startTrack();
                }
                if(logString.startsWith(END_LOG_STRING)){
                    TimeConsumingCheck.getInstance().endTrack();
                }
            }
        });
    }
}

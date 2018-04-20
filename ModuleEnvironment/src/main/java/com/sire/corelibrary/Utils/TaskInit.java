package com.sire.corelibrary.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/05
 * Author:Sire
 * Description:
 * ==================================================
 */

public class TaskInit {
    final CountDownLatch latch = new CountDownLatch(2);
    private List<TaskAction> workerList = new LinkedList<>();

    public TaskInit with(TaskAction taskAction){
        if(!workerList.contains(taskAction)){
            workerList.add(taskAction);
        }
        return this;
    }
    public void build(){
        for (int i = 0; i < workerList.size(); i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    public interface TaskAction{
        void excute();
    }



}

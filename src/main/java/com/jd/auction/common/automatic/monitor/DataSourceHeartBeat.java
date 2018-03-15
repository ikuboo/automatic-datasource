package com.jd.auction.common.automatic.monitor;

import com.google.common.base.Preconditions;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 数据源心跳
 * @author yuanchunsen@jd.com
 *         2018/3/15.
 */
public class DataSourceHeartBeat {
    private DataSourceHeartBeat(){}

    private static ScheduledExecutorService executorService;

    public static void init(Integer heartBeatRate){
        Preconditions.checkNotNull(heartBeatRate);
        Preconditions.checkArgument(heartBeatRate > 1, "heartBeatRate mast > 1");
        System.out.println("begin");
        executorService =  Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new HeartBeadTask(), 1, heartBeatRate, TimeUnit.SECONDS);
    }

    public static void destory(){
        if(null != executorService){
            //关闭线程池
            executorService.shutdownNow();
        }
    }
}

 class HeartBeadTask implements Runnable{
     @Override
     public void run() {
         System.out.println("HeartBeadTask.run");

         try {
             Thread.sleep(1000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
     }
 }

package com.hza.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by JoeHuang on 2017/7/10.
 */
@Component
public class MyTask2 {
    @Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次
    public void myTask2(){
        System.out.println("我的任务B "+new Date());
    }
}

package com.hza.task;

import com.hza.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by JoeHuang on 2017/7/10.
 */
public class SendEMailTask {

    @Autowired
    IMailService service;

    public void sendTextEmail() {
        System.out.println("开始任务"+new Date()+Thread.currentThread().getName());
        String s = "发送邮件  ";
        try {
            Thread.sleep(6000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(s+new Date()+Thread.currentThread().getName());
        service.initProperties("smtp","smtp.163.com","25","s872007871@163.com","872007871@qq.com");
        try {
            System.out.println("发送邮件"+new Date());
            service.sendTextEmail("定时邮件","定时邮件正文"+new Date(),"s872007871@163.com","");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTextEmail2() {
        System.out.println("开始任务2"+new Date()+Thread.currentThread().getName());
        String s = "发送邮件2 ";
        System.out.println(s+new Date()+Thread.currentThread().getName());
        service.initProperties("smtp","smtp.163.com","25","s872007871@163.com","872007871@qq.com");
        try {
            System.out.println("发送邮件"+new Date());
            service.sendTextEmail("定时邮件","定时邮件正文"+new Date(),"s872007871@163.com","");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

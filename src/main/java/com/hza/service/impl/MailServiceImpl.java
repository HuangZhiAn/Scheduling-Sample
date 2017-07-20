package com.hza.service.impl;

import org.springframework.stereotype.Service;
import com.hza.service.IMailService;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.util.*;

/**
 * Created by JoeHuang on 2017/7/4.
 * TUDO:带附件邮件
 */
@Service
public class MailServiceImpl implements IMailService {
    // 邮件发送协议
    private String protocol = "smtp";

    // SMTP邮件服务器
    private String host = "smtp.163.com";

    // SMTP邮件服务器默认端口
    private String port = "25";

    // 是否要求身份认证
    private String is_auth = "true";

    // 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
    private String is_enabled_debug_mod = "false";

    // 发件人
    private String from = "s872007871@163.com";

    // 收件人
    public String to = "872007871@qq.com";
    //  public static String to = "438729312@qq.com";
    // 初始化连接邮件服务器的会话信息
    private Properties props = null;

    private void init() {
        props = new Properties();
        props.setProperty("mail.transport.protocol", protocol);
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", port);
        if (is_auth != null && !is_auth.equals(""))
            props.setProperty("mail.smtp.auth", is_auth);
        if (is_enabled_debug_mod != null && !is_enabled_debug_mod.equals(""))
            props.setProperty("mail.debug", is_enabled_debug_mod);
    }

    public void initProperties(String protocol, String host, String port, String from, String to) {
        if (protocol != null && !protocol.equals(""))
            this.protocol = protocol;
        if(host!=null&&!host.equals(""))
            this.host = host;
        if (port != null && !port.equals(""))
            this.port = port;
        this.from = from;
        this.to = to;
        init();
    }

    /**
     * 发送简单的文本邮件
     * subject 主题
     */
    public void sendTextEmail(String subject, String text, String user, String password) throws Exception {
        // 创建Session实例对象
        Session session = Session.getDefaultInstance(props);

        // 创建MimeMessage实例对象
        MimeMessage message = new MimeMessage(session);
        // 设置发件人
        message.setFrom(new InternetAddress(from));
        // 设置邮件主题
        message.setSubject(subject);
        // 设置收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置纯文本内容为邮件正文
        message.setText(text);
        // 保存并生成最终的邮件内容
        message.saveChanges();

        // 获得Transport实例对象
        Transport transport = session.getTransport();
        // 打开连接s872007871@163.com   gfhbmddijxnrfxov
        transport.connect(host, user, password);
        // 将message对象传递给transport对象，将邮件发送出去
        transport.sendMessage(message, message.getAllRecipients());
        // 关闭连接
        transport.close();
    }

    /**
     * 发送带附件邮件
     * BUG:无正文
     * @param subject 主题
     * @param text    正文
     * @param list    附件列表
     */

    public void sendFilesMail(String subject, String text, List<File> list, String user, String password) throws AddressException, MessagingException {
        // 创建Session实例对象
        Session session = Session.getDefaultInstance(props);

        // 创建MimeMessage实例对象
        MimeMessage message = new MimeMessage(session);
        // 设置发件人
        message.setFrom(new InternetAddress(from));
        // 设置邮件主题
        message.setSubject(subject);
        // 设置收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置纯文本内容为邮件正文
        message.setText(text);
        //message.setContent("正文内容", "text/html;charset=UTF-8");

        Multipart multipart = new MimeMultipart();

        /*添加附件*/
        for (File file : list) {
            MimeBodyPart fileBody = new MimeBodyPart();
            fileBody.setHeader("Content-Type", "text/html; charset=UTF-8");
            DataSource source = new FileDataSource(file);
            fileBody.setDataHandler(new DataHandler(source));
            fileBody.setFileName(file.getName());
            System.out.println("文件名：" + file.getName());
            //设置文件内容
            // fileBody.setText(text);

            multipart.addBodyPart(fileBody);
        }
        message.setContent(multipart);

        // 保存并生成最终的邮件内容
        message.saveChanges();

        // 获得Transport实例对象
        Transport transport = session.getTransport();
        // 打开连接
        transport.connect(host, user, password);
        // 将message对象传递给transport对象，将邮件发送出去
        transport.sendMessage(message, message.getAllRecipients());
        // 关闭连接
        transport.close();

    }

    /**
     * 发送带内嵌图片、附件、多收件人(显示邮箱姓名)、邮件优先级、阅读回执的完整的HTML邮件
     */
    public void sendMultipleEmail(String subject, String text, List<byte[]> list,String contentType,List<String> fileName,String user, String password) throws Exception {
        // 指定中文编码格式
        String charset = "utf-8";
        // 创建Session实例对象
        Session session = Session.getDefaultInstance(props);
        // 创建MimeMessage实例对象
        MimeMessage message = new MimeMessage(session);
        // 设置主题
        message.setSubject(subject);
        // 设置发送人
        message.setFrom(new InternetAddress(from, user, charset));
        // 设置收件人
        message.setRecipients(MimeMessage.RecipientType.TO,
                new Address[]{
                        // 参数1：邮箱地址，参数2：姓名（在客户端收件只显示姓名，而不显示邮件地址），参数3：姓名中文字符串编码
                        new InternetAddress(to, "hza", charset),
                        /*new InternetAddress("xyang0917@163.com", "李四_163", charset),*/
                }
        );
        // 设置抄送
        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("872007871@qq.com", "hza", charset));
        // 设置密送
        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("872007871@qq.com", "hza", charset));
        // 设置发送时间
        message.setSentDate(new Date());
        // 设置回复人(收件人回复此邮件时,默认收件人)
        /*message.setReplyTo(InternetAddress.parse("\"" + MimeUtility.encodeText("田七") + "\" <417067629@qq.com>"));*/
        // 设置优先级(1:紧急 3:普通 5:低)
        message.setHeader("X-Priority", "1");
        // 要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
        /*message.setHeader("Disposition-Notification-To", from);*/

        // 创建一个MIME子类型为"mixed"的MimeMultipart对象，表示这是一封混合组合类型的邮件
        MimeMultipart mailContent = new MimeMultipart("mixed");
        message.setContent(mailContent);

        /*添加附件*/
        Iterator<String> iterator = fileName.iterator();
        for (byte[] file : list) {
            MimeBodyPart fileBody = new MimeBodyPart();
            fileBody.setHeader("Content-Type", "text/html; charset=UTF-8");
            //DataSource source = new FileDataSource(file);
            DataSource source = new ByteArrayDataSource(file,"text/html; charset=UTF-8");
            fileBody.setDataHandler(new DataHandler(source));
            fileBody.setFileName(iterator.next().toString());
            //System.out.println("文件名：" + file.getName());
            //设置文件内容
            /*fileBody.setText(text);*/

            mailContent.addBodyPart(fileBody);
        }

        // 附件
        /*MimeBodyPart attach1 = new MimeBodyPart();
        MimeBodyPart attach2 = new MimeBodyPart();*/

        // 内容
        MimeBodyPart mailBody = new MimeBodyPart();

        // 将附件和内容添加到邮件当中
        /*mailContent.addBodyPart(attach1);
        mailContent.addBodyPart(attach2);*/
        mailContent.addBodyPart(mailBody);

        // 附件1(利用jaf框架读取数据源生成邮件体)
        /*DataSource ds1 = new FileDataSource("resource/Earth.bmp");
        DataHandler dh1 = new DataHandler(ds1);
        attach1.setFileName(MimeUtility.encodeText("Earth.bmp"));
        attach1.setDataHandler(dh1);*/

        // 附件2
        /*DataSource ds2 = new FileDataSource("resource/如何学好C语言.txt");
        DataHandler dh2 = new DataHandler(ds2);
        attach2.setDataHandler(dh2);
        attach2.setFileName(MimeUtility.encodeText("如何学好C语言.txt"));*/

        // 邮件正文(内嵌图片+html文本)
        MimeMultipart body = new MimeMultipart("related");

        //邮件正文也是一个组合体,需要指明组合关系
        mailBody.setContent(body);

        // 邮件正文由html和图片构成
        /*MimeBodyPart imgPart = new MimeBodyPart();*/
        MimeBodyPart htmlPart = new MimeBodyPart();
        /*body.addBodyPart(imgPart);*/
        body.addBodyPart(htmlPart);

        // 正文图片
        /*DataSource ds3 = new FileDataSource("resource/firefoxlogo.png");
        DataHandler dh3 = new DataHandler(ds3);
        imgPart.setDataHandler(dh3);
        imgPart.setContentID("firefoxlogo.png");*/

        // html邮件内容
        MimeMultipart htmlMultipart = new MimeMultipart("alternative");
        htmlPart.setContent(htmlMultipart);
        MimeBodyPart htmlContent = new MimeBodyPart();
        htmlContent.setContent(

                "<span style='color:green'>"+text+

                        "<img src='cid:firefoxlogo.png' /></span>"

                , "text/html;charset=gbk");

        htmlMultipart.addBodyPart(htmlContent);

        // 保存邮件内容修改
        message.saveChanges();

        /*File eml = buildEmlFile(message);
        sendMailForEml(eml);*/

        // 发送邮件
        /*Transport.send(message,"s872007871@163.com","gfhbmddijxnrfxov");*/
        // 获得Transport实例对象
        Transport transport = session.getTransport();
        // 打开连接
        transport.connect(host,user,password);
        // 将message对象传递给transport对象，将邮件发送出去
        transport.sendMessage(message, message.getAllRecipients());
        // 关闭连接
        transport.close();
    }

    public static void main(String[] args) {
        File file = new File("D:/GraduationDesign/emailFile.txt");
        List<File> list = new ArrayList<File>();
        list.add(file);
        try {
            MailServiceImpl service = new MailServiceImpl();
            service.init();
            service.sendTextEmail("主题1","正文1","s872007871@163.com","gfhbmddijxnrfxov");
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.feng.geek.utils;


import com.feng.geek.model.dto.MailDto;
//import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import lombok.extern.slf4j.Slf4j;


;import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
public class MailUtil {

    private static String fromEmail = "153754997@qq.com";
    private static String password = "gzzviqeoyxincbbj"; // 替换为你的授权码

    public static int sendMail(MailDto mailDto) throws MessagingException, javax.mail.MessagingException {
        String host = "smtp.qq.com";
        int port = 465; // 使用SSL的端口

        // 设置邮件属性
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true"); // 启用SSL

        // 创建会话对象，认证用户和密码
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

            // 创建默认的 MimeMessage 对象
            Message message = new MimeMessage(session);

            // 设置发件人
            message.setFrom(new InternetAddress(fromEmail));

            // 设置收件人
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailDto.getToEmail()));

            // 设置邮件主题
            message.setSubject(mailDto.getTitle());

            // 设置邮件内容
            message.setText(mailDto.getContent());

            // 发送消息
            Transport.send(message);
            log.info("邮件发送成功！");
            return 1;
    }
}

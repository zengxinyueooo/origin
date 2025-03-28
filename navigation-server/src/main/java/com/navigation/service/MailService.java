package com.navigation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String mailUsername;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;  // 使用 Thymeleaf 的 TemplateEngine

    public void sendMailForActivationAccount(String activationUrl, String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            message.setSubject("激活账号");
            message.setFrom(mailUsername);
            message.setTo(email);
            message.setSentDate(new Date());

            // 准备 Thymeleaf 上下文数据
            org.thymeleaf.context.Context context = new Context();
            context.setVariable("activationUrl", activationUrl);

            // 渲染模板
            String templateContent = "activation-account.html";  // 模板文件名
            String text = templateEngine.process(templateContent, context);  // 使用 Thymeleaf 渲染模板

            // 设置邮件正文（HTML 格式）
            message.setText(text, true);

        } catch (MessagingException e) {
            throw new RuntimeException("邮件发送失败", e);
        }

        // 发送邮件
        javaMailSender.send(mimeMessage);
    }
}
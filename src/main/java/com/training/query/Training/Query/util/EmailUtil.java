package com.training.query.Training.Query.util;


import com.training.query.Training.Query.collections.MailServer;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;

import java.util.Map;
import java.util.Properties;
import jakarta.mail.*;


public class EmailUtil {

    public static String replaceTemplateVariables(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }


    public static void sendEmail(MailServer config, String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", config.isUseAuth());
        props.put("mail.smtp.starttls.enable", config.isUseTLS());
        props.put("mail.smtp.host", config.getHost());
        props.put("mail.smtp.port", config.getPort());

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getUsername(), config.getPassword());
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getFromEmail(), config.getFromName()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to: " + to);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



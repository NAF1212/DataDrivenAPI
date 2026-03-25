package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailUtil {

    public static void sendReport(String path)
            throws Exception {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.yopmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session =
                Session.getInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication
                            getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        "nafis@yopmail.com",
                                        "appPassword");
                            }
                        });

        Message message = new MimeMessage(session);
        message.setFrom(
                new InternetAddress("nafis@yopmail.com"));

        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse("ajay@yopmail.com"));

        message.setSubject(
                "Daily Hybrid API Automation Report");

        MimeBodyPart body = new MimeBodyPart();
        body.setText("Please find attached report.");

        MimeBodyPart attachment = new MimeBodyPart();
        attachment.attachFile(new File(path));

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);
        multipart.addBodyPart(attachment);

        message.setContent(multipart);

        Transport.send(message);
    }
}
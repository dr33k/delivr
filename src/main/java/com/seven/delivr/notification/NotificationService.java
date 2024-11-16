package com.seven.delivr.notification;

import com.seven.delivr.auth.verification.email.OTP;
import com.seven.delivr.base.AppService;
import com.seven.delivr.util.Constants;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NotificationService implements AppService {

    private final JavaMailSender sender;
    @Value("twilio.sid")
    private String twilioSid;
    @Value("twilio.token")
    private String twilioToken;
    @Value("twilio.from")
    private String twilioFrom;

    public NotificationService(JavaMailSender sender) {
        this.sender = sender;
    }
    @PostConstruct
    public void setup(){
        Twilio.init(twilioSid, twilioToken);
    }

    @Async("threadPoolTaskExecutor")
    public void sendMimeEmail(String subject, String body, String... toEmails) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmails);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setFrom("Rooks");

        log.info("Sending email to {}", toEmails[0]);
        sender.send(message);
    }

    @Async("threadPoolTaskExecutor")
    public void sendEmail(OTP otp) {
        try {
            MimeMessage message = sender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("Rooks");
            helper.setTo(otp.getUsername());
            helper.setSubject("Rooks: OTP");

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            try (InputStream is = classLoader.getResourceAsStream(Constants.OTP_TEMPLATE)) {

                String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                text = text.replace("{{OTP}}", String.valueOf(otp.getPin()));

                helper.setText(text, true);


                log.info("Sending email to {}", otp.getUsername());
                sender.send(message);
            }
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Async("threadPoolTaskExecutor")
    public void sendSMS(String body, List<String> receivers) {
        receivers.forEach(receiver->{
        Message.creator(
                        new PhoneNumber(twilioFrom),
                        new PhoneNumber(receiver),
                        body)
                .create();

        log.info("Sending SMS to {}", receiver);
    });
    }
    @Async("threadPoolTaskExecutor")
    public void sendSMS(Map<String, String> phoneNoToMessageBodyMap) {
        phoneNoToMessageBodyMap.forEach((phoneNo, messageBody)->{
        Message.creator(
                        new PhoneNumber(twilioFrom),
                        new PhoneNumber(phoneNo),
                        messageBody)
                .create();

        log.info("Sending SMS to {}", phoneNo);
    });
    }
}
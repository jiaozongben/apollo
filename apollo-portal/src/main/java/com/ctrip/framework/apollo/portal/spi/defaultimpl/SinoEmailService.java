package com.ctrip.framework.apollo.portal.spi.defaultimpl;

import com.ctrip.framework.apollo.portal.entity.bo.Email;
import com.ctrip.framework.apollo.portal.spi.EmailService;
import com.ctrip.framework.apollo.tracer.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;

public class SinoEmailService implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(SinoEmailService.class);
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void send(Email email) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email.getSenderEmailAddress());
            String[] to = email.getRecipients().stream().toArray(String[]::new);
            log.info("send email to: {}", Arrays.toString(to));
            helper.setTo(to);
            helper.setSubject(email.getSubject());
            helper.setText(email.getBody(), true);
            javaMailSender.send(message);
            log.info("send email success!");
        } catch (Throwable e) {
            log.error("send email failed", e);
            Tracer.logError("send email failed", e);
        }
    }
}

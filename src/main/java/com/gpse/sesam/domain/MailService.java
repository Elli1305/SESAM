package com.gpse.sesam.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender sender;

    private final TaskExecutor executor;

    @Autowired
    public MailService(final JavaMailSender sender, final TaskExecutor executor) {
        this.sender = sender;
        this.executor = executor;
    }

    public void send(final String from, final String to, final String subject, final String text) {
        executor.execute(() -> {
            try {
                final SimpleMailMessage mail = new SimpleMailMessage();

                mail.setSubject(subject);
                mail.setText(text);
                mail.setTo(to);
                mail.setFrom(from);

                sender.send(mail);
            } catch (Exception e) {
                LOG.warn("Unable to send mail to: " + to + ".", e);
            }
        });
    }
}

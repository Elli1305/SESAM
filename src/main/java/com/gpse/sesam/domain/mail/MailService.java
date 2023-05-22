package com.gpse.sesam.domain.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
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

	public void send(final MailInformation mailInformation) {
		executor.execute(() -> {
			try {
				final SimpleMailMessage mail = new SimpleMailMessage();

				mail.setSubject(mailInformation.subject());
				mail.setText(mailInformation.text());
				mail.setTo(mailInformation.to());
				mail.setFrom(mailInformation.from());

				sender.send(mail);
			} catch (final MailException e) {
				LOG.warn("Unable to send mail to: " + mailInformation.to() + ".", e);
			}
		});
	}
}

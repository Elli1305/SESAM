package com.gpse.sesam.domain.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



/**
 * Der MailService ist ein Service zum Versenden von E-Mails.
 * Die Klasse implementiert das Service-Interface.
 */
@Service
public class MailService {
	private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

	private final JavaMailSender sender;

	private final TaskExecutor executor;


	/**
	 * Erstellt eine neue Instanz des MailService und initialisiert den JavaMailSender und den TaskExecutor.
	 *
	 * @param sender   der JavaMailSender für das Versenden von E-Mails
	 * @param executor der TaskExecutor für das asynchrone Ausführen des E-Mail-Versands
	 */

	@Autowired
	public MailService(final JavaMailSender sender, final TaskExecutor executor) {
		this.sender = sender;
		this.executor = executor;
	}

	/**
	 * Sendet eine E-Mail mit den angegebenen Mailinformationen.
	 *
	 * @param mailInformation die Informationen für die E-Mail
	 */
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

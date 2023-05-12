package com.gpse.sesam.domain.mail;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class MailServiceTest {

	@Mock
	private JavaMailSender mailSender;

	@Mock
	private TaskExecutor taskExecutor;

	@InjectMocks
	private MailService mailService;

	@Captor
	private ArgumentCaptor<SimpleMailMessage> mailMessageArgumentCaptor;

	@Captor
	private ArgumentCaptor<Runnable> runnableArgumentCaptor;

	private AutoCloseable autoCloseable;

	private ListAppender<ILoggingEvent> listAppender;

	private final Logger logger = (Logger) LoggerFactory.getLogger(MailService.class);


	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	void sendShouldCallSenderWithCorrectArguments() {
		String subject = "subject";
		String text = "text";
		String to = "to";
		String from = "from";

		mailService.send(from, to, subject, text);

		verify(taskExecutor).execute(runnableArgumentCaptor.capture());
		runnableArgumentCaptor.getValue().run();

		verify(mailSender).send(mailMessageArgumentCaptor.capture());

		SimpleMailMessage sendMail = mailMessageArgumentCaptor.getValue();

		assertThat(sendMail.getFrom(), is(from));
		assertThat(sendMail.getTo().length, is(1));
		assertThat(sendMail.getTo()[0], is(to));
		assertThat(sendMail.getText(), is(text));
		assertThat(sendMail.getSubject(), is(subject));
	}

	@Test
	void sendShouldLogExceptionIfCallbackRaisesException() {
		setUpLogger();
		String subject = "subject";
		String text = "text";
		String to = "to";
		String from = "from";

		doThrow(new MailSendException("Test")).when(mailSender).send(isA(SimpleMailMessage.class));

		mailService.send(from, to, subject, text);

		verify(taskExecutor).execute(runnableArgumentCaptor.capture());
		runnableArgumentCaptor.getValue().run();

		List<ILoggingEvent> logsList = listAppender.list;

		assertThat(logsList.size(), is(1));
		assertThat(logsList.get(0).getLevel(), is(Level.WARN));
		assertThat(logsList.get(0).getMessage(), containsString("Unable to send mail to: " + to + "."));


	}

	private ListAppender<ILoggingEvent> setUpLogger() {
		listAppender = new ListAppender<>();
		listAppender.start();
		logger.addAppender(listAppender);
		return listAppender;
	}


}
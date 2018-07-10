package hu.exprog.beecomposit.jobs;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.velocity.VelocityContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sun.mail.util.MailSSLSocketFactory;

import hu.exprog.beecomposit.back.model.UserRegister;
import hu.exprog.beecomposit.middle.service.LogService;
import hu.exprog.beecomposit.middle.service.UserRegisterService;
import hu.exprog.honeyweb.utils.velocity.TemplateTools4Mails;

public class SendRegistrtionhMailsJob implements Job {

	private Object getService(String name) throws NamingException {
		InitialContext ic = new InitialContext();
		Object service = ic.lookup("java:/global/Homework4Telekocsi/" + name);
		return service;
	}

	private LogService getLogService() {
		LogService result = null;
		try {
			Object service = getService("LogService");
			if (service instanceof LogService) {
				result = (LogService) service;
			}
		} catch (NumberFormatException ex) {

		} catch (Throwable t) {
			System.out.println("service lookup failed");
		}
		return result;
	}

	private UserRegisterService getUserRegisterService() {
		UserRegisterService result = null;
		try {
			Object service = getService("UserRegisterService");
			if (service instanceof UserRegisterService) {
				result = (UserRegisterService) service;
			}
		} catch (NumberFormatException ex) {

		} catch (Throwable t) {
			System.out.println("service lookup failed");
		}
		return result;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LogService logService = getLogService();
		UserRegisterService userRegisterService = getUserRegisterService();

		StringBuffer msg = new StringBuffer();
		String from = context.getJobDetail().getJobDataMap().getString("PROP_SENDER");
		String fromName = context.getJobDetail().getJobDataMap().getString("PROP_SENDER_NAME");
		final String username = context.getJobDetail().getJobDataMap().getString("PROP_SMTP_USER");
		final String password = context.getJobDetail().getJobDataMap().getString("PROP_SMTP_PASSWORD");

		String host = context.getJobDetail().getJobDataMap().getString("PROP_SMTP_HOST");
		String port = context.getJobDetail().getJobDataMap().getString("PROP_SMTP_PORT");
		String sourcevm = context.getJobDetail().getJobDataMap().getString("sourcevm");
		String webContext = context.getJobDetail().getJobDataMap().getString("context");
		String velocityLogFile = context.getJobDetail().getJobDataMap().getString("velocityLogFile");
		String workingDirectory = context.getJobDetail().getJobDataMap().getString("workingDirectory");

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtps");
		props.setProperty("mail.smtps.starttls.enable", "true");
		props.setProperty("mail.smtps.ssl.enable", "true");
		MailSSLSocketFactory socketFactory;
		try {
			socketFactory = new MailSSLSocketFactory();
			socketFactory.setTrustAllHosts(true);
			props.put("mail.smtps.ssl.socketFactory", socketFactory);
		} catch (GeneralSecurityException e) {
			msg.append("hiba: " + e.getMessage());
			e.printStackTrace();
		}
		props.setProperty("mail.smtps.ssl.checkserveridentity", "false");
		props.setProperty("mail.smtps.ssl.trust", "*");
		props.setProperty("mail.smtps.socketFactory.fallback", "true");
		props.setProperty("mail.smtps.auth", "true");
		props.setProperty("mail.smtps.port", port); // 587
		props.setProperty("mail.smtps.host", host);

		Session remoteSession = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Transport transport = remoteSession.getTransport();
			List<UserRegister> users = userRegisterService.getLastIncompleteUserRegister();
			for (UserRegister userRegister : users) {
				Address address = new InternetAddress(userRegister.getEmailAddress());
				try {
					Message message = new MimeMessage(remoteSession);
					message.setFrom(new InternetAddress(from));
					List<Address> addressList = new LinkedList<Address>();
					addressList.add(new InternetAddress(userRegister.getEmailAddress()));
					message.setSubject("Regisztráció");
					Multipart mainPart = new MimeMultipart();
					BodyPart messageBodyPart = new MimeBodyPart();
					String source = null;
					if (sourcevm != null) {
						source = getMailContent(userRegister, sourcevm, fromName, workingDirectory, velocityLogFile, userRegister.getHashId(), userRegister.getUserName(), webContext);
						messageBodyPart.setContent(source, "text/html; charset=UTF-8");
						mainPart.addBodyPart(messageBodyPart);
						message.setContent(mainPart);

						((InternetAddress) address).setPersonal(userRegister.getUserName(), "UTF-8");

						transport.connect();

						message.setRecipients(Message.RecipientType.TO, addressList.toArray(new Address[] {}));

						try {
							Address[] addresses = message.getRecipients(Message.RecipientType.TO);
							transport.sendMessage(message, addresses);
							userRegister.setMailSentDate(new Date());
							userRegisterService.merge(userRegister);
						} catch (Exception e) {
							userRegisterService.updateMailSendTryCount(userRegister);
							e.printStackTrace();
						}

						transport.close();
						msg.append(users.size() + " levél küldése befejeződött...");
					} else {
						msg.append("Levél küldése sikertelem! Nincs regisztrációs sablon file beállítva!");
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

		} catch (MessagingException e) {
			e.printStackTrace();
			msg.append("hiba: " + e.getMessage());
		}

		logService.createLog(Level.FINE, "Levelek küldése befejeződött.", msg.toString());
	}

	private String getMailContent(UserRegister systemUser, String sourcevm, String fromName, String workingDirectory, String velocityLogFile, String hashId, String recipient, String context) {
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("subject", "regisztráció");
		velocityContext.put("fromName", fromName);
		velocityContext.put("hashId", hashId);
		velocityContext.put("recipient", recipient);
		velocityContext.put("context", context);
		String source = TemplateTools4Mails.transformSource(velocityLogFile, workingDirectory, sourcevm, velocityContext);
		return source;
	}
}
